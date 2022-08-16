package TransactionMonitor.com.bbd.model;

import lombok.Data;

@Data
public class TransactionValueSummary {
    float mean;
    String mode;
    float standardDeviation;
    float variance;

    public TransactionValueSummary(float mean, String mode, float standardDeviation, float variance) {
        this.mean = mean;
        this.mode = mode;
        this.standardDeviation = standardDeviation;
        this.variance = variance;
    }
}

