package com.ankurpathak.springsessiontest;

import org.springframework.data.redis.core.index.Indexed;

import java.math.BigInteger;

public class PrimitiveUtils {

    public  static BigInteger toBigInteger(String value){
        return BigInteger.valueOf(toLong(value));
    }


    public static long toLong(String value){
        try{
            return Long.valueOf(value);
        }catch (NumberFormatException ex){
            return 0L;
        }
    }


    public static int toInteger(String value){
        try{
            return Integer.valueOf(value);
        }catch (NumberFormatException ex){
            return 0;
        }
    }
}
