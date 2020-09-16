package org.khomenko.project.order.generator.main;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.khomenko.project.core.data.models.Customer;
import org.khomenko.project.core.data.models.Order;
import org.khomenko.project.core.data.models.Product;
import org.khomenko.project.order.generator.init.DatabaseIniter;

import org.khomenko.project.order.generator.producers.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = {
        "org.khomenko.project.order.generator.init",
        "org.khomenko.project.order.generator.producers"
})
@EntityScan("org.khomenko.project.core.data.models")
@EnableJpaRepositories("org.khomenko.project.core.data.repositories")
@Slf4j
public class Application implements ApplicationRunner {
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Autowired
    private DatabaseIniter databaseIniter;

    @Autowired
    private OrderProducer orderProducer;

    @Bean
    Faker javaFaker() {
        return new Faker();
    }

    @Bean
    KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    NewTopic orderGeneratorTopic() {
        return new NewTopic("orders", 1, (short) 1);
    }

    @Bean
    public ProducerFactory<String, Order> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Order> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (args.containsOption("init")) {
            databaseIniter.init();
        }

        Order order = Order.builder()
                .customer(Customer.builder().build())
                .products(List.of(Product.builder().name("product").build()))
                .build();
        orderProducer.produce(order);
    }
}
