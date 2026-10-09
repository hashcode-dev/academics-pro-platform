package com.hashcode.invoicely;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hashcode.invoicely", "com.academicspro.platform"})
public class InvoicelyApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvoicelyApplication.class, args);
    }

}
