package TransactionMonitor.com.bbd.model;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class DatesBetween {
    private ZonedDateTime dateFrom;
    private ZonedDateTime dateTo;

}
