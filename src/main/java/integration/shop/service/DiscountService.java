package integration.shop.service;

import integration.shop.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
class DiscountService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DiscountService.class);

    private final static double SIM_CARD_DISCOUNT = 0.5;
    private final static double SMARTPHONE_DISCOUNT = 0.8;
    private final static double TABLET_DISCOUNT = 0.9;

    public OrderItem addSimDiscount(OrderItem orderItem) {
        LOGGER.info("Counting sim card discount");
        BigDecimal priceAfterDiscount = calculateDiscount(orderItem, SIM_CARD_DISCOUNT);
        orderItem.setPriceAfterDiscount(priceAfterDiscount);
        return orderItem;
    }

    public OrderItem addPhoneDiscount(OrderItem orderItem) {
        LOGGER.info("Counting smartphone discount");
        BigDecimal priceAfterDiscount = calculateDiscount(orderItem, SMARTPHONE_DISCOUNT);
        orderItem.setPriceAfterDiscount(priceAfterDiscount);
        return orderItem;
    }

    public OrderItem addTabletDiscount(OrderItem orderItem) {
        LOGGER.info("Counting tablet discount");
        BigDecimal priceAfterDiscount = calculateDiscount(orderItem, TABLET_DISCOUNT);
        orderItem.setPriceAfterDiscount(priceAfterDiscount);
        return orderItem;
    }

    private BigDecimal calculateDiscount(OrderItem item, double discount) {
        BigDecimal price = item.getItemPrice();
        BigDecimal priceAfterDiscount =  price.multiply(BigDecimal.valueOf(discount));
        LOGGER.info("Item: {}, base price = {}, after discount = {}",
                item.getItemName(),
                price,
                priceAfterDiscount);
        return priceAfterDiscount;
    }
}
