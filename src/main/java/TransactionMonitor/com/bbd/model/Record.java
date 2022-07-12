package TransactionMonitor.com.bbd.model;

import lombok.Data;

import java.util.Date;

@Data
public class Record {
    private Date init_date;
    private Date conclusion_date;
    private Integer product_id;
    private Float value;
}
