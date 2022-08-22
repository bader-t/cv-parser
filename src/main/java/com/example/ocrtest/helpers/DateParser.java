package com.example.ocrtest.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class DateParser {
    private final Map<String, Pattern> regexMap;

    public DateParser() {
        this.regexMap= new HashMap<String, Pattern>(){{
            put("MMM yyyy",Pattern.compile("^[a-z]{3}\\s\\d{4}$",Pattern.CASE_INSENSITIVE));
            put("MMMM yyyy",Pattern.compile("^[a-z]{4,}\\s\\d{4}$",Pattern.CASE_INSENSITIVE));
            put("dd-MM-yyyy",Pattern.compile("^\\d{1,2}-\\d{1,2}-\\d{4}$", Pattern.CASE_INSENSITIVE));
            put("dd MM yyyy",Pattern.compile("^\\d{1,2}\\s\\d{1,2}\\s\\d{4}$", Pattern.CASE_INSENSITIVE));
            put("yyyy-MM-dd",Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$", Pattern.CASE_INSENSITIVE));
            put("MM/dd/yyyy",Pattern.compile("^\\d{1,2}/\\d{1,2}/\\d{4}$", Pattern.CASE_INSENSITIVE));
            put("yyyy/MM/dd",Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2}$", Pattern.CASE_INSENSITIVE));
            put("dd MMM yyyy",Pattern.compile("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", Pattern.CASE_INSENSITIVE));
            put("dd MMMM yyyy",Pattern.compile("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", Pattern.CASE_INSENSITIVE));
        }};
    }

    public String determineDateFormat(String matcher){
        for (Map.Entry<String, Pattern> entry : this.regexMap.entrySet()) {
            if (entry.getValue().matcher(matcher).find()) {
                return entry.getKey();
            }
        }
        return "Unknown";
    }

    public String parseDate(String matcher){
         String format = this.determineDateFormat(matcher);
        System.out.println(format);
        if (!format.equals("Unknown")){
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern(format)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter(Locale.US);


            return  LocalDate.parse(matcher, formatter).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return matcher;

    }
}
