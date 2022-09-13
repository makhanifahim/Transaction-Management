package TransactionMonitor.com.bbd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionSummary {
    float mean;
    String mode;
    float standard_deviation;
    float variance;

}

