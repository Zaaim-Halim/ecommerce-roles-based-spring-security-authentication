package com.springTuto.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private LogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

  
	@Autowired
    private UserDetailService userDetailService;

    @Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.authenticationProvider(authenticationProvider());
	}
	 @Override
	    public void configure(final WebSecurity web) throws Exception {
	        web.ignoring().antMatchers("/resources/**",
	                "/resources/**",
	                "/images/**",        
	                "/fonts/**",
	                "/css/**",
	                "/js/**",
	                "/font-awesome/**"
	               );
	    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	 http
         .csrf().disable()
         .authorizeRequests()
             .antMatchers("/login*","/register*").permitAll()
             .antMatchers("/User/**").hasRole("USER")
             .antMatchers("/Admin/**").hasRole("ADMIN")
             .anyRequest().authenticated()
             .and()
         .formLogin()
             .loginPage("/login")
             .defaultSuccessUrl("/index")
             .failureUrl("/login?error=true")
             .successHandler(myAuthenticationSuccessHandler)
             .failureHandler(authenticationFailureHandler)
             
             
         .permitAll()
             .and()
         .sessionManagement()
             .invalidSessionUrl("/invalidSession.html")
             .maximumSessions(1).sessionRegistry(sessionRegistry()).and()
             .sessionFixation().none()
         .and()
         .logout()
             .logoutSuccessHandler(myLogoutSuccessHandler)
             .invalidateHttpSession(true)
             .logoutSuccessUrl("/ogin")
             .deleteCookies("JSESSIONID")
             .permitAll()
           .and()
             .rememberMe().rememberMeServices(rememberMeServices()).key("theKey");
    	
 // @formatter:on
               
    }

    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    @Bean
    public RememberMeServices rememberMeServices() {
        return new CustomRememberMeServices("theKey", userDetailService, new InMemoryTokenRepositoryImpl());
    }

}

