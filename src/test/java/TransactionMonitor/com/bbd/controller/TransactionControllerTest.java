package TransactionMonitor.com.bbd.controller;

import TransactionMonitor.com.bbd.model.DatesBetween;
import TransactionMonitor.com.bbd.model.Record;
import TransactionMonitor.com.bbd.service.SaveRecordService;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class TransactionControllerTest {

    @Autowired
    private TransactionController transactionController;

    @Autowired
    private SaveRecordService service;

    @BeforeAll
    static void setUp() throws IOException {
        String filepath = ".//TestData";
        File file = new File(filepath);
        if(file.exists()==true) {
            FileUtils.cleanDirectory(file);
            file.delete();
        }
        System.out.println("Starting test and setting up ");
    }

    @Test
    void saveTransaction() {
        List<Record> records = new ArrayList<Record>();
        //1998
        Record rec = new Record("1998-01-01T13:00:00.505Z","1998-01-01T14:00:00.000Z",1,3008.0F);
        records.add(rec);
        Record rec1 = new Record("1998-02-02T13:00:00.505Z","1998-02-02T13:00:00.700Z",2,308.0F);
        records.add(rec1);
        Record rec2 = new Record("1998-03-03T13:00:00.505Z","1998-03-03T13:10:00.500Z",3,38.0F);
        records.add(rec2);

        Record rec3 = new Record("1998-04-04T13:00:00.505Z","1998-04-04T13:00:00.510Z",4,7.0F);
        records.add(rec3);
        Record rec4 = new Record("1998-05-01T13:00:00.505Z","1998-05-01T14:00:00.000Z",2,3208.0F);
        records.add(rec4);
        Record rec5 = new Record("1998-06-02T13:00:00.505Z","1998-06-02T13:00:00.700Z",6,8.0F);
        records.add(rec5);

        Record rec6 = new Record("1998-07-03T13:00:00.505Z","1998-07-03T13:10:00.500Z",7,7.0F);
        records.add(rec6);
        Record rec7 = new Record("1998-08-04T13:00:00.505Z","1998-08-04T13:00:00.510Z",2,8.8F);
        records.add(rec7);
        Record rec8 = new Record("1998-09-01T13:00:00.505Z","1998-09-01T14:00:00.000Z",5,1118.0F);
        records.add(rec8);

        Record rec9 = new Record("1998-10-02T13:00:00.505Z","1998-10-02T13:00:00.700Z",2,1108.0F);
        records.add(rec9);
        Record rec10 = new Record("1998-11-03T13:00:00.505Z","1998-11-03T13:10:00.500Z",7,108.0F);
        records.add(rec10);
        Record rec11 = new Record("1998-12-04T13:00:00.505Z","1998-12-04T13:00:00.510Z",2,3.8F);
        records.add(rec11);

        //1999
        Record rec12 = new Record("1999-01-01T13:00:00.505Z","1999-01-01T14:00:00.000Z",1,3001.9F);
        records.add(rec12);
        Record rec13 = new Record("1999-02-02T13:00:00.505Z","1999-02-02T13:00:00.700Z",2,301.9F);
        records.add(rec13);
        Record rec14 = new Record("1999-03-03T13:00:00.505Z","1999-03-03T13:10:00.500Z",2,31.9F);
        records.add(rec14);

        Record rec15 = new Record("1999-04-04T13:00:00.505Z","1999-04-04T13:00:00.510Z",4,9.0F);
        records.add(rec15);
        Record rec16 = new Record("1999-05-01T13:00:00.505Z","1999-05-01T14:00:00.000Z",5,3201.9F);
        records.add(rec16);
        Record rec17 = new Record("1999-06-02T13:00:00.505Z","1999-06-02T13:00:00.700Z",2,9.0F);
        records.add(rec17);

        Record rec18 = new Record("1999-07-03T13:00:00.505Z","1999-07-03T13:10:00.500Z",7,11.9F);
        records.add(rec18);
        Record rec19 = new Record("1999-08-04T13:00:00.505Z","1999-08-04T13:00:00.510Z",2,1.9F);
        records.add(rec19);
        Record rec20 = new Record("1999-09-01T13:00:00.505Z","1999-09-01T14:00:00.000Z",5,1119.0F);
        records.add(rec20);

        Record rec21 = new Record("1999-10-02T13:00:00.505Z","1999-10-02T13:00:00.700Z",6,1109.0F);
        records.add(rec21);
        Record rec22 = new Record("1999-11-03T13:00:00.505Z","1999-11-03T13:10:00.500Z",7,191.0F);
        records.add(rec22);
        Record rec23 = new Record("1999-12-04T13:00:00.505Z","1999-12-04T13:00:00.510Z",8,9.0F);
        records.add(rec23);

        //1997
        Record rec24 = new Record("1997-01-01T13:00:00.505Z","1997-01-01T14:00:00.000Z",1,3001.7F);
        records.add(rec24);
        Record rec25 = new Record("1997-02-02T13:00:00.505Z","1997-02-02T13:00:00.700Z",5,301.7F);
        records.add(rec25);
        Record rec26 = new Record("1997-03-03T13:00:00.505Z","1997-03-03T13:10:00.500Z",3,31.7F);
        records.add(rec26);

        Record rec27 = new Record("1997-04-04T13:00:00.505Z","1997-04-04T13:00:00.510Z",4,7.0F);
        records.add(rec27);
        Record rec28 = new Record("1997-05-01T13:00:00.505Z","1997-05-01T14:00:00.000Z",5,7201.0F);
        records.add(rec28);
        Record rec29 = new Record("1997-06-02T13:00:00.505Z","1997-06-02T13:00:00.700Z",6,7.0F);
        records.add(rec29);

        Record rec30 = new Record("1997-07-03T13:00:00.505Z","1997-07-03T13:10:00.500Z",7,17.0F);
        records.add(rec30);
        Record rec31 = new Record("1997-08-04T13:00:00.505Z","1997-08-04T13:00:00.510Z",8,7.0F);
        records.add(rec31);
        Record rec32 = new Record("1997-09-01T13:00:00.505Z","1997-09-01T14:00:00.000Z",5,7111.0F);
        records.add(rec32);

        Record rec33 = new Record("1997-10-02T13:00:00.505Z","1997-10-02T13:00:00.700Z",6,1171.0F);
        records.add(rec33);
        Record rec34 = new Record("1997-11-03T13:00:00.505Z","1997-11-03T13:10:00.500Z",5,107.0F);
        records.add(rec34);
        Record rec35 = new Record("1997-12-04T13:00:00.505Z","1997-12-04T13:00:00.510Z",8,3.7F);
        records.add(rec35);

        String actualResult = service.saveTransaction(records,"TestData");
        assertThat(actualResult).isEqualTo("Successfully Inserted");
    }

    @Test
    void checkTransactions() throws IOException, CsvException {
        long count = TransactionController.allTransactionInPresent("TestData").size();
        assertThat(count).isGreaterThanOrEqualTo(30);
    }

    @Test
    void wrongDateAndTimeFormat(){
        List<Record> records = new ArrayList<Record>();
        Record rec = new Record("1998-01-05","1998-01-05",1,3001.0F);
        records.add(rec);
        String actualResult = service.saveTransaction(records,"TestData");
        assertThat(actualResult).isEqualTo("Problem with index value : [0]");
    }

    @Test
    void wrongDateAndTime(){
        List<Record> records = new ArrayList<Record>();
        Record rec = new Record("1998-01-01T13:00:00.505Z","1998-01-01T13:00:00.005Z",1,3001.0F);
        records.add(rec);
        String actualResult = transactionController.saveTransaction(records);
        assertThat(actualResult).isEqualTo("Problem with index value : [0]");
    }

    @Test
    void checkSaveDataController(){
        List<Record> records = new ArrayList<Record>();
        Record rec = new Record("1998-01-01T13:00:00.505Z","1998-01-01T13:00:00.605Z",1,3001.0F);
        records.add(rec);
        Record rec1 = new Record("1998-01-01T13:10:00.505Z","1998-01-01T13:20:00.605Z",1,3001.0F);
        records.add(rec1);
        String actualResult = transactionController.saveTransaction(records);
        assertThat(actualResult).isEqualTo("Successfully Inserted");
    }

    @Test
    void checkOldest() throws IOException, ParseException, CsvException {
        String[] actualResult = {"1997-01-01T13:00:00.505Z", "1997-01-01T14:00:00.000Z", "1", "3001.7"};
        assertThat(transactionController.oldestTransaction("TestData")).isEqualTo(actualResult);
    }

    @Test
    void checkOldestInRange() throws IOException, ParseException, CsvException {
        DatesBetween dates = new DatesBetween("1998-01-01","1999-01-05");
        String[] actualResult = {"1998-01-01T13:00:00.505Z","1998-01-01T14:00:00.000Z", "1","3008.0"};
        assertThat(transactionController.oldTransactionInBetween(dates,"TestData")).isEqualTo(actualResult);
    }

    @Test
    void checkNewest() throws IOException, ParseException, CsvException {
        String[] actualResult = {"1999-12-04T13:00:00.505Z","1999-12-04T13:00:00.510Z","8","9.0"};
        assertThat(transactionController.newerTransaction("TestData")).isEqualTo(actualResult);
    }

    @Test
    void checkNewestTInRange() throws IOException, ParseException, CsvException {
        String[] actualResult={"1998-12-04T13:00:00.505Z","1998-12-04T13:00:00.510Z","2","3.8"};
        DatesBetween dates = new DatesBetween("1998-01-01","1998-12-05");
        assertThat(transactionController.newTransactionInBetween(dates,"TestData")).isEqualTo(actualResult);
    }

    @Test
    void meanOfAllTransaction() throws IOException, CsvException {
        float actualResult=1024.8555f;
        assertThat(transactionController.meanOfTransaction("TestData")).isEqualTo(actualResult);
    }

    @Test
    void meanInRange() throws IOException, ParseException, CsvException {
        DatesBetween dates = new DatesBetween("1998-01-01","1998-12-05");
        float actualResult=744.2166F;
        assertThat(transactionController.meanInRange(dates,"TestData")).isEqualTo(actualResult);
    }

    @Test
    void modeOfAllTransaction() throws IOException, CsvException {
        String actualResult="Mode (Amount) = 7.0 with count=5";
        assertThat(transactionController.modeOfTransaction("TestData")).isEqualTo(actualResult);
    }

    @Test
    void modeInRange() throws IOException, ParseException, CsvException {
        DatesBetween dates = new DatesBetween("1998-01-01","1998-12-05");
        String actualResult="Mode (Amount) = 7.0 with count=2";
        assertThat(transactionController.modeInRange(dates,"TestData")).isEqualTo(actualResult);
    }

    @Test
    void standersDeviation() throws IOException, CsvException {
        float actualResult=32.013363908686635F;
        assertThat(transactionController.sDOfTransaction("TestData")).isEqualTo(actualResult);
    }

    @Test
    void standardDeviationInRange() throws IOException, ParseException, CsvException {
        DatesBetween dates = new DatesBetween("1998-01-01","1999-12-05");
        float actualResult=27.331303F;
        assertThat(transactionController.sDInRange(dates,"TestData")).isEqualTo(actualResult);
    }

    @Test
    void varianceOfAllTransaction() throws IOException, CsvException {
        float actualResult=3302611.5F;
        assertThat(transactionController.varianceOfTransaction("TestData")).isEqualTo(actualResult);
    }

    @Test
    void varianceInRange() throws IOException, ParseException, CsvException {
        DatesBetween dates = new DatesBetween("1998-01-01","1999-12-05");
        float actualResult=1344185.4F;
        assertThat(transactionController.varianceInBetween(dates,"TestData")).isEqualTo(actualResult);
    }

    @Test
    void mostCommonProduct() throws IOException, CsvException {
        String actualResult="2";
        assertThat(transactionController.mostCommonProduct("TestData")).isEqualTo(actualResult);
    }

    @Test
    void mostCommonProductInRange() throws IOException, ParseException, CsvException {
        DatesBetween dates = new DatesBetween("1998-01-01","1999-12-05");
        String actualResult="2";
        assertThat(transactionController.mostCPInBetween(dates,"TestData")).isEqualTo(actualResult);
    }

    @Test
    void lestCommonProduct() throws IOException, CsvException {
        String actualResult="8";
        assertThat(transactionController.leastCommonProduct("TestData")).isEqualTo(actualResult);
    }

    @Test
    void listCommonProductInRange() throws IOException, ParseException, CsvException {
        DatesBetween dates = new DatesBetween("1998-01-01","1999-12-05");
        String actualResult="2";
        assertThat(transactionController.mostCPInBetween(dates,"TestData")).isEqualTo(actualResult);
    }

    @Test
    void timeDelta() throws IOException, ParseException, CsvException {
        String actualResult="Mean=1049922 Mode=0 Standard Deviation=1024.6570157862582";
        assertThat(transactionController.timeDelta("TestData"));
    }

    @Test
    void timeDeltaInBetween() throws IOException, ParseException, CsvException {
        DatesBetween dates = new DatesBetween("1998-01-01","1999-02-05");
        String actualResult="Mean=1157054 Mode=0 Standard Deviation=1075.66444581942";
        assertThat(transactionController.timeDeltaInRange(dates,"TestData"));
    }


    @Test
    void timeDeltaOfAllTransactionByProduct() throws IOException, ParseException, CsvException {
        String actualResult="Mean=599995 mode=0 Standard Deviation=774.5934417486376";
        assertThat(transactionController.timeDeltaWithProductId("7","TestData")).isEqualTo(actualResult);
    }

    @Test
    void TimeDeltaInBetweenByProduct() throws IOException, ParseException, CsvException {
        DatesBetween dates = new DatesBetween("1998-01-01","1999-02-05");
        String actualResult="Mean=600015 mode=0 Standard Deviation=774.6063516393343";
        assertThat(transactionController.timeDeltaInRangeWithProductId(dates,"2","TestData")).isEqualTo(actualResult);
    }


    @AfterAll
    static void tearDown() throws IOException {
        System.out.println("tearing down");
    }

}