package integration;

import integration.config.JmsTemplateConfig;
import integration.shop.flow.OrderFlows;
import integration.shop.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {IntegrationApp.class, JmsTemplateConfig.class})
public class OrderFlowTest {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    public void testSimpleOrder() {
        //given
        Order order = order();

        //when
        jmsTemplate.convertAndSend(OrderFlows.ORDER_INPUT_QUEUE, order);
        Summary result = (Summary) jmsTemplate.receiveAndConvert(OrderFlows.ORDER_CONFIRMATION_QUEUE);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(614L));
    }

    private Order order() {
        Order order = new Order();
        order.setId(1L);
        order.setItems(Arrays.asList(
                buildSimCard("SIM CARD", BigDecimal.valueOf(10), 6),
                buildTable("iPad", BigDecimal.valueOf(80), 2),
                buildSmartphone("iPhone 5", BigDecimal.valueOf(100), 1),
                buildSmartphone("iPhone 6", BigDecimal.valueOf(150), 3))
        );
        return order;
    }

    private OrderItem buildSmartphone(String name, BigDecimal price, int count) {
        Smartphone smartphone = new Smartphone(name, price);
        return orderItem(smartphone, count);
    }

    private OrderItem buildSimCard(String name, BigDecimal price, int count) {
        SimCard simCard = new SimCard(name, price);
        return orderItem(simCard, count);
    }

    private OrderItem buildTable(String name, BigDecimal price, int count) {
        Tablet tablet = new Tablet(name, price);
        return orderItem(tablet, count);
    }

    private OrderItem orderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCount(count);
        orderItem.setItem(item);
        return orderItem;
    }
}
