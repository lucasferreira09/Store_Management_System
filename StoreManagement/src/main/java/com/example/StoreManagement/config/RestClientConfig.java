package com.example.StoreManagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient pagBankRestClient(
            @Value("${pagbank.token}") String token,
            @Value("${pagbank.url}") String url
    ) {

        JsonMapper pagBankMapper = JsonMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build();

        JacksonJsonHttpMessageConverter jsonConverter =
                new JacksonJsonHttpMessageConverter(pagBankMapper);

        return RestClient.builder()
                .baseUrl(url)
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + token
                )
                .defaultHeader(
                        HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .messageConverters(converters -> {
                    converters.removeIf(
                            converter -> converter instanceof JacksonJsonHttpMessageConverter
                    );
                    converters.add(jsonConverter);
                })
                .build();
    }
}