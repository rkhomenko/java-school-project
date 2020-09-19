package org.khomenko.project.order.processor.main;

import lombok.extern.slf4j.Slf4j;

import org.khomenko.project.order.processor.processors.Processor;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

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
    JavaSparkContext javaStreamingContext(SparkConf sparkConf) {
        return new JavaSparkContext(sparkConf);
    }
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
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        Application application = context.getBean(Application.class);
        application.run(args);
    }

    public void run(String[] args) {
        orderProcessor.process();
    }
}
