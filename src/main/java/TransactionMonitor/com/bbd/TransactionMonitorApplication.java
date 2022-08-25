package TransactionMonitor.com.bbd;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@Configuration
@Slf4j
public class TransactionMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionMonitorApplication.class, args);
	}
}
