package com.ankurpathak.springsessiontest;

import com.ankurpathak.springsessiontest.controller.InvalidException;
import com.github.ankurpathak.primitive.bean.constraints.string.StringValidator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.math.BigInteger;
import java.time.Instant;
import java.util.*;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.valid4j.Assertive.ensure;
import static org.hamcrest.Matchers.*;

@Service
public class UserService extends AbstractDomainService<User, BigInteger> implements IUserService {

    private final IUserRepository dao;
    private final ITokenService tokenService;
    private final IEmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final IpService ipService;
    private final CountryCacheService countryService;

    public UserService(IUserRepository dao, ITokenService tokenService, IEmailService emailService, PasswordEncoder passwordEncoder, IpService ipService, CountryCacheService countryService) {
        this.dao = dao;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.ipService = ipService;
        this.countryService = countryService;
    }

    @Override
    protected ExtendedMongoRepository<User, BigInteger> getDao() {
        return dao;
    }

    @Override
    public Optional<User> byCandidateKey(String candidateKey) {
        ensure(candidateKey, not(isEmptyString()));
        return dao.byCandidateKey(PrimitiveUtils.toBigInteger(candidateKey), candidateKey);
    }

    @Override
    public void saveEmailToken(User user, Token token) {
        ensure(user, notNullValue());
        ensure(user.getEmail(), notNullValue());
        ensure(token, notNullValue());
        if (!StringUtils.isEmpty(token.getId())) {
            user.getEmail().setTokenId(token.getId());
            update(user);
        }
    }

    @Override
    public Optional<User> byEmail(String email) {
        ensure(email, not(isEmptyString()));
        return dao.byEmail(email);
    }

    @Override
    public Optional<User> byEmailTokenId(String tokenId) {
        ensure(tokenId, not(isEmptyString()));
        return dao.byEmailTokenId(tokenId);
    }

    @Override
    public void accountEnableEmail(User user, String email) {
        ensure(user, notNullValue());
        ensure(user.getEmail(), notNullValue());
        if (!StringUtils.isEmpty(user.getEmail().getValue()))
            tokenService.deleteById(user.getEmail().getTokenId());
        Token token = tokenService.generateToken();
        saveEmailToken(user, token);
        emailService.sendForAccountEnable(user, token);
    }

    @Override
    public Token.TokenStatus accountEnable(String token) {
        return verifyEmailToken(token);
    }

    @Override
    public Token.TokenStatus forgetPasswordEnable(String token) {
        return verifyPasswordToken(token);
    }

    private Token.TokenStatus verifyPasswordToken(String value) {
        var token = tokenService.byValue(value);
        if (token.isPresent()) {
            if (token.get().getExpiry().isBefore(Instant.now())) {
                tokenService.delete(token.get());
                return Token.TokenStatus.EXPIRED;
            }
            Optional<User> user = byPasswordTokenId(token.get().getId());
            if (user.isPresent()) {
                UserDetails details = CustomUserDetails.getInstance(user.get(), Set.of(Role.Privilege.PRIV_FORGET_PASSWORD));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        details,
                        null,
                        details.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                tokenService.delete(token.get());
                return Token.TokenStatus.VALID;
            }

            return Token.TokenStatus.INVALID;
        } else return Token.TokenStatus.EXPIRED;

    }

    @Override
    public Optional<User> byPasswordTokenId(String tokenId) {
        ensure(tokenId, not(is(isEmptyString())));
        return dao.byPasswordTokenId(tokenId);
    }

    @Override
    public void forgotPasswordEmail(User user, String email) {
        ensure(user, notNullValue());
        ensure(user.getEmail(), notNullValue());
        ensure(user.getPassword(), notNullValue());
        if (!StringUtils.isEmpty(user.getPassword().getTokenId()))
            tokenService.deleteById(user.getPassword().getTokenId());
        Token token = tokenService.generateToken();
        savePasswordToken(user, token);
        emailService.sendForForgetPassword(user, token);
    }

    @Override
    public void savePasswordToken(User user, Token token) {
        ensure(user, notNullValue());
        ensure(user.getPassword(), notNullValue());
        ensure(token, notNullValue());
        if (!StringUtils.isEmpty(token.getId())) {
            user.getPassword().setTokenId(token.getId());
            update(user);
        }
    }

    @Override
    public void validateExistingPassword(User user, UserDto dto) {
        ensure(user, notNullValue());
        ensure(dto, notNullValue());
        ensure(user.getPassword(), notNullValue());
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword().getValue()))
            throw new InvalidException(ApiCode.INVALID_PASSWORD, Params.PASSWORD, dto.getCurrentPassword());
    }


    private Token.TokenStatus verifyEmailToken(String value) {
        var token = tokenService.byValue(value);
        if (token.isPresent()) {
            final Calendar cal = Calendar.getInstance();
            if (token.get().getExpiry().isBefore(Instant.now())) {
                tokenService.delete(token.get());
                return Token.TokenStatus.EXPIRED;
            }
            Optional<User> user = byEmailTokenId(token.get().getId());
            if (user.isPresent()) {
                user.get().setEnabled(true);
                user.get().getEmail().setTokenId(null);
                user.get().getEmail().setChecked(true);
                update(user.get());
                tokenService.delete(token.get());
                return Token.TokenStatus.VALID;
            }

            return Token.TokenStatus.INVALID;

        } else return Token.TokenStatus.EXPIRED;

    }

    @Override
    public Map<String, Object> possibleCandidateKeys(String username) {
        if (StringUtils.isEmpty(username))
            return Collections.emptyMap();
        Map<String, Object> possibleKeys = new LinkedHashMap<>();

        if (StringValidator.email(username, false))
            possibleKeys.put(Params.EMAIL, username);
        else if (StringValidator.contact(username, false))
            possibleKeys.put(Params.CONTACT, username);

        BigInteger possibleId = PrimitiveUtils.toBigInteger(username);
        if (possibleId.compareTo(BigInteger.ZERO) > 0) {
            possibleKeys.put(Params.ID, possibleId);
        }

        possibleKeys.put(Params.USERNAME, username);
        return possibleKeys;
    }

    @Override
    public Set<String> possibleContacts(String username){
        if(StringUtils.isNumeric(username))
            return Collections.emptySet();
        Set<String> possibleContacts = new LinkedHashSet<>();
        SecurityUtil.getDomainContext()
                .map(DomainContext::getRemoteAddress)
                .flatMap(ipService::ipToCountryAlphaCode)
                .map(countryService::alphaCodeToCallingCodes)
                .ifPresent(callingCodes -> {
                    callingCodes.stream()
                            .map(callingCode->String.format("+%s%s",callingCode, username))
                            .forEach(possibleContacts::add);
                });
        return possibleContacts;
    }





    @Override
    public User create(User entity) {
        ensure(entity, notNullValue());
        return dao.persist(entity);
    }
}
