package integration.shop.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SimCard extends Item {

    //some additional fields

    public SimCard(String name, BigDecimal price) {
        super(name, price);
    }
}
