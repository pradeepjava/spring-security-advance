package com.eazybytes.config;

import com.eazybytes.filters.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
public class ProjectConfig {

    @Bean
    SecurityFilterChain getSecurityFilterChainForProduction(HttpSecurity http) throws Exception {

        CsrfTokenRequestAttributeHandler handler=new CsrfTokenRequestAttributeHandler();
        handler.setCsrfRequestAttributeName("_csrf");

        /**
         *  From Spring Security 6, below actions will not happen by default,
         *  1) The Authentication details will not be saved automatically into SecurityContextHolder. To change this behaviour either we need to save
         *      these details explicitly into SecurityContextHolder or we can configure securityContext().requireExplicitSave(false) like shown below.
         *  2) The Session & JSessionID will not be created by default. Inorder to create a session after intial login, we need to configure
         *      sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)) like shown below.
         */

        http.securityContext().requireExplicitSave(false).and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .cors().configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration corsConfiguration=new CorsConfiguration();
                        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                        corsConfiguration.setMaxAge(3600L);
                        return corsConfiguration;
                    }
                }).and()
                /**
                 *  In Spring Security 5, the default behavior is that the CsrfToken will be loaded on every request. Where as with
                 *  Spring Security 6, the default is that the lookup of the CsrfToken will be deferred until it is needed. The developer
                 *  has to write logic to read the CSRF token and send it as part of the response. When framework sees the CSRF token
                 *  in the response header, it takes care of sending the same as Cookie as well. For the same, we need to use CsrfTokenRequestAttributeHandler
                 *  and create a filter with the name CsrfCookieFilter which runs every time after the Spring Security in built filter BasicAuthenticationFilter
                 *  like shown below. More details about Filters, are discussed inside the Section 8 of the course.
                 */

                .csrf(csrf ->csrf.csrfTokenRequestHandler(handler).ignoringRequestMatchers("/contact","/register")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)

                .authorizeHttpRequests()
                .requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans", "/user").authenticated()
                .requestMatchers("/contact", "/notices", "/register").permitAll()
                .and().formLogin()
                .and().httpBasic();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}