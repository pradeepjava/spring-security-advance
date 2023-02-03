package com.eazybytes.config;

import com.eazybytes.model.Authority;
import com.eazybytes.model.Customer;
import com.eazybytes.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EazyBankAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String pass = authentication.getCredentials().toString();
        List<Customer> customers = repository.findByEmail(userName);
        if (!customers.isEmpty()) {
            String existingPass = customers.get(0).getPwd();
            if (encoder.matches(pass, existingPass)) {
                return new UsernamePasswordAuthenticationToken(userName, pass, getAuthorities(customers.get(0).getAuthorities()));
            } else {
                throw new BadCredentialsException("Bad credentials.");
            }
        } else {
            throw new BadCredentialsException("User not exists.");
        }
    }
private List<GrantedAuthority>getAuthorities(Set<Authority>authorities){
        return authorities.stream().map(a->new SimpleGrantedAuthority(a.getName())).collect(Collectors.toList());
}
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
