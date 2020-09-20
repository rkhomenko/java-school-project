package org.khomenko.project.order.processor.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.khomenko.project.core.data.models.OrderCompact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderProcessor implements Processor {
    @Autowired
    private JavaStreamingContext streamingContext;

    @Autowired
    private JavaInputDStream<ConsumerRecord<Long, String>> events;

    @Override
    @SneakyThrows
    public void process() {
        log.info("Order processor started");

        JavaDStream<OrderCompact> orders = events.map(record -> {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(record.value(), OrderCompact.class);
        });

        orders.foreachRDD((rdd, time) -> {
            SparkSession spark = SparkSession.builder().config(rdd.context().getConf()).getOrCreate();
            Dataset<Row> ordersDataset = spark.createDataFrame(rdd, OrderCompact.class);
            ordersDataset.createOrReplaceTempView("orders");

            Dataset<Row> amount = spark.sql("select sum(amount) from orders");
            log.info("============================ {} ============================", time);
            amount.show();
        });

        streamingContext.start();
        streamingContext.awaitTermination();
    }
}
