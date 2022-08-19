package TransactionMonitor.com.bbd.controller;

import TransactionMonitor.com.bbd.config.Logges;
import TransactionMonitor.com.bbd.model.Transaction;
import TransactionMonitor.com.bbd.model.TransactionSummary;
import TransactionMonitor.com.bbd.service.ProductService;
import TransactionMonitor.com.bbd.service.TransactionService;
import TransactionMonitor.com.bbd.service.TransactionTimeDeltaSummaryService;
import TransactionMonitor.com.bbd.service.TransactionValueSummaryService;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@ControllerAdvice
public class RpcTransactionController {
    @Autowired
    private final TransactionService service=new TransactionService();
    @Autowired
    private final TransactionValueSummaryService valueSummaryService=new TransactionValueSummaryService();
    @Autowired
    private final TransactionTimeDeltaSummaryService timeDeltaSummaryService=new TransactionTimeDeltaSummaryService();
    @Autowired
    private final ProductService productService=new ProductService();

    private final Logges logges = new Logges();
    String info="INFO";

    @PostMapping("/create_transactions")
    public String createTransactions(@RequestBody List<Transaction> transactions, @PathVariable(required=false) String typeOfData) {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        logges.addInfoLog("RPC - POST in create_transaction is been fired",info);
        return service.saveTransaction(transactions, typeOfData);
    }

    @GetMapping("/oldest_transaction")
    public List<String[]> oldestTransaction(@RequestParam (required = false) String from_date,@RequestParam (required = false) String to_date,@RequestParam (required = false) String product_id,@RequestParam (required = false) String typeOfData) throws ParseException, IOException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        String p_id=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        if(product_id!=null){p_id=product_id;}
        List<String[]> oldestTransact= new ArrayList();
        oldestTransact.add(service.oldestTransaction(typeOfData,p_id,from,to));
        logges.addInfoLog("RPC - GET in oldest_transaction with from="+from+" and to="+to+" with product id="+product_id,info);
        return oldestTransact;
    }

    @GetMapping("/newest_transaction")
    public List<String[]> newestTransaction(@RequestParam (required = false) String from_date,@RequestParam (required = false) String to_date,@RequestParam (required = false) String product_id,@RequestParam (required = false) String typeOfData) throws ParseException, IOException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        String p_id=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        if(product_id!=null){p_id=product_id;}
        List<String[]> newestTransact= new ArrayList();
        newestTransact.add(service.newerTransaction(typeOfData,p_id,from,to));
        logges.addInfoLog("RPC - GET in newest_transaction with from="+from+" and to="+to+" with product id="+product_id,info);
        return newestTransact;
    }

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

    @GetMapping("/most_common_product")
    public String[][] mostCommonTransaction(@RequestParam (required = false) String from_date,@RequestParam (required = false) String to_date,@RequestParam (required = false) String typeOfData) throws ParseException, IOException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        logges.addInfoLog("RPC - GET in most common product with from="+from+" and to="+to,info);
        return productService.CommonProduct(typeOfData,from,to,true,false);
    }

    @GetMapping("/lest_common_product")
    public String[][] lestCommonTransaction(@RequestParam (required = false) String from_date,@RequestParam (required = false) String to_date,@RequestParam (required = false) String typeOfData) throws ParseException, IOException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        logges.addInfoLog("RPC - GET in lest common product with from="+from+" and to="+to,info);
        return productService.CommonProduct(typeOfData,from,to,false,true);
    }

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
