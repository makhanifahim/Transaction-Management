package TransactionMonitor.com.bbd.model;

import lombok.Data;

@Data
public class Transaction {
    private String init_date;
    private String conclusion_date;
    private String product_id;
    private Float value;

    public Transaction(String init_date, String conclusion_date, String product_id, Float value) {
        this.init_date = init_date;
        this.conclusion_date = conclusion_date;
        this.product_id = product_id;
        this.value = value;
    }

    public Transaction(String init_date, String conclusion_date, String product_id, String s1) {
        this.init_date = init_date;
        this.conclusion_date = conclusion_date;
        this.product_id = product_id;
        this.value = Float.valueOf(s1);
    }
}
