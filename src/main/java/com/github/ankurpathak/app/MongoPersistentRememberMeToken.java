package com.github.ankurpathak.app;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.util.Date;

@Document(collection = "persistentlogins")
@CompoundIndexes({
        @CompoundIndex(name = "persistentloginsUsernameIdx", def = "{ 'username' : 1 }"),
        @CompoundIndex(name = "persistentloginsSeriesIdx", def = "{ 'series' : 1 }", unique = true)
})
public class MongoPersistentRememberMeToken extends PersistentRememberMeToken {

    @Id
    private String id;


    public String getId() {
        return id;
    }

    public MongoPersistentRememberMeToken() {
        this(null, null, null, null);
    }


    public MongoPersistentRememberMeToken(final PersistentRememberMeToken token) {
        this(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());

    }


    public MongoPersistentRememberMeToken(final String username, final String series, final String tokenValue, final Date date) {
        super(username, series, tokenValue, date);
    }


}