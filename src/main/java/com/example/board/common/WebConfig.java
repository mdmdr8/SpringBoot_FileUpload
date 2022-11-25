package com.example.board.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//리액트에서 오는 cors를 에러 없이 접속 허용
@Configuration
public class WebConfig implements WebMvcConfigurer {
   public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**").allowedOrigins("http://localhost:3000");
//      .allowedOrigins("GET", "POST", 'PUT', "DELETE");
      
   }

}
