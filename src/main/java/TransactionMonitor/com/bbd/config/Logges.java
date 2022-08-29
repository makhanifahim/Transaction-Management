package TransactionMonitor.com.bbd.config;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class Logges {
    public void addInfoLog(String message,String logType){
        try {
            if(Objects.equals(logType, "INFO"))
                log.info(message);
            else if(Objects.equals(logType, "ERROR"))
                log.error(message);
        }
        catch (Exception e){
            log.error(message);
        }
    }
}

