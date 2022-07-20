package TransactionMonitor.com.bbd.controller;

import TransactionMonitor.com.bbd.model.Record;
import TransactionMonitor.com.bbd.model.datesBetween;
import TransactionMonitor.com.bbd.service.SaveRecordService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private SaveRecordService service;
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
    @GetMapping("/listcommon")
    private String[][] listOfCommonProduct() throws IOException, CsvException {
        int c=0;
        int j=0;
        String[][] products = new String[1000][2];
        List listYears = Files.list(Paths.get(".\\Data")).collect(Collectors.toList());
        //System.out.println(listYears.stream().count());
        for(int year=0;year<listYears.size();year++){
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
    private String[] dateFromPath(String DayPath) throws ParseException {
        SimpleDateFormat fileDate = new SimpleDateFormat("dd-mm-yyyy");
        int indexOfDash = DayPath.indexOf('.');
        String DateFromPath=DayPath.substring(0, indexOfDash);
        String[] splitDate = DateFromPath.split("-", 0);
        return splitDate;
    }
    public String findDifference(Date start_date, Date end_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date d1 = start_date;
        Date d2 = end_date;
        long difference_In_Time = d2.getTime() - d1.getTime();
        long difference_In_Seconds = (difference_In_Time / 1000) % 60;
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
        long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
        long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));
        long difference_In_Days  = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
        System.out.print("Difference " + "between two dates is: ");
        return difference_In_Years + " years, " + difference_In_Days + " days, " + difference_In_Hours + " hours, " + difference_In_Minutes + " minutes, " + difference_In_Seconds + " seconds";
    }

    //List all transaction between two time and dates
    @GetMapping("/listTransaction")
    public List allTransactionInBetween(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        Date dateFrom = dates.getDateFrom();
        Date dateTo = dates.getDateTo();
        List paths=listOfPathsBetweenTwoDates(dates);
        ArrayList<String[]> temp = new ArrayList<>();
        for(int p=0;p<paths.size();p++){
            CSVReader readfile = new CSVReader(new FileReader(paths.get(p).toString()));
            readfile.readNext();
            List<String[]> rows=readfile.readAll();
            for (int i=0;i<rows.size();i++) {
                String[] transactions=new String[4];
                transactions[0]=rows.get(i)[0];
                transactions[1]=rows.get(i)[1];
                transactions[2]=rows.get(i)[2];
                transactions[3]=rows.get(i)[3];
                temp.add(transactions);
            }
        }
        return temp;

    }
    @GetMapping("/ListPath")
    public List<String> listOfPathsBetweenTwoDates(@RequestBody datesBetween dates) throws IOException, ParseException {
        Date dateFrom = dates.getDateFrom();
        Date dateTo = dates.getDateTo();
        int monthFrom = dateFrom.getMonth()+1;
        int quaterFrom = getQuater(monthFrom);
        int yearFrom = dateFrom.getYear()+1900;
        int dayFrom=dateFrom.getDate();
        int monthto = dateTo.getMonth()+1;
        int quaterTo = getQuater(monthto);
        int yearto = dateTo.getYear()+1900;
        int dayto=dateTo.getDate();
        List<String> BetweenFilePath= new ArrayList<String>();
        File directoryPathYear = new File(".//Data");
        String yearsPath[] = directoryPathYear.list();
        for (int y=0;y<yearsPath.length;y++){
            if(yearFrom<=Integer.parseInt(String.valueOf(yearsPath[y])) && yearto>=Integer.parseInt(String.valueOf(yearsPath[y]))){
                if(Integer.parseInt(yearsPath[y])==yearFrom){
                    File directoryPathQua = new File(".//Data//"+yearsPath[y]);
                    String quaterPath[] = directoryPathQua.list();
                    for(int q=0;q<directoryPathQua.list().length;q++){
                        if(quaterFrom<=Integer.parseInt(String.valueOf(quaterPath[q]))){
                            File directoryPathDay = new File(".//Data//"+yearsPath[y]+"//"+quaterPath[q]);
                            String DayPath[]=directoryPathDay.list();
                            for (int d=0;d<directoryPathDay.list().length;d++){
                                String[] TransacDate =dateFromPath(DayPath[d]);

                                if(monthFrom<Integer.parseInt(TransacDate[1])) {
                                    //System.out.println(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]);
                                    BetweenFilePath.add(String.valueOf(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                                }
                                if(monthFrom==Integer.parseInt(TransacDate[1])&&yearFrom==Integer.parseInt(TransacDate[2])){
                                    if(dayFrom<=Integer.parseInt(TransacDate[0])){
                                      //  System.out.println(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]);
                                        BetweenFilePath.add(String.valueOf(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                                    }
                                }
                            }
                            //System.out.println(".//Data//"+yearsPath[y]+"//"+quaterPath[q]);
                        }
                    }
                }
                else if(Integer.parseInt(yearsPath[y])==yearto){
                    File directoryPathQua = new File(".//Data//"+yearsPath[y]);
                    String quaterPath[] = directoryPathQua.list();
                    for(int q=0;q<directoryPathQua.list().length;q++){
                        if(quaterTo>=Integer.parseInt(String.valueOf(quaterPath[q]))){
                            File directoryPathDay = new File(".//Data//"+yearsPath[y]+"//"+quaterPath[q]);
                            String DayPath[]=directoryPathDay.list();
                            for (int d=0;d<directoryPathDay.list().length;d++){
                                String[] TransacDate =dateFromPath(DayPath[d]);
                                if(monthto>Integer.parseInt(TransacDate[1])) {
                                    //System.out.println(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]);
                                    BetweenFilePath.add(String.valueOf(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                                }
                                if(monthto==Integer.parseInt(TransacDate[1])&&yearto==Integer.parseInt(TransacDate[2])){
                                    if(dayto>=Integer.parseInt(TransacDate[0])){
                                      //  System.out.println(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]);
                                        BetweenFilePath.add(String.valueOf(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    File directoryPathQua = new File(".//Data//"+yearsPath[y]);
                    String quaterPath[] = directoryPathQua.list();
                    for(int q=0;q<directoryPathQua.list().length;q++){
                        File directoryPathDay = new File(".//Data//"+yearsPath[y]+"//"+quaterPath[q]);
                        String DayPath[]=directoryPathDay.list();
                        for (int d=0;d<directoryPathDay.list().length;d++){
                            //System.out.println(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]);
                            BetweenFilePath.add(String.valueOf(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                        }
                    }
                }
            }
        }
        //return "month="+monthFrom+",yearfrom="+yearFrom+"monthtoo="+monthto+",yearTo="+yearto;
        return BetweenFilePath;
    }

    //Working
    @PostMapping("/save")
    public String saveTransaction(@RequestBody List<Record> record) {
        try{
            service.saveTransaction(record);
            return "Successfully inserted";
        }
        catch (Exception ex){
            return ex+"";
        }
    }

    //working
    @GetMapping("/oldest")
    public String[] oldestTransaction() throws IOException, CsvValidationException {
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

    //Working
    @GetMapping("/oldestTInRange")
    public String[] oldTransactionInBetween(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        List transaction=allTransactionInBetween(dates);
        String[] oldestTransaction=new String[4];
        oldestTransaction= (String[]) transaction.get(0);
        //for calculating month vise
//        for(int t=1;t<transaction.size();t++){
//            String[] temp= (String[]) transaction.get(t);
//            Date transacDate=new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy").parse(temp[0]);
//            System.out.println(transacDate);
//        }
        return oldestTransaction;
    }

    //Working
    @GetMapping("/newestTInRange")
    public String[] newTransactionInBetween(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        List transaction=allTransactionInBetween(dates);
        String[] oldestTransaction=new String[4];
        int len= transaction.size()-1;
        oldestTransaction= (String[]) transaction.get(len);
        return oldestTransaction;
    }

    //Working
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

    //Working
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

    //Working
    @GetMapping("/meanInRange")
    public float meanInRange(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        Date dateFrom = dates.getDateFrom();
        Date dateTo = dates.getDateTo();
        float TotalAmount=0;
        float Totalfiles=0;
        List paths=listOfPathsBetweenTwoDates(dates);
        for(int p=0;p<paths.size();p++){
            CSVReader readfile = new CSVReader(new FileReader(paths.get(p).toString()));
            readfile.readNext();
            List<String[]> rows=readfile.readAll();
            for (String[] row : rows) {
                Totalfiles++;
                TotalAmount= Float.parseFloat(row[3])+TotalAmount;
            }
        }
        return TotalAmount/Totalfiles;
    }

    //Working
    @GetMapping("/mode")
    public String modeOfTransaction() throws IOException, CsvException {
        int c=0;
        String[][] values = new String[1000][2];
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

    //Working
    @GetMapping("modeInRange")
    public String modeInRange(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        Date dateFrom = dates.getDateFrom();
        Date dateTo = dates.getDateTo();
        ArrayList<String[]> transaction= (ArrayList<String[]>) allTransactionInBetween(dates);
        int c=0;
        int present=0;
        String[][] values = new String[transaction.size()][2];
        for(int t=0;t<transaction.size();t++){
            //System.out.println(transaction.get(i)[0]);
            present=0;
            String[] row=transaction.get(t);
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

    //Working
    @GetMapping("/standardDeviation")
    public String SDOfTransaction() throws IOException, CsvException {
        return "Standard Deviation="+Math.sqrt(meanOfTransaction());
    }

    //Working
    @GetMapping("/SDInRange")
    public float sDInRange(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        float mean=meanInRange(dates);
        return (float) Math.sqrt(mean);
    }

    //Working
    @GetMapping("/variance")
    public float VarianceOfTransaction() throws IOException, CsvException {
        float mean=meanOfTransaction();
        List listYears = Files.list(Paths.get(".\\Data")).collect(Collectors.toList());
        float XSeq = 0;
        float TotalXX = 0;
        int TotalTran=0;
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
                        TotalTran++;
                        XSeq=Float.parseFloat(row[3])-mean;
                        XSeq=XSeq*XSeq;
                        System.out.println("X-mean="+XSeq+" amount="+Float.parseFloat(row[3]));
                        TotalXX=TotalXX+XSeq;
                    }
                }
            }
        }
        System.out.println("Squar total = "+TotalXX +" Total transaction"+TotalTran);
        return TotalXX/TotalTran;
    }

    //Working
    @GetMapping("/varianceInRange")
    public float varianceInBetween(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        Date dateFrom = dates.getDateFrom();
        Date dateTo = dates.getDateTo();
        List paths=listOfPathsBetweenTwoDates(dates);
        int TotalDays=0;
        float XSeq = 0;
        float TotalXX = 0;
        float mean=meanInRange(dates);
        for(int fday=0;fday<paths.stream().count();fday++){
            //System.out.println(listDaylyFile.get(fday));
            CSVReader readfile = new CSVReader(new FileReader(paths.get(fday).toString()));
            //for Removing header
            readfile.readNext();
            List<String[]> rows=readfile.readAll();

            for (String[] row : rows) {
                TotalDays++;
                XSeq=Float.parseFloat(row[3])-mean;
                //System.out.println(XSeq);
                XSeq=XSeq*XSeq;
                //System.out.println("X-mean="+XSeq+" amount="+Float.parseFloat(row[3])+" mean="+mean);
                TotalXX=TotalXX+XSeq;
            }
        }
        return TotalXX/TotalDays;
    }

    //Working
    @GetMapping("/mostCommonProduct")
    public String mostCommonProduct() throws IOException, CsvException {
        int c=0;
        int j=0;
        String[][] products=listOfCommonProduct();
        int count=Integer.parseInt(products[0][1]);
        String prod=products[0][0];
        for(int i=1;i<products.length-1;i++){
            if(products[i][0]!=null) {
                if(Integer.parseInt(products[i][1])>count){
                    count=Integer.parseInt(products[i][1]);
                    count=count+1;
                    prod=products[i][0];
                    System.out.println(products[i][0]+" -- "+products[i][1]);
                }
            }
        }
        return "most common product is ("+prod+") with repetation of "+count;
    }

    //Working
    @GetMapping("/mostCPInRange")
    public String mostCPInBetween(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        Date dateFrom = dates.getDateFrom();
        Date dateTo = dates.getDateTo();
        List paths=listOfPathsBetweenTwoDates(dates);
        int c=0;
        String[][] products = new String[1000][2];
        for(int fday=0;fday<paths.stream().count();fday++){
            //System.out.println(listDaylyFile.get(fday));
            CSVReader readfile = new CSVReader(new FileReader(paths.get(fday).toString()));
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
        int count=Integer.parseInt(products[0][1]);
        String prod=products[0][0];
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
        return prod;
    }

    //Working
    @GetMapping("/leastCommonProduct")
    public String leastCommonProduct() throws IOException, CsvException {
        int c=0;
        int j=0;
        String[][] products=listOfCommonProduct();
        int count=Integer.parseInt(products[0][1]);
        String prod=products[0][0];
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

    //Not working
    @GetMapping("/leastCPInRange")
    public String leastCommonProduct(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        Date dateFrom = dates.getDateFrom();
        Date dateTo = dates.getDateTo();
        List paths=listOfPathsBetweenTwoDates(dates);
        int c=0;
        String[][] products = new String[1000][2];
        for(int fday=0;fday<paths.stream().count();fday++){
            //System.out.println(listDaylyFile.get(fday));
            CSVReader readfile = new CSVReader(new FileReader(paths.get(fday).toString()));
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
        int count=Integer.parseInt(products[0][1]);
        String prod=products[0][0];
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
        return prod;
    }

    //working
    @GetMapping("/getTimeDelta")
    public String timeDelta() throws IOException, CsvException, ParseException {
        List listYears = Files.list(Paths.get(".\\Data")).collect(Collectors.toList());
        long TotalProcessMilliSeconds = 0;
        long TotalTransaction=0;
        int c=0;
        String[][] values = new String[1000][2];
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
                        Date dateFrom=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[0]);
                        Date dateTo=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[1]);
//                        System.out.println(row[0]+"----"+row[1]);
//                        System.out.println((dateTo.getTime()-dateFrom.getTime())+"---------"+findDifference(dateFrom,dateTo));
                        TotalProcessMilliSeconds = TotalProcessMilliSeconds+(dateTo.getTime() - dateFrom.getTime());
                        TotalTransaction++;
                        present=0;
                        for(int i=0;i<values.length;i++){
                            if(values[i][0]!=null) {
                                Date tFrom=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[0]);
                                Date tTo=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[1]);
                                if(values[i][0].toString().equals(String.valueOf(tFrom.getTime()-tTo.getTime()))){
                                    present=1;
                                    values[i][1]= String.valueOf(Integer.parseInt(values[i][1])+1);
                                }
                            }
                        }
                        if(present==0) {
                            values[c][0] = String.valueOf(TotalProcessMilliSeconds);
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
        return "Mean="+TotalProcessMilliSeconds/TotalTransaction+" Mode="+mode+" with Count="+count+" Standard Deviation="+Math.sqrt(TotalProcessMilliSeconds/TotalTransaction);
    }

    @GetMapping("/getTimeDeltaInRange")
    public String timeDeltaInRange(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        List transaction=allTransactionInBetween(dates);
        long TotalProcessMilliSeconds = 0;
        long TotalTransaction=0;
        int c=0;
        int present=0;
        String[][] values = new String[transaction.size()][2];
        for(int t=0;t<transaction.size();t++){
            //System.out.println(transaction.get(i)[0]);
            String[] row= (String[]) transaction.get(t);
            Date dateFrom=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[0]);
            Date dateTo=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[1]);
//                        System.out.println(row[0]+"----"+row[1]);
//                        System.out.println((dateTo.getTime()-dateFrom.getTime())+"---------"+findDifference(dateFrom,dateTo));
            TotalProcessMilliSeconds = TotalProcessMilliSeconds+(dateTo.getTime() - dateFrom.getTime());
            TotalTransaction++;
            present=0;
            for(int i=0;i<values.length;i++){
                if(values[i][0]!=null) {
                    Date tFrom=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[0]);
                    Date tTo=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[1]);
                    if(values[i][0].toString().equals(String.valueOf(tFrom.getTime()-tTo.getTime()))){
                        present=1;
                        values[i][1]= String.valueOf(Integer.parseInt(values[i][1])+1);
                    }
                }
            }
            if(present==0) {
                values[c][0] = String.valueOf(TotalProcessMilliSeconds);
                values[c][1] = String.valueOf(0);
                c++;
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
        return "Mean="+TotalProcessMilliSeconds/TotalTransaction+" Mode="+mode+" with Count="+count+" Standard Deviation="+Math.sqrt(TotalProcessMilliSeconds/TotalTransaction);
    }

    @GetMapping("/getTimeDeltaByProduct/{productId}")
    public String timeDelta(@PathVariable String productId) throws IOException, CsvException, ParseException {
        List listYears = Files.list(Paths.get(".\\Data")).collect(Collectors.toList());
        long TotalProcessMilliSeconds = 0;
        long TotalTransaction=0;
        int c=0;
        String[][] values = new String[1000][2];
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
                        //////
                         if(Objects.equals(row[2], productId)) {
                             Date dateFrom = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[0]);
                             Date dateTo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[1]);
                                                     System.out.println(row[0]+"----"+row[1]);
                                                     System.out.println((dateTo.getTime()-dateFrom.getTime())+"---------"+findDifference(dateFrom,dateTo));
                             TotalProcessMilliSeconds = TotalProcessMilliSeconds+(dateTo.getTime() - dateFrom.getTime());
                             TotalTransaction++;
                             present = 0;
                             for (int i = 0; i < values.length; i++) {
                                 if (values[i][0] != null) {
                                     Date tFrom = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[0]);
                                     Date tTo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[1]);
                                     if (values[i][0].toString().equals(String.valueOf(tFrom.getTime() - tTo.getTime()))) {
                                         present = 1;
                                         values[i][1] = String.valueOf(Integer.parseInt(values[i][1]) + 1);
                                     }
                                 }
                             }
                             if (present == 0) {
                                 values[c][0] = String.valueOf(TotalProcessMilliSeconds);
                                 values[c][1] = String.valueOf(0);
                                 c++;
                             }
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
        System.out.println(TotalProcessMilliSeconds+" --- "+TotalTransaction);
        return "Mean="+TotalProcessMilliSeconds/TotalTransaction+" Mode="+mode+" with Count="+count+" Standard Deviation="+Math.sqrt(TotalProcessMilliSeconds/TotalTransaction);
    }

    @GetMapping("/getTimeDeltaInRange/{productId}")
    public String timeDeltaInRangewithProductId(@RequestBody datesBetween dates,@PathVariable String productId) throws IOException, ParseException, CsvException {
        List transaction=allTransactionInBetween(dates);
        long TotalProcessMilliSeconds = 0;
        long TotalTransaction=0;
        int c=0;
        int present=0;
        String[][] values = new String[transaction.size()][2];
        for(int t=0;t<transaction.size();t++){
            //System.out.println(transaction.get(i)[0]);
            String[] row= (String[]) transaction.get(t);
            if(row[2].equals(productId)) {
                Date dateFrom = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[0]);
                Date dateTo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[1]);
                //                        System.out.println(row[0]+"----"+row[1]);
                //                        System.out.println((dateTo.getTime()-dateFrom.getTime())+"---------"+findDifference(dateFrom,dateTo));
                TotalProcessMilliSeconds = TotalProcessMilliSeconds + (dateTo.getTime() - dateFrom.getTime());
                TotalTransaction++;
                present = 0;
                for (int i = 0; i < values.length; i++) {
                    if (values[i][0] != null) {
                        Date tFrom = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[0]);
                        Date tTo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[1]);
                        if (values[i][0].toString().equals(String.valueOf(tFrom.getTime() - tTo.getTime()))) {
                            present = 1;
                            values[i][1] = String.valueOf(Integer.parseInt(values[i][1]) + 1);
                        }
                    }
                }
                if (present == 0) {
                    values[c][0] = String.valueOf(TotalProcessMilliSeconds);
                    values[c][1] = String.valueOf(0);
                    c++;
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
        return "Mean="+TotalProcessMilliSeconds/TotalTransaction+" Mode="+mode+" with Count="+count+" Standard Deviation="+Math.sqrt(TotalProcessMilliSeconds/TotalTransaction);
    }
}
