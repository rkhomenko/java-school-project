package org.khomenko.project.order.generator.generators;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.khomenko.project.core.data.models.Customer;
import org.khomenko.project.core.data.models.Order;
import org.khomenko.project.core.data.models.PriceCategory;
import org.khomenko.project.core.data.models.Product;
import org.khomenko.project.core.data.models.ProductCategory;
import org.khomenko.project.order.generator.producers.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
class OrderSender implements Runnable {
    private final OrderProducer orderProducer;

    @Override
    public void run() {
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

@Service
@Slf4j
public class MultiThreadOrderGenerator implements OrderGenerator {
    @Autowired
    OrderProducer orderProducer;

    @Value(value = "${my.generator.threads}")
    private Integer threadNum;

    @Override
    @PostConstruct
    @SneakyThrows
    public void generate() {
        log.info("MultiThreadOrderGenerator started");

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(threadNum);

        for (int i = 0; i < threadNum; i++) {
            executorService.scheduleAtFixedRate(new OrderSender(orderProducer),
                    ThreadLocalRandom.current().nextInt(100, 300),
                    ThreadLocalRandom.current().nextInt(200, 300),
                    TimeUnit.MILLISECONDS);
        }
    }
}
