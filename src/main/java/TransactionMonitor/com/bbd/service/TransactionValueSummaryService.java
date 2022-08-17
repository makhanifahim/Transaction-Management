package TransactionMonitor.com.bbd.service;

import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
@Service
@Slf4j
public class TransactionValueSummaryService {
    @Autowired
    TransactionService transactionService = new TransactionService();

    private static String findModel(String[][] values){
        int count=0;
        String mode="";
        for (String[] value : values) {
            if (value[0] != null) {
                if (Integer.parseInt(value[1]) > count) {
                    count = Integer.parseInt(value[1]) + 1;
                    mode = value[0];
                }
            }
        }
        return mode;
    }
    public float meanOfTransaction(Date from_date, Date to_date, String product_id, String TypeOfData) throws IOException, CsvException, ParseException {
        List<String[]> allTransaction = transactionService.allTransaction(TypeOfData,product_id,from_date,to_date);
        int TotalDays=0;
        float TotalAmount=0;
        for (int t = 0; t< (long) allTransaction.size(); t++){
            String[] Transaction;
            Transaction= allTransaction.get(t);
            TotalDays++;
            TotalAmount= Float.parseFloat(Transaction[3])+TotalAmount;
        }
        log.info("User has Requested for Mean which is {}",TotalAmount/TotalDays);
        return TotalAmount/TotalDays;
    }
    public String modeOfTransaction(Date from_date,Date to_date,String product_id,String TypeOfData) throws IOException, CsvException, ParseException {
        int c=0;
        List<String[]> allTransaction = transactionService.allTransaction(TypeOfData,product_id,from_date,to_date);
        String[][] values = new String[allTransaction.size()][2];
        int present;
        for (int t = 0; t< (long) allTransaction.size(); t++){
            String[] row;
            row= allTransaction.get(t);
            present=0;
            for(int i=0;i<values.length;i++){
                if(values[i][0]!=null) {
                    if(values[i][0].equals(row[3])){
                        present=1;
                        values[i][1]= String.valueOf(Integer.parseInt(values[i][1])+1);
                    }
                }
            }
            if(present==0) {
                values[c][0] = row[3];
                values[c][1] = String.valueOf(0);
                c++;
            }
        }
        log.info("User has Requested for Mode which is {}",findModel(values));
        return findModel(values);
    }
    public float standardDeviationOfTransaction(Date from_date,Date to_date,String product_id,String TypeOfData) throws IOException, CsvException, ParseException {

        log.info("User has Requested for Standard Deviation which is {}","Standard Deviation="+Math.sqrt(meanOfTransaction(from_date,to_date,product_id,TypeOfData)));
        return (float) Math.sqrt(meanOfTransaction(from_date,to_date,product_id,TypeOfData));
    }
    public float varianceOfTransaction(Date from_date,Date to_date,String product_id,String TypeOfData) throws IOException, CsvException, ParseException {
        float mean=meanOfTransaction(from_date,to_date,product_id,TypeOfData);
        float XSeq=0;
        float TotalXX = 0;
        int TotalTran=0;
        List<String[]> allTransaction = transactionService.allTransaction(TypeOfData,product_id,from_date,to_date);
        for (int t = 0; t< (long) allTransaction.size(); t++){
            String[] row;
            row= allTransaction.get(t);
            TotalTran++;
            XSeq=Float.parseFloat(row[3])-mean;
            XSeq=XSeq*XSeq;
            TotalXX=TotalXX+XSeq;
        }
        log.info("User has Requested for Variance {}",TotalXX/TotalTran);
        return TotalXX/TotalTran;
    }
}
