package integration.shop.service;

import integration.shop.model.Summary;
import integration.shop.model.OrderItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
class SummaryService {

    public Summary sumUp(List<OrderItem> items) {
        BigDecimal totalPrice = countTotalPrice(items);
        return new Summary(totalPrice, items);
    }

    private BigDecimal countTotalPrice(List<OrderItem> items) {
        return items
                .stream()
                .map(OrderItem::totalPriceAfterDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
