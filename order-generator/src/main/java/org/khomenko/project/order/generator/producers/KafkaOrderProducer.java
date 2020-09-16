package org.khomenko.project.order.generator.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.khomenko.project.core.data.models.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaOrderProducer implements OrderProducer {
    private final String topicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaOrderProducer(@Value("${my.kafka.topicName}") final String topicName) {
        this.topicName = topicName;
    }

    @Override
    @SneakyThrows
    public void produce(Order order) {
        ObjectMapper objectMapper = new ObjectMapper();
        kafkaTemplate.send(topicName, objectMapper.writeValueAsString(order));
    }
}
