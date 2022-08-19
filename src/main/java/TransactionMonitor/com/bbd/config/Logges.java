package TransactionMonitor.com.bbd.config;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class Logges {
    public boolean addInfoLog(String message,String logType){
        try {
            if(Objects.equals(logType, "info"))
                log.info(message);
            else
                log.error(message);
            return true;
        }
        catch (Exception e){
            log.error(message);
            return false;
        }
    }
}

