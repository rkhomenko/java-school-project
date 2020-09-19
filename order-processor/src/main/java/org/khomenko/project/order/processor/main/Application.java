package org.khomenko.project.order.processor.main;

import lombok.extern.slf4j.Slf4j;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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

        SparkConf sparkConf = new SparkConf().setAppName("order-processor");

        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<String> inputFile = sparkContext.textFile("/home/rk/source/java-school-project/file.txt");

        JavaRDD<String> words = inputFile.flatMap(content -> Arrays.asList(content.split(" ")).iterator());
        JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<>(s, 1));

        JavaPairRDD<String, Integer> counts = ones.reduceByKey(Integer::sum);

        List<Tuple2<String, Integer>> output = counts.collect();
        for (Tuple2<?,?> tuple : output) {
            System.out.println(tuple._1() + ": " + tuple._2());
        }

        sparkContext.stop();
    }

//    @Override
//    public void run(ApplicationArguments args) {
//        log.info("Started");
//
//        orderProcessor.process();
//    }
}
