package org.khomenko.project.core.data.serializers.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.khomenko.project.core.data.models.Order;
import org.khomenko.project.core.data.models.Product;

import java.io.IOException;

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

        gen.writeNumberField("id", order.getId());
        gen.writeNumberField("customerId", order.getCustomer().getId());
        gen.writeStringField("orderDateTime", order.getOrderDate().toString());

        gen.writeFieldName("products");
        gen.writeArray(products, 0, products.length);

        gen.writeEndObject();
    }
}
