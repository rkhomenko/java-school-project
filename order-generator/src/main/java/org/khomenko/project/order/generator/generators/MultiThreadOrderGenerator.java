package org.khomenko.project.order.generator.generators;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.khomenko.project.core.data.models.Customer;
import org.khomenko.project.core.data.models.Order;
import org.khomenko.project.core.data.models.PriceCategory;
import org.khomenko.project.core.data.models.Product;
import org.khomenko.project.core.data.models.ProductCategory;
import org.khomenko.project.core.data.repositories.CustomerRepository;
import org.khomenko.project.core.util.annotations.EnableMultiThreadScheduled;
import org.khomenko.project.core.util.annotations.MultiThreadScheduled;
import org.khomenko.project.order.generator.producers.OrderProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@EnableMultiThreadScheduled
@Slf4j
public class MultiThreadOrderGenerator implements OrderGenerator {
    @Autowired
    OrderProducer orderProducer;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Transactional
    @MultiThreadScheduled(threads = 4, initDelay = 200, period = 300)
    @SneakyThrows
    public void generate() {
        log.info("MultiThreadOrderGenerator started. {}", Thread.currentThread().getName());

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

        customerRepository.findByLastName("kek");
    }
}
