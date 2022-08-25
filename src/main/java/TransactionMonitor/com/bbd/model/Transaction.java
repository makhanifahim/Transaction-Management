package TransactionMonitor.com.bbd.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transaction {
    private String init_date;
    private String conclusion_date;
    private String product_id;
    private BigDecimal value;

    public Transaction(String init_date, String conclusion_date, String product_id, BigDecimal value) {
        this.init_date = init_date;
        this.conclusion_date = conclusion_date;
        this.product_id = product_id;
        this.value = value;
    }

}
