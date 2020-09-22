package nl.miwgroningen.se.ch3.bacchux;

import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class BacchuxApplication {

	public static void main(String[] args) {
		SpringApplication.run(BacchuxApplication.class, args);
	}

}
