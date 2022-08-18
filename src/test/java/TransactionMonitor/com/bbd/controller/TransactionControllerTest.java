package TransactionMonitor.com.bbd.controller;

import TransactionMonitor.com.bbd.model.Transaction;
import TransactionMonitor.com.bbd.service.TransactionService;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TransactionControllerTest {

    @Autowired
    private TransactionController transactionController;

    @Autowired
    private TransactionService service;

    @BeforeAll
    static void setUp() throws IOException {
        String filepath = ".//TestData";
        File file = new File(filepath);
        if(file.exists()) {
            FileUtils.cleanDirectory(file);
            file.delete();
        }
        System.out.println("Starting test and setting up ");
    }

    @Test
    void saveTransaction() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        //1998
        Transaction rec = new Transaction("1998-01-01T12:00:00.505Z","1998-01-01T14:00:55.000Z",1,3008.0F);
        transactions.add(rec);
        Transaction rec1 = new Transaction("1998-02-02T11:00:00.505Z","1998-02-02T13:00:40.770Z",2,308.0F);
        transactions.add(rec1);
        Transaction rec2 = new Transaction("1998-03-03T01:00:00.505Z","1998-03-03T13:10:01.500Z",3,38.0F);
        transactions.add(rec2);

        Transaction rec3 = new Transaction("1998-04-04T13:00:00.505Z","1998-04-04T13:00:02.510Z",4,7.0F);
        transactions.add(rec3);
        Transaction rec4 = new Transaction("1998-05-01T13:00:00.505Z","1998-05-01T14:00:03.000Z",2,3208.0F);
        transactions.add(rec4);
        Transaction rec5 = new Transaction("1998-06-02T13:00:00.505Z","1998-06-02T13:00:04.700Z",6,8.0F);
        transactions.add(rec5);

        Transaction rec6 = new Transaction("1998-07-03T13:00:00.505Z","1998-07-03T13:10:07.500Z",7,7.0F);
        transactions.add(rec6);
        Transaction rec7 = new Transaction("1998-08-04T13:00:00.505Z","1998-08-04T13:00:05.510Z",2,8.8F);
        transactions.add(rec7);
        Transaction rec8 = new Transaction("1998-09-01T13:00:00.505Z","1998-09-01T14:00:06.000Z",5,1118.0F);
        transactions.add(rec8);

        Transaction rec9 = new Transaction("1998-10-02T13:00:00.505Z","1998-10-02T13:00:08.700Z",2,1108.0F);
        transactions.add(rec9);
        Transaction rec10 = new Transaction("1998-11-03T13:00:00.505Z","1998-11-03T13:10:00.500Z",7,108.0F);
        transactions.add(rec10);
        Transaction rec11 = new Transaction("1998-12-04T13:00:00.505Z","1998-12-04T13:00:09.510Z",2,3.8F);
        transactions.add(rec11);

        //1999
        Transaction rec12 = new Transaction("1999-01-01T13:00:00.505Z","1999-01-01T14:00:07.000Z",1,3001.9F);
        transactions.add(rec12);
        Transaction rec13 = new Transaction("1999-02-02T13:00:00.505Z","1999-02-02T13:00:02.700Z",2,301.9F);
        transactions.add(rec13);
        Transaction rec14 = new Transaction("1999-03-03T13:00:00.505Z","1999-03-03T13:10:03.500Z",2,31.9F);
        transactions.add(rec14);

        Transaction rec15 = new Transaction("1999-04-04T13:00:00.505Z","1999-04-04T13:00:04.510Z",4,9.0F);
        transactions.add(rec15);
        Transaction rec16 = new Transaction("1999-05-01T13:00:00.505Z","1999-05-01T14:00:05.000Z",5,3201.9F);
        transactions.add(rec16);
        Transaction rec17 = new Transaction("1999-06-02T13:00:00.505Z","1999-06-02T13:00:08.700Z",2,9.0F);
        transactions.add(rec17);

        Transaction rec18 = new Transaction("1999-07-03T13:00:00.505Z","1999-07-03T13:10:07.500Z",7,11.9F);
        transactions.add(rec18);
        Transaction rec19 = new Transaction("1999-08-04T13:00:00.505Z","1999-08-04T13:00:05.510Z",2,1.9F);
        transactions.add(rec19);
        Transaction rec20 = new Transaction("1999-09-01T13:00:00.505Z","1999-09-01T14:00:01.000Z",5,1119.0F);
        transactions.add(rec20);

        Transaction rec21 = new Transaction("1999-10-02T13:00:00.505Z","1999-10-02T13:00:10.700Z",6,1109.0F);
        transactions.add(rec21);
        Transaction rec22 = new Transaction("1999-11-03T13:00:00.505Z","1999-11-03T13:10:10.500Z",7,191.0F);
        transactions.add(rec22);
        Transaction rec23 = new Transaction("1999-12-04T13:00:00.505Z","1999-12-04T13:00:00.920Z",8,9.0F);
        transactions.add(rec23);

        //1997
        Transaction rec24 = new Transaction("1997-01-01T13:00:00.505Z","1997-01-01T14:01:00.000Z",1,3001.7F);
        transactions.add(rec24);
        Transaction rec25 = new Transaction("1997-02-02T13:00:00.505Z","1997-02-02T13:05:00.700Z",5,301.7F);
        transactions.add(rec25);
        Transaction rec26 = new Transaction("1997-03-03T13:00:00.505Z","1997-04-03T13:20:00.500Z",3,31.7F);
        transactions.add(rec26);

        Transaction rec27 = new Transaction("1997-04-04T13:00:00.505Z","1997-04-04T13:20:00.600Z",4,7.0F);
        transactions.add(rec27);
        Transaction rec28 = new Transaction("1997-05-01T13:00:00.505Z","1997-05-01T14:05:00.700Z",5,7201.0F);
        transactions.add(rec28);
        Transaction rec29 = new Transaction("1997-06-02T13:00:00.505Z","1997-06-02T13:10:00.800Z",6,7.0F);
        transactions.add(rec29);

        Transaction rec30 = new Transaction("1997-07-03T13:00:00.505Z","1997-07-03T13:10:00.900Z",7,17.0F);
        transactions.add(rec30);
        Transaction rec31 = new Transaction("1997-08-04T13:00:00.505Z","1997-08-04T13:00:10.510Z",8,7.0F);
        transactions.add(rec31);
        Transaction rec32 = new Transaction("1997-09-01T13:00:00.505Z","1997-10-01T14:00:00.000Z",5,7111.0F);
        transactions.add(rec32);

        Transaction rec33 = new Transaction("1997-10-02T13:00:00.505Z","1997-10-02T13:00:00.700Z",6,1171.0F);
        transactions.add(rec33);
        Transaction rec34 = new Transaction("1997-11-03T13:00:00.505Z","1997-11-03T13:10:00.500Z",5,107.0F);
        transactions.add(rec34);
        Transaction rec35 = new Transaction("1997-12-04T13:00:00.505Z","1997-12-04T13:01:00.510Z",8,3.7F);
        transactions.add(rec35);

        String actualResult = service.saveTransaction(transactions,"TestData");
        assertThat(actualResult).isEqualTo("Successfully Inserted");
    }


    @Test
    void wrongDateAndTimeFormat(){
        List<Transaction> transactions = new ArrayList<Transaction>();
        Transaction rec = new Transaction("1998-01-05","1998-01-05",1,3001.0F);
        transactions.add(rec);
        String actualResult = service.saveTransaction(transactions,"TestData");
        assertThat(actualResult).isEqualTo("Problem with index value : [0]");
    }

    @Test
    void wrongDateAndTime(){
        List<Transaction> transactions = new ArrayList<Transaction>();
        Transaction rec = new Transaction("1998-01-01T13:00:00.505Z","1998-01-01T13:00:00.005Z",1,3001.0F);
        transactions.add(rec);
        String actualResult = transactionController.saveTransactions(transactions,"TestData");
        assertThat(actualResult).isEqualTo("Problem with index value : [0]");
    }

    @Test
    void checkSaveDataController(){
        List<Transaction> transactions = new ArrayList<Transaction>();
        Transaction rec = new Transaction("1998-01-01T13:00:00.505Z","1998-01-01T13:00:00.605Z",1,3001.0F);
        transactions.add(rec);
        Transaction rec1 = new Transaction("1998-01-01T13:10:00.505Z","1998-01-01T13:20:00.605Z",1,3001.0F);
        transactions.add(rec1);
        String actualResult = transactionController.saveTransactions(transactions,"TestData");
        assertThat(actualResult).isEqualTo("Successfully Inserted");
    }

    @Test
    void checkOldest() throws IOException, ParseException, CsvException {
        List<String[]> expectedResult=new ArrayList<>();
        String[] transaction = new String[] {"1997-01-01T13:00:00.505Z", "1997-01-01T13:00:00.505Z", "1", "3001.7"};
        expectedResult.add(transaction);
        List<String[]> actualResult= transactionController.getTransactions(null,null,true,false,null,"TestData");
        assertTrue(Objects.equals(actualResult.get(0)[1], expectedResult.get(0)[1]) && Objects.equals(actualResult.get(0)[0], expectedResult.get(0)[0]) && Objects.equals(actualResult.get(0)[2], expectedResult.get(0)[2] )&& Objects.equals(actualResult.get(0)[3], expectedResult.get(0)[3]));
    }

    @Test
    void checkNewest() throws IOException, ParseException, CsvException {
        List<String[]> expectedResult = new ArrayList<>();
        String[] transaction = new String[] {"1999-12-04T13:00:00.505Z","1999-12-04T13:00:00.505Z","8","9.0"};
        expectedResult.add(transaction);
        List<String[]> actualResult= transactionController.getTransactions(null,null,false,true,null,"TestData");
        assertTrue(Objects.equals(actualResult.get(0)[1], expectedResult.get(0)[1]) && Objects.equals(actualResult.get(0)[0], expectedResult.get(0)[0]) && Objects.equals(actualResult.get(0)[2], expectedResult.get(0)[2] )&& Objects.equals(actualResult.get(0)[3], expectedResult.get(0)[3]));
    }

    @Test
    void checkOldestInRange() throws IOException, ParseException, CsvException {
        String date_from="1998-01-01";
        String date_to="1999-01-05";
        List<String[]> expectedResult=new ArrayList<>();
        String[] transaction = new String[] {"1998-01-01T12:00:00.505Z","1998-01-01T12:00:00.505Z", "1","3008.0"};
        expectedResult.add(transaction);
        List<String[]> actualResult= transactionController.getTransactions(date_from,date_to,true,false,null,"TestData");
        assertTrue(Objects.equals(actualResult.get(0)[1], expectedResult.get(0)[1]) && Objects.equals(actualResult.get(0)[0], expectedResult.get(0)[0]) && Objects.equals(actualResult.get(0)[2], expectedResult.get(0)[2] )&& Objects.equals(actualResult.get(0)[3], expectedResult.get(0)[3]));
    }

    @Test
    void checkNewestTInRange() throws IOException, ParseException, CsvException {
        String date_from="1998-01-01";
        String date_to="1998-12-05";
        List<String[]> expectedResult=new ArrayList<>();
        String[] transaction = new String[] {"1998-01-01T12:00:00.505Z","1998-01-01T12:00:00.505Z", "1","3008.0"};
        expectedResult.add(transaction);
        List<String[]> actualResult= transactionController.getTransactions(date_from,date_to,true,false,null,"TestData");
        assertTrue(Objects.equals(actualResult.get(0)[1], expectedResult.get(0)[1]) && Objects.equals(actualResult.get(0)[0], expectedResult.get(0)[0]) && Objects.equals(actualResult.get(0)[2], expectedResult.get(0)[2] )&& Objects.equals(actualResult.get(0)[3], expectedResult.get(0)[3]));
    }


    @AfterAll
    static void tearDown() throws IOException {
        System.out.println("tearing down");
    }

}