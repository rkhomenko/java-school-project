package org.khomenko.project.order.processor.processors;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class OrderProcessor implements Processor {
    @Autowired
    JavaStreamingContext streamingContext;

    @Override
    @SneakyThrows
    public void process() {
        log.info("Order processor started");

        JavaRDD<String> inputFile = streamingContext.sparkContext().textFile("/home/rk/source/java-school-project/file.txt");
        JavaRDD<String> words = inputFile.flatMap(content -> Arrays.asList(content.split(" ")).iterator());
        JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<>(s, 1));
        JavaPairRDD<String, Integer> counts = ones.reduceByKey(Integer::sum);

        List<Tuple2<String, Integer>> output = counts.collect();
        for (Tuple2<?, ?> tuple : output) {
            System.out.println(tuple._1() + ": " + tuple._2());
        }

        streamingContext.awaitTermination();
    }
}
