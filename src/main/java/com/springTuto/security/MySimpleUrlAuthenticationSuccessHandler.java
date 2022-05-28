package com.springTuto.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.springTuto.account.models.AppUser;
import com.springTuto.account.models.RoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component("myAuthenticationSuccessHandler")
public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
 
        final HttpSession session = request.getSession(false);
        if (session != null) {
            session.setMaxInactiveInterval(30 * 60);

            String username;
            if (authentication.getPrincipal() instanceof AppUser) {
            	username = ((AppUser)authentication.getPrincipal()).getUsername();
            }
            else {
            	username = authentication.getName();
            }
            session.setAttribute("user", authentication.getPrincipal());
            session.setAttribute("user1", authentication.getPrincipal());
        }
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);

    }

    protected void handle(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
        final String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(final Authentication authentication) {
        boolean isAdmin = false;
        boolean isaUser = false;
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals(RoleEnum.ROLE_USER.name())) {
                isaUser = true;

            }else if (grantedAuthority.getAuthority().equals(RoleEnum.ROLE_ADMIN.name())) {
                isaUser = false;
                isAdmin = true;
                break;
            }
        }
        if (isaUser) {
        	 String username;
             if (authentication.getPrincipal() instanceof AppUser) {
             	username = ((AppUser)authentication.getPrincipal()).getUsername();
             }
             else {
             	username = authentication.getName();
             }

            return "/User/index?email="+username;

        }else if (isAdmin) {
        	String username;
            if (authentication.getPrincipal() instanceof AppUser) {
            	username = ((AppUser)authentication.getPrincipal()).getUsername();
            }
            else {
            	username = authentication.getName();
            }
            return "/Admin/index?user="+username;
        }
        else {
            throw new IllegalStateException();
        }
    }

    protected void clearAuthenticationAttributes(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    public void setRedirectStrategy(final RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}