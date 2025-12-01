package com.devwiki.leafy.webclient;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {



    @Bean(name = "discordWebClient")
    public WebClient discordWebClient() {
        return WebClient.builder()
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    // @Bean
    // @Qualifier("kakaoAuthWebClient")
    // public WebClient kakaoAuthWebClient(){
    //     return WebClient.builder()
    //             .baseUrl("https://kauth.kakao.com")
    //             .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
    //             .build();
    // }
    //
    //
    // @Bean
    // @Qualifier("kakaoApiWebClient")
    // public WebClient kakaoApiWebClient() {
    //     return WebClient.builder()
    //             .baseUrl("https://kapi.kakao.com")
    //             .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
    //             .build();
    // }





}
