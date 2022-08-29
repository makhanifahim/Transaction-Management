package TransactionMonitor.com.bbd.controller.rest_apis;


import TransactionMonitor.com.bbd.config.Logges;
import TransactionMonitor.com.bbd.model.Product;
import TransactionMonitor.com.bbd.model.Transaction;
import TransactionMonitor.com.bbd.model.TransactionSummary;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest
class RestTransactionControllerTest {

    @Autowired
    private RestTransactionController transactionController;

    @Autowired
    private static Logges logges;

    @BeforeAll
    static void BeforeAll() {
        log.info("Starting Test and Setting Up In Rest");
    }
    @BeforeEach
    void beforeAll() {
        log.info("Test Started");
    }

    @Test
    void saveTransaction() throws IOException {
        String filepath = ".//TestData";
        File file = new File(filepath);
        if(file.exists())
            FileUtils.cleanDirectory(file);

        List<Transaction> records = new ArrayList<>();
        //1998
        Transaction  rec = new Transaction("1998-01-01T13:00:00.505Z","1998-01-01T14:00:00.000Z","1", new BigDecimal("3008.0"));
        records.add(rec);

        Transaction rec1 = new Transaction("1998-02-02T13:00:00.505Z","1998-02-03T13:00:00.700Z","2",new BigDecimal("308.0"));
        records.add(rec1);

        Transaction rec2 = new Transaction("1998-03-03T13:00:00.505Z","1998-03-03T13:10:00.500Z","3",new BigDecimal("38.0"));
        records.add(rec2);


        Transaction rec3 = new Transaction("1998-04-04T13:00:00.505Z","1998-04-04T13:00:00.900Z","4",new BigDecimal("7.0"));
        records.add(rec3);

        Transaction rec4 = new Transaction("1998-05-01T13:00:00.505Z","1998-05-01T14:00:00.900Z","2",new BigDecimal("3208.0"));
        records.add(rec4);

        Transaction rec5 = new Transaction("1998-06-02T13:00:00.505Z","1998-06-02T13:00:00.900Z","6",new BigDecimal("8.0"));
        records.add(rec5);


        Transaction rec6 = new Transaction("1998-07-03T13:00:00.505Z","1998-07-03T13:10:00.900Z","7",new BigDecimal("7.0"));
        records.add(rec6);

        Transaction rec7 = new Transaction("1998-08-04T13:00:00.505Z","1998-08-04T13:00:00.900Z","2",new BigDecimal("8.8"));
        records.add(rec7);

        Transaction rec8 = new Transaction("1998-09-01T13:00:00.505Z","1998-09-01T14:00:00.900Z","5",new BigDecimal("1118.0"));
        records.add(rec8);


        Transaction rec9 = new Transaction("1998-10-02T13:00:00.505Z","1998-10-02T13:00:00.900Z","2",new BigDecimal("1108.0"));
        records.add(rec9);

        Transaction rec10 = new Transaction("1998-11-03T13:00:00.505Z","1998-11-03T13:10:00.900Z","7",new BigDecimal("108.0"));
        records.add(rec10);

        Transaction rec11 = new Transaction("1998-12-04T13:00:00.505Z","1998-12-04T13:00:00.900Z","2",new BigDecimal("3.8"));
        records.add(rec11);

        //1999

        Transaction rec12 = new Transaction("1999-01-01T13:00:00.505Z","1999-01-01T14:00:00.900Z","1",new BigDecimal("3001.9"));
        records.add(rec12);

        Transaction rec13 = new Transaction("1999-02-02T13:00:00.505Z","1999-02-02T13:00:00.900Z", "2", new BigDecimal("301.9"));
        records.add(rec13);

        Transaction rec14 = new Transaction("1999-03-03T13:00:00.505Z","1999-03-03T13:10:00.500Z","2",new BigDecimal("31.9"));
        records.add(rec14);


        Transaction rec15 = new Transaction("1999-04-04T13:00:00.505Z","1999-04-04T13:00:00.900Z","4",new BigDecimal("9.0"));
        records.add(rec15);

        Transaction rec16 = new Transaction("1999-05-01T13:00:00.505Z","1999-05-01T14:00:00.900Z","5",new BigDecimal("3201.9"));
        records.add(rec16);

        Transaction rec17 = new Transaction("1999-06-02T13:00:00.505Z","1999-06-02T13:00:00.900Z","2",new BigDecimal("9.0"));
        records.add(rec17);


        Transaction rec18 = new Transaction("1999-07-03T13:00:00.505Z","1999-07-03T13:10:00.900Z","7",new BigDecimal("11.9"));
        records.add(rec18);

        Transaction rec19 = new Transaction("1999-08-04T13:00:00.505Z","1999-08-04T13:00:00.510Z","2",new BigDecimal("1.9"));
        records.add(rec19);

        Transaction rec20 = new Transaction("1999-09-01T13:00:00.505Z","1999-09-01T14:00:00.900Z","5",new BigDecimal("1119.0"));
        records.add(rec20);


        Transaction rec21 = new Transaction("1999-10-02T13:00:00.505Z","1999-10-02T13:00:00.900Z","6",new BigDecimal("1109.0"));
        records.add(rec21);

        Transaction rec22 = new Transaction("1999-11-03T13:00:00.505Z","1999-11-03T13:10:00.900Z","7",new BigDecimal("191.0"));
        records.add(rec22);

        Transaction rec23 = new Transaction("1999-12-04T13:00:00.505Z","1999-12-04T13:00:00.900Z","8",new BigDecimal("9.0"));
        records.add(rec23);

        //1997

        Transaction rec24 = new Transaction("1997-01-01T13:00:00.505Z","1997-01-01T14:00:00.900Z","1",new BigDecimal("3001.7"));
        records.add(rec24);

        Transaction rec25 = new Transaction("1997-02-02T13:00:00.505Z","1997-02-02T13:00:00.900Z","5",new BigDecimal("301.7"));
        records.add(rec25);

        Transaction rec26 = new Transaction("1997-03-03T13:00:00.505Z","1997-03-03T13:10:00.900Z","3",new BigDecimal("31.7"));
        records.add(rec26);


        Transaction rec27 = new Transaction("1997-04-04T13:00:00.505Z","1997-04-04T13:00:00.900Z","4",new BigDecimal("7.0"));
        records.add(rec27);

        Transaction rec28 = new Transaction("1997-05-01T13:00:00.505Z","1997-05-01T14:00:00.900Z","5",new BigDecimal("7201.0"));
        records.add(rec28);

        Transaction rec29 = new Transaction("1997-06-02T13:00:00.505Z","1997-06-02T13:00:00.900Z","6",new BigDecimal("7.0"));
        records.add(rec29);


        Transaction rec30 = new Transaction("1997-07-03T13:00:00.505Z","1997-07-03T13:10:00.900Z","7",new BigDecimal("17.0"));
        records.add(rec30);

        Transaction rec31 = new Transaction("1997-08-04T13:00:00.505Z","1997-08-04T13:00:00.910Z","8",new BigDecimal("7.0"));
        records.add(rec31);

        Transaction rec32 = new Transaction("1997-09-01T13:00:00.505Z","1997-09-01T14:00:00.000Z","5",new BigDecimal("7111.0"));
        records.add(rec32);


        Transaction rec33 = new Transaction("1997-10-02T13:00:00.505Z","1997-10-02T13:00:00.505Z","6",new BigDecimal("1171.0"));
        records.add(rec33);

        Transaction rec34 = new Transaction("1997-11-03T13:00:00.505Z","1997-11-03T13:10:00.500Z","5",new BigDecimal("107.0"));
        records.add(rec34);

        Transaction rec35 = new Transaction("1997-12-04T13:00:00.505Z","1997-12-05T13:00:00.510Z","8",new BigDecimal("3.7"));
        records.add(rec35);

        ResponseEntity<String> actualResult = transactionController.saveTransactions(records,"TestData");
        assertThat(actualResult).isEqualTo("Successfully Inserted");
    }

