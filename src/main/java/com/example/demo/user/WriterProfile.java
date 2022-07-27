package com.example.demo.user;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames = true)
@Data
public class WriterProfile {
    private String email;
    private String writerName;
    private String country;
    private String description;
}
