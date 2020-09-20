package org.khomenko.project.core.data.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.khomenko.project.core.data.serializers.json.JsonOrderFieldNames;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
public class OrderCompact {
    @JsonProperty(JsonOrderFieldNames.ID)
    private final Long id;

    @JsonProperty(JsonOrderFieldNames.CUSTOMER_ID)
    private final Long customerId;

    @JsonProperty(JsonOrderFieldNames.ORDER_DATE_TIME)
    private final ZonedDateTime orderDate;

    @JsonProperty(JsonOrderFieldNames.PRODUCTS)
    private final List<Product> products;

    @JsonProperty(JsonOrderFieldNames.AMOUNT)
    private final Integer amount;
}
