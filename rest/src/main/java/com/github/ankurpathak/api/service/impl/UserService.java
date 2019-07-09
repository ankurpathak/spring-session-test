package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.domain.model.Role;
import com.github.ankurpathak.api.security.util.SecurityUtil;
import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.exception.InvalidException;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.security.dto.CustomUserDetails;
import com.github.ankurpathak.api.domain.repository.mongo.IUserRepository;
import com.github.ankurpathak.api.security.dto.DomainContext;
import com.github.ankurpathak.api.service.IEmailService;
import com.github.ankurpathak.api.service.ITokenService;
import com.github.ankurpathak.api.service.IUserService;
import com.github.ankurpathak.api.service.IpService;
import com.github.ankurpathak.api.util.LogUtil;
import com.github.ankurpathak.api.util.PrimitiveUtils;
import com.github.ankurpathak.primitive.string.StringValidator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
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
import static org.valid4j.Assertive.require;


@Service
public class UserService extends AbstractDomainService<User, BigInteger> implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


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
        require(candidateKey, not(emptyString()));
        return dao.byCandidateKey(PrimitiveUtils.toBigInteger(candidateKey), candidateKey);
    }

    @Override
    public void saveEmailToken(User user, Token token) {
        require(user, notNullValue());
        require(token, notNullValue());
        if (user.getEmail() != null) {
            if(!StringUtils.isEmpty(token.getId())){
                user.getEmail().setTokenId(token.getId());
                update(user);
            }else{
                LogUtil.logFieldEmpty(log, Token.class.getSimpleName(), Model.Token.Field.ID, token.getId());
            }

        }else {
            LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.EMAIL, String.valueOf(user.getId()));
        }
    }

    @Override
    public Optional<User> byEmail(String email) {
        require(email, not(emptyString()));
        return dao.findByCriteria(Criteria.where(Model.User.QueryKey.EMAIL).is(email), PageRequest.of(0, 1), User.class)
                .findFirst();
    }

    @Override
    public Optional<User> byEmailTokenId(String tokenId) {
        require(tokenId, not(emptyString()));
        return dao.byEmailTokenId(tokenId);
    }

    @Override
    public void accountEnableEmail(String email) {
        require(email, notNullValue());
        byEmail(email)
                .filter(User::isEnabled)
                .ifPresentOrElse(user -> {
                        Optional.ofNullable(user.getEmail())
                                .ifPresentOrElse(x -> {
                                    Optional.ofNullable(x.getTokenId())
                                            .filter(String::isEmpty)
                                            .ifPresentOrElse(tokenService::deleteById,() -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.EMAIL_TOKEN_ID, String.valueOf(user.getId())));
                                    tokenService.generateToken()
                                            .ifPresentOrElse(token -> {
                                                saveEmailToken(user, token);
                                                emailService.sendForAccountEnable(user, token);
                                            }, () -> LogUtil.logNull(log, Token.class.getSimpleName()));

                            }, () -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.EMAIL, String.valueOf(user.getId())));

                    }, () -> { throw new NotFoundException(email, Params.EMAIL, User.class.getSimpleName(), ApiCode.NOT_FOUND);}
                );
    }

    @Override
    public Token.TokenStatus accountEnable(@Nonnull String token) {
        require(token, not(emptyString()));
        return verifyEmailToken(token);
    }

    @Override
    public Token.TokenStatus forgetPasswordEnable(String token) {
        require(token, not(emptyString()));
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
        require(tokenId, not(emptyString()));
        return dao.byPasswordTokenId(tokenId);
    }

    @Override
    public void forgotPasswordEmail(User user) {
        require(user, notNullValue());
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
        require(user, notNullValue());
        require(token, notNullValue());
        if (user.getPassword() != null && !StringUtils.isEmpty(token.getId())) {
            user.getPassword().setTokenId(token.getId());
            update(user);
        }
    }

    @Override
    public void validateExistingPassword(User user, UserDto dto) {
        require(user, notNullValue());
        require(dto, notNullValue());
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
            possibleKeys.put(Params.PHONE, username);

        BigInteger possibleId = PrimitiveUtils.toBigInteger(username);
        if (possibleId.compareTo(BigInteger.ZERO) > 0) {
            possibleKeys.put(Params.MONGO_ID, possibleId);
        }

        possibleKeys.put(Params.USERNAME, username);
        return possibleKeys;
    }

    @Override
    public Set<String> possibleContacts(String username) {
        require(username, not(emptyString()));
        if (!StringUtils.isNumeric(username))
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
        require(entity, notNullValue());
        return dao.persist(entity);
    }
}
