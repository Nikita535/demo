package com.example.demo.service.impl;

import com.example.demo.dto.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationService {
    private KafkaTemplate<String,String> kafkaTemplate;

    public NotificationService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @SneakyThrows
    public void sendMessage(Notification message) {
        kafkaTemplate.send("notification-topic",new ObjectMapper().writeValueAsString(message));
    }

    public Notification createNotification(List<String> emails, String theme, String message,String key) {
        return new Notification().setEmails(emails).setTheme(theme).setMessage(message).setKey(key);
    }
}
