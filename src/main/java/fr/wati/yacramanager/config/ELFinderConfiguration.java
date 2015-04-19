package fr.wati.yacramanager.config;

import java.util.HashMap;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutorFactory;
import cn.bluejoe.elfinder.controller.executor.DefaultCommandExecutorFactory;
import cn.bluejoe.elfinder.impl.DefaultFsServiceConfig;

@Configuration
public class ELFinderConfiguration  implements EnvironmentAware{

	private RelaxedPropertyResolver propertyResolver;


	@Override
	public void setEnvironment(Environment env) {
		this.propertyResolver = new RelaxedPropertyResolver(env,
				"elfinder.");
	}

	@Bean
	public CommandExecutorFactory commandExecutorFactory() {
		DefaultCommandExecutorFactory commandExecutorFactory = new DefaultCommandExecutorFactory();
		commandExecutorFactory
				.setClassNamePattern("cn.bluejoe.elfinder.controller.executors.%sCommandExecutor");
		commandExecutorFactory.setMap(new HashMap<String,CommandExecutor>());
		return commandExecutorFactory;
	}

	
	@Bean
	public DefaultFsServiceConfig defaultFsServiceConfig() {
		DefaultFsServiceConfig config = new DefaultFsServiceConfig();
		config.setTmbWidth(propertyResolver.getProperty("tmbWidth", Integer.class));
		return config;
	}
}
