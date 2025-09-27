package com.anisha.NotificationServiceF.configs;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBeans {

    @Bean
    public ObjectMapper getobjectMapper(){
        return new ObjectMapper();
    }

}
