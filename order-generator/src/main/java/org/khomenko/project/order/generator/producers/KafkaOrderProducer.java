package org.khomenko.project.order.generator.producers;

import org.khomenko.project.core.data.models.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaOrderProducer implements OrderProducer {
    @Value(value = "${kafka.topicName}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    @Override
    public void produce(Order order) {
        kafkaTemplate.send(topicName, order);
    }
}
