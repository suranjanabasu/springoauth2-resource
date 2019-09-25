package com.javainuse.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class ResourceServerConfiguration extends
        ResourceServerConfigurerAdapter {
    private static final String RESOURCE_ID = "messages-resource";

    @Override
    public void configure(ResourceServerSecurityConfigurer security) throws Exception {
        security.resourceId(RESOURCE_ID)
               // .tokenServices(tokenServices())
                .tokenStore(tokenStore());
    }

    //.addFilterBefore(new MyTokenFilter(), AbstractPreAuthenticatedProcessingFilter.class)


    @Override
    public void configure(HttpSecurity http) throws Exception {
                http.requestMatcher(new OAuthRequestedMatcher())
                .anonymous().disable()
                        //.addFilter(new OauthPostProcessingFilter())
                //.addFilterAfter(new OauthPostProcessingFilter(), OAuth2AuthenticationProcessingFilter.class)
                .addFilterAfter(new OauthPostProcessingFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .authorizeRequests()
                        .antMatchers("/test/**").access("#oauth2.hasScope('message.read') or hasRole('CLIENT') or hasRole('MESSAGING_CLIENT')")
                ;

//        http
//                .csrf().disable()
//                .antMatcher("/test/**")
//                .authorizeRequests()
//                .antMatchers("/test/**").access("#oauth2.hasScope('message.read') or hasRole('CLIENT') or hasRole('MESSAGING_CLIENT')");
    }

//    @Bean
//
//    //Making this primary to avoid any accidental duplication with another token service instance of the same name
//    public DefaultTokenServices tokenServices() {
//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setTokenStore(tokenStore());
//        //defaultTokenServices.setSupportRefreshToken(true);
//        return defaultTokenServices;
//    }

    @Bean(name="tokenStore")
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    private static class OAuthRequestedMatcher implements RequestMatcher {
    public boolean matches(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        System.out.println("Auth is "+auth);
        // Determine if the client request contained an OAuth Authorization
        boolean haveOauth2Token = (auth != null) && auth.startsWith("Bearer");
        boolean haveAccessToken = request.getParameter("access_token")!=null;
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("***** Here 11 "+username);
        return haveOauth2Token || haveAccessToken;
    }
}


}
