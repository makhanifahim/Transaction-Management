package TransactionMonitor.com.bbd.service;

import TransactionMonitor.com.bbd.model.TransactionSummary;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
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
            log.info("Maximum Count is "+maxCount);
            return mode;
        }
        return 0;
    }
    private float variance(long[] array, long mean,String TypeOfData,String product_id,Date from_date,Date to_date) throws IOException, ParseException, CsvException {
        float XSeq;
        float TotalXX = 0;
        int TotalTran=0;
        List<String[]> allTransaction = transactionService.allTransaction(TypeOfData,product_id,from_date,to_date);
        for (int t = 0; t< (long) array.length; t++){
            TotalTran++;
            XSeq=array[t]-mean;
            XSeq=XSeq*XSeq;
            TotalXX=TotalXX+XSeq;
        }
        log.info("User has Requested for Variance {}",TotalXX/TotalTran);
        return TotalXX/TotalTran;

    }
    public TransactionSummary timeDelta(String TypeOfData, String product_id, Date from_date, Date to_date) throws IOException, CsvException, ParseException {
        if(Objects.equals(TypeOfData, "") ||TypeOfData==null)
            TypeOfData="Data";
        List<String[]> transaction=transactionService.allTransaction(TypeOfData,product_id,from_date,to_date);
        long TotalProcessMilliSeconds = 0;
        long TotalTransaction=0;
        long[] values = new long[transaction.size()];
        for(int t=0;t<transaction.size();t++){
            String[] row= (String[]) transaction.get(t);
            Date dateFrom=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[0]);
            Date dateTo=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[1]);
            TotalProcessMilliSeconds = TotalProcessMilliSeconds+(dateTo.getTime() - dateFrom.getTime());
            TotalTransaction++;
            values[t] = TotalProcessMilliSeconds;
        }
        log.info("User has Requested for TimeDelta details"+"Mean="+TotalProcessMilliSeconds/TotalTransaction+" Mode="+String.valueOf(mode(values))+" Standard Deviation="+Math.sqrt((float)TotalProcessMilliSeconds/TotalTransaction));
        float variance = variance(values,TotalProcessMilliSeconds/TotalTransaction,TypeOfData,product_id,from_date,to_date);
        return new TransactionSummary((float) TotalProcessMilliSeconds/TotalTransaction,String.valueOf(mode(values)),(float) TotalProcessMilliSeconds/TotalTransaction, variance);
    }
}