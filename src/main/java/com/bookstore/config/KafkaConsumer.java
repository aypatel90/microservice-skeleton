package com.bookstore.config;

import com.bookstore.domain.DomainObject;
import com.bookstore.exception.BusinessException;
import com.bookstore.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    public static final String TOPIC = "book-store";
    public static final String GROUP_ID = "com.bookstore";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void flightEventConsumer(@Payload DomainObject message) {
        logger.debug("Consumer consume Kafka message 1 -> {} ",message);
        logger.debug("Consumer consume Kafka message Key -> {} ",message.getKey());
        logger.debug("Consumer consume Kafka message Object -> {} ",message.getDataObject());

        if(message.getDataObject() != null) {
            for(Map.Entry<String, Object> entry : ((LinkedHashMap<String,Object>)(message.getDataObject())).entrySet()){
                if(entry.getKey().contains("email")) {
                    try {
                        emailService.sendHtmlEmail((String) entry.getValue());
                    } catch (MessagingException exception) {
                        logger.error("Unable to send mail to "+entry.getValue(),exception);
                        throw new BusinessException("Unable to send mail to "+entry.getValue());
                    }
                }
            }
        }
    }

}
