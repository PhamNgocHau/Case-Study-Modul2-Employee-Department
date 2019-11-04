package com.codegym.employe.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class EmployeeFormatter implements Formatter<LocalDate> {

    @Override
    public LocalDate parse(String date, Locale locale) throws ParseException {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return object.toString();
    }
}
