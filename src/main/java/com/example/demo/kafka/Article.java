package com.example.demo.kafka;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Table(name = "articles")
@ToString(includeFieldNames = true)
@Data
public class Article implements Serializable {

    @Id
    @GeneratedValue
    private String articleId;
    private String category;
    private String title;
    private String writerName;
    private String body;
    private Timestamp recordTimeStamp;
    private String recordDate;
    private String status;
//    private Long sequenceNumber;
//    private Long kafkaOffset;
}