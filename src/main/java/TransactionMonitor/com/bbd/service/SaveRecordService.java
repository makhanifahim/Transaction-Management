package TransactionMonitor.com.bbd.service;

import TransactionMonitor.com.bbd.model.Record;
import org.springframework.stereotype.Service;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

@Service
public class SaveRecordService {

    public String saveTransaction(Record record){

        try {
            Date init_date = record.getInit_date();
            int date = init_date.getDate();
            int month = init_date.getMonth()+1;
            int year = init_date.getYear() + 1900;
            int quarter=0;
            if(month<4)
                quarter=1;
            else if(month>=4 && month<7)
                quarter=2;
            else if(month>=7 && month<10)
                quarter=3;
            else
                quarter=4;
            File file= new File("C:\\Users\\bbdnet10191\\Desktop\\Books\\Data\\"+year+"\\"+quarter);
//          String Header[] = {"init_date", "conclusion_date", "product_id", "value"};
            file.mkdirs();
            CSVWriter writer = new CSVWriter(new FileWriter(file+"\\"+date+"-"+month+"-"+year+".csv",true));
            String line1[] = {record.getInit_date().toString(),record.getConclusion_date().toString() ,record.getProduct_id().toString(), record.getValue().toString()};
            writer.writeNext(line1);
            writer.flush();
        }
        catch (Exception ex){
            System.out.println(ex);
        }
        return "true";
    }
}
