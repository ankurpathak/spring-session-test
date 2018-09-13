package com.ankurpathak.springsessiontest;

import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.client.Google2Client;
import org.pac4j.oauth.client.TwitterClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Pac4jConfig {



        @Bean
        public FacebookClient facebookClient(){
            return new FacebookClient("145278422258960", "be21409ba8f39b5dae2a7de525484da8");
        }


        @Bean
        public Google2Client googleClient(){
            return new Google2Client("326660090908-3so8blmps3nv6460tqv0js9salijsg26.apps.googleusercontent.com", "ukfB2KmsBDGckQ9CIlW5qJrk");
        }


        @Bean
        public TwitterClient twitterClient(){
            return new TwitterClient();
        }





}