package fr.wati.yacramanager.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"fr.wati.yacramanager"})
@PropertySource(value = { "classpath:database-yacra.properties" })
@Import(value={PersistenceConfig.class,MetricsConfiguration.class,MailConfiguration.class,AsyncConfiguration.class,AspectConfiguration.class})
public class RootConfig {

}
