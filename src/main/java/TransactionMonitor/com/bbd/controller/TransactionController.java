package TransactionMonitor.com.bbd.controller;

import TransactionMonitor.com.bbd.model.Record;
import TransactionMonitor.com.bbd.service.SaveRecordService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private SaveRecordService service;
    @PostMapping("/save")
    public String saveTransaction(@RequestBody List<Record> record)
    {
        try{
            service.saveTransaction(record);
            return "Successfully inserted";
        }
        catch (Exception ex){
            return ex+"";
        }
//        Date init_date = record.getInit_date();
//        int month = init_date.getMonth();
//        int year = init_date.getYear();
//        return "init_date = "+(month+1) + "year = "+(year+1900);
    }
    @GetMapping("/oldest")
    public String[] oldertransaction() throws IOException, CsvValidationException {
        List listFiles = Files.list(Paths.get(".\\Data")).collect(Collectors.toList());

        List listQuaFiles = Files.list(Paths.get(listFiles.get(0).toString())).collect(Collectors.toList());
        System.out.println(listQuaFiles.get(0).toString());

        List listDalyFiles = Files.list(Paths.get(listQuaFiles.get(0).toString())).collect(Collectors.toList());
        CSVReader reader = new CSVReader(new FileReader(listDalyFiles.get(0).toString()));

        List<String[]> st = new ArrayList<>();

        String datalist[];
        while ((datalist = reader.readNext()) != null) {
            st.add(datalist);
        }
        //for shorting date vise if not but it already includes that
        //st=  st.stream().sorted((s1,s2)-> s1[0].compareTo(s2[0])).collect(Collectors.toList());
        return st.get(0);
    }

    @GetMapping("/newest")
    public String[] newertransaction() throws IOException, CsvValidationException {
        List listFiles = Files.list(Paths.get(".\\Data")).collect(Collectors.toList());
        Collections.sort(listFiles, Collections.reverseOrder());
        List listQuaFiles = Files.list(Paths.get(listFiles.get(0).toString())).collect(Collectors.toList());
        Collections.sort(listQuaFiles, Collections.reverseOrder());
        System.out.println(listQuaFiles.get(0).toString());
        List listDalyFiles = Files.list(Paths.get(listQuaFiles.get(0).toString())).collect(Collectors.toList());
        Collections.sort(listDalyFiles, Collections.reverseOrder());
        CSVReader reader = new CSVReader(new FileReader(listDalyFiles.get(0).toString()));
        List<String[]> st = new ArrayList<>();

        String datalist[];
        while ((datalist = reader.readNext()) != null) {
            st.add(datalist);
        }
        st=  st.stream().sorted((s1,s2)-> s1[0].compareTo(s2[0])).collect(Collectors.toList());

        //Counting totel number of transaction present in that file
        long RowsCount = Files.lines(Path.of(listDalyFiles.get(0).toString())).count();
        //System.out.println("Number of Rows of transaction in fille "+RowsCount);
        return st.get((int)RowsCount-1);
    }
}
