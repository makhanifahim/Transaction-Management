package TransactionMonitor.com.bbd.service;

import TransactionMonitor.com.bbd.config.Logges;
import TransactionMonitor.com.bbd.model.Transaction;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class TransactionService {

    private final Logges logges = new Logges();
    String error="ERROR";

    private int getQuarter(int month){
        return switch (month) {
            case 1, 2, 3 -> 1;
            case 4, 5, 6 -> 2;
            case 7, 8, 9 -> 3;
            case 10, 11, 12 -> 4;
            default -> 0;
        };
    }
    private String conDay(Date date){
        int d = date.getDate();
        String day = d + "";
        if (d < 10)
            day = 0 + "" + d;
        return day;
    }
    private String conMonth(Date date){
        int m=date.getMonth() + 1;
        String month;
        if (m < 10) {
            month = "" + 0 + m;
        } else {
            month = m + "";
        }
        return month;
    }
    private int conYear(Date date){
        return date.getYear() + 1900;
    }
    private Date configDate(String dateAndTime) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(dateAndTime);;
        d.setHours(d.getHours() - 5);
        d.setMinutes(d.getMinutes() - 30);
        return d;
    }
    public Date StringTODate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }
    private String[] dateFromPath(String DayPath){
        SimpleDateFormat fileDate = new SimpleDateFormat("dd-MM-yyyy");
        int indexOfDash = DayPath.indexOf('.');
        String DateFromPath=DayPath.substring(0, indexOfDash);
        return DateFromPath.split("-", 0);
    }
    private List<Integer> checkPassedData(List<Transaction> transactions){
        List<Integer> problem = new ArrayList(transactions.size());
        transactions.forEach(rec->{
            try{
                Date init_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(rec.getInit_date());
                Date Conclusion_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(rec.getConclusion_date());
                if(init_date.compareTo(Conclusion_date)>0){
                    problem.add(transactions.indexOf(rec));
                }
            }catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return problem;
    }
    private boolean isFileExist(String typeOfData,Date init_date){
        String date = conDay(init_date);
        String month=conMonth(init_date);
        int year = conYear(init_date);
        int quarter = getQuarter(Integer.parseInt(month));
        File checkFile = new File(".\\"+typeOfData+"\\" + year + "\\" + quarter + "\\" + date + "-" + month + "-" + year + ".csv");
        return checkFile.exists();
    }
    private void insertDataInFile(Date init_date, boolean isFileNew, String typeOfData, String[] data) throws IOException {
        String date = conDay(init_date);
        String month=conMonth(init_date);
        int year = conYear(init_date);
        int quarter = getQuarter(Integer.parseInt(month));
        CSVWriter writer = new CSVWriter(new FileWriter(".\\"+typeOfData+"\\" + year + "\\" + quarter + "\\" + date + "-" + month + "-" + year + ".csv", true));
        if(isFileNew) {
            String[] line1 = {"init_date", "conclusion_date", "product_id", "value"};
            writer.writeNext(line1);
        }
        writer.writeNext(data);
        writer.flush();
    }

    //All files in present
    private List<String> allFilesInPresent(String TypeOfData) throws IOException {
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
    //All files in between two dates
    public List<String> allFilesInBetween(Date dateFrom,Date dateTo, String TypeOfData){
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
    //Save Transactions
    public String saveTransaction(List<Transaction> transactions, String typeOfData){
        List<Integer> errorIn = new ArrayList(transactions.size());
        if(checkPassedData(transactions).size()>0){
            logges.addInfoLog("Data passed in body was not valid",error);
            return "Problem with index value : "+ Arrays.toString(checkPassedData(transactions).toArray());
        }
        else{
            transactions.forEach(transaction -> {
                try {
                    Date init_date=configDate(transaction.getInit_date());
                    String month=conMonth(init_date);
                    int year = conYear(init_date);
                    int quarter = getQuarter(Integer.parseInt(month));
                    File file = new File(".\\"+typeOfData+"\\" + year + "\\" + quarter);
                    file.mkdirs();
                    String[] line1 = {transaction.getInit_date(), transaction.getConclusion_date(), transaction.getProduct_id().toString(), transaction.getValue().toString()};
                    insertDataInFile(init_date,!isFileExist(typeOfData,init_date),typeOfData,line1);

                }
                catch (Exception ex) {
                    logges.addInfoLog("Data passed in body was not valid :"+ex.toString(),error);
                    errorIn.add(transactions.indexOf(transaction));
                }
            });
            if(errorIn.size()>0) {
                logges.addInfoLog("Data passed in body was not valid index with error is :"+Arrays.toString(errorIn.toArray()),error);
                return "Problem with index value : " + Arrays.toString(errorIn.toArray());
            }else
                return "Successfully Inserted";
        }
    }
    //All Transaction
    public List<String[]> allTransaction(String TypeOfData,String product_id,Date from_date,Date to_date) throws IOException, ParseException, CsvException {
        List paths;
        if(from_date==null && to_date==null)
            paths=allFilesInPresent(TypeOfData);
        else if(from_date==null && to_date!=null) {
            paths = allFilesInBetween(StringTODate(oldestTransaction(TypeOfData,product_id,null,null).getInit_date()),to_date,TypeOfData);
        }
        else if(from_date!=null && to_date==null){
            paths = allFilesInBetween(from_date,StringTODate(newerTransaction(TypeOfData,product_id,null,null).getInit_date()),TypeOfData);
        }
        else{
            paths = allFilesInBetween(from_date,to_date,TypeOfData);
        }
        ArrayList<String[]> temp = new ArrayList<>();
        for (Object path : paths) {
            CSVReader readFile = new CSVReader(new FileReader(path.toString()));
            readFile.readNext();
            List<String[]> rows = readFile.readAll();
            for (String[] row : rows) {
                if (Objects.equals(row[2], product_id))
                    temp.add(row);
                else if(product_id==null)
                    temp.add(row);
            }
        }
        return temp;
    }
    public List<Transaction> allTransactionInFormat(String TypeOfData,String product_id,Date from_date,Date to_date) throws IOException, ParseException, CsvException {
        List<String[]> listOfTransaction=allTransaction(TypeOfData,product_id,from_date,to_date);
        List<Transaction> transactions= new ArrayList<>();
        for (String[] strings : listOfTransaction)
            transactions.add(new Transaction(strings[0], strings[1], strings[2], strings[3]));
        return transactions;
    }
    //Oldest Transaction
    public Transaction oldestTransaction(String TypeOfData,String product_id, Date from_date, Date to_date) throws IOException, ParseException, CsvException {
        if(Objects.equals(TypeOfData, "") ||TypeOfData==null)
            TypeOfData="Data";
        List<String[]> transaction = allTransaction(TypeOfData,product_id,from_date,to_date);
        Date oldestTransactionDate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transaction.get(0)[0]);
        String[] oldestTransaction;
        oldestTransaction=transaction.get(0);
        for (String[] transac : transaction) {
            Date dateOfTransaction = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transac[0]);
            if ((dateOfTransaction.getTime() - oldestTransactionDate.getTime()) < 0) {
                oldestTransactionDate = dateOfTransaction;
                oldestTransaction = transac;
            }
        }
        return new Transaction(oldestTransaction[0],oldestTransaction[1],oldestTransaction[2],oldestTransaction[3]);
    }
    //Newest Transaction
    public Transaction newerTransaction(String TypeOfData,String product_id,Date from_date,Date to_date) throws IOException, CsvException, ParseException {
        if(Objects.equals(TypeOfData, "") ||TypeOfData==null)
            TypeOfData="Data";
        List<String[]> transaction = allTransaction(TypeOfData,product_id,from_date,to_date);
        Date newestTransactiondate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transaction.get(0)[0]);
        String[] newestTransaction = new String[4];
        newestTransaction=transaction.get(0);
        for (String[] strings : transaction) {
            Date dateOftransaction = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(strings[0]);
            if ((dateOftransaction.getTime() - newestTransactiondate.getTime()) > 0) {
                newestTransactiondate = dateOftransaction;
                newestTransaction = strings;
            }
        }
        return  new Transaction(newestTransaction[0],newestTransaction[1],newestTransaction[2],newestTransaction[3]);
    }

}
