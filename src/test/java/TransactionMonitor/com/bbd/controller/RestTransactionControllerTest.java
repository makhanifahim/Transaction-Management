package TransactionMonitor.com.bbd.controller;

import TransactionMonitor.com.bbd.model.DatesBetween;
import TransactionMonitor.com.bbd.model.Transaction;
import TransactionMonitor.com.bbd.model.TransactionSummary;
import TransactionMonitor.com.bbd.service.TransactionService;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class RestTransactionControllerTest {

    @Autowired
    private RestTransactionController transactionController;

    @Autowired
    private TransactionService service;

    @BeforeAll
    static void setUp() throws IOException {
        System.out.println("Starting test and setting up ");
    }
    @BeforeEach
    void beforeAll() {
        System.out.println("test started");
    }

    @Test
    void saveTransaction() throws IOException {
        String filepath = ".//TestData";
        File file = new File(filepath);
        if(file.exists()==true) {
            FileUtils.cleanDirectory(file);
            file.delete();
        }

        List<Transaction> records = new ArrayList<Transaction>();
        //1998
        Transaction  rec = new Transaction("1998-01-01T13:00:00.505Z","1998-01-01T14:00:00.000Z",1,3008.0F);
        records.add(rec);

        Transaction rec1 = new Transaction("1998-02-02T13:00:00.505Z","1998-02-03T13:00:00.700Z",2,308.0F);
        records.add(rec1);

        Transaction rec2 = new Transaction("1998-03-03T13:00:00.505Z","1998-03-03T13:10:00.500Z",3,38.0F);
        records.add(rec2);


        Transaction rec3 = new Transaction("1998-04-04T13:00:00.505Z","1998-04-04T13:00:00.900Z",4,7.0F);
        records.add(rec3);

        Transaction rec4 = new Transaction("1998-05-01T13:00:00.505Z","1998-05-01T14:00:00.900Z",2,3208.0F);
        records.add(rec4);

        Transaction rec5 = new Transaction("1998-06-02T13:00:00.505Z","1998-06-02T13:00:00.900Z",6,8.0F);
        records.add(rec5);


        Transaction rec6 = new Transaction("1998-07-03T13:00:00.505Z","1998-07-03T13:10:00.900Z",7,7.0F);
        records.add(rec6);

        Transaction rec7 = new Transaction("1998-08-04T13:00:00.505Z","1998-08-04T13:00:00.900Z",2,8.8F);
        records.add(rec7);

        Transaction rec8 = new Transaction("1998-09-01T13:00:00.505Z","1998-09-01T14:00:00.900Z",5,1118.0F);
        records.add(rec8);


        Transaction rec9 = new Transaction("1998-10-02T13:00:00.505Z","1998-10-02T13:00:00.900Z",2,1108.0F);
        records.add(rec9);

        Transaction rec10 = new Transaction("1998-11-03T13:00:00.505Z","1998-11-03T13:10:00.900Z",7,108.0F);
        records.add(rec10);

        Transaction rec11 = new Transaction("1998-12-04T13:00:00.505Z","1998-12-04T13:00:00.900Z",2,3.8F);
        records.add(rec11);

        //1999

        Transaction rec12 = new Transaction("1999-01-01T13:00:00.505Z","1999-01-01T14:00:00.900Z",1,3001.9F);
        records.add(rec12);

        Transaction rec13 = new Transaction("1999-02-02T13:00:00.505Z","1999-02-02T13:00:00.900Z",2,301.9F);
        records.add(rec13);

        Transaction rec14 = new Transaction("1999-03-03T13:00:00.505Z","1999-03-03T13:10:00.500Z",2,31.9F);
        records.add(rec14);


        Transaction rec15 = new Transaction("1999-04-04T13:00:00.505Z","1999-04-04T13:00:00.900Z",4,9.0F);
        records.add(rec15);

        Transaction rec16 = new Transaction("1999-05-01T13:00:00.505Z","1999-05-01T14:00:00.900Z",5,3201.9F);
        records.add(rec16);

        Transaction rec17 = new Transaction("1999-06-02T13:00:00.505Z","1999-06-02T13:00:00.900Z",2,9.0F);
        records.add(rec17);


        Transaction rec18 = new Transaction("1999-07-03T13:00:00.505Z","1999-07-03T13:10:00.900Z",7,11.9F);
        records.add(rec18);

        Transaction rec19 = new Transaction("1999-08-04T13:00:00.505Z","1999-08-04T13:00:00.510Z",2,1.9F);
        records.add(rec19);

        Transaction rec20 = new Transaction("1999-09-01T13:00:00.505Z","1999-09-01T14:00:00.900Z",5,1119.0F);
        records.add(rec20);


        Transaction rec21 = new Transaction("1999-10-02T13:00:00.505Z","1999-10-02T13:00:00.900Z",6,1109.0F);
        records.add(rec21);

        Transaction rec22 = new Transaction("1999-11-03T13:00:00.505Z","1999-11-03T13:10:00.900Z",7,191.0F);
        records.add(rec22);

        Transaction rec23 = new Transaction("1999-12-04T13:00:00.505Z","1999-12-04T13:00:00.900Z",8,9.0F);
        records.add(rec23);

        //1997

        Transaction rec24 = new Transaction("1997-01-01T13:00:00.505Z","1997-01-01T14:00:00.900Z",1,3001.7F);
        records.add(rec24);

        Transaction rec25 = new Transaction("1997-02-02T13:00:00.505Z","1997-02-02T13:00:00.900Z",5,301.7F);
        records.add(rec25);

        Transaction rec26 = new Transaction("1997-03-03T13:00:00.505Z","1997-03-03T13:10:00.900Z",3,31.7F);
        records.add(rec26);


        Transaction rec27 = new Transaction("1997-04-04T13:00:00.505Z","1997-04-04T13:00:00.900Z",4,7.0F);
        records.add(rec27);

        Transaction rec28 = new Transaction("1997-05-01T13:00:00.505Z","1997-05-01T14:00:00.900Z",5,7201.0F);
        records.add(rec28);

        Transaction rec29 = new Transaction("1997-06-02T13:00:00.505Z","1997-06-02T13:00:00.900Z",6,7.0F);
        records.add(rec29);


        Transaction rec30 = new Transaction("1997-07-03T13:00:00.505Z","1997-07-03T13:10:00.900Z",7,17.0F);
        records.add(rec30);

        Transaction rec31 = new Transaction("1997-08-04T13:00:00.505Z","1997-08-04T13:00:00.910Z",8,7.0F);
        records.add(rec31);

        Transaction rec32 = new Transaction("1997-09-01T13:00:00.505Z","1997-09-01T14:00:00.000Z",5,7111.0F);
        records.add(rec32);


        Transaction rec33 = new Transaction("1997-10-02T13:00:00.505Z","1997-10-02T13:00:00.505Z",6,1171.0F);
        records.add(rec33);

        Transaction rec34 = new Transaction("1997-11-03T13:00:00.505Z","1997-11-03T13:10:00.500Z",5,107.0F);
        records.add(rec34);

        Transaction rec35 = new Transaction("1997-12-04T13:00:00.505Z","1997-12-05T13:00:00.510Z",8,3.7F);
        records.add(rec35);

        String actualResult = transactionController.saveTransactions(records,"TestData");
        assertThat(actualResult).isEqualTo("Successfully Inserted");
    }

    @Test
    void wrongDateAndTimeFormat(){
        List<Transaction> records = new ArrayList<Transaction>();
        Transaction  rec = new Transaction("1998-01-05","1998-01-05",1,3001.0F);
        records.add(rec);
        String actualResult = transactionController.saveTransactions(records,"TestData");
        assertThat(actualResult).isEqualTo("Problem with index value : [0]");
    }

    @Test
    void wrongDateAndTime(){
        List<Transaction> records = new ArrayList<Transaction>();
        Transaction  rec = new Transaction("1998-01-01T13:00:00.505Z","1998-01-01T13:00:00.005Z",1,3001.0F);
        records.add(rec);
        String actualResult = transactionController.saveTransactions(records,"TestData");
        assertThat(actualResult).isEqualTo("Problem with index value : [0]");
    }

    @Test
    void checkOldest() throws IOException, ParseException, CsvException {
        String[] testResult = {"1997-01-01T13:00:00.505Z", "1997-01-01T14:00:00.000Z", "1", "3001.7"};
        List<String[]> actualResult =transactionController.getTransactions(null,null,true,false,null,"TestData");
        assertThat(actualResult.get(0)[0].equals(testResult[0]) && actualResult.get(0)[1].equals(testResult[1]) && actualResult.get(0)[2].equals(testResult[2]) && actualResult.get(0)[3].equals(testResult[3]));
    }

    @Test
    void checkOldestInRange() throws IOException, ParseException, CsvException {
        DatesBetween dates = new DatesBetween("1998-01-01","1999-01-05");
        String from ="1998-01-01";
        String to= "1999-01-01";
        String[] testResult = {"1998-01-01T13:00:00.505Z","1998-01-01T14:00:00.000Z", "1","3008.0"};
        List<String[]> actualResult =transactionController.getTransactions(from,to,true,false,null,"TestData");
        assertThat(actualResult.get(0)[0].equals(testResult[0]) && actualResult.get(0)[1].equals(testResult[1]) && actualResult.get(0)[2].equals(testResult[2]) && actualResult.get(0)[3].equals(testResult[3]));
    }

    @Test
    void checkNewest() throws IOException, ParseException, CsvException {
        String[] testResult = {"1999-12-04T13:00:00.505Z","1999-12-04T13:00:00.510Z","8","9.0"};
        List<String[]> actualResult =transactionController.getTransactions(null,null,false,true,null,"TestData");
        assertThat(actualResult.get(0)[0].equals(testResult[0]) && actualResult.get(0)[1].equals(testResult[1]) && actualResult.get(0)[2].equals(testResult[2]) && actualResult.get(0)[3].equals(testResult[3]));
    }

    @Test
    void checkNewestTInRange() throws IOException, ParseException, CsvException {
        String[] testResult={"1998-12-04T13:00:00.505Z","1998-12-04T13:00:00.510Z","2","3.8"};
        String from ="1998-01-01";
        String to= "1998-12-05";
        List<String[]> actualResult =transactionController.getTransactions(from,to,false,true,null,"TestData");
        assertThat(actualResult.get(0)[0].equals(testResult[0]) && actualResult.get(0)[1].equals(testResult[1]) && actualResult.get(0)[2].equals(testResult[2]) && actualResult.get(0)[3].equals(testResult[3]));
    }

    @Test
    void mostCommonProduct() throws IOException, CsvException, ParseException {
        String[][] actualResult=new String[1][2];
        actualResult[0][0]= "5";
        actualResult[0][1]= "7";
        assertThat(transactionController.allProducts(null,null,"TestData",true,false)).isEqualTo(actualResult);
    }

    @Test
    void mostCommonProductInRange() throws IOException, ParseException, CsvException {
        DatesBetween dates = new DatesBetween("1998-01-01","1999-12-05");
        String[][] testResult=new String[1][2];
        testResult[0][0]= "5";
        testResult[0][1]= "7";
        assertThat(transactionController.allProducts(null,null,"TestData",true,false)).isEqualTo(testResult);
    }

    @Test
    void lestCommonProduct() throws IOException, CsvException, ParseException {
        String[][] actualResult= new String[1][2];
        actualResult[0][0]= "3";
        actualResult[0][1]= "2";
        assertThat(transactionController.allProducts(null,null,"TestData",false,true)).isEqualTo(actualResult);
    }

    @Test
    void listCommonProductInRange() throws IOException, ParseException, CsvException {
        DatesBetween dates = new DatesBetween("1998-01-01","1999-12-05");
        String[][] testResult=new String[1][2];
        testResult[0][0]= "3";
        testResult[0][1]= "2";
        assertThat(transactionController.allProducts(null,null,"TestData",false,true)).isEqualTo(testResult);
    }

    @Test
    void transactionValueSummary() throws IOException, ParseException, CsvException {
        TransactionSummary actualResult = transactionController.getSummary(null, null, null, "TestData");
        TransactionSummary existingResult = new TransactionSummary(1024.8555F, "7.0", 32.013363F, 3302611.5F);
        assertThat(actualResult).isEqualTo(existingResult);
    }

    @Test
    void transactionTimeDeltaSummary() throws IOException, ParseException, CsvException {
        TransactionSummary actualResult = transactionController.getTimeDeltaSummary(null, null, null, "TestData");
        TransactionSummary existingResult = new TransactionSummary(5850274.0F, "1.2002665E7", 5850274.0F, 2.49092147E16F);
        assertThat(actualResult).isEqualTo(existingResult);
    }
    @AfterAll
    static void tearDown() throws IOException {
        System.out.println("tearing down");
    }
    @AfterEach
    void afterAll() {
        System.out.println("Testing ended");
    }
}