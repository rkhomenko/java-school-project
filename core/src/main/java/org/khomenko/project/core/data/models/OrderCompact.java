package org.khomenko.project.core.data.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.khomenko.project.core.data.serializers.json.JsonOrderFieldNames;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderCompact implements Serializable {
    @JsonProperty(JsonOrderFieldNames.ID)
    private Long id;

    @JsonProperty(JsonOrderFieldNames.CUSTOMER_ID)
    private Long customerId;

    @JsonProperty(JsonOrderFieldNames.ORDER_DATE)
    private String orderDate;

    @JsonProperty(JsonOrderFieldNames.ORDER_TIME)
    private String orderTime;

    @JsonProperty(JsonOrderFieldNames.PRODUCTS)
    private List<Long> products;

    @JsonProperty(JsonOrderFieldNames.AMOUNT)
    private Integer amount;
}
