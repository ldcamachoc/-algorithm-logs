package com.armory.app.logs;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public interface Constants {
	double MEG = (Math.pow(1024, 2));
	int RECORD_COUNT = 4000000;
	String RECORD = "Any type of record \n";	
	SimpleDateFormat ISO= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss,SSS'Z'");// "yyyy-MM-dd HH:mm:ss,SSS'Z'"
	DateTimeFormatter IS2O= DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss,SSS'Z'");
	
	String DATE_FORMAT_REGEX = "(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2}).*";
    Pattern DATE_FORMAT_PATTERN = Pattern.compile(DATE_FORMAT_REGEX);
    String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";
}
