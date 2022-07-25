package TransactionMonitor.com.bbd.controller;

import TransactionMonitor.com.bbd.model.Record;
import TransactionMonitor.com.bbd.model.datesBetween;
import TransactionMonitor.com.bbd.service.SaveRecordService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
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

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private SaveRecordService service;
    private int getQuater(int month){
        return switch (month) {
            case 1, 2, 3 -> 1;
            case 4, 5, 6 -> 2;
            case 7, 8, 9 -> 3;
            case 10, 11, 12 -> 4;
            default -> 0;
        };
    }
    private String[][] listCommonProd() throws IOException, CsvException {
        int c=0;
        List allTransaction = allTransactionInPresent();
        String[][] products = new String[allTransaction.size()][2];
        for (int t = 0; t< (long) allTransaction.size(); t++){
            String[] row=new String[4];
            row= (String[]) allTransaction.get(t);
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
        return products;
    }
    private String[][] listCommonProdInRange(datesBetween dates) throws IOException, ParseException, CsvException {
        int c=0;
        List allTransaction = allTransactionInBetween(dates);
        String[][] products = new String[allTransaction.size()][2];
        for (int t = 0; t< (long) allTransaction.size(); t++){
            String[] row=new String[4];
            row= (String[]) allTransaction.get(t);
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
        return products;
    }
    private String[] dateFromPath(String DayPath) throws ParseException {
        SimpleDateFormat fileDate = new SimpleDateFormat("dd-mm-yyyy");
        int indexOfDash = DayPath.indexOf('.');
        String DateFromPath=DayPath.substring(0, indexOfDash);
        return DateFromPath.split("-", 0);
    }
    private List<String> allFilesInPresent() throws IOException {
        List<String> paths= new ArrayList<String>();
        List listYears = Files.list(Paths.get(".\\Data")).toList();
        for(int year = 0; year< (long) listYears.size(); year++){
            List listQua = Files.list(Paths.get(listYears.get(year).toString())).toList();
            for(int quater = 0; quater< (long) listQua.size(); quater++){
                //System.out.println(listQua.get(quater));
                List listDaylyFile = Files.list(Paths.get(listQua.get(quater).toString())).toList();
                for(int fday = 0; fday< (long) listDaylyFile.size(); fday++){
                    //System.out.println(listDaylyFile.get(fday));
                    paths.add(listDaylyFile.get(fday).toString());
                }
            }
        }
        return paths;
    }
    private List<String[]> allTransactionInPresent() throws IOException, CsvException {
        List paths=allFilesInPresent();
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

                if(yearFrom==yearto){
                    File directoryPathQua = new File(".//Data//"+yearsPath[y]);
                    String quaterPath[] = directoryPathQua.list();
                    for(int q=0;q<directoryPathQua.list().length;q++){
                        if(quaterFrom<=Integer.parseInt(String.valueOf(quaterPath[q]))){
                            File directoryPathDay = new File(".//Data//"+yearsPath[y]+"//"+quaterPath[q]);
                            String DayPath[]=directoryPathDay.list();
                            for (int d=0;d<directoryPathDay.list().length;d++){
                                String[] TransacDate =dateFromPath(DayPath[d]);

                                if(monthFrom<Integer.parseInt(TransacDate[1])&&monthto>Integer.parseInt(TransacDate[1])) {
                                    //System.out.println(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]);
                                    BetweenFilePath.add(String.valueOf(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                                }
                                if(monthFrom==Integer.parseInt(TransacDate[1])&&yearFrom==Integer.parseInt(TransacDate[2])){
                                    if(dayFrom<=Integer.parseInt(TransacDate[0])){
                                        //  System.out.println(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]);
                                        BetweenFilePath.add(String.valueOf(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                                    }
                                }
                                if(monthto==Integer.parseInt(TransacDate[1])&&yearto==Integer.parseInt(TransacDate[2])){
                                    if(dayto>=Integer.parseInt(TransacDate[0])){
                                        //  System.out.println(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]);
                                        BetweenFilePath.add(String.valueOf(".//Data//"+yearsPath[y]+"//"+quaterPath[q]+"//"+DayPath[d]));
                                    }
                                }
                            }
                            //System.out.println(".//Data//"+yearsPath[y]+"//"+quaterPath[q]);
                        }
                    }
                }
                else if(Integer.parseInt(yearsPath[y])==yearFrom){
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
    public List<String[]> allTransactionInBetween(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
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
    private String findmodel(String[][] values){
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
            System.out.println(array[i]);
        }
        if (maxCount > 1) {
            System.out.println(maxCount);
            return mode;
        }
        return 0;
    }
    private boolean dateChecker(@RequestBody datesBetween dates){
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
    @PostMapping("/save")
    public String saveTransaction(@RequestBody List<Record> record) {
        try{
            return service.saveTransaction(record);
        }
        catch (Exception ex){
            return ex+"";
        }
    }

    @GetMapping("/oldest")
    public String[] oldestTransaction() throws IOException, CsvException, ParseException {
        List<String[]> transaction = allTransactionInPresent();
        Date oldestTransactiondate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transaction.get(0)[0]);
        String[] oldestTransaction = new String[4];
        for(int t=0;t<transaction.size();t++){
            Date dateOftransaction=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transaction.get(t)[0]);
            if((dateOftransaction.getTime() - oldestTransactiondate.getTime())<0){
                oldestTransactiondate=dateOftransaction;
                oldestTransaction=transaction.get(t);
            }
            //System.out.println(dateOftransaction.getTime() - oldestTransactiondate.getTime());
        }
        return oldestTransaction;
    }

    @GetMapping("/try")
    public String[] trying() throws IOException, CsvException, ParseException {
        List<String[]> transaction = allTransactionInPresent();
        Date oldestTransactiondate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transaction.get(0)[0]);
        String[] oldestTransaction = new String[4];
        for(int t=0;t<transaction.size();t++){
            Date dateOftransaction=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transaction.get(t)[0]);
            if((dateOftransaction.getTime() - oldestTransactiondate.getTime())>0){
                oldestTransactiondate=dateOftransaction;
                oldestTransaction=transaction.get(t);
            }
            //System.out.println(dateOftransaction.getTime() - oldestTransactiondate.getTime());
        }
        return oldestTransaction;
    }

    @GetMapping("/oldestTInRange")
    public String[] oldTransactionInBetween(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        if(dateChecker(dates)) {
            List<String[]> transaction = allTransactionInBetween(dates);
            Date oldestTransactiondate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transaction.get(0)[0]);
            String[] oldestTransaction = new String[4];
            for(int t=0;t<transaction.size();t++){
                Date dateOftransaction=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transaction.get(t)[0]);
                if((dateOftransaction.getTime() - oldestTransactiondate.getTime())<0){
                    oldestTransactiondate=dateOftransaction;
                    oldestTransaction=transaction.get(t);
                }
                //System.out.println(dateOftransaction.getTime() - oldestTransactiondate.getTime());
            }
            return oldestTransaction;
        }else
            return null;
    }

    @GetMapping("/newestTInRange")
    public String[] newTransactionInBetween(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        if(dateChecker(dates)) {
            List<String[]> transaction = allTransactionInBetween(dates);
            Date newestTransactiondate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transaction.get(0)[0]);
            String[] newestTransaction = new String[4];
            for(int t=1;t<transaction.size();t++){
                Date dateOftransaction=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transaction.get(t)[0]);
                if((dateOftransaction.getTime() - newestTransactiondate.getTime())>0){
                    newestTransactiondate=dateOftransaction;
                    newestTransaction=transaction.get(t);
                }
                //System.out.println(dateOftransaction.getTime() - oldestTransactiondate.getTime());
            }
            return newestTransaction;
        }else
            return null;
    }
    //---- change
    @GetMapping("/newest")
    public String[] newerTransaction() throws IOException, CsvException, ParseException {
        List<String[]> transaction = allTransactionInPresent();
        Date newestTransactiondate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transaction.get(0)[0]);
        String[] newestTransaction = new String[4];
        for(int t=0;t<transaction.size();t++){
            Date dateOftransaction=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transaction.get(t)[0]);
            if((dateOftransaction.getTime() - newestTransactiondate.getTime())>0){
                newestTransactiondate=dateOftransaction;
                newestTransaction=transaction.get(t);
            }
            //System.out.println(dateOftransaction.getTime() - oldestTransactiondate.getTime());
        }
        return newestTransaction;
    }

    @GetMapping("/mean")
    public float meanOfTransaction() throws IOException, CsvException {
        List allTransaction = allTransactionInPresent();
        int TotalDays=0;
        float TotalAmount=0;
        for (int t=0;t<allTransaction.stream().count();t++){
            String[] Transaction=new String[4];
            Transaction= (String[]) allTransaction.get(t);
            TotalDays++;
            TotalAmount= Float.parseFloat(Transaction[3])+TotalAmount;
        }
        return TotalAmount/TotalDays;
    }

    @GetMapping("/meanInRange")
    public float meanInRange(@RequestBody(required = false) datesBetween dates) throws IOException, ParseException, CsvException {

        if(dateChecker(dates) || dates.equals(null)) {
            List allTransaction = allTransactionInBetween(dates);
            int TotalDays = 0;
            float TotalAmount = 0;
            for (int t = 0; t < allTransaction.stream().count(); t++) {
                String[] Transaction = new String[4];
                Transaction = (String[]) allTransaction.get(t);
                TotalDays++;
                TotalAmount = Float.parseFloat(Transaction[3]) + TotalAmount;
            }
            return TotalAmount / TotalDays;
        }
        else
            return 0;
    }

    @GetMapping("modeInRange")
    public String modeInRange(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        if(dateChecker(dates)) {
            int c = 0;
            List allTransaction = allTransactionInBetween(dates);
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
            return findmodel(values);
        }
        else
            return "Wrong Date Range";
    }

    @GetMapping("/standardDeviation")
    public String SDOfTransaction() throws IOException, CsvException {
        return "Standard Deviation="+Math.sqrt(meanOfTransaction());
    }

    @GetMapping("/SDInRange")
    public float sDInRange(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        return (float) Math.sqrt(meanInRange(dates));
    }

    @GetMapping("/mostCommonProduct")
    public String mostCommonProduct() throws IOException, CsvException {
        int c=0;
        int j=0;
        String[][] products=listCommonProd();
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
        //System.out.println("most common product is ("+prod+") with repetation of "+count);
        return prod;
    }

    @GetMapping("/mostCPInRange")
    public String mostCPInBetween(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        if(dateChecker(dates)) {
            int c = 0;
            int j = 0;
            String[][] products = listCommonProdInRange(dates);
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
            //System.out.println("most common product is ("+prod+") with repetation of "+count);
            return prod;
        }
        else
            return "Wrong Date Range";
    }

    @GetMapping("/variance")
    public float VarianceOfTransaction() throws IOException, CsvException {
        float mean=meanOfTransaction();
        float XSeq = 0;
        float TotalXX = 0;
        int TotalTran=0;
        List allTransaction = allTransactionInPresent();
        for (int t=0;t<allTransaction.stream().count();t++){
            String[] row=new String[4];
            row= (String[]) allTransaction.get(t);
            TotalTran++;
            XSeq=Float.parseFloat(row[3])-mean;
            XSeq=XSeq*XSeq;
            System.out.println("X-mean="+XSeq+" amount="+Float.parseFloat(row[3]));
            TotalXX=TotalXX+XSeq;
        }
        System.out.println("Squar total = "+TotalXX +" Total transaction"+TotalTran);
        return TotalXX/TotalTran;
    }

    @GetMapping("/varianceInRange")
    public float varianceInBetween(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        if(dateChecker(dates)) {
            float mean = meanOfTransaction();
            float XSeq = 0;
            float TotalXX = 0;
            int TotalTran = 0;
            List allTransaction = allTransactionInBetween(dates);
            for (int t = 0; t < allTransaction.stream().count(); t++) {
                String[] row = new String[4];
                row = (String[]) allTransaction.get(t);
                TotalTran++;
                XSeq = Float.parseFloat(row[3]) - mean;
                XSeq = XSeq * XSeq;
                System.out.println("X-mean=" + XSeq + " amount=" + Float.parseFloat(row[3]));
                TotalXX = TotalXX + XSeq;
            }
            System.out.println("Squar total = " + TotalXX + " Total transaction" + TotalTran);
            return TotalXX / TotalTran;
        }
        else
            return 0;
    }

    @GetMapping("/leastCommonProduct")
    public String leastCommonProduct() throws IOException, CsvException {
        int c=0;
        int j=0;
        String[][] products=listCommonProd();
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
//        System.out.println("most common product is ("+prod+") with repetation of "+count);
        return prod;
    }

    @GetMapping("/leastCPInRange")
    public String leastCommonProduct(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        if(dateChecker(dates)) {
            int c = 0;
            int j = 0;
            String[][] products = listCommonProdInRange(dates);
            int count = Integer.parseInt(products[0][1]);
            String prod = products[0][0];
            for (int i = 1; i < products.length - 1; i++) {
                if (products[i][0] != null) {
                    //System.out.println(products[i][0]+" "+(Integer.parseInt(products[i][1])+1));
                    if (Integer.parseInt(products[i][1]) <= count && Integer.parseInt(products[i][1]) != 0) {
                        count = Integer.parseInt(products[i][1]);
                        count = count + 1;
                        prod = products[i][0];

                    }
                }
            }
            //        System.out.println("most common product is ("+prod+") with repetation of "+count);
            return prod;
        }
        else
            return "Wrong Date Range";
    }

    @GetMapping("/getTimeDelta")
    public String timeDelta() throws IOException, CsvException, ParseException {
        List transaction=allTransactionInPresent();
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
        return "Mean="+TotalProcessMilliSeconds/TotalTransaction+" Mode="+String.valueOf(mode(values))+" Standard Deviation="+Math.sqrt(TotalProcessMilliSeconds/TotalTransaction);
    }

    @GetMapping("/getTimeDeltaInRange")
    public String timeDeltaInRange(@RequestBody datesBetween dates) throws IOException, ParseException, CsvException {
        List transaction=allTransactionInBetween(dates);
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

        return "Mean="+TotalProcessMilliSeconds/TotalTransaction+" Mode="+String.valueOf(mode(values))+" Standard Deviation="+Math.sqrt(TotalProcessMilliSeconds/TotalTransaction);
    }

    @GetMapping("/getTimeDeltaByProduct/{productId}")
    public String timeDelta(@PathVariable String productId) throws IOException, CsvException, ParseException {
        List transaction=allTransactionInPresent();
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
        return "Mean="+TotalProcessMilliSeconds/TotalTransaction+" mode="+mode(values)+" Standard Deviation="+Math.sqrt(TotalProcessMilliSeconds/TotalTransaction);
    }

    @GetMapping("/getTimeDeltaInRange/{productId}")
    public String timeDeltaInRangewithProductId(@RequestBody datesBetween dates,@PathVariable String productId) throws IOException, ParseException, CsvException {
        if(dateChecker(dates)) {
            List transaction = allTransactionInBetween(dates);
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
            return "Mean=" + TotalProcessMilliSeconds / TotalTransaction +" mode="+mode(values)+"Standard Deviation=" + Math.sqrt(TotalProcessMilliSeconds / TotalTransaction);
        }
        else
            return "Wrong Date Range";
    }

    @GetMapping("/mode")
    public String modeOfTransaction() throws IOException, CsvException {
        int c=0;
        List allTransaction = allTransactionInPresent();
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
        return findmodel(values);
    }
}
