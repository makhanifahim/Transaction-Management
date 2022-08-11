package TransactionMonitor.com.bbd.model;

import lombok.Data;

@Data
public class Record {
    private String init_date;
    private String conclusion_date;
    private Integer product_id;
    private Float value;

    public Record(String init_date, String conclusion_date, Integer product_id, Float value) {
        this.init_date = init_date;
        this.conclusion_date = conclusion_date;
        this.product_id = product_id;
        this.value = value;
    }
}
