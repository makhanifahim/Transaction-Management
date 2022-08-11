package TransactionMonitor.com.bbd.model;

import lombok.Data;

import java.sql.Date;

@Data
public class DatesBetween {
    private Date dateFrom;
    private Date dateTo;

    public DatesBetween(String dateFrom, String dateTo) {
        this.dateFrom = Date.valueOf(dateFrom);
        this.dateTo = Date.valueOf(dateTo);
    }
}
