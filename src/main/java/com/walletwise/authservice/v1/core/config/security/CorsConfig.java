package com.walletwise.authservice.v1.core.config.security;

import com.walletwise.authservice.v1.core.config.annotations.Generated;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Generated
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        //api-gateways 
        config.addAllowedOrigin("http://api-gateway:8080"); 
        config.addAllowedOrigin("http://localhost:8080");   
        //documentation-service                                           
        config.addAllowedOrigin("http://documentation-service:8080");
        config.addAllowedOrigin("http://localhost:9090");
        
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}