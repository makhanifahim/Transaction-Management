package TransactionMonitor.com.bbd;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
@Configuration
@Slf4j
public class TransactionMonitorApplication {

	public static void main(String[] args) {
		log.info("Application Started");
		SpringApplication.run(TransactionMonitorApplication.class, args);
	}
	@Bean
	public Docket createDocket() {
		return  new Docket(DocumentationType.SWAGGER_2)  //UI screen type
				.select()  //to specify  RestControllers
				.apis(RequestHandlerSelectors.basePackage("TransactionMonitor.com.bbd.controller")) //base pkg for RestContrllers
				.paths(PathSelectors.any()) // to specify request paths
				.build() // builds the Docket obj
				.useDefaultResponseMessages(true)
				.apiInfo(getApiInfo());
	}
	private ApiInfo getApiInfo() {
		Contact contact=new Contact("Transaction Moniter","http://www.bbdsoftware.com","Transaction@bbd.co.za");
		return  new ApiInfo("Transaction API",
				"Gives Info from Past Transaction",
				"0.1.RELEASE",
				"",
				contact,
				"",
				"",
				Collections.emptyList());
	}
}
