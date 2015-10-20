package integration.shop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public abstract class Item implements Serializable {

    private String name;
    private BigDecimal price;

}
