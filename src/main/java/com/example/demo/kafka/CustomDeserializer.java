package com.example.demo.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class CustomDeserializer implements Deserializer<Article>{
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public Article deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            Article article = objectMapper.readValue(data, Article.class);
            return article;
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to Article");
        }
    }

    @Override
    public void close() {
    }
}