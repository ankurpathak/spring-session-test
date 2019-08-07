package com.github.ankurpathak.api.util;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.Collection;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class MatcherUtil {
    public static Matcher<Collection<?>>  collectionEmpty(){
        return Matchers.allOf(Matchers.notNullValue(), Matchers.empty());
    }
    public static Matcher<Collection<?>>  notCollectionEmpty(){
        return Matchers.allOf(Matchers.notNullValue(), Matchers.not(Matchers.empty()));
    }


    public static Matcher<Iterable<?>> iterableEmpty(){
        return Matchers.allOf(Matchers.notNullValue(), Matchers.emptyIterable());
    }

    public static Matcher<Iterable<?>> notIterableEmpty(){
        return Matchers.allOf(Matchers.notNullValue(), Matchers.not(Matchers.emptyIterable()));
    }


    public static Matcher<Map<?, ?>> mapEmpty(){
        return Matchers.allOf(Matchers.notNullValue(), Matchers.anEmptyMap());
    }

    public static Matcher<String> stringEmpty(){

        return not(emptyOrNullString());
    }

    public static Matcher<String> notStringEmpty(){

        return Matchers.allOf(Matchers.notNullValue(), Matchers.not(Matchers.emptyString()));
    }

    public static Matcher<Map<?, ?>> notMapEmpty(){
        return Matchers.allOf(Matchers.notNullValue(), Matchers.not(Matchers.anEmptyMap()));
    }
}
