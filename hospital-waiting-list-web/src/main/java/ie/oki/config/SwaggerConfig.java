package ie.oki.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Configures the swagger.
 *
 * @author Zoltan Toth
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private MessageSource messageSource;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(Predicates.not(regex("/error.*")))
            .build()
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
            messageSource.getMessage("swagger.title", null, LocaleContextHolder.getLocale()),
            messageSource.getMessage("swagger.description", null, LocaleContextHolder.getLocale()),
            messageSource.getMessage("swagger.version", null, LocaleContextHolder.getLocale()),
            messageSource.getMessage("swagger.termsOfServiceUrl", null, LocaleContextHolder.getLocale()),
            new Contact(
                messageSource.getMessage("swagger.contact.name", null, LocaleContextHolder.getLocale()),
                null,
                null
            ),
            messageSource.getMessage("swagger.license", null, LocaleContextHolder.getLocale()),
            messageSource.getMessage("swagger.licenseUrl", null, LocaleContextHolder.getLocale()),
            new ArrayList<>());
    }
}
