package cat.nexia.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Classe Main de l'aplicació
 */
@SpringBootApplication
@ComponentScan(basePackages = "cat.nexia.spring")
public class SpringBootNexiaApplication  {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootNexiaApplication.class, args);
	}


}
