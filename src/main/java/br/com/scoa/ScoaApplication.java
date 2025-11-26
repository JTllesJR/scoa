package br.com.scoa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScoaApplication {

    public static void main(String[] args) {
        System.out.println("##### INICIANDO SCOA #####");
        SpringApplication.run(ScoaApplication.class, args);
    }
}