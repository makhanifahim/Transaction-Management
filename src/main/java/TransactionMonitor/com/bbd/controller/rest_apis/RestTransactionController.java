package TransactionMonitor.com.bbd.controller.rest_apis;
import TransactionMonitor.com.bbd.config.Logges;
import TransactionMonitor.com.bbd.model.Product;
import TransactionMonitor.com.bbd.model.Transaction;
import TransactionMonitor.com.bbd.model.TransactionSummary;
import TransactionMonitor.com.bbd.service.ProductService;
import TransactionMonitor.com.bbd.service.TransactionService;
import TransactionMonitor.com.bbd.service.TransactionTimeDeltaSummaryService;
import TransactionMonitor.com.bbd.service.TransactionValueSummaryService;
import com.opencsv.exceptions.CsvException;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/rest_api")
public class RestTransactionController {

    @Autowired
    private TransactionService service;
    @Autowired
    private TransactionValueSummaryService valueSummaryService;
    @Autowired
    private TransactionTimeDeltaSummaryService timeDeltaSummaryService;
    @Autowired
    private ProductService productService;

    private final Logges logges = new Logges();
    String info="INFO";

    //TODO 1 Rest Post /transaction for inserting list of transaction
    @Timed(value = "REST_transaction_post.time", description = "Time taken for rest api POST rest_api/transaction ")
    @PostMapping("/transactions")
    public ResponseEntity<String> saveTransactions(@RequestBody List<Transaction> transactions, @PathVariable (required=false) String typeOfData) {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        logges.addInfoLog("POST in Transaction is been fired",info);
        if(Objects.equals(service.saveTransaction(transactions, typeOfData), "Successfully Inserted"))
            return ResponseEntity.status(HttpStatus.CREATED).body(service.saveTransaction(transactions,typeOfData));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(service.saveTransaction(transactions,typeOfData));
    }

    //TODO 2 Get Transactions
    @Timed(value = "REST_Transaction_get.time", description = "Time taken for rest api GET rest_api/transaction ")
    @GetMapping("/transactions")
    public List<Transaction> getTransactions(@RequestParam (required = false) String from_date,@RequestParam (required = false) String to_date,@RequestParam (required = false) boolean oldest,@RequestParam (required = false) boolean newest,@RequestParam (required = false) String product_id,@RequestParam (required=false) String typeOfData) throws IOException, ParseException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        String p_id=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        if(product_id!=null){p_id=product_id;}
        if(oldest && newest){
            List<Transaction> Transact= new ArrayList();
            Transact.add(service.oldestTransaction(typeOfData,p_id,from,to));
            Transact.add(service.newerTransaction(typeOfData,p_id,from,to));
            logges.addInfoLog("GET in Transaction is been fired with oldest && newest = true",info);
            return Transact;
        }
        else if(oldest){
            List<Transaction> oldestTransact= new ArrayList();
            oldestTransact.add(service.oldestTransaction(typeOfData,p_id,from,to));
            logges.addInfoLog("GET in Transaction is been fired with oldest=true",info);
            return oldestTransact;
        }
        else if(newest) {
            List<Transaction> newestTransact = new ArrayList();
            newestTransact.add(service.newerTransaction(typeOfData,p_id,from,to));
            logges.addInfoLog("GET in Transaction is been fired with newest=true",info);
            return newestTransact;
        }
        else{
            logges.addInfoLog("GET in Transaction is been fired for all transaction",info);
            return service.allTransaction(typeOfData, p_id,from, to);
        }
    }

    //TODO 3 Get Transactions_value_summary
    @Timed(value = "REST_transaction_value_summary.time", description = "Time taken for rest api GET rest_api/transaction_value_summary")
    @GetMapping("/transaction_value_summary")
    public TransactionSummary getSummary(@RequestParam (required = false) String from_date, @RequestParam (required = false) String to_date, @RequestParam (required = false) String product_id, @RequestParam (required=false) String typeOfData) throws IOException, ParseException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from = null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        logges.addInfoLog("GET in Transaction Value Summary is been fired with from="+from+" and to="+to+" with product id="+product_id,info);
        return new TransactionSummary(valueSummaryService.meanOfTransaction(from,to,product_id,typeOfData),valueSummaryService.modeOfTransaction(from,to,product_id,typeOfData),valueSummaryService.standardDeviationOfTransaction(from,to,product_id,typeOfData),valueSummaryService.varianceOfTransaction(from,to,product_id,typeOfData));
    }

    //TODO 4 GET Products List
    @Timed(value = "REST_Products.time", description = "Time taken for rest api GET rest_api/products")
    @GetMapping("/products")
    public List<Product> allPro(@RequestParam (required = false) String from_date, @RequestParam (required = false) String to_date, @RequestParam (required=false) String typeOfData, @RequestParam (required = false) boolean most_common, @RequestParam (required = false) boolean lest_common) throws IOException, CsvException, ParseException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from = null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        if(most_common && lest_common){
            logges.addInfoLog("GET in Products for Most common product and list common product with from=\"+from+\" and to=\"+to+\" with product id=\"+product_id,info",info);
            return productService.CommonProduct(typeOfData,from,to,true,true);
        }
        else if(most_common) {
            logges.addInfoLog("GET in Products for Most common product with from=\"+from+\" and to=\"+to+\" with product id=\"+product_id,info",info);
            return productService.CommonProduct(typeOfData, from, to, true, false);
        }else if(lest_common) {
            logges.addInfoLog("GET in Products for list common product with from=\"+from+\" and to=\"+to+\" with product id=\"+product_id,info",info);
            return productService.CommonProduct(typeOfData, from, to, false, true);
        }else {
            logges.addInfoLog("GET in Products list Common products with from=\"+from+\" and to=\"+to+\" with product id=\"+product_id,info",info);
            return productService.listCommonProduct(typeOfData, from, to);
        }
    }

    //TODO 5 GET Transactions_time_delta_summary
    @Timed(value = "REST_transaction_time_delta.time", description = "Time taken for rest api GET rest_api/transaction _time_delta_summary")
    @GetMapping("/transaction_time_delta_summary")
    public TransactionSummary getTimeDeltaSummary(@RequestParam (required = false) String from_date, @RequestParam (required = false) String to_date, @RequestParam (required = false) String product_id, @RequestParam (required=false) String typeOfData) throws IOException, ParseException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from = null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        logges.addInfoLog("GET in transaction_time_delta_summary with from=\"+from+\" and to=\"+to+\" with product id=\"+product_id,info",info);
        return timeDeltaSummaryService.timeDelta(typeOfData,product_id,from,to);
    }
}