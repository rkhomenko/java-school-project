package org.khomenko.project.order.generator.generators;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.khomenko.project.core.data.models.Customer;
import org.khomenko.project.core.data.models.Order;
import org.khomenko.project.core.data.models.PriceCategory;
import org.khomenko.project.core.data.models.Product;
import org.khomenko.project.core.data.repositories.CustomerRepository;
import org.khomenko.project.core.data.repositories.OrderRepository;
import org.khomenko.project.core.data.repositories.ProductRepository;
import org.khomenko.project.core.util.annotations.EnableMultiThreadScheduled;
import org.khomenko.project.core.util.annotations.MultiThreadScheduled;
import org.khomenko.project.order.generator.producers.OrderProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@ConditionalOnProperty(value = "my.generation", havingValue = "true")
@EnableMultiThreadScheduled
@Slf4j
public class MultiThreadOrderGenerator implements OrderGenerator {
    @Autowired
    OrderProducer orderProducer;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private final List<Customer> customers = new ArrayList<>();
    private final Map<PriceCategory, List<Product>> priceProductMap = new ConcurrentHashMap<>();

    private static <T> T getRandomValue(List<T> list) {
        int idx = ThreadLocalRandom.current().nextInt(0, list.size());
        return list.get(idx);
    }

    private static int generateRandomFromMean(int mean) {
        final double variability = 0.25;
        int value = (int) (variability * mean);

        if (mean - value < 1) {
            throw new RuntimeException("Bad min value");
        }

        if (value == 0) {
            return ThreadLocalRandom.current().nextInt(mean, mean + 1);
        }

        return ThreadLocalRandom.current().nextInt(mean - value, mean + value);
    }

    @PostConstruct
    public void initCache() {
        customerRepository.findAll().forEach(customers::add);
        productRepository.findAll().forEach(product -> {
            List<Product> products;
            if (priceProductMap.containsKey(product.getPriceCategory())) {
                products = priceProductMap.get(product.getPriceCategory());
            } else {
                products = new ArrayList<>();
            }
            products.add(product);
            priceProductMap.put(product.getPriceCategory(), products);
        });

        log.info("Cache loaded: {} customers", customers.size());
    }

    @Override
    @Transactional
    @MultiThreadScheduled(threads = 4,
            initDelay = 200,
            initDelayVar = 100,
            period = 300,
            periodVar = 100
    )
    @SneakyThrows
    public void generate() {
        Customer customer = getRandomValue(customers);
        int productCount = generateRandomFromMean(customer.getMeanOrderItemsCount());
        List<Product> availableProducts = priceProductMap.get(customer.getPreferredPriceCategory());
        List<Product> products = Stream.generate(() -> getRandomValue(availableProducts))
                .limit(productCount)
                .collect(Collectors.toList());

        Order order = Order.builder()
                .customer(customer)
                .orderDate(ZonedDateTime.now())
                .products(products)
                .amount(products.stream()
                        .mapToInt(Product::getPrice)
                        .sum())
                .build();

        orderRepository.save(order);
        orderProducer.produce(order);
    }
}
