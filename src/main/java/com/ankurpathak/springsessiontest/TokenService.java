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
        this.dao = dao;
    }


    @Override
    protected ExtendedRepository<Token, String> getDao() {
        return dao;
    }

    @Override
    public Token generateToken() {
        Token token = Token.getInstance();
        do{
            try{
                return dao.insert(token);
            }catch (DuplicateKeyException ex){
                token = Token.getInstance();
            }

        }while (true);

    }

    @Override
    public Optional<Token> byValue(String token) {
        ensure(token, not(isEmptyString()));
        return dao.byValue(token);
    }
}
