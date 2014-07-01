package fr.wati.yacramanager.utils;

import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

@SuppressWarnings("serial")
public class CustomObjectMapper extends ObjectMapper {
    public CustomObjectMapper() {
    	configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS , false);          
        setDateFormat(new ISO8601DateFormat());
    }
    
    public static void main(String[] args) {
    	System.out.println(new ISO8601DateFormat().format(new Date()));
	}
}
