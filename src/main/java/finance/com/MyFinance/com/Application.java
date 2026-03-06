package finance.com.MyFinance.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "finance.com")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}