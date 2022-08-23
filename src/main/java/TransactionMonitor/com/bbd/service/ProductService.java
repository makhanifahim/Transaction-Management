package TransactionMonitor.com.bbd.service;

import TransactionMonitor.com.bbd.model.Product;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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

    public List<Product> listCommonProduct(String TypeOfData, Date from_date, Date to_date) throws IOException, ParseException, CsvException {
        List<Product> productsList = new ArrayList<Product>();
        String[][] products = listCommonProd(TypeOfData,from_date,to_date);
        for(int p=0;p<products.length;p++){
            productsList.add(new Product(products[p][0]));
        }
        return productsList;
    }

    public List<Product> CommonProduct(String TypeOfData, Date from_date, Date to_date, boolean most_common, boolean lest_common) throws IOException, CsvException, ParseException {
        if (Objects.equals(TypeOfData, "") ||TypeOfData==null)
            TypeOfData="Data";
        String[][] products=listCommonProd(TypeOfData,from_date,to_date);
        List<Product> CommonProduct = new ArrayList<Product>();
        int countM=Integer.parseInt(products[0][1]),countL=Integer.parseInt(products[0][1]);
        String prodM=products[0][0],prodL=products[0][0];
        for(int i=1;i<products.length-1;i++){
            if(products[i][0]!=null) {
                if(most_common)
                    if(Integer.parseInt(products[i][1])>countM){
                        countM=Integer.parseInt(products[i][1]);
                        countM=countM+1;
                        prodM=products[i][0];
                    }
                if(lest_common)
                    if(Integer.parseInt(products[i][1])<countL){
                        countL=Integer.parseInt(products[i][1]);
                        countL=countL+1;
                        prodL=products[i][0];
                    }
            }
        }
        if(most_common&&lest_common){
            CommonProduct.add(new Product(prodM));
            CommonProduct.add(new Product(prodL));
        }
        else if(most_common)
            CommonProduct.add(new Product(prodM));
        else if(lest_common)
            CommonProduct.add(new Product(prodL));
        return CommonProduct;
    }
}
