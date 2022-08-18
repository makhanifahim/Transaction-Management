package TransactionMonitor.com.bbd.model;

import lombok.Data;

@Data
public class TransactionSummary {
    float mean;
    String mode;
    float standard_deviation;
    float variance;

    public TransactionSummary(float mean, String mode, float standard_deviation, float variance) {
        this.mean = mean;
        this.mode = mode;
        this.standard_deviation = standard_deviation;
        this.variance = variance;
    }
}

