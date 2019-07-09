package com.github.ankurpathak.api.service.impl;


import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.repository.mongo.ITokenRepository;
import com.github.ankurpathak.api.service.ITokenService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Optional;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.valid4j.Assertive.require;

@Service
public class TokenService extends AbstractDomainService<Token, String> implements ITokenService {

    private final ITokenRepository dao;

    public TokenService(ITokenRepository dao) {
        super(dao);
        this.dao = dao;
    }



    @Override
    public Optional<Token> generateToken() {
        return Mono.justOrEmpty(Token.getInstance())
                .map(dao::insert)
                .retry(10)
                .map(Optional::of)
                .switchIfEmpty(Mono.just(Optional.empty()))
                .block();
    }

    @Override
    public Optional<Token> byValue(String token) {
        require(token, not(emptyString()));
        return dao.byValue(token);
    }
}
