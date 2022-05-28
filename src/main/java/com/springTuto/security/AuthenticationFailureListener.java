package com.springTuto.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    @SuppressWarnings("unused")
	@Autowired
    private HttpServletRequest request;

    @Override
    public void onApplicationEvent(final AuthenticationFailureBadCredentialsEvent e) {
      
       
    }

}