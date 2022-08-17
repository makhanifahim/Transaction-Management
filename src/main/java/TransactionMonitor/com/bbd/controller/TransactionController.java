package TransactionMonitor.com.bbd.controller;

import TransactionMonitor.com.bbd.model.DatesBetween;
import TransactionMonitor.com.bbd.model.Transaction;
import TransactionMonitor.com.bbd.model.TransactionValueSummary;
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
    public TransactionValueSummary getSummary(@RequestParam (required = false) String from_date, @RequestParam (required = false) String to_date, @RequestParam (required = false) String product_id, @RequestParam (required=false) String typeOfData) throws IOException, ParseException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from = null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        TransactionValueSummary valueSummary=new TransactionValueSummary(valueSummaryService.meanOfTransaction(from,to,product_id,typeOfData),valueSummaryService.modeOfTransaction(from,to,product_id,typeOfData),valueSummaryService.standardDeviationOfTransaction(from,to,product_id,typeOfData),valueSummaryService.varianceOfTransaction(from,to,product_id,typeOfData));
        return valueSummary;
    }

    @GetMapping("/transaction_value_summary/{summary_type}")
    public TransactionValueSummary getPerticularSummary(@PathVariable(required = false) String summary_type,@RequestParam (required = false) String from_date, @RequestParam (required = false) String to_date, @RequestParam (required = false) String product_id, @RequestParam (required=false) String typeOfData) throws IOException, ParseException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from = null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        float mean = 0,sDevi=0,variance=0;
        String mode = null;
        if(Objects.equals(summary_type, "mean") ||summary_type==null)
            mean= Float.parseFloat(String.valueOf(valueSummaryService.meanOfTransaction(from,to,product_id,typeOfData)));
        if(Objects.equals(summary_type, "mode") ||summary_type==null)
            mode=valueSummaryService.modeOfTransaction(from,to,product_id,typeOfData);
        if(Objects.equals(summary_type, "standard_deviation") ||summary_type==null)
            sDevi=valueSummaryService.standardDeviationOfTransaction(from,to,product_id,typeOfData);
        if(Objects.equals(summary_type, "variance") ||summary_type==null)
            variance=valueSummaryService.varianceOfTransaction(from,to,product_id,typeOfData);
        if(summary_type!=null)
            return new TransactionValueSummary(mean,mode,sDevi,variance);
        else
            return getSummary(from_date,to_date,product_id,typeOfData);

    }


    @GetMapping("/mean")
    public float meanOfTransaction(@PathVariable (required=false) String TypeOfData) throws IOException, CsvException {
        if(Objects.equals(TypeOfData, "") ||TypeOfData==null)
            TypeOfData="Data";
        List<String[]> allTransaction = allTransactionInPresent(TypeOfData);
        int TotalDays=0;
        float TotalAmount=0;
        for (int t = 0; t< (long) allTransaction.size(); t++){
            String[] Transaction=new String[4];
            Transaction= (String[]) allTransaction.get(t);
            TotalDays++;
            TotalAmount= Float.parseFloat(Transaction[3])+TotalAmount;
        }
        log.info("User has Requested for Mean which is {}",TotalAmount/TotalDays);
        return TotalAmount/TotalDays;
    }

    @PostMapping("/meanInRange")
    public float meanInRange(@RequestBody DatesBetween dates,@PathVariable (required=false) String TypeOfData) throws IOException, ParseException, CsvException {
        if(dateChecker(dates) || dates.equals(null)) {
            if(TypeOfData==""||TypeOfData==null)
                TypeOfData="Data";
            List allTransaction = allTransactionInBetween(dates,TypeOfData);
            int TotalDays = 0;
            float TotalAmount = 0;
            for (int t = 0; t < allTransaction.stream().count(); t++) {
                String[] Transaction = new String[4];
                Transaction = (String[]) allTransaction.get(t);
                TotalDays++;
                TotalAmount = Float.parseFloat(Transaction[3]) + TotalAmount;
            }
            log.info("User has Requested for mean in range {} is {}",dates,TotalAmount/TotalDays);
            return TotalAmount / TotalDays;
        }
        else{
            log.error("User has Requested for mean in range {} which is not Proper",dates);
            return 0;
        }
    }

    @GetMapping("/mode")
    public String modeOfTransaction(@PathVariable (required=false) String TypeOfData) throws IOException, CsvException {
        if (TypeOfData==""||TypeOfData==null)
            TypeOfData="Data";
        int c=0;
        List allTransaction = allTransactionInPresent(TypeOfData);
        String[][] values = new String[allTransaction.size()][2];
        int present;
        for (int t=0;t<allTransaction.stream().count();t++){
            String[] row;
            row= (String[]) allTransaction.get(t);
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
        log.info("User has Requested for Mode which is {}",findModel(values));
        return findModel(values);
    }

    @PostMapping("modeInRange")
    public String modeInRange(@RequestBody DatesBetween dates,@PathVariable (required=false) String TypeOfData) throws IOException, ParseException, CsvException {
        if(dateChecker(dates)) {
            if(TypeOfData==""||TypeOfData==null)
                TypeOfData="Data";
            int c = 0;
            List allTransaction = allTransactionInBetween(dates,TypeOfData);
            String[][] values = new String[allTransaction.size()][2];
            int present;
            for (int t = 0; t < allTransaction.stream().count(); t++) {
                String[] row;
                row = (String[]) allTransaction.get(t);
                present = 0;
                for (int i = 0; i < values.length; i++) {
                    if (values[i][0] != null) {
                        if (values[i][0].toString().equals(row[3].toString())) {
                            present = 1;
                            values[i][1] = String.valueOf(Integer.parseInt(values[i][1]) + 1);
                        }
                    }
                }
                if (present == 0) {
                    values[c][0] = row[3];
                    values[c][1] = String.valueOf(0);
                    c++;
                }
            }
            log.info("User has Requested for Mode in range {} is {}",dates,findModel(values));
            return findModel(values);
        }
        else {
            log.error("User has Requested for Mode in range {} which is not Proper",dates);
            return "Wrong Date Range";
        }
    }

    @GetMapping("/standardDeviation")
    public float sDOfTransaction(@PathVariable (required=false) String TypeOfData) throws IOException, CsvException {
        if(TypeOfData==""||TypeOfData==null)
            TypeOfData="Data";
        log.info("User has Requested for Standard Deviation which is {}","Standard Deviation="+Math.sqrt(meanOfTransaction(TypeOfData)));
        return (float) Math.sqrt(meanOfTransaction(TypeOfData));
    }

    @PostMapping("/SDInRange")
    public float sDInRange(@RequestBody DatesBetween dates,@PathVariable (required=false) String TypeOfData) throws IOException, ParseException, CsvException {
        if(TypeOfData==""||TypeOfData==null)
            TypeOfData="Data";
        if(dateChecker(dates)) {
            if(TypeOfData==""||TypeOfData==null)
                TypeOfData="Data";
            log.info("User has Requested for Standard Deviation in range {} is {}",dates,Math.sqrt(meanInRange(dates,TypeOfData)));
            return (float) Math.sqrt(meanInRange(dates,TypeOfData));
        }
        else {
            log.error("User has Requested for Standard Deviation in range {} which is not Proper",dates);
            return 0;
        }
    }

    @GetMapping("/mostCommonProduct")
    public String mostCommonProduct(@PathVariable (required=false) String TypeOfData) throws IOException, CsvException {
        if (TypeOfData==""||TypeOfData==null)
            TypeOfData="Data";
        int c=0;
        int j=0;
        String[][] products=listCommonProd(TypeOfData);
        int count=Integer.parseInt(products[0][1]);
        String prod=products[0][0];
        for(int i=1;i<products.length-1;i++){
            if(products[i][0]!=null) {
                if(Integer.parseInt(products[i][1])>count){
                    count=Integer.parseInt(products[i][1]);
                    count=count+1;
                    prod=products[i][0];
                }
            }
        }
        log.info("User has Requested for Most Common Product {}",prod);
        return prod;
    }

    @PostMapping("/mostCPInRange")
    public String mostCPInBetween(@RequestBody DatesBetween dates,@PathVariable (required=false) String TypeOfData) throws IOException, ParseException, CsvException {
        if(dateChecker(dates)) {
            if(TypeOfData==""||TypeOfData==null)
                TypeOfData="Data";
            int c = 0;
            int j = 0;
            String[][] products = listCommonProdInRange(dates,TypeOfData);
            int count = Integer.parseInt(products[0][1]);
            String prod = products[0][0];
            for (int i = 1; i < products.length - 1; i++) {
                if (products[i][0] != null) {
                    if (Integer.parseInt(products[i][1]) > count) {
                        count = Integer.parseInt(products[i][1]);
                        count = count + 1;
                        prod = products[i][0];
                    }
                }
            }
            log.info("User has Requested for Most Common Product in range {} is {}",dates,prod);
            return prod;
        }
        else {
            log.error("User has Requested for Most Common Product in range {} which is not Proper",dates);
            return "Wrong Date Range";
        }
    }

    @GetMapping("/variance")
    public float varianceOfTransaction(@PathVariable (required=false) String TypeOfData) throws IOException, CsvException {
        if(TypeOfData==""||TypeOfData==null)
            TypeOfData="Data";
        float mean=meanOfTransaction(TypeOfData);
        float XSeq = 0;
        float TotalXX = 0;
        int TotalTran=0;
        List allTransaction = allTransactionInPresent(TypeOfData);
        for (int t=0;t<allTransaction.stream().count();t++){
            String[] row=new String[4];
            row= (String[]) allTransaction.get(t);
            TotalTran++;
            XSeq=Float.parseFloat(row[3])-mean;
            XSeq=XSeq*XSeq;
            TotalXX=TotalXX+XSeq;
        }
        log.info("User has Requested for Variance {}",TotalXX/TotalTran);
        return TotalXX/TotalTran;
    }

    @PostMapping("/varianceInRange")
    public float varianceInBetween(@RequestBody DatesBetween dates,@PathVariable (required=false) String TypeOfData) throws IOException, ParseException, CsvException {
        if(dateChecker(dates)) {
            if(TypeOfData==""||TypeOfData==null)
                TypeOfData="Data";
            float mean = meanOfTransaction(TypeOfData);
            float XSeq = 0;
            float TotalXX = 0;
            int TotalTran = 0;
            List allTransaction = allTransactionInBetween(dates,TypeOfData);
            for (int t = 0; t < allTransaction.stream().count(); t++) {
                String[] row = new String[4];
                row = (String[]) allTransaction.get(t);
                TotalTran++;
                XSeq = Float.parseFloat(row[3]) - mean;
                XSeq = XSeq * XSeq;
                TotalXX = TotalXX + XSeq;
            }
            log.info("User has Requested for variance in range {} is {}",dates,TotalXX/TotalTran);
            return TotalXX / TotalTran;
        }
        else{
            log.error("User has Requested for variance in range {} which is not Proper",dates);
            return 0;
        }
    }

    @GetMapping("/leastCommonProduct")
    public String leastCommonProduct(@PathVariable (required=false) String TypeOfData) throws IOException, CsvException {
        if(TypeOfData==""||TypeOfData==null)
            TypeOfData="Data";
        int c=0;
        int j=0;
        String[][] products=listCommonProd(TypeOfData);
        int count=Integer.parseInt(products[0][1]);
        String prod=products[0][0];
        for(int i=1;i<products.length-1;i++){
            if(products[i][0]!=null) {
                if(Integer.parseInt(products[i][1])<=count && Integer.parseInt(products[i][1])!=0){
                    count=Integer.parseInt(products[i][1]);
                    count=count+1;
                    prod=products[i][0];

                }
            }
        }
        log.info("User has Requested for Least Common Product is {}",prod);
        return prod;
    }

    @PostMapping("/leastCPInRange")
    public String leastCommonProduct(@RequestBody DatesBetween dates,@PathVariable (required=false) String TypeOfData) throws IOException, ParseException, CsvException {
        if(dateChecker(dates)) {
            if(TypeOfData==""||TypeOfData==null)
                TypeOfData="Data";
            int c = 0;
            int j = 0;
            String[][] products = listCommonProdInRange(dates,TypeOfData);
            int count = Integer.parseInt(products[0][1]);
            String prod = products[0][0];
            for (int i = 1; i < products.length - 1; i++) {
                if (products[i][0] != null) {
                    if (Integer.parseInt(products[i][1]) <= count && Integer.parseInt(products[i][1]) != 0) {
                        count = Integer.parseInt(products[i][1]);
                        count = count + 1;
                        prod = products[i][0];

                    }
                }
            }
            log.info("User has Requested for Lest Common Product in range {} is {}",dates,prod);
            return prod;
        }
        else {
            log.error("User has Requested for Lest Common Product in range {} which is not Proper",dates);
            return "Wrong Date Range";
        }
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

    public String oldestFile() throws IOException {
        return allFilesInPresent("Data").get(0);
    }

    public String newestFile() throws IOException, CsvException {
        return allFilesInPresent("Data").get((allFilesInPresent("Data").size()-1));
    }
}
