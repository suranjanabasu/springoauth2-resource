package com.javainuse.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(4)
public class OauthPostProcessingFilter extends GenericFilterBean {

    Logger log = LoggerFactory.getLogger(OauthPostProcessingFilter.class);



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        log.info("Starting Processing for filter OauthPostProcessingFilter");
        String auth = req.getHeader("Authorization");
        log.info("Auth is "+auth);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Username is "+username);
        log.info("****** "+SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        //Store Value in TokenStore backed by Redis
        filterChain.doFilter(servletRequest, servletResponse);

        log.info("After Processing for filter OauthPostProcessingFilter");

    }


}
