package integration.shop.model;


import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

@Value
public class Summary implements Serializable {

    private BigDecimal totalPrice;
    private Collection<OrderItem> items;

}
