package TransactionMonitor.com.bbd.controller;

import TransactionMonitor.com.bbd.model.Product;
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
public class RpcTransactionControllerTest {

    @Autowired
    private RpcTransactionController transactionController;

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
    void wrongDateAndTimeFormat(){
        List<Transaction> records = new ArrayList<Transaction>();
        Transaction  rec = new Transaction("1998-01-05","1998-01-05",1,3001.0F);
        records.add(rec);
        String actualResult = transactionController.createTransactions(records,"TestData");
        assertThat(actualResult).isEqualTo("Problem with index value : [0]");
    }

    @Test
    void wrongDateAndTime(){
        List<Transaction> records = new ArrayList<Transaction>();
        Transaction  rec = new Transaction("1998-01-01T13:00:00.505Z","1998-01-01T13:00:00.005Z",1,3001.0F);
        records.add(rec);
        String actualResult = transactionController.createTransactions(records,"TestData");
        assertThat(actualResult).isEqualTo("Problem with index value : [0]");
    }

    @Test
    void checkOldest() throws IOException, ParseException, CsvException {
        Transaction testResult =new Transaction("1997-01-01T13:00:00.505Z", "1997-01-01T14:00:00.000Z", "1", "3001.7");
        List<Transaction> actualResult =transactionController.oldestTransaction(null,null,null,"TestData");
        assertThat(actualResult.get(0).getInit_date().equals(testResult.getInit_date()) && actualResult.get(0).getConclusion_date().equals(testResult.getConclusion_date()) && actualResult.get(0).getProduct_id().equals(testResult.getProduct_id()) && actualResult.get(0).getValue().equals(testResult.getValue()));
    }

    @Test
    void checkOldestInRange() throws IOException, ParseException, CsvException {
        String from ="1998-01-01";
        String to= "1999-01-01";
        Transaction testResult = new Transaction("1998-01-01T13:00:00.505Z","1998-01-01T14:00:00.000Z", "1","3008.0");
        List<Transaction> actualResult =transactionController.oldestTransaction(from,to,null,"TestData");
        assertThat(actualResult.get(0).getInit_date().equals(testResult.getInit_date()) && actualResult.get(0).getConclusion_date().equals(testResult.getConclusion_date()) && actualResult.get(0).getProduct_id().equals(testResult.getProduct_id()) && actualResult.get(0).getValue().equals(testResult.getValue()));
    }

    @Test
    void checkNewest() throws IOException, ParseException, CsvException {
        Transaction testResult = new Transaction("1999-12-04T13:00:00.505Z","1999-12-04T13:00:00.510Z","8","9.0");
        List<Transaction> actualResult =transactionController.newestTransaction(null,null,null,"TestData");
        assertThat(actualResult.get(0).getInit_date().equals(testResult.getInit_date()) && actualResult.get(0).getConclusion_date().equals(testResult.getConclusion_date()) && actualResult.get(0).getProduct_id().equals(testResult.getProduct_id()) && actualResult.get(0).getValue().equals(testResult.getValue()));
    }
    @Test
    void checkNewestTInRange() throws IOException, ParseException, CsvException {
        Transaction testResult=new Transaction("1998-12-04T13:00:00.505Z","1998-12-04T13:00:00.510Z","2","3.8");
        String from ="1998-01-01";
        String to= "1998-12-05";
        List<Transaction> actualResult =transactionController.newestTransaction(from,to,null,"TestData");
        assertThat(actualResult.get(0).getInit_date().equals(testResult.getInit_date()) && actualResult.get(0).getConclusion_date().equals(testResult.getConclusion_date()) && actualResult.get(0).getProduct_id().equals(testResult.getProduct_id()) && actualResult.get(0).getValue().equals(testResult.getValue()));
    }
    @Test
    void checkMean() throws ParseException, IOException, CsvException {
        float actualResult=transactionController.meanTransaction(null,null,null,"TestData");
        float expectedResult=1024.8555F;
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void checkMode() throws ParseException, IOException, CsvException {
        String actualResult=transactionController.modeTransaction(null,null,null,"TestData");
        String expectedResult="7.0";
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void checkStandardDeviation() throws ParseException, IOException, CsvException {
        float actualResult=transactionController.standardDeviationTransaction(null,null,null,"TestData");
        float expectedResult=32.013363F;
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void checkVariance() throws ParseException, IOException, CsvException {
        float actualResult=transactionController.varianceTransaction(null,null,null,"TestData");
        float expectedResult=3302611.5F;
        assertThat(actualResult).isEqualTo(expectedResult);
    }
    @Test
    void mostCommonProduct() throws IOException, CsvException, ParseException {
        List<Product> actualResult= new ArrayList<Product>();
        actualResult.add(new Product("5"));
        assertThat(transactionController.mostCommonTransaction(null,null,"TestData")).isEqualTo(actualResult);
    }
    @Test
    void mostCommonProductInRange() throws IOException, ParseException, CsvException {
        String from_date="1998-01-01";
        String to_date="1999-12-05";
        List<Product> testResult= new ArrayList<Product>();
        testResult.add(new Product("2"));
        assertThat(transactionController.mostCommonTransaction(from_date,to_date,"TestData")).isEqualTo(testResult);
    }
    
    @Test
    void lestCommonProduct() throws IOException, CsvException, ParseException {
        List<Product> actualResult= new ArrayList<Product>();
        actualResult.add(new Product("3"));
        assertThat(transactionController.lestCommonTransaction(null,null,"TestData")).isEqualTo(actualResult);
    }
    @Test
    void listCommonProductInRange() throws IOException, ParseException, CsvException {
        String from_date="1998-01-01";
        String to_date="1999-12-05";
        List<Product> actualResult= new ArrayList<Product>();
        actualResult.add(new Product("3"));
        assertThat(transactionController.lestCommonTransaction(null,null,"TestData")).isEqualTo(actualResult);
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

