package jsl.group.microservices.integration.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jsl.group.commons.event.EventType;
import jsl.group.commons.models.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("product")
@SecurityRequirement(name = "security_authentication")
@Tag(name = "Product Integration Services", description = "APIs about product services component")
public class IntegrationController {
    private final RestTemplate restTemplate;
    private final IntegrationService integrationService;

    public IntegrationController(RestTemplate restTemplate, IntegrationService integrationService) {
        this.restTemplate = restTemplate;
        this.integrationService = integrationService;
    }

    @PostMapping(consumes = "application/json", produces = "plain/text")
    public ResponseEntity<String> addProduct(@Valid @RequestBody ProductRequest productRequest, UriComponentsBuilder uriComponentsBuilder) {
        String addedProductId = integrationService.createProduct(productRequest);
        URI location = uriComponentsBuilder.path("/product/{productId}").buildAndExpand(addedProductId).toUri();
        return ResponseEntity.ok(location.toString());
    }

    @GetMapping(value = "/{product-id}", produces = "application/json")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable(name = "product-id") String productId) {
        return ResponseEntity.ok(integrationService.getProduct(productId));
    }

    @PutMapping(value = "/{product-id}", produces = "application/json")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable(name = "product-id") String productId, @Valid @RequestBody ProductRequest productRequest) {
        productRequest.setId(productId);
        return ResponseEntity.ok(integrationService.updateProduct(productRequest));
    }

    @DeleteMapping(value = "/{product-id}", produces = "plain/text")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "product-id") String productId) {
        return ResponseEntity.ok(integrationService.deleteProduct(productId));
    }

    @PostMapping(value = "/inventory/increment/{product-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InventoryResponse> incrementInventory(@PathVariable(name = "product-id") String productId,
                                                                @Valid @RequestBody ProductDetailsRequest productDetailsRequest) {
        productDetailsRequest.setProductId(productId);
        return ResponseEntity.ok(integrationService.addProductCount(productDetailsRequest));
    }

    @PostMapping(value = "/inventory/decrement/{product-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InventoryResponse> decrementInventory(@PathVariable(name = "product-id") String productId,
                                                      @Valid @RequestBody ProductDetailsRequest productDetailsRequest) {
        productDetailsRequest.setProductId(productId);
        return ResponseEntity.ok(integrationService.subtractProductCount(productDetailsRequest));
    }

    @PostMapping(value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void purchaseOrderedItems(@RequestBody OrderItemList orderItemList, @RequestParam(value = "event") String event) {
        EventType eventType = EventType.valueOf(event.toUpperCase());
        OrderItemList newOrderItemList = new OrderItemList(orderItemList.orderItemList(), eventType);
        restTemplate.postForObject("http://order-service:8080/order/purchase", newOrderItemList, Void.class);
    }
}
