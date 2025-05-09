package jsl.group.checkout_service.config;

import jsl.group.commons.InventoryServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class CheckoutClientConfiguration {
    @Bean
    public InventoryServiceGrpc.InventoryServiceBlockingStub inventoryServiceBlockingStub(GrpcChannelFactory channelFactory) {
        return InventoryServiceGrpc.newBlockingStub(channelFactory.createChannel("inventory"));
    }
}
