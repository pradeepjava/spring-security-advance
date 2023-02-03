package com.eazybytes.filters;

import jakarta.servlet.*;
import org.apache.catalina.security.SecurityConfig;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.logging.Logger;

public class AuthoritiesLoggingFilter implements Filter {
    Logger logger = Logger.getLogger(AuthoritiesLoggingFilter.class.getName());
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null){
            logger.info("AuthoritiesOfUSer is:"+auth.getAuthorities());
        }
        chain.doFilter(request,response);
    }
}
