package fr.wati.yacramanager.config.apidoc;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;

@Configuration
@EnableSwagger
public class SwaggerConfiguration implements EnvironmentAware {
    public static final String DEFAULT_INCLUDE_PATTERN = "/app/api/.*";


	private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Swagger Spring MVC configuration
     */
    @Bean
    public SwaggerSpringMvcPlugin swaggerSpringMvcPlugin(SpringSwaggerConfig springSwaggerConfig) {
        return new SwaggerSpringMvcPlugin(springSwaggerConfig)
                .apiInfo(apiInfo())
                .genericModelSubstitutes(ResponseEntity.class)
                .includePatterns(environment.getProperty("swagger.include.pattern", DEFAULT_INCLUDE_PATTERN));
    }

    /**
     * API Info as it appears on the swagger-ui page
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
        		environment.getProperty("swagger.title"),
        		environment.getProperty("swagger.description"),
        		environment.getProperty("swagger.termsOfServiceUrl"),
        		environment.getProperty("swagger.contact"),
        		environment.getProperty("swagger.license"),
        		environment.getProperty("swagger.licenseUrl"));
    }
}