    @Test
    void checkOldest() throws IOException, ParseException, CsvException {
        Transaction testResult =new Transaction("1997-01-01T13:00:00.505Z", "1997-01-01T14:00:00.000Z", "1", new BigDecimal("3001.7"));
        List<Transaction> actualResult =transactionController.getTransactions(null,null,true,false,null,"TestData");
        assertThat(actualResult.get(0).getInit_date().equals(testResult.getInit_date()) && actualResult.get(0).getConclusion_date().equals(testResult.getConclusion_date()) && actualResult.get(0).getProduct_id().equals(testResult.getProduct_id()) && actualResult.get(0).getValue().equals(testResult.getValue()));
    }

    @Test
    void checkOldestInRange() throws IOException, ParseException, CsvException {
        String from ="1998-01-01";
        String to= "1999-01-01";
        Transaction testResult = new Transaction("1998-01-01T13:00:00.505Z","1998-01-01T14:00:00.000Z", "1",new BigDecimal("3008.0"));
        List<Transaction> actualResult =transactionController.getTransactions(from,to,true,false,null,"TestData");
        assertThat(actualResult.get(0).getInit_date().equals(testResult.getInit_date()) && actualResult.get(0).getConclusion_date().equals(testResult.getConclusion_date()) && actualResult.get(0).getProduct_id().equals(testResult.getProduct_id()) && actualResult.get(0).getValue().equals(testResult.getValue()));
    }

