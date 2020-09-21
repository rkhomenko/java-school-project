package org.khomenko.project.order.processor.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.size;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.khomenko.project.core.data.models.OrderCompact;
import org.khomenko.project.core.data.serializers.json.JsonOrderFieldNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderProcessor implements Processor {
    @Value("${my.db.serverUrl}")
    private String serverUrl;

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
            ordersDataset = ordersDataset.withColumn("products_count", size(ordersDataset.col(JsonOrderFieldNames.PRODUCTS)));
            ordersDataset.createOrReplaceTempView("orders_view");

            ordersDataset.show(10);

            Dataset<Row> amount = spark.sql("select now() as time, sum(amount) as total_amount," +
                    "mean(amount) as mean_amount," +
                    "min(amount) as min_amount," +
                    "max(amount) as max_amount," +
                    "mean(products_count) as mean_products_count," +
                    "count(id) as orders_count" +
                    " from orders_view");

            amount.write()
                    .mode(SaveMode.Append)
                    .format("jdbc")
                    .option("url", serverUrl)
                    .option("dbtable", "spark_statistics")
                    .option("user", userName)
                    .option("password", password)
                    .save();
        });

        streamingContext.start();
        streamingContext.awaitTermination();
    }
}
