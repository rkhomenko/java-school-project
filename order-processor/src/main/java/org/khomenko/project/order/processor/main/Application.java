package org.khomenko.project.order.processor.main;

import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.spark.SparkConf;
//import org.apache.spark.streaming.Durations;
//import org.apache.spark.streaming.api.java.JavaInputDStream;
//import org.apache.spark.streaming.api.java.JavaStreamingContext;
//import org.apache.spark.streaming.kafka010.ConsumerStrategies;
//import org.apache.spark.streaming.kafka010.ConsumerStrategy;
//import org.apache.spark.streaming.kafka010.KafkaUtils;
//import org.apache.spark.streaming.kafka010.LocationStrategies;
//import org.khomenko.project.order.processor.processors.Processor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//@SpringBootApplication
//@ComponentScan(basePackages = {
//        "org.khomenko.project.order.processor.processors"
//})
@Slf4j
public class Application {
//    @Value("${my.kafka.bootstrapAddress}")
//    private String bootstrapAddress;
//
//    @Value("${my.kafka.topicNames}")
//    private String[] topicNames;
//
//    @Value("${my.spark.master}")
//    private String sparkMasterUrl;
//
//    @Autowired
//    private Processor orderProcessor;
//
//    @Bean
//    SparkConf sparkConf(@Value("${my.spark.appName}") final String appName) {
//        SparkConf sparkConf = new SparkConf();
//        sparkConf.setAppName(appName);
//        sparkConf.setMaster(sparkMasterUrl);
//        return sparkConf;
//    }
//
//    @Bean
//    JavaStreamingContext javaStreamingContext(SparkConf sparkConf) {
//        return new JavaStreamingContext(sparkConf, Durations.seconds(1));
//    }
//
//    @Bean
//    ConsumerStrategy<Long, String> consumerStrategy() {
//        Map<String, Object> kafkaParams = new HashMap<>();
//        kafkaParams.put("bootstrap.servers", bootstrapAddress);
//        kafkaParams.put("key.deserializer", StringDeserializer.class);
//        kafkaParams.put("value.deserializer", StringDeserializer.class);
//        //kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
//        kafkaParams.put("auto.offset.reset", "latest");
//        kafkaParams.put("enable.auto.commit", false);
//        return ConsumerStrategies.Subscribe(new ArrayList<>(Arrays.asList(topicNames)), kafkaParams);
//    }
//
//    @Bean
//    JavaInputDStream<ConsumerRecord<Long, String>> javaInputDStream(JavaStreamingContext streamingContext,
//                                                                      ConsumerStrategy<Long, String> consumerStrategy) {
//        return KafkaUtils.createDirectStream(streamingContext,
//                LocationStrategies.PreferConsistent(),
//                consumerStrategy);
//    }

    public static void main(String[] args) {
        log.info("Kek");
    }

//    @Override
//    public void run(ApplicationArguments args) {
//        log.info("Started");
//
//        orderProcessor.process();
//    }
}
