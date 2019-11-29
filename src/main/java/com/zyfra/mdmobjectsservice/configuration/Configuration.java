package com.zyfra.mdmobjectsservice.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyfra.mdmclient.model.MDMConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${mdm.requests:requests.json}")
    private String mdmRequest;


    @Value("${mdm.mapping:mapping.json}")
    private String mdmMapping;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public Map<String, MDMConfiguration> getConfiguration() {

        Resource resource = new ClassPathResource(mdmRequest);
        InputStream inputStream;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            return null;
        }

        try {
            return objectMapper.readValue(inputStream, new TypeReference<Map<String, MDMConfiguration>>() {
            });
        } catch (IOException e) {
            return null;
        }
    }

    @Bean
    public JsonNode getMapping() {

        Resource resource = new ClassPathResource(mdmMapping);
        InputStream inputStream;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            return null;
        }

        try {
            return objectMapper.readTree(inputStream);
        } catch (IOException e) {
            return null;
        }
    }
}
