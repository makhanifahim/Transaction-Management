package TransactionMonitor.com.bbd.service;

import TransactionMonitor.com.bbd.model.Record;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

@Service
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

            records.stream().forEach(record->{
                try {
                    Date init_date = record.getInit_date();
                    int d = init_date.getDate();
                    String date=d+"";
                    if(d<10){
                        date=0+""+d;
                    }
                    int m = init_date.getMonth()+1;
                    String month;
                    if(m<10){
                        month=""+0+m;
                    }
                    else{
                        month= m+"";
                    }
                    int year = init_date.getYear() + 1900;
                    int quarter=getQuater(m);
                    File file= new File(".\\Data\\"+year+"\\"+quarter);
                    file.mkdirs();

                    String filename=".\\Data\\"+year+"\\"+ quarter+"\\"+date+"-"+month+"-"+year+".csv";
                    File checkfile= new File(".\\Data\\"+year+"\\"+ quarter+"\\"+date+"-"+month+"-"+year+".csv");
                    boolean checkFilePresent=checkfile.exists();

                    if(checkFilePresent==true){
                        CSVWriter writer = new CSVWriter(new FileWriter(file+"\\"+date+"-"+month+"-"+year+".csv",true));
                        String line1[] = {record.getInit_date().toString(),record.getConclusion_date().toString() ,record.getProduct_id().toString(), record.getValue().toString()};
                        writer.writeNext(line1);
                        writer.flush();
                    }
                    else{
                        //Csv Header
                        //"init_date", "conclusion_date", "product_id", "value"
                        CSVWriter writer = new CSVWriter(new FileWriter(file+"\\"+date+"-"+month+"-"+year+".csv",true));
                        String line1[] = {"init_date","conclusion_date","product_id","value"};
                        String line2[] = {record.getInit_date().toString(),record.getConclusion_date().toString() ,record.getProduct_id().toString(), record.getValue().toString()};
                        writer.writeNext(line1);
                        writer.writeNext(line2);
                        writer.flush();
                    }

                }
                catch (Exception ex){
                    System.out.println(ex);
                }
            });
        return "true";
    }
}
