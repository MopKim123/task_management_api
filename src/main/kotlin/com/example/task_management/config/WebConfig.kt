package com.example.task_management.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig{
    @Bean
    fun corsConfigurer(): WebMvcConfigurer{
        return object: WebMvcConfigurer{
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000","http://127.0.0.1:5500","http://localhost:5173")
                    .allowedMethods("GET","POST","DELETE","PUT")
                    .allowedHeaders("*")
                    .allowCredentials(true)
            }
        }
    }
}