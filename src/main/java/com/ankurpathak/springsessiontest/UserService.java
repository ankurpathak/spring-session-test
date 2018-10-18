package com.ankurpathak.springsessiontest;

import com.ankurpathak.springsessiontest.controller.InvalidException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Calendar;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.valid4j.Assertive.ensure;
import static org.hamcrest.Matchers.*;

@Service
public class UserService extends AbstractDomainService<User,BigInteger> implements IUserService  {

    private final IUserRepository dao;
    private final ITokenService tokenService;
    private final IEmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository dao, ITokenService tokenService, IEmailService emailService, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected ExtendedMongoRepository<User,BigInteger> getDao() {
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
        if(!StringUtils.isEmpty(token.getId())){
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
        if(!StringUtils.isEmpty(user.getEmail().getValue()))
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
        if(token.isPresent()){
            if (token.get().getExpiry().isBefore(Instant.now())) {
                tokenService.delete(token.get());
                return Token.TokenStatus.EXPIRED;
            }
            Optional<User> user = byPasswordTokenId(token.get().getId());
            if(user.isPresent()){
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
        }else return Token.TokenStatus.EXPIRED;

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
        if(!StringUtils.isEmpty(user.getPassword().getTokenId()))
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
        if(!StringUtils.isEmpty(token.getId())){
            user.getPassword().setTokenId(token.getId());
            update(user);
        }
    }

    @Override
    public void validateExistingPassword(User user, UserDto dto) {
        ensure(user, notNullValue());
        ensure(dto, notNullValue());
        ensure(user.getPassword(), notNullValue());
        if(!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword().getValue()))
            throw new InvalidException( ApiCode.INVALID_PASSWORD, Params.PASSWORD, dto.getCurrentPassword());
    }


    private Token.TokenStatus verifyEmailToken(String value){
        var token = tokenService.byValue(value);
        if(token.isPresent()){
            final Calendar cal = Calendar.getInstance();
            if (token.get().getExpiry().isBefore(Instant.now())) {
                tokenService.delete(token.get());
                return Token.TokenStatus.EXPIRED;
            }
            Optional<User> user = byEmailTokenId(token.get().getId());
            if(user.isPresent()){
                user.get().setEnabled(true);
                user.get().getEmail().setTokenId(null);
                user.get().getEmail().setChecked(true);
                update(user.get());
                tokenService.delete(token.get());
                return Token.TokenStatus.VALID;
            }

            return Token.TokenStatus.INVALID;

        }else return Token.TokenStatus.EXPIRED;

    }


    @Override
    public User create(User entity) {
        ensure(entity, notNullValue());
        return dao.persist(entity);
    }
}
