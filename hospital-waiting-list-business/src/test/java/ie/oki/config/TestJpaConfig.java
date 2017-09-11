package ie.oki.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Zoltan Toth
 */
@Configuration
@EntityScan("ie.oki.model")
@EnableJpaRepositories("ie.oki.repository")
public class TestJpaConfig {
}
