package jsl.group.microservices.core.product.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product-store")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Version
    private Integer version;
    public String productName;
    public String productBarcode;
    public String productDescription;
    public BigDecimal productPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String toString() {
        return "ProductEntity{" +
                "version=" + version +
                ", productName='" + productName + '\'' +
                ", productBarcode='" + productBarcode + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productPrice=" + productPrice +
                ", id='" + id + '\'' +
                '}';
    }
}
