package integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("integration.shop")
class IntegrationApp {

    public static void main(String[] args) {
        SpringApplication.run(IntegrationApp.class, args);
    }
}
