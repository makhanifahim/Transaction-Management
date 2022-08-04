# Transaction-Management
 
This Application can searve Following Searvicess.

  * Service that accepts a list of transactions and stores those transactions to csv files. A transaction consists of (init_date, conclusion_date, product_id, value). 
      url = (/api/save)
  * The csv folder will be serve as a staging ground and follow specific structure year/quarter/date.csv  like example 2022/1/2022-02-21
  * the oldest transaction
      url = (/api/oldest)
  * the newest transaction
      url = (api/new))
  * the mean
      url = (/api/mean)
  * the mode
      url = (/api/mode)
  * the standard deviation
      url = (/api/mode)
  * the variance
      url = (/api/standardDeviation)
  * the most common product
      url = (/api/mostCommonProduct)
  * the least common product
      url = (/api/leastCommonProduct)
  * the mean, mode, standard deviation time delta between transactions
      url = (/api/getTimeDelta)
  * the mean, mode, standard deviation time delta between transactions of specific products
      url = (/getTimeDeltaByProduct/{productId})
  * all of the above is quarriable for a specific date range.
