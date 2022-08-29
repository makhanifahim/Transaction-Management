package TransactionMonitor.com.bbd.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class Transaction {
    private ZonedDateTime init_date;
    private ZonedDateTime conclusion_date;
    private String product_id;
    private BigDecimal value;

    public Transaction(String init_date,String conclusion_date, String product_id, BigDecimal value) {
        this.init_date = ZonedDateTime.parse(init_date);
        this.conclusion_date = ZonedDateTime.parse(conclusion_date);
        this.product_id = product_id;
        this.value = value;
    }

}
