package com.eazybytes.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ValidateUserFilter extends OncePerRequestFilter {
    Charset charset = StandardCharsets.UTF_8;
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/user");
    }
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse=(HttpServletResponse)response;
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader != null) {
            String encodedUserPas = authHeader.substring(6);
            byte[] encodedBytes = encodedUserPas.getBytes(StandardCharsets.UTF_8);
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
                String decodedString = new String(decodedBytes, charset);
                int index = decodedString.indexOf(':');
                if (index == -1) {
                    throw new IllegalArgumentException("Illegal format supplied");
                }
                String email = decodedString.substring(0, index);
                if (email.contains("test")) {
                    httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            } catch (Exception e) {
                throw new BadCredentialsException("Bad credentials");
            }
        }
        chain.doFilter(request,response);
    }
}
