package nl.miwgroningen.se.ch3.bacchux;

import nl.miwgroningen.se.ch3.bacchux.model.UserChecker;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class BacchuxApplication {

	@Autowired
	UserChecker userChecker;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(BacchuxApplication.class, args);

		context.getBean(UserChecker.class)
	}

}
