package fr.wati.yacramanager.config;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutorFactory;
import cn.bluejoe.elfinder.controller.executor.DefaultCommandExecutorFactory;
import cn.bluejoe.elfinder.impl.DefaultFsServiceConfig;

@Configuration
@ComponentScan(basePackages = {"fr.wati.yacramanager.web.elfinder"})
public class ELFinderConfig {

	@Autowired
	private Environment env;

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
		config.setTmbWidth(env.getProperty("tmbWidth", Integer.class));
		return config;
	}
}
