package TransactionMonitor.com.bbd.service;

import TransactionMonitor.com.bbd.model.Transaction;
import TransactionMonitor.com.bbd.model.TransactionSummary;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionTimeDeltaSummaryService {
    @Autowired
    TransactionService transactionService;
    private float mode(long[] array) {
        long mode = array[0];
        int maxCount = 0;
        for (long value : array) {
            int count = 0;
            for (long l : array) {
                if (l == value) count++;
                if (count > maxCount) {
                    mode = value;
                    maxCount = count;
                }
            }
        }
        if (maxCount > 1) {
            return mode;
        }
        return 0;
    }
    private float variance(long[] array, long mean,String TypeOfData,String product_id,Date from_date,Date to_date) throws IOException, ParseException, CsvException {
        float XSeq;
        float TotalXX = 0;
        int TotalTran=0;
        for (int t = 0; t< (long) array.length; t++){
            TotalTran++;
            XSeq=array[t]-mean;
            XSeq=XSeq*XSeq;
            TotalXX=TotalXX+XSeq;
        }
        return TotalXX/TotalTran;

    }
    public TransactionSummary timeDelta(String TypeOfData, String product_id, Date from_date, Date to_date) throws IOException, CsvException, ParseException {
        if(Objects.equals(TypeOfData, "") ||TypeOfData==null)
            TypeOfData="Data";
        List<Transaction> transaction=transactionService.allTransaction(TypeOfData,product_id,from_date,to_date);
        long TotalProcessSeconds = 0;
        long TotalTransaction=0;
        long[] values = new long[transaction.size()];
        for(int t=0;t<transaction.size();t++){
            Transaction row=transaction.get(t);
            ZonedDateTime dateFrom=row.getInit_date();
            ZonedDateTime dateTo=row.getConclusion_date();
            TotalProcessSeconds = TotalProcessSeconds+(dateTo.toEpochSecond() - dateFrom.toEpochSecond());
            TotalTransaction++;
            values[t] = TotalProcessSeconds;
        }
        float variance = variance(values,TotalProcessSeconds/TotalTransaction,TypeOfData,product_id,from_date,to_date);
        return new TransactionSummary((float) TotalProcessSeconds/TotalTransaction,String.valueOf(mode(values)),(float) TotalProcessSeconds/TotalTransaction, variance);
    }
}
