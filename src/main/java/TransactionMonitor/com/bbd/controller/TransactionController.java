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
import java.util.*;
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
    public float meanOfTransaction() throws IOException, CsvException {
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
        return (TotalAmount/TotalDays);
    }
    @GetMapping("/mode")
    public String modeOfTransaction() throws IOException, CsvException {
        int c=0;
        int j=0;
        String[][] values = new String[100][2];
        List listYears = Files.list(Paths.get(".\\Data")).collect(Collectors.toList());
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
                    int present=0;
                    for (String[] row : rows) {
                        //  System.out.println(row[0] + "," + row[1] + "," + row[2] + "," + row[3]);
                        present=0;
                        for(int i=0;i<values.length;i++){
                            if(values[i][0]!=null) {
                                if(values[i][0].toString().equals(row[3].toString())){
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

                }
            }
        }
        int count=0;
        String mode="";
        for(int i=0;i<values.length;i++){
            if(values[i][0]!=null) {
                if(Integer.parseInt(values[i][1])>count){
                    count=Integer.parseInt(values[i][1])+1;
                    mode=values[i][0];
                }
            }
        }
        return "Mode (Amount) = "+mode+" with count="+count;
    }
    @GetMapping("/standardDeviation")
    public String SDOfTransaction() throws IOException, CsvException {
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
        return "Standard Deviation="+Math.sqrt(TotalAmount/TotalDays);
    }
    @GetMapping("/variance")
    public float VarianceOfTransaction() throws IOException, CsvException {
        float mean=meanOfTransaction();
        List listYears = Files.list(Paths.get(".\\Data")).collect(Collectors.toList());
        float XSeq = 0;
        float TotalXX = 0;
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
                        XSeq=Float.parseFloat(row[3])-mean;
                        XSeq=XSeq*XSeq;
                        System.out.println("X-mean="+XSeq+" amount="+Float.parseFloat(row[3]));
                        TotalXX=TotalXX+XSeq;
                    }
                }
            }
        }
        return TotalXX/TotalDays;
    }
    @GetMapping("/mostCommonProduct")
    public String mostCommonProduct() throws IOException, CsvException {
        String prod="";
        int c=0;
        int j=0;
        String[][] products=listOfCommonProduct();
        int count=Integer.parseInt(products[0][1]);
        for(int i=1;i<products.length-1;i++){
            if(products[i][0]!=null) {
                //System.out.println(products[i][0]+" "+(Integer.parseInt(products[i][1])+1));
                if(Integer.parseInt(products[i][1])>count){
                    count=Integer.parseInt(products[i][1]);
                    count=count+1;
                    prod=products[i][0];

                }
            }
        }
        return "most common product is ("+prod+") with repetation of "+count;
    }
    @GetMapping("/leastCommonProduct")
    public String listCommonProduct() throws IOException, CsvException {
        String prod="";
        int c=0;
        int j=0;
        String[][] products=listOfCommonProduct();
        int count=Integer.parseInt(products[0][1]);
        for(int i=1;i<products.length-1;i++){
            if(products[i][0]!=null) {
                //System.out.println(products[i][0]+" "+(Integer.parseInt(products[i][1])+1));
                if(Integer.parseInt(products[i][1])<=count && Integer.parseInt(products[i][1])!=0){
                    count=Integer.parseInt(products[i][1]);
                    count=count+1;
                    prod=products[i][0];

                }
            }
        }
        return "most common product is ("+prod+") with repetation of "+count;
    }
    public String[][] listOfCommonProduct() throws IOException, CsvException {
        int c=0;
        int j=0;
        String[][] products = new String[1000][2];
        List listYears = Files.list(Paths.get(".\\Data")).collect(Collectors.toList());
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
                        int present=0;
                        //  System.out.println(row[0] + "," + row[1] + "," + row[2] + "," + row[3]);
                        for(int i=0;i<products.length;i++){
                            if(products[i][0]!=null) {
                                if(products[i][0].toString().equals(row[2].toString())){
                                   present=1;
                                   products[i][1]=String.valueOf(Integer.parseInt(products[i][1])+1);
                               }
                            }
                        }
                        if(present==0){
                            products[c][0] = row[2];
                            products[c][1] = String.valueOf(0);
                            c++;
                        }
                    }

                }
            }
        }
        return products;
    }
}
