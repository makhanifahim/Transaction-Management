package TransactionMonitor.com.bbd.controller;

import TransactionMonitor.com.bbd.model.DatesBetween;
import TransactionMonitor.com.bbd.model.Transaction;
import TransactionMonitor.com.bbd.model.TransactionSummary;
import TransactionMonitor.com.bbd.service.ProductService;
import TransactionMonitor.com.bbd.service.TransactionService;
import TransactionMonitor.com.bbd.service.TransactionValueSummaryService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api")
@Slf4j
public class TransactionController {
    @Autowired
    private TransactionService service;

    @Autowired
    private TransactionValueSummaryService valueSummaryService;

    @Autowired
    private ProductService productService;

    private static int getQuarter(int month){
        return switch (month) {
            case 1, 2, 3 -> 1;
            case 4, 5, 6 -> 2;
            case 7, 8, 9 -> 3;
            case 10, 11, 12 -> 4;
            default -> 0;
        };
    }
    private static String[][] listCommonProd(String TypeOfData) throws IOException, CsvException {
        int c=0;
        List allTransaction = allTransactionInPresent(TypeOfData);
        String[][] products = new String[allTransaction.size()][2];
        for (int t = 0; t< (long) allTransaction.size(); t++){
            String[] row=new String[4];
            row= (String[]) allTransaction.get(t);
            int present=0;
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
        return products;
    }
    private static String[][] listCommonProdInRange(DatesBetween dates,String TypeOfData) throws IOException, ParseException, CsvException {
        if(TypeOfData==""||TypeOfData==null)
            TypeOfData="Data";
        int c=0;
        List allTransaction = allTransactionInBetween(dates,TypeOfData);
        String[][] products = new String[allTransaction.size()][2];
        for (int t = 0; t< (long) allTransaction.size(); t++){
            String[] row=new String[4];
            row= (String[]) allTransaction.get(t);
            int present=0;
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
        return products;
    }
    private static String[] dateFromPath(String DayPath){
        SimpleDateFormat fileDate = new SimpleDateFormat("dd-mm-yyyy");
        int indexOfDash = DayPath.indexOf('.');
        String DateFromPath=DayPath.substring(0, indexOfDash);
        return DateFromPath.split("-", 0);
    }
    private static List<String> allFilesInPresent(String TypeOfData) throws IOException {
        List<String> paths= new ArrayList<String>();
        List listYears = Files.list(Paths.get(".\\"+TypeOfData)).toList();
        for(int year = 0; year< (long) listYears.size(); year++){
            List listQua = Files.list(Paths.get(listYears.get(year).toString())).toList();
            for(int quater = 0; quater< (long) listQua.size(); quater++){
                List listDaylyFile = Files.list(Paths.get(listQua.get(quater).toString())).toList();
                for(int fday = 0; fday< (long) listDaylyFile.size(); fday++){
                    paths.add(listDaylyFile.get(fday).toString());
                }
            }
        }
        return paths;
    }
    public static List<String[]> allTransactionInPresent(String TypeOfData) throws IOException, CsvException {
        List paths=allFilesInPresent(TypeOfData);
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
    public static List<String> listOfPathsBetweenTwoDates(DatesBetween dates,String TypeOfData){
        Date dateFrom = dates.getDateFrom();
        Date dateTo = dates.getDateTo();
        int monthFrom = dateFrom.getMonth()+1;
        int quaterFrom = getQuarter(monthFrom);
        int yearFrom = dateFrom.getYear()+1900;
        int dayFrom=dateFrom.getDate();
        int monthto = dateTo.getMonth()+1;
        int quaterTo = getQuarter(monthto);
        int yearto = dateTo.getYear()+1900;
        int dayto=dateTo.getDate();
        List<String> BetweenFilePath= new ArrayList<String>();
        File directoryPathYear = new File(".//"+TypeOfData);
        String yearsPath[] = directoryPathYear.list();
        for (int y=0;y<yearsPath.length;y++){
            if(yearFrom<=Integer.parseInt(String.valueOf(yearsPath[y])) && yearto>=Integer.parseInt(String.valueOf(yearsPath[y]))){

                if(yearFrom==yearto){
                    File directoryPathQua = new File(".//"+TypeOfData+"//"+yearsPath[y]);
                    String quaterPath[] = directoryPathQua.list();
                    for(int q=0;q<directoryPathQua.list().length;q++){
                        if(quaterFrom<=Integer.parseInt(String.valueOf(quaterPath[q]))){
                            File directoryPathDay = new File(".//"+TypeOfData+"//"+yearsPath[y]+"//"+quaterPath[q]);
                            String DayPath[]=directoryPathDay.list();
                            for (int d=0;d<directoryPathDay.list().length;d++){
                                String[] TransacDate =dateFromPath(DayPath[d]);

                                if(monthFrom<Integer.parseInt(TransacDate[1])&&monthto>Integer.parseInt(TransacDate[1])) {
                                    BetweenFilePath.add(String.valueOf(".//"+TypeOfData+"//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                                }
                                if(monthFrom==Integer.parseInt(TransacDate[1])&&yearFrom==Integer.parseInt(TransacDate[2])){
                                    if(dayFrom<=Integer.parseInt(TransacDate[0])){
                                        BetweenFilePath.add(String.valueOf(".//"+TypeOfData+"//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                                    }
                                }
                                if(monthto==Integer.parseInt(TransacDate[1])&&yearto==Integer.parseInt(TransacDate[2])){
                                    if(dayto>=Integer.parseInt(TransacDate[0])){
                                        BetweenFilePath.add(String.valueOf(".//"+TypeOfData+"//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                                    }
                                }
                            }
                        }
                    }
                }
                else if(Integer.parseInt(yearsPath[y])==yearFrom){
                    File directoryPathQua = new File(".//"+TypeOfData+"//"+yearsPath[y]);
                    String quaterPath[] = directoryPathQua.list();
                    for(int q=0;q<directoryPathQua.list().length;q++){
                        if(quaterFrom<=Integer.parseInt(String.valueOf(quaterPath[q]))){
                            File directoryPathDay = new File(".//"+TypeOfData+"//"+yearsPath[y]+"//"+quaterPath[q]);
                            String DayPath[]=directoryPathDay.list();
                            for (int d=0;d<directoryPathDay.list().length;d++){
                                String[] TransacDate =dateFromPath(DayPath[d]);

                                if(monthFrom<Integer.parseInt(TransacDate[1])) {
                                    BetweenFilePath.add(String.valueOf(".//"+TypeOfData+"//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                                }
                                if(monthFrom==Integer.parseInt(TransacDate[1])&&yearFrom==Integer.parseInt(TransacDate[2])){
                                    if(dayFrom<=Integer.parseInt(TransacDate[0])){
                                        BetweenFilePath.add(String.valueOf(".//"+TypeOfData+"//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                                    }
                                }
                            }
                        }
                    }
                }
                else if(Integer.parseInt(yearsPath[y])==yearto){
                    File directoryPathQua = new File(".//"+TypeOfData+"//"+yearsPath[y]);
                    String quaterPath[] = directoryPathQua.list();
                    for(int q=0;q<directoryPathQua.list().length;q++){
                        if(quaterTo>=Integer.parseInt(String.valueOf(quaterPath[q]))){
                            File directoryPathDay = new File(".//"+TypeOfData+"//"+yearsPath[y]+"//"+quaterPath[q]);
                            String DayPath[]=directoryPathDay.list();
                            for (int d=0;d<directoryPathDay.list().length;d++){
                                String[] TransacDate =dateFromPath(DayPath[d]);
                                if(monthto>Integer.parseInt(TransacDate[1])) {
                                    BetweenFilePath.add(String.valueOf(".//"+TypeOfData+"//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                                }
                                if(monthto==Integer.parseInt(TransacDate[1])&&yearto==Integer.parseInt(TransacDate[2])){
                                    if(dayto>=Integer.parseInt(TransacDate[0])){
                                        BetweenFilePath.add(String.valueOf(".//"+TypeOfData+"//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    File directoryPathQua = new File(".//"+TypeOfData+"//"+yearsPath[y]);
                    String quaterPath[] = directoryPathQua.list();
                    for(int q=0;q<directoryPathQua.list().length;q++){
                        File directoryPathDay = new File(".//"+TypeOfData+"//"+yearsPath[y]+"//"+quaterPath[q]);
                        String DayPath[]=directoryPathDay.list();
                        for (int d=0;d<directoryPathDay.list().length;d++){
                            BetweenFilePath.add(String.valueOf(".//"+TypeOfData+"//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                        }
                    }
                }
            }
        }
        return BetweenFilePath;
    }
    public static List<String[]> allTransactionInBetween(DatesBetween dates,String TypeOfData) throws IOException, ParseException, CsvException {
        Date dateFrom = dates.getDateFrom();
        Date dateTo = dates.getDateTo();
        List paths=listOfPathsBetweenTwoDates(dates,TypeOfData);
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
    private static String findModel(String[][] values){
        int count=0;
        String mode=values[0][0];
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
    private static long mode(long[] array) {
        long mode = array[0];
        int maxCount = 0;
        for (int i = 0; i < array.length; i++) {
            long value = array[i];
            int count = 0;
            for (int j = 0; j < array.length; j++) {
                if (array[j] == value) count++;
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
    private boolean dateChecker(@RequestBody DatesBetween dates){
        try {
            Date init_date = dates.getDateFrom();
            Date Conclusion_date = dates.getDateTo();
            if (init_date.compareTo(Conclusion_date) > 0)
                return false;
            else
                return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @PostMapping("/transactions")
    public String saveTransactions(@RequestBody List<Transaction> transactions,@PathVariable (required=false) String typeOfData) {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        return service.saveTransaction(transactions, typeOfData);
    }

    @GetMapping("/transactions")
    public List<String[]> getTransactions(@RequestParam (required = false) String from_date,@RequestParam (required = false) String to_date,@RequestParam (required = false) boolean oldest,@RequestParam (required = false) boolean newest,@RequestParam (required = false) String product_id,@RequestParam (required=false) String typeOfData) throws IOException, ParseException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        String p_id=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        if(product_id!=null){p_id=product_id;}
        if(oldest && newest){
            List<String[]> Transac= new ArrayList();
            Transac.add(service.oldestTransaction(typeOfData,p_id,from,to));
            Transac.add(service.newerTransaction(typeOfData,p_id,from,to));
            return Transac;
        }
        else if(oldest){
            List<String[]> oldestTransac= new ArrayList();
            oldestTransac.add(service.oldestTransaction(typeOfData,p_id,from,to));
            return oldestTransac;
        }
        else if(newest) {
            List<String[]> newestTransac = new ArrayList();
            newestTransac.add(service.newerTransaction(typeOfData,p_id,from,to));
            return newestTransac;
        }
        else{return service.allTransaction(typeOfData, p_id,from, to);}
    }

    @GetMapping("/transaction_value_summary")
    public TransactionSummary getSummary(@RequestParam (required = false) String from_date, @RequestParam (required = false) String to_date, @RequestParam (required = false) String product_id, @RequestParam (required=false) String typeOfData) throws IOException, ParseException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from = null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        TransactionSummary valueSummary=new TransactionSummary(valueSummaryService.meanOfTransaction(from,to,product_id,typeOfData),valueSummaryService.modeOfTransaction(from,to,product_id,typeOfData),valueSummaryService.standardDeviationOfTransaction(from,to,product_id,typeOfData),valueSummaryService.varianceOfTransaction(from,to,product_id,typeOfData));
        return valueSummary;
    }

    @GetMapping("/products")
    public String[][] allProducts(@RequestParam (required = false) String from_date, @RequestParam (required = false) String to_date, @RequestParam (required=false) String typeOfData,@RequestParam (required = false) boolean most_common,@RequestParam (required = false) boolean lest_common) throws IOException, CsvException, ParseException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from = null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        if(most_common && lest_common){
            return productService.CommonProduct(typeOfData,from,to,true,true);
        }
        else if(most_common)
            return productService.CommonProduct(typeOfData,from,to,true,false);
        else if(lest_common)
            return productService.CommonProduct(typeOfData,from,to,false,true);
        else
            return productService.listCommonProd(typeOfData,from,to);
    }


    @GetMapping("/getTimeDelta")
    public String timeDelta(@PathVariable (required=false) String TypeOfData) throws IOException, CsvException, ParseException {
        if(TypeOfData==""||TypeOfData==null)
            TypeOfData="Data";
        List transaction=allTransactionInPresent(TypeOfData);
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
        log.info("User has Requested for TimeDelta details"+"Mean="+TotalProcessMilliSeconds/TotalTransaction+" Mode="+String.valueOf(mode(values))+" Standard Deviation="+Math.sqrt(TotalProcessMilliSeconds/TotalTransaction));
        return "Mean="+TotalProcessMilliSeconds/TotalTransaction+" Mode="+String.valueOf(mode(values))+" Standard Deviation="+Math.sqrt(TotalProcessMilliSeconds/TotalTransaction);
    }

    @PostMapping("/getTimeDeltaInRange")
    public String timeDeltaInRange(@RequestBody DatesBetween dates,@PathVariable (required=false) String TypeOfData) throws IOException, ParseException, CsvException {
        if(TypeOfData==""||TypeOfData==null)
            TypeOfData="Data";
        List transaction=allTransactionInBetween(dates,TypeOfData);
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
        log.info("User has Requested for TimeDelta details in between "+dates+"Mean="+TotalProcessMilliSeconds/TotalTransaction+" Mode="+String.valueOf(mode(values))+" Standard Deviation="+Math.sqrt(TotalProcessMilliSeconds/TotalTransaction));
        return "Mean="+TotalProcessMilliSeconds/TotalTransaction+" Mode="+String.valueOf(mode(values))+" Standard Deviation="+Math.sqrt(TotalProcessMilliSeconds/TotalTransaction);
    }

    @GetMapping("/getTimeDeltaByProduct/{productId}")
    public String timeDeltaWithProductId(@PathVariable String productId,@PathVariable (required=false) String TypeOfData) throws IOException, CsvException, ParseException {
        if(TypeOfData==""||TypeOfData==null)
            TypeOfData="Data";
        List transaction=allTransactionInPresent(TypeOfData);
        long TotalProcessMilliSeconds = 0;
        long TotalTransaction=0;
        int c=0;
        long[] values = new long[transaction.size()];
        for(int t=0;t<transaction.size();t++){
            String[] row= (String[]) transaction.get(t);
            if(row[2].equals(productId)){
                Date dateFrom=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[0]);
                Date dateTo=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[1]);
                TotalProcessMilliSeconds = TotalProcessMilliSeconds+(dateTo.getTime() - dateFrom.getTime());
                TotalTransaction++;
                values[c] = TotalProcessMilliSeconds;
                c++;
            }
        }
        log.info("User has Requested for TimeDelta for Particular Product details"+"Mean="+TotalProcessMilliSeconds/TotalTransaction+" Mode="+String.valueOf(mode(values))+" Standard Deviation="+Math.sqrt(TotalProcessMilliSeconds/TotalTransaction));
        return "Mean="+TotalProcessMilliSeconds/TotalTransaction+" mode="+mode(values)+" Standard Deviation="+Math.sqrt(TotalProcessMilliSeconds/TotalTransaction);
    }

    @PostMapping("/getTimeDeltaInRangeByProduct/{productId}")
    public String timeDeltaInRangeWithProductId(@RequestBody DatesBetween dates, @PathVariable String productId,@PathVariable (required=false) String TypeOfData) throws IOException, ParseException, CsvException {
        if(dateChecker(dates)) {
            if(TypeOfData==""||TypeOfData==null)
                TypeOfData="Data";
            List transaction = allTransactionInBetween(dates,TypeOfData);
            long TotalProcessMilliSeconds = 0;
            long TotalTransaction = 0;
            int c = 0;
            long[] values = new long[transaction.size()];
            for (int t = 0; t < transaction.size(); t++) {
                String[] row = (String[]) transaction.get(t);
                if (row[2].equals(productId)) {
                    Date dateFrom = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[0]);
                    Date dateTo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(row[1]);
                    TotalProcessMilliSeconds = TotalProcessMilliSeconds + (dateTo.getTime() - dateFrom.getTime());
                    TotalTransaction++;
                    values[c] = TotalProcessMilliSeconds;
                    c++;
                }
            }
            log.info("User has Requested for TimeDelta details for Perticular Product in between "+dates+"Mean="+TotalProcessMilliSeconds/TotalTransaction+" Mode="+String.valueOf(mode(values))+" Standard Deviation="+Math.sqrt(TotalProcessMilliSeconds/TotalTransaction));
            return "Mean=" + TotalProcessMilliSeconds / TotalTransaction +" mode="+mode(values)+" Standard Deviation=" + Math.sqrt(TotalProcessMilliSeconds / TotalTransaction);

        }
        else

            return "Wrong Date Range";
    }

}
