package TransactionMonitor.com.bbd.service;

import TransactionMonitor.com.bbd.config.Logges;
import TransactionMonitor.com.bbd.model.Transaction;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
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
        Date d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(dateAndTime);
        d.setHours(d.getHours() - 5);
        d.setMinutes(d.getMinutes() - 30);
        return d;
    }
    public Date StringTODate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }
    private String[] dateFromPath(String DayPath){
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
            for(int quarter = 0; quarter< (long) listQua.size(); quarter++){
                List listDailyFile = Files.list(Paths.get(listQua.get(quarter).toString())).toList();
                for(int fDay = 0; fDay< (long) listDailyFile.size(); fDay++){
                    paths.add(listDailyFile.get(fDay).toString());
                }
            }
        }
        return paths;
    }
    //All files in between two dates
    public List<String> allFilesInBetween(Date dateFrom,Date dateTo, String TypeOfData){
        int monthFrom = dateFrom.getMonth()+1;
        int quarterFrom = getQuarter(monthFrom);
        int yearFrom = dateFrom.getYear()+1900;
        int dayFrom=dateFrom.getDate();
        int monthTo = dateTo.getMonth()+1;
        int quarterTo = getQuarter(monthTo);
        int yearTo = dateTo.getYear()+1900;
        int dayTo=dateTo.getDate();
        List<String> BetweenFilePath= new ArrayList<>();
        File directoryPathYear = new File(".//"+TypeOfData);
        String[] yearsPath = directoryPathYear.list();
        for (String s : yearsPath) {
            if (yearFrom <= Integer.parseInt(String.valueOf(s)) && yearTo >= Integer.parseInt(String.valueOf(s))) {
                if (yearFrom == yearTo) {
                    File directoryPathQua = new File(".//" + TypeOfData + "//" + s);
                    String[] quarterPath = directoryPathQua.list();
                    for (int q = 0; q < directoryPathQua.list().length; q++) {
                        if (quarterFrom <= Integer.parseInt(String.valueOf(quarterPath[q]))) {
                            File directoryPathDay = new File(".//" + TypeOfData + "//" + s + "//" + quarterPath[q]);
                            String[] DayPath = directoryPathDay.list();
                            for (int d = 0; d < directoryPathDay.list().length; d++) {
                                String[] TransactDate = dateFromPath(DayPath[d]);

                                if (monthFrom < Integer.parseInt(TransactDate[1]) && monthTo > Integer.parseInt(TransactDate[1])) {
                                    BetweenFilePath.add(".//" + TypeOfData + "//" + s + "//" + quarterPath[q] + "//" + DayPath[d]);
                                }
                                if (monthFrom == Integer.parseInt(TransactDate[1]) && yearFrom == Integer.parseInt(TransactDate[2])) {
                                    if (dayFrom <= Integer.parseInt(TransactDate[0])) {
                                        BetweenFilePath.add(".//" + TypeOfData + "//" + s + "//" + quarterPath[q] + "//" + DayPath[d]);
                                    }
                                }
                                if (monthTo == Integer.parseInt(TransactDate[1]) && yearTo == Integer.parseInt(TransactDate[2])) {
                                    if (dayTo >= Integer.parseInt(TransactDate[0])) {
                                        BetweenFilePath.add(".//" + TypeOfData + "//" + s + "//" + quarterPath[q] + "//" + DayPath[d]);
                                    }
                                }
                            }
                        }
                    }
                }
                else if (Integer.parseInt(s) == yearFrom) {
                    File directoryPathQua = new File(".//" + TypeOfData + "//" + s);
                    String[] quarterPath = directoryPathQua.list();
                    for (int q = 0; q < directoryPathQua.list().length; q++) {
                        if (quarterFrom <= Integer.parseInt(String.valueOf(quarterPath[q]))) {
                            File directoryPathDay = new File(".//" + TypeOfData + "//" + s + "//" + quarterPath[q]);
                            String[] DayPath = directoryPathDay.list();
                            for (int d = 0; d < directoryPathDay.list().length; d++) {
                                String[] TransactDate = dateFromPath(DayPath[d]);

                                if (monthFrom < Integer.parseInt(TransactDate[1])) {
                                    BetweenFilePath.add(String.valueOf(".//" + TypeOfData + "//" + s + "//" + quarterPath[q] + "//" + DayPath[d]));
                                }
                                if (monthFrom == Integer.parseInt(TransactDate[1]) && yearFrom == Integer.parseInt(TransactDate[2])) {
                                    if (dayFrom <= Integer.parseInt(TransactDate[0])) {
                                        BetweenFilePath.add(String.valueOf(".//" + TypeOfData + "//" + s + "//" + quarterPath[q] + "//" + DayPath[d]));
                                    }
                                }
                            }
                        }
                    }
                }
                else if (Integer.parseInt(s) == yearTo) {
                    File directoryPathQua = new File(".//" + TypeOfData + "//" + s);
                    String[] quarterPath = directoryPathQua.list();
                    for (int q = 0; q < directoryPathQua.list().length; q++) {
                        if (quarterTo >= Integer.parseInt(String.valueOf(quarterPath[q]))) {
                            File directoryPathDay = new File(".//" + TypeOfData + "//" + s + "//" + quarterPath[q]);
                            String[] DayPath = directoryPathDay.list();
                            for (int d = 0; d < directoryPathDay.list().length; d++) {
                                String[] TransactDate = dateFromPath(DayPath[d]);
                                if (monthTo > Integer.parseInt(TransactDate[1])) {
                                    BetweenFilePath.add(String.valueOf(".//" + TypeOfData + "//" + s + "//" + quarterPath[q] + "//" + DayPath[d]));
                                }
                                if (monthTo == Integer.parseInt(TransactDate[1]) && yearTo == Integer.parseInt(TransactDate[2])) {
                                    if (dayTo >= Integer.parseInt(TransactDate[0])) {
                                        BetweenFilePath.add(String.valueOf(".//" + TypeOfData + "//" + s + "//" + quarterPath[q] + "//" + DayPath[d]));
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    File directoryPathQua = new File(".//" + TypeOfData + "//" + s);
                    String[] quarterPath = directoryPathQua.list();
                    for (int q = 0; q < directoryPathQua.list().length; q++) {
                        File directoryPathDay = new File(".//" + TypeOfData + "//" + s + "//" + quarterPath[q]);
                        String[] dayPath = directoryPathDay.list();
                        for (int d = 0; d < directoryPathDay.list().length; d++) {
                            BetweenFilePath.add(String.valueOf(".//" + TypeOfData + "//" + s + "//" + quarterPath[q] + "//" + dayPath[d]));
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
            }else{
                return "Successfully Inserted";
            }
        }
    }
    //All Transaction
    public List<Transaction> allTransaction(String TypeOfData,String product_id,Date from_date,Date to_date) throws IOException, ParseException, CsvException {
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
        ArrayList<Transaction> temp = new ArrayList<>();
        for (Object path : paths) {
            CSVReader readFile = new CSVReader(new FileReader(path.toString()));
            readFile.readNext();
            List<String[]> rows = readFile.readAll();
            for (String[] row : rows) {
                if (Objects.equals(row[2], product_id))
                    temp.add(new Transaction(row[0],row[1],row[2],new BigDecimal(row[3])));
                else if(product_id==null)
                    temp.add(new Transaction(row[0],row[1],row[2],new BigDecimal(row[3])));
            }
        }
        return temp;
    }
    //Oldest Transaction
    public Transaction oldestTransaction(String TypeOfData,String product_id, Date from_date, Date to_date) throws IOException, ParseException, CsvException {
        if(Objects.equals(TypeOfData, "") ||TypeOfData==null)
            TypeOfData="Data";
        List<Transaction> transaction = allTransaction(TypeOfData,product_id,from_date,to_date);
        Date oldestTransactionDate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transaction.get(0).getInit_date());
        Transaction oldestTransaction;
        oldestTransaction=transaction.get(0);
        for (Transaction transact : transaction) {
            Date dateOfTransaction = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transact.getInit_date());
            if ((dateOfTransaction.getTime() - oldestTransactionDate.getTime()) < 0) {
                oldestTransactionDate = dateOfTransaction;
                oldestTransaction = transact;
            }
        }
        return oldestTransaction;
    }
    //Newest Transaction
    public Transaction newerTransaction(String TypeOfData,String product_id,Date from_date,Date to_date) throws IOException, CsvException, ParseException {
        if(Objects.equals(TypeOfData, "") ||TypeOfData==null)
            TypeOfData="Data";
        List<Transaction> transaction = allTransaction(TypeOfData,product_id,from_date,to_date);
        Date newestTransactionDate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(transaction.get(0).getInit_date());
        Transaction newestTransaction=transaction.get(0);
        for (Transaction strings : transaction) {
            Date dateOfTransaction = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(strings.getInit_date());
            if ((dateOfTransaction.getTime() - newestTransactionDate.getTime()) > 0) {
                newestTransactionDate = dateOfTransaction;
                newestTransaction = strings;
            }
        }
        return  newestTransaction;
    }

}
