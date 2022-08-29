package TransactionMonitor.com.bbd.controller.rpc_apis;

import TransactionMonitor.com.bbd.model.Product;
import TransactionMonitor.com.bbd.model.Transaction;
import TransactionMonitor.com.bbd.model.TransactionSummary;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest
public class RpcTransactionControllerTest {

    @Autowired
    private RpcTransactionController transactionController;


    @BeforeAll
    static void BeforeAll() {
        log.info("Starting Test and Setting Up In Rpc");
    }
    @BeforeEach
    void beforeAll() {
        log.info("Test Started");
    }

    @Test
    void checkOldest() throws IOException, ParseException, CsvException {
        Transaction testResult =new Transaction("1997-01-01T13:00:00.505Z", "1997-01-01T14:00:00.000Z", "1", new BigDecimal("3001.7"));
        List<Transaction> actualResult =transactionController.oldestTransaction(null,null,null,"TestData");
        assertThat(actualResult.get(0).getInit_date().equals(testResult.getInit_date()) && actualResult.get(0).getConclusion_date().equals(testResult.getConclusion_date()) && actualResult.get(0).getProduct_id().equals(testResult.getProduct_id()) && actualResult.get(0).getValue().equals(testResult.getValue()));
    }

    @Test
    void checkOldestInRange() throws IOException, ParseException, CsvException {
        String from ="1998-01-01";
        String to= "1999-01-01";
        Transaction testResult = new Transaction("1998-01-01T13:00:00.505Z","1998-01-01T14:00:00.000Z", "1",new BigDecimal("3008.0"));
        List<Transaction> actualResult =transactionController.oldestTransaction(from,to,null,"TestData");
        assertThat(actualResult.get(0).getInit_date().equals(testResult.getInit_date()) && actualResult.get(0).getConclusion_date().equals(testResult.getConclusion_date()) && actualResult.get(0).getProduct_id().equals(testResult.getProduct_id()) && actualResult.get(0).getValue().equals(testResult.getValue()));
    }

    @Test
    void checkNewest() throws IOException, ParseException, CsvException {
        Transaction testResult = new Transaction("1999-12-04T13:00:00.505Z","1999-12-04T13:00:00.510Z","8",new BigDecimal("9.0"));
        List<Transaction> actualResult =transactionController.newestTransaction(null,null,null,"TestData");
        assertThat(actualResult.get(0).getInit_date().equals(testResult.getInit_date()) && actualResult.get(0).getConclusion_date().equals(testResult.getConclusion_date()) && actualResult.get(0).getProduct_id().equals(testResult.getProduct_id()) && actualResult.get(0).getValue().equals(testResult.getValue()));
    }
    @Test
    void checkNewestTInRange() throws IOException, ParseException, CsvException {
        Transaction testResult=new Transaction("1998-12-04T13:00:00.505Z","1998-12-04T13:00:00.510Z","2",new BigDecimal("3.8"));
        String from ="1998-01-01";
        String to= "1998-12-05";
        List<Transaction> actualResult =transactionController.newestTransaction(from,to,null,"TestData");
        assertThat(actualResult.get(0).getInit_date().equals(testResult.getInit_date()) && actualResult.get(0).getConclusion_date().equals(testResult.getConclusion_date()) && actualResult.get(0).getProduct_id().equals(testResult.getProduct_id()) && actualResult.get(0).getValue().equals(testResult.getValue()));
    }
    @Test
    void checkMean() throws ParseException, IOException, CsvException {
        float actualResult=transactionController.meanTransaction(null,null,null,"TestData");
        float expectedResult=1024.8553F;
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
        float expectedResult=32.013363f;
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void checkVariance() throws ParseException, IOException, CsvException {
        float actualResult=transactionController.varianceTransaction(null,null,null,"TestData");
        float expectedResult=3302613.2F;
        assertThat(actualResult).isEqualTo(expectedResult);
    }
    @Test
    void mostCommonProduct() throws IOException, CsvException, ParseException {
        List<Product> actualResult= new ArrayList<>();
        actualResult.add(new Product("5"));
        assertThat(transactionController.mostCommonTransaction(null,null,"TestData")).isEqualTo(actualResult);
    }
    @Test
    void mostCommonProductInRange() throws IOException, ParseException, CsvException {
        String from_date="1998-01-01";
        String to_date="1999-12-05";
        List<Product> testResult= new ArrayList<>();
        testResult.add(new Product("2"));
        assertThat(transactionController.mostCommonTransaction(from_date,to_date,"TestData")).isEqualTo(testResult);
    }

    @Test
    void lestCommonProduct() throws IOException, CsvException, ParseException {
        List<Product> actualResult= new ArrayList<>();
        actualResult.add(new Product("3"));
        assertThat(transactionController.lestCommonTransaction(null,null,"TestData")).isEqualTo(actualResult);
    }
    @Test
    void listCommonProductInRange() throws IOException, ParseException, CsvException {
        String from_date="1998-01-01";
        String to_date="1999-12-05";
        List<Product> actualResult= new ArrayList<>();
        actualResult.add(new Product("3"));
        assertThat(transactionController.lestCommonTransaction(from_date,to_date,"TestData")).isEqualTo(actualResult);
    }

    @Test
    void transactionTimeDeltaSummary() throws IOException, ParseException, CsvException {
        TransactionSummary actualResult = transactionController.getTimeDeltaSummary(null, null, null, "TestData");
        TransactionSummary existingResult = new TransactionSummary(5850.0F, "15600.0", 5850.0F, 1.01321343E11F);
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
