package TransactionMonitor.com.bbd.model;

import lombok.Data;

@Data
public class Record {
    private String init_date;
    private String conclusion_date;
    private Integer product_id;
    private Float value;
}
