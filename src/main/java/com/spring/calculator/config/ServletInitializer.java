package com.spring.calculator.config;

import com.spring.calculator.CalculatorApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CalculatorApplication.class);
    }
}

// TODO:
//      1. prisiloginus su narsykles back migutuku galima grizt atgal i login ir vel i calculator ir taip vaikscioti(panaikinti)
//      2. skaiciavimu paieska