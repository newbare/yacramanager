package fr.wati.yacramanager.services;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@SuppressWarnings("serial")
public class CustomObjectMapper extends ObjectMapper {
    public CustomObjectMapper() {
    	configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS , false);  
    	configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //setDateFormat(new ISO8601DateFormat());
        registerModule(new JodaModule());
    }
    
    @PostConstruct
    public void customConfiguration() {
        // Uses Enum.toString() for serialization of an Enum
        this.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        // Uses Enum.toString() for deserialization of an Enum
        this.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }
}
