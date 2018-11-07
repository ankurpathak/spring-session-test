package com.ankurpathak.springsessiontest;


import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.valid4j.Assertive.ensure;

@Service
public class TokenService extends AbstractDomainService<Token, String> implements ITokenService {

    private final ITokenRepository dao;

    public TokenService(ITokenRepository dao) {
        super(dao);
        this.dao = dao;
    }



    @Override
    public Optional<Token> generateToken() {
        Token token = Token.getInstance();
        int i = 1;
        do{
            try{
                return Optional.of(dao.insert(token));
            }catch (DuplicateKeyException ex){
                token = Token.getInstance();
            }
            i++;
        }while (i <= 10);

        return Optional.empty();
    }

    @Override
    public Optional<Token> byValue(String token) {
        ensure(token, not(isEmptyString()));
        return dao.byValue(token);
    }
}
