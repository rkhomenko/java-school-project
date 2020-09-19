package org.khomenko.project.order.processor.main;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.ConsumerStrategy;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.khomenko.project.order.processor.processors.Processor;

import org.apache.spark.SparkConf;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = {
        "org.khomenko.project.order.processor.processors"
})
@PropertySource(value = "classpath:application.properties")
@Slf4j
public class Application {
    @Value("${my.kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value("${my.kafka.topicName}")
    private String topicName;

    @Autowired
    private Processor orderProcessor;

    @Bean
    SparkConf sparkConf(@Value("${my.spark.appName}") final String appName) {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName(appName);
        return sparkConf;
    }

    @Bean
    JavaStreamingContext javaStreamingContext(SparkConf sparkConf) {
        return new JavaStreamingContext(sparkConf, Durations.seconds(1));
    }

    @Bean
    ConsumerStrategy<Long, String> consumerStrategy() {
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
        kafkaParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        kafkaParams.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return ConsumerStrategies.Subscribe(new ArrayList<>(Collections.singletonList(topicName)), kafkaParams);
    }

    @Bean
    JavaInputDStream<ConsumerRecord<Long, String>> javaInputDStream(JavaStreamingContext streamingContext,
                                                                    ConsumerStrategy<Long, String> consumerStrategy) {
        return KafkaUtils.createDirectStream(streamingContext,
                LocationStrategies.PreferConsistent(),
                consumerStrategy);
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        Application application = context.getBean(Application.class);
        application.run(args);
    }

    public void run(String[] args) {
        orderProcessor.process();
    }
}
