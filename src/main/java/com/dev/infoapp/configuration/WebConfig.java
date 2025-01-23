package com.dev.infoapp.configuration;  

import org.springframework.context.annotation.Configuration;  
import org.springframework.web.reactive.config.CorsRegistry;  
import org.springframework.web.reactive.config.WebFluxConfigurer;  

@Configuration  
public class WebConfig implements WebFluxConfigurer {  
    
    @Override  
    public void addCorsMappings(CorsRegistry registry) {  
        registry.addMapping("/api/**")  
            .allowedOrigins("*")  
            .allowedMethods("GET", "POST", "PUT", "DELETE")  
            .allowedHeaders("*")  
            .allowCredentials(true);  
    }  
}