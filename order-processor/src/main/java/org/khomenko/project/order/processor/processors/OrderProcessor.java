package org.khomenko.project.order.processor.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.khomenko.project.core.data.models.OrderCompact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderProcessor implements Processor {
    @Value("${my.db.driver}")
    private String driver;

    @Value("${my.db.serverUrl}")
    private String serverUrl;

    @Value("${my.db.dbName}")
    private String databaseName;

    @Value("${my.db.userName}")
    private String userName;

    @Value("${my.db.password}")
    private String password;

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

            Dataset<Row> amount = spark.sql("select now(), sum(amount) as sum_amount from orders");
            amount.show();

            amount.write()
                    .mode(SaveMode.Append)
                    .format("jdbc")
                    .option("url", serverUrl)
                    .option("dbtable", "spark_orders_amount")
                    .option("user", userName)
                    .option("password", password)
                    .save();
        });

        streamingContext.start();
        streamingContext.awaitTermination();
    }
}
