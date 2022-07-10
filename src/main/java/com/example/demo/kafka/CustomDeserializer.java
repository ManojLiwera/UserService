package com.example.demo.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Map;

@Slf4j
public class CustomDeserializer<T extends Serializable> implements Deserializer<T>{
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            System.out.println("Null received at deserializing");
            return null;
        }
        try {
            T value = (T) SerializationUtils.deserialize(data);
            return value;
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            throw new SerializationException("Error when serializing Article to byte[]");
        }
    }

    @Override
    public void close() {
    }
}