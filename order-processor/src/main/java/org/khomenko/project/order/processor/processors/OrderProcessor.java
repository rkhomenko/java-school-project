package org.khomenko.project.order.processor.processors;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderProcessor implements Processor {
    @Autowired
    private JavaStreamingContext streamingContext;

    @Autowired
    private JavaInputDStream<ConsumerRecord<Long, String>> orders;

    @Override
    @SneakyThrows
    public void process() {
        log.info("Order processor started");

        JavaPairDStream<Long, String> results = orders
                .mapToPair(
                        record -> new Tuple2<>(record.key(), record.value())
                );
        JavaDStream<String> lines = results
                .map(Tuple2::_2);
        JavaDStream<String> words = lines
                .flatMap(
                        x -> Arrays.asList(x.split("\\s+")).iterator()
                );
        JavaPairDStream<String, Integer> wordCounts = words
                .mapToPair(
                        s -> new Tuple2<>(s, 1)
                )
                .reduceByKey(Integer::sum);

        wordCounts.foreachRDD(
                javaRdd -> {
                    Map<String, Integer> wordCountMap = javaRdd.collectAsMap();
                    for (String key : wordCountMap.keySet()) {
                        List<Pair<String, Integer>> wordList = Arrays.asList(Pair.of(key, wordCountMap.get(key)));
                        JavaRDD<Pair<String, Integer>> rdd = streamingContext.sparkContext().parallelize(wordList);
                        rdd.collect().forEach(pair -> System.out.printf("<%s, %d>\n", pair.getLeft(), pair.getRight()));
                    }
                }
        );

        streamingContext.start();
        streamingContext.awaitTermination();
    }
}
