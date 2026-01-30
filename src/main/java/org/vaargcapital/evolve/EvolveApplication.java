package org.vaargcapital.evolve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.vaargcapital.evolve.config.GoogleSheetsProperties;

@SpringBootApplication
@EnableConfigurationProperties(GoogleSheetsProperties.class)
public class EvolveApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvolveApplication.class, args);
    }
}
