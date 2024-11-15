//package com.myproject.projectmanagement.configs;
//
//import io.github.cdimascio.dotenv.Dotenv;
//import jakarta.annotation.PostConstruct;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class EnvConfig {
//    private static final Dotenv dotenv = Dotenv.configure()
//            .ignoreIfMalformed()
//            .load();
//
//    @Bean
//    public Dotenv dotenv() {
//        return dotenv;
//    }
//
//    @PostConstruct
//    public void init() {
//        System.setProperty("DB_URL", dotenv.get("DB_URL"));
//        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
//        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
//        System.setProperty("PORT", dotenv.get("PORT"));
//        System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
//        System.setProperty("JWT_HEADER", dotenv.get("JWT_HEADER"));
//    }
//}
