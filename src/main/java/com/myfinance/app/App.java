package com.myfinance.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

        //para teste -> curl -X POST http://localhost:8080/transactions -H "Content-Type: application/json" -d @transaction.json

        //para exportar csv -> curl -X GET http://localhost:8080/transactions --output transactions.csv
        System.out.println("Server rodando!");
    }
}