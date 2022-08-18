package TransactionMonitor.com.bbd.service;

import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ProductService {

    @Autowired
    TransactionService transactionService;

    private String[][] trimArray(String[][] products){
        int k=0;
        for (String[] product : products) {
            if (product[0] != null)
                k++;
        }
        String[][] productList = new String[k][2];
        for(int p=0;p<k;p++){
            if(products[p][0]!=null) {
                productList[p][0] = products[p][0];
                productList[p][1] = products[p][1];
            }
        }
        return productList;
    }

    public String[][] listCommonProd(String TypeOfData, Date from_date, Date to_date) throws IOException, CsvException, ParseException {
        int c=0;
        List<String[]> allTransaction = transactionService.allTransaction(TypeOfData,null,from_date,to_date);
        String[][] products = new String[allTransaction.size()][2];
        for (int t = 0; t< (long) allTransaction.size(); t++){
            String[] row=allTransaction.get(t);
            int present=0;
            for(int i=0;i<products.length;i++){
                if(products[i][0]!=null) {
                    if(products[i][0].equals(row[2])){
                        present=1;
                        products[i][1]=String.valueOf(Integer.parseInt(products[i][1])+1);
                    }
                }
            }
            if(present==0){
                products[c][0] = row[2];
                products[c][1] = String.valueOf(0);
                c++;
            }
        }
        return trimArray(products);
    }

    public String[][] CommonProduct(String TypeOfData, Date from_date, Date to_date,boolean most_common,boolean lest_common) throws IOException, CsvException, ParseException {
        if (Objects.equals(TypeOfData, "") ||TypeOfData==null)
            TypeOfData="Data";
        String[][] products=listCommonProd(TypeOfData,from_date,to_date);
        String[][] CommonProduct= new String[1][2];
        int count=Integer.parseInt(products[0][1]);
        String prod=products[0][0];
        for(int i=1;i<products.length-1;i++){
            if(products[i][0]!=null) {
                if(most_common)
                    if(Integer.parseInt(products[i][1])>count){
                        count=Integer.parseInt(products[i][1]);
                        count=count+1;
                        prod=products[i][0];
                    }
                if(lest_common)
                    if(Integer.parseInt(products[i][1])<count){
                        count=Integer.parseInt(products[i][1]);
                        count=count+1;
                        prod=products[i][0];
                    }
            }
        }
        CommonProduct[0][0]= prod;
        CommonProduct[0][1]= String.valueOf(count);
        log.info("User has Requested for Most Common Product {} with count {}",prod,count);
        return CommonProduct;
    }
}
