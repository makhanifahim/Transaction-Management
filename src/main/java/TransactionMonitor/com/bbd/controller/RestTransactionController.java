package TransactionMonitor.com.bbd.controller;

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

@RestController
@RequestMapping("/rest_api")
@Slf4j
public class RestTransactionController {
    @Autowired
    private final TransactionService service=new TransactionService();
    @Autowired
    private final TransactionValueSummaryService valueSummaryService=new TransactionValueSummaryService();
    @Autowired
    private final TransactionTimeDeltaSummaryService timeDeltaSummaryService=new TransactionTimeDeltaSummaryService();
    @Autowired
    private final ProductService productService=new ProductService();

    @PostMapping("/transactions")
    public String saveTransactions(@RequestBody List<Transaction> transactions,@PathVariable (required=false) String typeOfData) {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        return service.saveTransaction(transactions, typeOfData);
    }

    @GetMapping("/transactions")
    public List<String[]> getTransactions(@RequestParam (required = false) String from_date,@RequestParam (required = false) String to_date,@RequestParam (required = false) boolean oldest,@RequestParam (required = false) boolean newest,@RequestParam (required = false) String product_id,@RequestParam (required=false) String typeOfData) throws IOException, ParseException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from=null,to=null;
        String p_id=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        if(product_id!=null){p_id=product_id;}
        if(oldest && newest){
            List<String[]> Transact= new ArrayList();
            Transact.add(service.oldestTransaction(typeOfData,p_id,from,to));
            Transact.add(service.newerTransaction(typeOfData,p_id,from,to));
            return Transact;
        }
        else if(oldest){
            List<String[]> oldestTransact= new ArrayList();
            oldestTransact.add(service.oldestTransaction(typeOfData,p_id,from,to));
            return oldestTransact;
        }
        else if(newest) {
            List<String[]> newestTransact = new ArrayList();
            newestTransact.add(service.newerTransaction(typeOfData,p_id,from,to));
            return newestTransact;
        }
        else{return service.allTransaction(typeOfData, p_id,from, to);}
    }

    @GetMapping("/transaction_value_summary")
    public TransactionSummary getSummary(@RequestParam (required = false) String from_date, @RequestParam (required = false) String to_date, @RequestParam (required = false) String product_id, @RequestParam (required=false) String typeOfData) throws IOException, ParseException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from = null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        return new TransactionSummary(valueSummaryService.meanOfTransaction(from,to,product_id,typeOfData),valueSummaryService.modeOfTransaction(from,to,product_id,typeOfData),valueSummaryService.standardDeviationOfTransaction(from,to,product_id,typeOfData),valueSummaryService.varianceOfTransaction(from,to,product_id,typeOfData));
    }

    @GetMapping("/products")
    public String[][] allProducts(@RequestParam (required = false) String from_date, @RequestParam (required = false) String to_date, @RequestParam (required=false) String typeOfData,@RequestParam (required = false) boolean most_common,@RequestParam (required = false) boolean lest_common) throws IOException, CsvException, ParseException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from = null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        if(most_common && lest_common){
            return productService.CommonProduct(typeOfData,from,to,true,true);
        }
        else if(most_common)
            return productService.CommonProduct(typeOfData,from,to,true,false);
        else if(lest_common)
            return productService.CommonProduct(typeOfData,from,to,false,true);
        else
            return productService.listCommonProd(typeOfData,from,to);
    }

    @GetMapping("/transaction_time_delta_summary")
    public TransactionSummary getTimeDeltaSummary(@RequestParam (required = false) String from_date, @RequestParam (required = false) String to_date, @RequestParam (required = false) String product_id, @RequestParam (required=false) String typeOfData) throws IOException, ParseException, CsvException {
        if(Objects.equals(typeOfData, "") ||typeOfData==null)
            typeOfData="Data";
        Date from = null,to=null;
        if(from_date!=null){from = new SimpleDateFormat("yyyy-MM-dd").parse(from_date);}
        if(to_date!=null) {to = new SimpleDateFormat("yyyy-MM-dd").parse(to_date);}
        return timeDeltaSummaryService.timeDelta(typeOfData,product_id,from,to);
    }
}
