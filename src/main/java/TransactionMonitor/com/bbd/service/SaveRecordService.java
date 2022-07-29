package TransactionMonitor.com.bbd.service;

import TransactionMonitor.com.bbd.model.Record;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SaveRecordService {

    private int getQuater(int month){
        int quarter=0;
        switch (month){
            case 1,2,3:
                quarter=1;
                break;
            case 4,5,6:
                quarter=2;
                break;
            case 7,8,9:
                quarter=3;
                break;
            case 10,11,12:
                quarter=4;
                break;
        }
        return quarter;
    }

    public String saveTransaction(List<Record> records){
        List problem = new ArrayList(records.size());
        List errorIn = new ArrayList(records.size());
        records.stream().forEach(record->{
            try{
                Date init_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(record.getInit_date());
                Date Conclusion_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(record.getConclusion_date());
                if(init_date.compareTo(Conclusion_date)>0){
                    problem.add(records.indexOf(record));
                    log.error("Tried to add wrong data in which init_date {} is higher the Conclusion_date{}",record.getInit_date(),record.getConclusion_date());
                }
            }catch (ParseException e) {
                e.printStackTrace();
                log.error(e.toString());
            }
        });
        if(problem.size()>0){
            for(int i=0; i<problem.size();i++){
                System.out.println();
            }
            return "Problem with index value : "+ Arrays.toString(problem.toArray());
        }
        else {
            records.stream().forEach(record -> {
                try {
                    Date init_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(record.getInit_date());
                    Date Conclusion_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(record.getConclusion_date());
                    init_date.setHours(init_date.getHours() - 5);
                    init_date.setMinutes(init_date.getMinutes() - 30);
                    Conclusion_date.setHours(Conclusion_date.getHours() - 5);
                    Conclusion_date.setMinutes(Conclusion_date.getMinutes() - 30);
                    int d = init_date.getDate();
                    String date = d + "";
                    if (d < 10) {
                        date = 0 + "" + d;
                    }
                    int m = init_date.getMonth() + 1;
                    String month;
                    if (m < 10) {
                        month = "" + 0 + m;
                    } else {
                        month = m + "";
                    }
                    int year = init_date.getYear() + 1900;
                    int quarter = getQuater(m);
                    File file = new File(".\\Data\\" + year + "\\" + quarter);
                    file.mkdirs();

                    String filename = ".\\Data\\" + year + "\\" + quarter + "\\" + date + "-" + month + "-" + year + ".csv";
                    File checkfile = new File(".\\Data\\" + year + "\\" + quarter + "\\" + date + "-" + month + "-" + year + ".csv");
                    boolean checkFilePresent = checkfile.exists();

                    if (checkFilePresent == true) {
                        log.info("Saving Transaction starts with {} In present file ",record.getInit_date());
                        CSVWriter writer = new CSVWriter(new FileWriter(file + "\\" + date + "-" + month + "-" + year + ".csv", true));
                        String line1[] = {record.getInit_date(), record.getInit_date(), record.getProduct_id().toString(), record.getValue().toString()};
                        writer.writeNext(line1);
                        writer.flush();
                    } else {
                        log.info("Saving Transaction starts with {} In new file",record.getInit_date());
                        CSVWriter writer = new CSVWriter(new FileWriter(file + "\\" + date + "-" + month + "-" + year + ".csv", true));
                        String line1[] = {"init_date", "conclusion_date", "product_id", "value"};
                        String line2[] = {record.getInit_date(), record.getConclusion_date(), record.getProduct_id().toString(), record.getValue().toString()};
                        writer.writeNext(line1);
                        writer.writeNext(line2);
                        writer.flush();
                    }

                } catch (Exception ex) {
                    errorIn.add(records.indexOf(record));
                    log.error(ex.toString());
                }
            });
            if(errorIn.size()>0) {
                log.error("Problem with index value : " + Arrays.toString(errorIn.stream().toArray()));
                return "Problem with index value : " + Arrays.toString(errorIn.stream().toArray());
            }
                else{
                log.info("List of Data has been saved");
                return "Successfully Inserted ";
            }
        }
    }
}
