package TransactionMonitor.com.bbd.controller.rpc_apis;

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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@RequestMapping("/rpc_api")
@RestController
@ControllerAdvice
public class RpcTransactionController {
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

    @Timed(value = "RPC_create_transaction.time", description = "Time taken for rest api GET rpc_api/create_transaction ")
    @PostMapping("/create_transactions")
    public String createTransactions(@RequestBody List<Transaction> transactions, @PathVariable(required=false) String typeOfData) {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        logges.addInfoLog("RPC - POST in create_transaction is been fired",info);
        return service.saveTransaction(transactions, typeOfData);
    }

    @Timed(value = "RPC_oldest_transaction.time", description = "Time taken for rest api GET rpc_api/oldest_transaction ")
    @GetMapping("/oldest_transaction")
    public List<Transaction> oldestTransaction(@RequestParam (required = false) String from_date,@RequestParam (required = false) String to_date,@RequestParam (required = false) String product_id,@RequestParam (required = false) String typeOfData) throws ParseException, IOException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        String p_id=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        if(product_id!=null){p_id=product_id;}
        List<Transaction> oldestTransact= new ArrayList();
        oldestTransact.add(service.oldestTransaction(typeOfData,p_id,from,to));
        logges.addInfoLog("RPC - GET in oldest_transaction with from="+from+" and to="+to+" with product id="+product_id,info);
        return oldestTransact;
    }

    @Timed(value = "RPC_newest_transaction.time", description = "Time taken for rest api GET rpc_api/newest_transaction ")
    @GetMapping("/newest_transaction")
    public List<Transaction> newestTransaction(@RequestParam (required = false) String from_date,@RequestParam (required = false) String to_date,@RequestParam (required = false) String product_id,@RequestParam (required = false) String typeOfData) throws ParseException, IOException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        String p_id=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        if(product_id!=null){p_id=product_id;}
        List<Transaction> newestTransact= new ArrayList();
        newestTransact.add(service.newerTransaction(typeOfData,p_id,from,to));
        logges.addInfoLog("RPC - GET in newest_transaction with from="+from+" and to="+to+" with product id="+product_id,info);
        return newestTransact;
    }

    @Timed(value = "RPC_transaction_mean.time", description = "Time taken for rest api GET rpc_api/mean ")
    @GetMapping("/mean")
    public float meanTransaction(@RequestParam (required = false) String from_date,@RequestParam (required = false) String to_date,@RequestParam (required = false) String product_id,@RequestParam (required = false) String typeOfData) throws ParseException, IOException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        String p_id=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        if(product_id!=null){p_id=product_id;}
        logges.addInfoLog("RPC - GET in mean with from="+from+" and to="+to+" with product id="+product_id,info);
        return valueSummaryService.meanOfTransaction(from,to,p_id,typeOfData);
    }

    @Timed(value = "RPC_transaction_mode.time", description = "Time taken for rest api GET rpc_api/mode ")
    @GetMapping("/mode")
    public String modeTransaction(@RequestParam (required = false) String from_date,@RequestParam (required = false) String to_date,@RequestParam (required = false) String product_id,@RequestParam (required = false) String typeOfData) throws ParseException, IOException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        String p_id=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        if(product_id!=null){p_id=product_id;}
        logges.addInfoLog("RPC - GET in mode with from="+from+" and to="+to+" with product id="+product_id,info);
        return valueSummaryService.modeOfTransaction(from,to,p_id,typeOfData);
    }

    @Timed(value = "RPC_transaction_standard_deviation.time", description = "Time taken for rest api GET rpc_api/standard_deviation")
    @GetMapping("/standard_deviation")
    public float standardDeviationTransaction(@RequestParam (required = false) String from_date,@RequestParam (required = false) String to_date,@RequestParam (required = false) String product_id,@RequestParam (required = false) String typeOfData) throws ParseException, IOException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        String p_id=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        if(product_id!=null){p_id=product_id;}
        logges.addInfoLog("RPC - GET in standard deviation with from="+from+" and to="+to+" with product id="+product_id,info);
        return valueSummaryService.standardDeviationOfTransaction(from,to,p_id,typeOfData);
    }

    @Timed(value = "RPC_transaction_variance.time", description = "Time taken for rest api GET rpc_api/variance ")
    @GetMapping("/variance")
    public float varianceTransaction(@RequestParam (required = false) String from_date,@RequestParam (required = false) String to_date,@RequestParam (required = false) String product_id,@RequestParam (required = false) String typeOfData) throws ParseException, IOException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        String p_id=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        if(product_id!=null){p_id=product_id;}
        logges.addInfoLog("RPC - GET in variance with from="+from+" and to="+to+" with product id="+product_id,info);
        return valueSummaryService.varianceOfTransaction(from,to,p_id,typeOfData);
    }

    @Timed(value = "RPC_most_common_product.time", description = "Time taken for rest api GET rpc_api/most_common_product ")
    @GetMapping("/most_common_product")
    public List<Product> mostCommonTransaction(@RequestParam (required = false) String from_date, @RequestParam (required = false) String to_date, @RequestParam (required = false) String typeOfData) throws ParseException, IOException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        logges.addInfoLog("RPC - GET in most common product with from="+from+" and to="+to,info);
        return productService.CommonProduct(typeOfData,from,to,true,false);
    }

    @Timed(value = "RPC_lest_common_product.time", description = "Time taken for rest api GET rpc_api/lest_common_product ")
    @GetMapping("/lest_common_product")
    public List<Product> lestCommonTransaction(@RequestParam (required = false) String from_date, @RequestParam (required = false) String to_date, @RequestParam (required = false) String typeOfData) throws ParseException, IOException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        logges.addInfoLog("RPC - GET in lest common product with from="+from+" and to="+to,info);
        return productService.CommonProduct(typeOfData,from,to,false,true);
    }

    @Timed(value = "RPC_time_delta.time", description = "Time taken for rest api GET rpc_api/time_delta ")
    @GetMapping("/time_delta")
    public TransactionSummary getTimeDeltaSummary(@RequestParam (required = false) String from_date, @RequestParam (required = false) String to_date, @RequestParam (required = false) String product_id, @RequestParam (required=false) String typeOfData) throws IOException, ParseException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from = null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        logges.addInfoLog("RPC - GET in time_delta with from="+from+" and to="+to+" with product id="+product_id,info);
        return timeDeltaSummaryService.timeDelta(typeOfData,product_id,from,to);
    }

}