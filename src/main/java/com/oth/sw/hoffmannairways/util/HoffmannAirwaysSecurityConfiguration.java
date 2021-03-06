package com.oth.sw.hoffmannairways.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HoffmannAirwaysSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String[] ALLOW_ACCESS_WITHOUT_AUTHENTICATION = {
            "/css/**", "/img/**", "/fonts/**", "/js/**", "/", "/flights", "/login", "/forgotPassword", "/register", "/error", "/static/img/**"
    };
    private static final String[] ALLOW_ACCESS_FOR_STAFF = {
            "/repairplane", "/editflight/**", "/createflight", "/planes"
    };
    @Autowired
    private UserDetailsService userSecurityService;
    @Autowired
    private HoffmannAirwaysSecurityUtilities securityUtilities;

    private BCryptPasswordEncoder passwordEncoder() {
        return securityUtilities.passwordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(ALLOW_ACCESS_FOR_STAFF).hasAuthority("STAFF");

        http.authorizeRequests()
                .antMatchers(ALLOW_ACCESS_WITHOUT_AUTHENTICATION)
                .permitAll().anyRequest().authenticated();
        http
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/?logout")
                .deleteCookies("remember-me")
                .permitAll()
                .and()
                .rememberMe();
        // Cross-Site Request Forgery ausschalten
        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService)
                .passwordEncoder(passwordEncoder());
    }


}
