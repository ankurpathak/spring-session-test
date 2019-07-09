package com.github.ankurpathak.api.util;

import org.hamcrest.Matcher;

import java.util.Collection;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class MatcherUtil {
    public static Matcher<Collection<?>>  collectionEmpty(){
        return allOf(notNullValue(), empty());
    }
    public static Matcher<Collection<?>>  notCollectionEmpty(){
        return allOf(notNullValue(), not(empty()));
    }


    public static Matcher<Iterable<?>> iterableEmpty(){
        return allOf(notNullValue(), emptyIterable());
    }

    public static Matcher<Iterable<?>> notIterableEmpty(){
        return allOf(notNullValue(), not(emptyIterable()));
    }


    public static Matcher<Map<?, ?>> mapEmpty(){
        return allOf(notNullValue(), anEmptyMap());
    }

    public static Matcher<Map<?, ?>> emptyString(){
        return allOf(notNullValue(), emptyString());
    }

    public static Matcher<Map<?, ?>> notMapEmpty(){
        return allOf(notNullValue(), not(anEmptyMap()));
    }
}
