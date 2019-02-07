package com.github.ankurpathak.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
public class RedisHttpSessionListener implements HttpSessionListener {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
       // savePrincipalNameIndexName(event.getSession());
        RedisOperationsSessionRepository sessionRepository = applicationContext.getBean(RedisOperationsSessionRepository.class);
        var sessions = sessionRepository.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, String.valueOf(2));
        System.out.printf("Number of sessions: %d%n",sessions.size());
        System.out.printf("Session Created: %s%n",event.getSession().getId());

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        RedisOperationsSessionRepository sessionRepository = applicationContext.getBean(RedisOperationsSessionRepository.class);
        var sessions = sessionRepository.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, String.valueOf(2));
        System.out.printf("Number of sessions: %d%n",sessions.size());
        System.out.printf("Session Destroyed: %s%n",event.getSession().getId());
    }


    private void savePrincipalNameIndexName(HttpSession session){
        SecurityUtil.getMe().ifPresent(user -> session.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, user.getId()));
    }
}
