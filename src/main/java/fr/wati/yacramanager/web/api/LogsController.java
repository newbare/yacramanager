package fr.wati.yacramanager.web.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import fr.wati.yacramanager.beans.Role;
import fr.wati.yacramanager.web.dto.LoggerDTO;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/app/admin")
public class LogsController {

    @RequestMapping(value = "/logs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LoggerDTO> getList() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<LoggerDTO> loggers = new ArrayList<>();
        for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
            loggers.add(new LoggerDTO(logger));
        }
        return loggers;
    }

    @RequestMapping(value = "/logs",
            method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed({Role.ROLE_ADMIN})
    public void changeLevel(@RequestBody LoggerDTO jsonLogger) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger(jsonLogger.getName()).setLevel(Level.valueOf(jsonLogger.getLevel()));
    }
}