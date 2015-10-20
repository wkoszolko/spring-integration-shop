package integration.shop.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Tablet extends Item {

    //some additional fields

    public Tablet(String name, BigDecimal price) {
        super(name, price);
    }
}
