package TransactionMonitor.com.bbd.model;

import lombok.Data;

@Data
public class Product {
    String product_id;

    public Product(String product) {
        this.product_id = product;
    }
}
