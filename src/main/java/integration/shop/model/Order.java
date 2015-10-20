package integration.shop.model;


import lombok.*;

import java.io.Serializable;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Order implements Serializable {

    private long id;
    private Collection<OrderItem> items;

}