    @Test
    void checkNewest() throws IOException, ParseException, CsvException {
        Transaction testResult = new Transaction("1999-12-04T13:00:00.505Z","1999-12-04T13:00:00.510Z","8",new BigDecimal("9.0"));
        List<Transaction> actualResult =transactionController.getTransactions(null,null,false,true,null,"TestData");
        assertThat(actualResult.get(0).getInit_date().equals(testResult.getInit_date()) && actualResult.get(0).getConclusion_date().equals(testResult.getConclusion_date()) && actualResult.get(0).getProduct_id().equals(testResult.getProduct_id()) && actualResult.get(0).getValue().equals(testResult.getValue()));
    }

    @Test
    void checkNewestTInRange() throws IOException, ParseException, CsvException {
        Transaction testResult=new Transaction("1998-12-04T13:00:00.505Z","1998-12-04T13:00:00.510Z","2",new BigDecimal("3.8"));
        String from ="1998-01-01";
        String to= "1998-12-05";
        List<Transaction> actualResult =transactionController.getTransactions(from,to,false,true,null,"TestData");
        assertThat(actualResult.get(0).getInit_date().equals(testResult.getInit_date()) && actualResult.get(0).getConclusion_date().equals(testResult.getConclusion_date()) && actualResult.get(0).getProduct_id().equals(testResult.getProduct_id()) && actualResult.get(0).getValue().equals(testResult.getValue()));
    }

    @Test
    void mostCommonProduct() throws IOException, CsvException, ParseException {
        List<Product> actualResult= new ArrayList<>();
        actualResult.add(new Product("5"));
        assertThat(transactionController.allPro(null,null,"TestData",true,false)).isEqualTo(actualResult);
    }

    @Test
    void mostCommonProductInRange() throws IOException, ParseException, CsvException {
        String from_date="1998-01-01";
        String to_date="1999-12-05";
        List<Product> testResult= new ArrayList<>();
        testResult.add(new Product("2"));
        assertThat(transactionController.allPro(from_date,to_date,"TestData",true,false)).isEqualTo(testResult);
    }

    @Test
    void lestCommonProduct() throws IOException, CsvException, ParseException {
        List<Product> actualResult= new ArrayList<>();
        actualResult.add(new Product("3"));
        assertThat(transactionController.allPro(null,null,"TestData",false,true)).isEqualTo(actualResult);
    }

    @Test
    void listCommonProductInRange() throws IOException, ParseException, CsvException {
        String from_date="1998-01-01";
        String to_date="1999-12-05";
        List<Product> actualResult= new ArrayList<>();
        actualResult.add(new Product("3"));
        assertThat(transactionController.allPro(from_date,to_date,"TestData",false,true)).isEqualTo(actualResult);
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
        TransactionSummary existingResult = new TransactionSummary(5850.0F, "7800.0", 5850.0F, 2.49077166E10F);
        assertThat(actualResult).isEqualTo(existingResult);
    }
    @AfterAll
    static void tearDown(){
        log.info("Ending Test and Setting Down");
    }
    @AfterEach
    void afterAll() {
        log.info("Test ended");
    }
}