package integration.shop.flow;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.support.Consumer;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import static org.springframework.integration.dsl.IntegrationFlows.*;
import static org.springframework.integration.dsl.jms.Jms.messageDriverChannelAdapter;
import static org.springframework.integration.dsl.jms.Jms.outboundAdapter;

@Configuration("OrderFlowConfiguration")
public class OrderFlows {

    public final static String ORDER_INPUT_QUEUE = "order_input_queue";
    public final static String ORDER_CONFIRMATION_QUEUE = "order_confirmation_queue";

    @Autowired
    @Qualifier("jmsConnectionFactory")
    private ConnectionFactory connectionFactory;

    private Queue orderQueue() {
        return new ActiveMQQueue(ORDER_INPUT_QUEUE);
    }

    private Queue confirmationQueue() {
        return new ActiveMQQueue(ORDER_CONFIRMATION_QUEUE);
    }

    @Bean(name = "orderFlow")
    public IntegrationFlow orderFlow() {
        //noinspection unchecked
        return from(messageDriverChannelAdapter(connectionFactory).destination(orderQueue()))
                .filter("@orderValidator.validate(payload)")
                .split("payload.items", (Consumer) null)
                .filter("@warehouseCheckService.isAvailable(payload)")
                .routeToRecipients(r -> r
                        .recipientFlow("payload.isSimCard()", subFlow -> subFlow
                                .handle("discountService", "addSimDiscount")
                                .channel("confirmation.input"))
                        .recipientFlow("payload.isSmartphone()", subFlow -> subFlow
                                .handle("discountService", "addPhoneDiscount")
                                .channel("confirmation.input"))
                        .recipientFlow("payload.isTablet()", subFlow -> subFlow
                                .handle("discountService", "addTabletDiscount")
                                .channel("confirmation.input")))
                .get();
    }

    @Bean
    public IntegrationFlow confirmation() {
        return f -> f
                .aggregate()
                .handle("summaryService", "sumUp")
                .handle(outboundAdapter(connectionFactory).destination(confirmationQueue()));
    }

}
