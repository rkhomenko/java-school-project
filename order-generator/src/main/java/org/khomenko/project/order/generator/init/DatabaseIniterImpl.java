package org.khomenko.project.order.generator.init;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import org.khomenko.project.core.data.models.Customer;
import org.khomenko.project.core.data.models.PriceCategory;
import org.khomenko.project.core.data.models.Product;
import org.khomenko.project.core.data.models.ProductCategory;
import org.khomenko.project.core.data.repositories.CustomerRepository;
import org.khomenko.project.core.data.repositories.OrderRepository;
import org.khomenko.project.core.data.repositories.ProductRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class DatabaseIniterImpl implements DatabaseIniter {
    private static final int CUSTOMERS_COUNT = 10000;
    private static final int PRODUCTS_COUNT = 100000;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private Faker faker;

    @Override
    @Transactional
    public void init() {
        log.info("Database init started");
        // Delete all data

        log.info("Delete all orders");
        orderRepository.deleteAll();

        log.info("Delete all products");
        productRepository.deleteAll();

        log.info("Delete all customers");
        customerRepository.deleteAll();

        // Create all data
        log.info("Create all customers");
        customerRepository.saveAll(Stream.generate(() -> {
            int meanOrderItemsCount = ThreadLocalRandom.current().nextInt(1, 20);
            int priceCategoryIdx = ThreadLocalRandom.current().nextInt(PriceCategory.values().length);

            return Customer.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .meanOrderItemsCount(meanOrderItemsCount)
                    .preferredPriceCategory(PriceCategory.values()[priceCategoryIdx])
                    .build();
        }).limit(CUSTOMERS_COUNT).collect(Collectors.toList()));

        log.info("Create all products");
        productRepository.saveAll(Stream.generate(() -> {
            int priceCategoryIdx = ThreadLocalRandom.current().nextInt(PriceCategory.values().length);
            PriceCategory priceCategory = PriceCategory.values()[priceCategoryIdx];
            int price = ThreadLocalRandom.current().nextInt(priceCategory.minCost, priceCategory.maxCost);

            int productCategoryIdx = ThreadLocalRandom.current().nextInt(ProductCategory.values().length);
            ProductCategory productCategory = ProductCategory.values()[productCategoryIdx];

            return Product.builder()
                    .name(UUID.randomUUID().toString())
                    .productCategory(productCategory)
                    .priceCategory(priceCategory)
                    .price(price)
                    .build();
        }).limit(PRODUCTS_COUNT).collect(Collectors.toList()));
    }
}
