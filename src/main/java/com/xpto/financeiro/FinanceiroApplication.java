package com.xpto.financeiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FinanceiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceiroApplication.class, args);
        System.out.println(" SISTEMA FINANCEIRO XPTO INICIADO!");
        System.out.println(" Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println(" API Docs: http://localhost:8080/api-docs");
        System.out.println(" Health Check: http://localhost:8080/actuator/health");
    }
}