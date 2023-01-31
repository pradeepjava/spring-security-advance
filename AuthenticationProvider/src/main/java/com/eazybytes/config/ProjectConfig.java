package com.eazybytes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class ProjectConfig {
    @Bean
    SecurityFilterChain getSecurityFilterChainForProduction(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests().requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans").authenticated().requestMatchers("/contact", "/notices", "/register").permitAll().and().formLogin().and().httpBasic();
        return http.build();
    }
//below method is for in-memory users
   /* @Bean
    InMemoryUserDetailsManager configureInMemoryUsers(){

        InMemoryUserDetailsManager inMemoryUserDetailsManager=new InMemoryUserDetailsManager();
        UserDetails admin=User.withUsername("admin").password("1234").authorities("admin").build();
        UserDetails user=User.withUsername("user").password("1234").authorities("read").build();
        inMemoryUserDetailsManager.createUser(admin);
        inMemoryUserDetailsManager.createUser(user);
        return inMemoryUserDetailsManager;
    }*/

    //below method is for jdbc
//    @Bean
//    UserDetailsManager configureJdbcUsers(DataSource dataSource){
//        return new JdbcUserDetailsManager(dataSource);
//    }

    //Creating bean of BCryptPassword
    @Bean
    public PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }
}