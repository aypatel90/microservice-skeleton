package com.bookstore.config;

import com.bookstore.domain.DomainObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    public static final String TOPIC = "book-store";

    private final KafkaTemplate<String, DomainObject> kafkaTemplate;

    public void sendFlightEvent(DomainObject event){
        try {
            kafkaTemplate.send(TOPIC , event).get();
            logger.debug("Producer produced the message {} ",event);
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Exception : ",e);
        }
        // write your handlers and post-processing logic, based on your use case
    }

}
