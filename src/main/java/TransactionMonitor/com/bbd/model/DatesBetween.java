package TransactionMonitor.com.bbd.model;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class DatesBetween {
    private Date dateFrom;
    private Date dateTo;

    public DatesBetween(String dateFrom, String dateTo) throws ParseException {
        this.dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse(dateFrom);
        this.dateTo = new SimpleDateFormat("yyyy-MM-dd").parse(dateTo);
    }
}
