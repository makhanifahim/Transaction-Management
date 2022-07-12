package TransactionMonitor.com.bbd.service;

import TransactionMonitor.com.bbd.model.Record;
import org.springframework.stereotype.Service;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;

@Service
public class SaveRecordService {

    public String saveTransaction(Record record){

        try {
            File file= new File("C:\\Users\\bbdnet10191\\Desktop\\Books\\xyz");
//          file.createNewFile();
            file.mkdir();
            CSVWriter writer = new CSVWriter(new FileWriter(file+"\\Openfile2.csv",true));
//            String Header[] = {"init_date", "conclusion_date", "product_id", "value"};
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
