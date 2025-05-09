package jsl.group.shipping_service.config;

import jsl.group.commons.event.Event;
import jsl.group.commons.models.ShippingItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;

@Configuration
public class ShippingProcessingConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ShippingProcessingConfiguration.class);

    @Bean
    public Consumer<Event<Integer, List<ShippingItems>>> shippingProcessing() {
        return integerListEvent -> {
            log.info("Items to ship: {}: {}", integerListEvent.getZonedDateTime(), integerListEvent.getData());
        };
    }
}
