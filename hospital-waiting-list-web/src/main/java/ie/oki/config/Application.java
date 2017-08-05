package ie.oki.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Zoltan Toth
 */
@EnableCaching
@SpringBootApplication
@ComponentScan("ie.oki")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}