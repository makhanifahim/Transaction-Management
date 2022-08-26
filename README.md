# Transaction-Management

## For using this application java and maven is required , this application is based on Java spring boot,
### Quick Start
   * Java : https://www.java.com/en/download/
   * Maven : https://maven.apache.org/download.cgi
   
->Please go through it and download and configer it as per your Operating system 

->After installing Required this you can run the jar file using command

->Open the cmd in location where the file is Downloaded 

->java-(name of jar file)
### Following Dependency is been added in maven

   ->Basic Dependency
   * spring-boot-starter-web
   * spring-boot-devtools
   * lombok
   
   ->Dependency For Test
   * spring-boot-starter-test
   * mockito-core
   * commons-io
   
   ->Dependency For Swagger
   * springfox-swagger2
   * springfox-swagger-ui
   
   ->Dependency For csv
   * opencv
   * commons-lang3
   
## This Application can searve Following Searvicess.

 * Service that accepts a list of transactions and stores those transactions to csv files. A transaction consists of (init_date, conclusion_date, product_id, value). 

 * The csv folder will be serve as a staging ground and follow specific structure year/quarter/date.csv  like example 2022/1/2022-02-21

**To open Swagger (interactive) API documentation, navigate your browser to [YOUR-URL]/swagger-ui.html**
**All Logs will be saved in web.log file
### Rest API Endpoints
  - rest_api/transactions
    * POST - Add List of Transactions
    * GET - Get list of Transactions
    * GET ?from_date={value}&to_date={value}  - Get List of Transaction between this dates
    
    * GET ?oldest=true  - Get Oldest Transaction in compare to all
    * GET ?oldest=true&product_id={value}  - Get oldest Transaction in compare to all of perticular product
    * GET ?from_date={value}&to_date={value}&oldest=true  - Get Oldest Transaction in compare to all transaction done in between two dates
    * GET ?from_date={value}&to_date={value}&oldest=true&product_id={value} - Get Oldest Transaction in compare to all transaction done in between two dates of       perticular product
    
    * GET ?newest=true  - Get Newest Transaction in compare to all
    * GET ?newest=true&product_id={value}  - Get Newest Transaction in compare to all of perticular product
    * GET ?from_date={value}&to_date={value}&oldest=true  - Get Newest Transaction in compare to all transaction done in between two dates
    * GET ?from_date={value}&to_date={value}&newest=true&product_id={value} - Get newest Transaction in compare to all transaction done in between two dates of perticular product
   
  
  - rest_api/transaction_value_summary
    * GET  -  Returns value summary with mean,mode,standard deviation,variance
    * GET ?from_date={value}&to_date={value} - Returns value summary with mean,mode,standard deviation,variance (calculates transaction done in between two dates)  
  
  
  - rest_api/products
    * GET -  Retuns all the products with its count it is been sold
    * GET ?from_date={value}&to_date={value} - Returns all Products with its count it is been sold between two dates 
    * GET ?most_common=true - Returns most common products and its total times solded 
    * GET ?lest_common=true - Returns lest common product and its total times solded
    * GET ?from_date={value}&to_date={value}?most_common= true - Returns all Products with its count it is been sold between two dates 
    * GET ?from_date={value}&to_date={value}?lest_common - Returns all Products with its count it is been sold between two dates 
    
  - rest_api/transaction_time_delta_summary
    * GET - Returns time Delta summary with mean,mode,standard deviation
    * GET ?product_id={value} - Returns time Delta summary of perticulat product with mean,mode,standard deviation
    * GET ?from_date={value}&to_data={value} - Returns time Delta summary with mean,mode,standard deviation (calculation is done for all transaction in between dates)
    * GET ?from_date={value}&to_data={value}&product_id={value} - Returns time Delta summary with mean,mode,standard deviation (calculation is done for all transaction in between two dates)
    
### RPC API Endpoints
  - rpc_api/create_transaction 
    * POST - Save List of Transactions in files
    
  - rpc_api/oldest_transaction
    * GET  - returns oldest transaction from all the transaction 
    * GET ?product_id={value}  - returns oldest transaction of perticular product from all the transaction
    * GET ?from_date={value}&to_date={value} - returns oldest transaction from all the transaction done in between two dates
    * GET ?from_date={value}&to_date={value}&product_id={value} - returns oldest transaction from all the transaction od perticular product done in between two dates 

  - rpc_api/newest_transaction
    * GET  - returns newest transaction from all the transaction 
    * GET ?product_id={value}  - returns newest transaction of perticular product from all the transaction
    * GET ?from_date={value}&to_date={value} - returns newest transaction from all the transaction done in between two dates
    * GET ?from_date={value}&to_date={value}&product_id={value} - returns newest transaction from all the transaction od perticular product done in between two dates 
 
  - rpc_api/mean
    * GET  - returns mean of all transaction 
    * GET ?product_id={value}  - returns mean of all transaction of perticular products
    * GET ?from_date={value}&to_date={value} - returns mean of all transaction done in between two dates
    * GET ?from_date={value}&to_date={value}&product_id={value} - returns mean of all transaction done in between two dates of perticular product
   
  - rpc_api/mode
    * GET  - returns mode of all transaction 
    * GET ?product_id={value}  - returns mode of all transaction of perticular products
    * GET ?from_date={value}&to_date={value} - returns mode of all transaction done in between two dates
    * GET ?from_date={value}&to_date={value}&product_id={value} - returns mode of all transaction done in between two dates of perticular product
    
  - rpc_api/standard_deviation
    * GET  - returns standard_deviation of all transaction 
    * GET ?product_id={value}  - returns standard_deviation of all transaction of perticular products
    * GET ?from_date={value}&to_date={value} - returns standard_deviation of all transaction done in between two dates
    * GET ?from_date={value}&to_date={value}&product_id={value} - returns standard_deviation of all transaction done in between two dates of perticular product
    
  - rpc_api/variance
    * GET  - returns variance of all transaction 
    * GET ?product_id={value}  - returns variance of all transaction of perticular products
    * GET ?from_date={value}&to_date={value} - returns variance of all transaction done in between two dates
    * GET ?from_date={value}&to_date={value}&product_id={value} - returns variance of all transaction done in between two dates of perticular product
    
  - rpc_api/most_common_product
    * GET  - returns most_common_product of all transaction 
    * GET ?from_date={value}&to_date={value} - returns most_common_product of all transaction done in between two dates
  
  - rpc_api/lest_common_product
    * GET  - returns lest_common_product of all transaction 
    * GET ?from_date={value}&to_date={value} - returns lest_common_product of all transaction done in between two dates
  
  - rpc_api/time_delta
    * GET  - returns time delta(mean,mode,standard deviation,variance) of all transaction 
    * GET ?from_date={value}&to_date={value} - returns time delta(mean,mode,standard deviation,variance) of all transaction done in between two dates
    * GET ?from_date={value}&to_date={value}&product_id={value} - returns time delta(mean,mode,standard deviation,variance) of all transaction done in between two dates of perticular products
    
