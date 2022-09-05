package TransactionMonitor.com.bbd;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info=@Info(title ="Transaction Api",version = "1.2.1",description = "Documentation for Api's"))
public class TransactionMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionMonitorApplication.class, args);
	}
}
