package fr.wati.yacramanager.web.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import fr.wati.yacramanager.beans.Role;
import fr.wati.yacramanager.web.dto.LoggerDTO;
import fr.wati.yacramanager.web.dto.ResponseWrapper;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/app/admin")
public class LogsController {

    @RequestMapping(value = "/logs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<List<LoggerDTO>> getList(@RequestParam(value="page",defaultValue="0") int page,@RequestParam(value="size",defaultValue="100") int size,@RequestParam(value="key",defaultValue="") final String filterKey) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<LoggerDTO> loggers = new ArrayList<>();
        List<Logger> loggersList = context.getLoggerList();
        Collection<Logger> filteredCollection = Collections2.filter(loggersList, new Predicate<Logger>() {
			@Override
			public boolean apply(Logger input) {
				return StringUtils.containsIgnoreCase(input.getName(), filterKey);
			}
		});
        List<List<Logger>> partitions = Lists.partition(Lists.newArrayList(filteredCollection), size);
        for (ch.qos.logback.classic.Logger logger : partitions.get(page)) {
            loggers.add(new LoggerDTO(logger));
        }
        return new ResponseWrapper<List<LoggerDTO>>(loggers, filteredCollection.size());
    }

    @RequestMapping(value = "/logs",
            method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed({Role.ADMIN})
    public void changeLevel(@RequestBody LoggerDTO jsonLogger) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger(jsonLogger.getName()).setLevel(Level.valueOf(jsonLogger.getLevel()));
    }
}