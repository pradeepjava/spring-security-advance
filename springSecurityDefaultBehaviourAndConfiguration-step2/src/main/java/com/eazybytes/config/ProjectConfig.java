package com.eazybytes.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectConfig {
    @Bean
    @ConditionalOnProperty(prefix = "security", name = "configuration",havingValue = "prod")
    SecurityFilterChain getSecurityFilterChainForProduction(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans").authenticated()
                .requestMatchers("/contact", "/notices").permitAll()
                .and().formLogin()
                .and().httpBasic();
        return http.build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "security", name = "configuration", havingValue = "qa")
    SecurityFilterChain getSecurityFilterChainForQA(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests().anyRequest().permitAll().and().formLogin().and().httpBasic();
        return http.build();
    }
}
