package TransactionMonitor.com.bbd.controller;

import TransactionMonitor.com.bbd.model.Record;
import TransactionMonitor.com.bbd.service.SaveRecordService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
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
    public String[] olderTransaction() throws IOException, CsvValidationException {
        List listYear = Files.list(Paths.get(".\\Data")).collect(Collectors.toList());

        List listQuaFiles = Files.list(Paths.get(listYear.get(0).toString())).collect(Collectors.toList());
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
        return st.get(1);
    }

    @GetMapping("/newest")
    public String[] newerTransaction() throws IOException, CsvValidationException {
        List listYear = Files.list(Paths.get(".\\Data")).collect(Collectors.toList());
        Collections.sort(listYear, Collections.reverseOrder());
        List listQuaFiles = Files.list(Paths.get(listYear.get(0).toString())).collect(Collectors.toList());
        Collections.sort(listQuaFiles, Collections.reverseOrder());
        List listDalyFiles = Files.list(Paths.get(listQuaFiles.get(0).toString())).collect(Collectors.toList());
        Collections.sort(listDalyFiles, Collections.reverseOrder());
        CSVReader reader = new CSVReader(new FileReader(listDalyFiles.get(0).toString()));
        List<String[]> st = new ArrayList<>();

        String datalist[];
        while ((datalist = reader.readNext()) != null) {
            st.add(datalist);
        }
        //st=  st.stream().sorted((s1,s2)-> s1[0].compareTo(s2[0])).collect(Collectors.toList());

        //Counting totel number of transaction present in that file
        long RowsCount = Files.lines(Path.of(listDalyFiles.get(0).toString())).count();
        //System.out.println("Number of Rows of transaction in fille "+RowsCount);
        return st.get((int)RowsCount-1);
    }

    @GetMapping("/mean")
    public String meanOfTransaction() throws IOException, CsvException {
        List listYears = Files.list(Paths.get(".\\Data")).collect(Collectors.toList());
        float TotalAmount = 0;
        int TotalDays=0;
        //System.out.println(listYears.stream().count());
        for(int year=0;year<listYears.stream().count();year++){
            List listQua = Files.list(Paths.get(listYears.get(year).toString())).collect(Collectors.toList());
            for(int quater=0;quater<listQua.stream().count();quater++){
                //System.out.println(listQua.get(quater));
                List listDaylyFile = Files.list(Paths.get(listQua.get(quater).toString())).collect(Collectors.toList());
                for(int fday=0;fday<listDaylyFile.stream().count();fday++){
                    //System.out.println(listDaylyFile.get(fday));
                    CSVReader readfile = new CSVReader(new FileReader(listDaylyFile.get(fday).toString()));
                    //for Removing header
                    readfile.readNext();
                    List<String[]> rows=readfile.readAll();

                    for (String[] row : rows) {
                        TotalDays++;
                        TotalAmount= Float.parseFloat(row[3])+TotalAmount;
                    }
                }
            }
        }
        return "mean="+TotalAmount+"/"+TotalDays+"="+(TotalAmount/TotalDays);
    }

    @GetMapping("/try")
    public int total() throws IOException, CsvException {
        float total=0;
        CSVReader readfile = new CSVReader(new FileReader("Data//2002//1//1-1-2002.csv"));
        //for Removing header
        readfile.readNext();
        List<String[]> rows=readfile.readAll();

        for (String[] row : rows) {
            System.out.println(row[0] + "," + row[1] + "," + row[2] + "," + row[3]);
            total= Float.parseFloat(row[3])+total;
        }
        System.out.println("total:"+total);
        return 1;
    }
}
