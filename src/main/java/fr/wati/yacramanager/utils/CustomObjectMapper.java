package fr.wati.yacramanager.utils;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

@SuppressWarnings("serial")
@Component
public class CustomObjectMapper extends ObjectMapper {
    public CustomObjectMapper() {
    	configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS , false);          
        setDateFormat(new ISO8601DateFormat());
    }
    
    public static void main(String[] args) {
    	System.out.println(new ISO8601DateFormat().format(new Date()));
	}
    @PostConstruct
    public void customConfiguration() {
        // Uses Enum.toString() for serialization of an Enum
        this.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        // Uses Enum.toString() for deserialization of an Enum
        this.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }
}
