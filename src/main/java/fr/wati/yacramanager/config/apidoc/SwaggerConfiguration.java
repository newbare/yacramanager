package fr.wati.yacramanager.config.apidoc;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;

import fr.wati.yacramanager.config.Constants;

@Configuration
@EnableSwagger
@Profile("!" + Constants.SPRING_PROFILE_FAST)
public class SwaggerConfiguration implements EnvironmentAware {
    public static final String DEFAULT_INCLUDE_PATTERN = "/app/api/.*";


    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "swagger.");
    }

    /**
     * Swagger Spring MVC configuration
     */
    @Bean
    public SwaggerSpringMvcPlugin swaggerSpringMvcPlugin(SpringSwaggerConfig springSwaggerConfig) {
        return new SwaggerSpringMvcPlugin(springSwaggerConfig)
                .apiInfo(apiInfo())
                .genericModelSubstitutes(ResponseEntity.class)
                .includePatterns(StringUtils.split(propertyResolver.getProperty("includePatterns", DEFAULT_INCLUDE_PATTERN),","));
    }

    /**
     * API Info as it appears on the swagger-ui page
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
        		propertyResolver.getProperty("title"),
        		propertyResolver.getProperty("description"),
        		propertyResolver.getProperty("termsOfServiceUrl"),
        		propertyResolver.getProperty("contact"),
        		propertyResolver.getProperty("license"),
        		propertyResolver.getProperty("licenseUrl"));
    }
}
