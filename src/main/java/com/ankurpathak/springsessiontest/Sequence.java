package com.ankurpathak.springsessiontest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigInteger;

@Document(collection = Documents.SEQUENCE)
@JsonInclude(Include.NON_EMPTY)
public final class Sequence extends Domain<String> implements Serializable {
    private BigInteger curr;

    public BigInteger getCurr() {
        return curr;
    }

    public void setCurr(BigInteger curr) {
        this.curr = curr;
    }


    @Override
    public Sequence id(String id) {
        super.id(id);
        return this;
    }


    public static Sequence getUserSequenceInitialValue(){
        return getInstance().id(ID_USER_SEQ).curr(BigInteger.ONE);
    }


    public static Sequence getInstance(){
        return new Sequence();
    }


    public static final String ID_USER_SEQ = "user_seq";

    public Sequence curr(BigInteger curr) {
        this.curr = curr;
        return this;
    }


}
