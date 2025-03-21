package com.fullstack.springboot.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

//    @Bean
//    public ModelMapper modelMapper() {
//        return new ModelMapper();
//    }
    
    @Bean
    public ModelMapper getMapper() {
      ModelMapper modelMapper = new ModelMapper();
      modelMapper.getConfiguration()
              .setFieldMatchingEnabled(true)
              .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
              .setMatchingStrategy(MatchingStrategies.STRICT);

      return modelMapper;
    }
}