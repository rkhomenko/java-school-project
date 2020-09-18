package org.khomenko.project.order.processor.processors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.Arrays;

@Service
public class OrderProcessor implements Processor {
    @Autowired
    JavaInputDStream<ConsumerRecord<Long, String>> orders;

    @Override
    public void process() {
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
    }
}
