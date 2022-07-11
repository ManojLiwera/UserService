package com.example.demo.kafka;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.example.demo.utils.Constants.KAFKA_ARTICLE_TOPIC;
import static com.example.demo.utils.Constants.KAFKA_BOOTSTRAP_SERVER;

@Log4j2
public class KafkaContentConsumer {

//    @Autowired private ArticleRepository articleRepository;

    Map<String, Object> consumerConfig = new HashMap<>() {
        {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVER);
            put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");
            put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        }};


    public void start() {
        DefaultKafkaConsumerFactory<String, String> kafkaConsumerFactory =
                new DefaultKafkaConsumerFactory<String, String>(consumerConfig, new StringDeserializer(), new CustomDeserializer());

        ContainerProperties containerProperties = new ContainerProperties(KAFKA_ARTICLE_TOPIC);
        containerProperties.setMessageListener(
                (MessageListener<String, Article>) record -> {
                    log.info("Received An Article Update...");
                    Article article = record.value();
//                    article.setKafkaOffset(record.offset());
                    article.setRecordTimeStamp(Timestamp.from(Instant.now()));
                    saveArticle(article);
                });

        ConcurrentMessageListenerContainer container =
                new ConcurrentMessageListenerContainer<>(
                        kafkaConsumerFactory,
                        containerProperties);

        log.info("Starting user service consumer...");
        container.start();
        log.info("Waiting for incoming content updates...");
    }


    public void saveArticle(Article article){
//        articleRepository.save(article);
        log.info("Article Persisted in DB...");

    }
}