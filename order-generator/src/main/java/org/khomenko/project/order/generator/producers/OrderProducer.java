package org.khomenko.project.order.generator.producers;

import org.khomenko.project.core.data.models.Order;

public interface OrderProducer {
    void produce(Order order);
}
