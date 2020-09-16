package org.khomenko.project.order.generator.producers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.khomenko.project.core.data.models.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaOrderProducer implements OrderProducer {
    private final String topicName;

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    @Autowired
    public KafkaOrderProducer(@Value("${my.kafka.topicName}") final String topicName) {
        this.topicName = topicName;
    }

    @Override
    public void produce(Order order) {
        kafkaTemplate.send(topicName, order);
    }
}
