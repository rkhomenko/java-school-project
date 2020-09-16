package org.khomenko.project.order.generator.generators;

import lombok.extern.slf4j.Slf4j;
import org.khomenko.project.core.data.models.Customer;
import org.khomenko.project.core.data.models.Order;
import org.khomenko.project.core.data.models.PriceCategory;
import org.khomenko.project.core.data.models.Product;
import org.khomenko.project.core.data.models.ProductCategory;
import org.khomenko.project.order.generator.producers.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class MultiThreadOrderGenerator implements OrderGenerator {
    @Autowired
    OrderProducer orderProducer;

    @Override
    @Scheduled(fixedDelay = 1000)
    public void generate() {
        log.info("MultiThreadOrderGenerator started");
        Order order = Order.builder()
                .id(ThreadLocalRandom.current().nextLong())
                .customer(Customer.builder()
                        .id(ThreadLocalRandom.current().nextLong())
                        .firstName("kek")
                        .lastName("lol")
                        .meanOrderItemsCount(7)
                        .preferredPriceCategory(PriceCategory.MEDIUM)
                        .build())
                .orderDate(ZonedDateTime.now())
                .products(List.of(Product.builder()
                        .id(ThreadLocalRandom.current().nextLong())
                        .name("product")
                        .productCategory(ProductCategory.BOOKS)
                        .price(400)
                        .priceCategory(PriceCategory.HIGH)
                        .build()))
                .build();
        orderProducer.produce(order);
    }
}
