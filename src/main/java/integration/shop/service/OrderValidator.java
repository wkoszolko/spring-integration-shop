package integration.shop.service;

import integration.shop.model.Order;
import org.springframework.stereotype.Service;

@Service
class OrderValidator {

    /**
     * Order should have more then one item
     */
    public boolean validate(Order order) {
        return order.getItems()!=null && order.getItems().size()>0;
    }
}
