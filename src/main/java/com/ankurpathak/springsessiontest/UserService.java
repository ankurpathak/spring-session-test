package com.ankurpathak.springsessiontest;

import com.github.ankurpathak.primitive.bean.constraints.string.StringValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.valid4j.Assertive.ensure;


@Service
public class UserService extends AbstractDomainService<User, BigInteger> implements IUserService {

    private final IUserRepository dao;
    private final ITokenService tokenService;
    private final IEmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final IpService ipService;
    private final CountryCacheService countryService;

    public UserService(IUserRepository dao, ITokenService tokenService, IEmailService emailService, PasswordEncoder passwordEncoder, IpService ipService, CountryCacheService countryService) {
        super(dao);
        this.dao = dao;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.ipService = ipService;
        this.countryService = countryService;
    }

    @Override
    public Optional<User> byCandidateKey(String candidateKey) {
        ensure(candidateKey, not(emptyString()));
        return dao.byCandidateKey(PrimitiveUtils.toBigInteger(candidateKey), candidateKey);
    }

    @Override
    public void saveEmailToken(User user, Token token) {
        ensure(user, notNullValue());
        ensure(token, notNullValue());
        if (user.getEmail() != null && !StringUtils.isEmpty(token.getId())) {
            user.getEmail().setTokenId(token.getId());
            update(user);
        }
    }

    @Override
    public Optional<User> byEmail(String email) {
        ensure(email, not(emptyString()));
        return dao.byEmail(email);
    }

    @Override
    public Optional<User> byEmailTokenId(String tokenId) {
        ensure(tokenId, not(emptyString()));
        return dao.byEmailTokenId(tokenId);
    }

    @Override
    public void accountEnableEmail(User user) {
        ensure(user, notNullValue());
        if (user.getEmail() != null && !StringUtils.isEmpty(user.getEmail().getTokenId()))
            tokenService.deleteById(user.getEmail().getTokenId());
        tokenService.generateToken()
                .ifPresent(token -> {
                    saveEmailToken(user, token);
                    emailService.sendForAccountEnable(user, token);
                });
    }

    @Override
    public Token.TokenStatus accountEnable(@Nonnull String token) {
        ensure(token, not(emptyString()));
        return verifyEmailToken(token);
    }

    @Override
    public Token.TokenStatus forgetPasswordEnable(String token) {
        ensure(token, not(emptyString()));
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
        ensure(tokenId, not(emptyString()));
        return dao.byPasswordTokenId(tokenId);
    }

    @Override
    public void forgotPasswordEmail(User user) {
        ensure(user, notNullValue());
        if (user.getPassword() != null && !StringUtils.isEmpty(user.getPassword().getTokenId()))
            tokenService.deleteById(user.getPassword().getTokenId());
        tokenService.generateToken()
                .ifPresent(token -> {
                    savePasswordToken(user, token);
                    emailService.sendForForgetPassword(user, token);
                });

    }

    @Override
    public void savePasswordToken(User user, Token token) {
        ensure(user, notNullValue());
        ensure(token, notNullValue());
        if (user.getPassword() != null && !StringUtils.isEmpty(token.getId())) {
            user.getPassword().setTokenId(token.getId());
            update(user);
        }
    }

    @Override
    public void validateExistingPassword(User user, UserDto dto) {
        ensure(user, notNullValue());
        ensure(dto, notNullValue());
        Optional.ofNullable(user.getPassword())
                .ifPresentOrElse(password -> {
                    if (!passwordEncoder.matches(dto.getCurrentPassword(), password.getValue()))
                        throw new InvalidException(ApiCode.INVALID_PASSWORD, Params.PASSWORD, dto.getCurrentPassword());

                }, () -> {
                    throw new InvalidException(ApiCode.INVALID_PASSWORD, Params.PASSWORD, dto.getCurrentPassword());
                });

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
    public Set<String> possibleContacts(String username) {
        ensure(username, not(emptyString()));
        if (StringUtils.isNumeric(username))
            return Collections.emptySet();
        Set<String> possibleContacts = new LinkedHashSet<>();
        SecurityUtil.getDomainContext()
                .map(DomainContext::getRemoteAddress)
                .flatMap(ipService::ipToCountryAlphaCode)
                .map(countryService::alphaCodeToCallingCodes)
                .ifPresent(callingCodes -> {
                    callingCodes.stream()
                            .map(callingCode -> String.format("+%s%s", callingCode, username))
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
