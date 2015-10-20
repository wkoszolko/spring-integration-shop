package integration.shop.flow;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.support.Consumer;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

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

    @Bean(name = "confirmationChannel")
    public DirectChannel confirmationChannel() {
        return new DirectChannel();
    }

    @Bean(name = "orderFlow")
    public IntegrationFlow orderFlow() {
        return IntegrationFlows
                .from(messageDriverChannelAdapter(connectionFactory).destination(orderQueue()))
                .filter("@orderValidator.validate(payload)")
                .split("payload.items", (Consumer) null)
                .filter("@warehouseCheckService.isAvailable(payload)")
                .routeToRecipients(r -> r
                        .recipientFlow("payload.isSimCard()", subFlow -> subFlow
                                .handle("discountService", "addSimDiscount")
                                .channel("confirmationChannel"))
                        .recipientFlow("payload.isSmartphone()", subFlow -> subFlow
                                .handle("discountService", "addPhoneDiscount")
                                .channel("confirmationChannel"))
                        .recipientFlow("payload.isTablet()", subFlow -> subFlow
                                .handle("discountService", "addTabletDiscount")
                                .channel("confirmationChannel")))
                .get();
    }

    @Bean(name = "confirmationFlow")
    public IntegrationFlow confirmationFlow() {
        return IntegrationFlows
                .from("confirmationChannel")
                .aggregate()
                .handle("summaryService", "sumUp")
                .handle(outboundAdapter(connectionFactory).destination(confirmationQueue()))
                .get();
    }

}
