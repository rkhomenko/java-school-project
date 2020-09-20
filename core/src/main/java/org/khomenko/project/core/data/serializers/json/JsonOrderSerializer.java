package org.khomenko.project.core.data.serializers.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.khomenko.project.core.data.models.Order;
import org.khomenko.project.core.data.models.Product;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.ZonedDateTime;

public class JsonOrderSerializer extends StdSerializer<Order> {
    public JsonOrderSerializer() {
        this(null);
    }

    protected JsonOrderSerializer(Class<Order> t) {
        super(t);
    }

    @Override
    public void serialize(Order order, JsonGenerator gen, SerializerProvider provider) throws IOException {
        long[] products = order.getProducts().stream()
                .mapToLong(Product::getId)
                .toArray();

        gen.writeStartObject();

        gen.writeNumberField(JsonOrderFieldNames.ID, order.getId());
        gen.writeNumberField(JsonOrderFieldNames.CUSTOMER_ID, order.getCustomer().getId());
        gen.writeNumberField(JsonOrderFieldNames.AMOUNT, order.getAmount());

        ZonedDateTime zonedDateTime = order.getOrderDate();
        gen.writeStringField(JsonOrderFieldNames.ORDER_DATE, Date.valueOf(zonedDateTime.toLocalDate()).toString());
        gen.writeStringField(JsonOrderFieldNames.ORDER_TIME, Time.valueOf(zonedDateTime.toLocalTime()).toString());

        gen.writeFieldName(JsonOrderFieldNames.PRODUCTS);
        gen.writeArray(products, 0, products.length);

        gen.writeEndObject();
    }
}
