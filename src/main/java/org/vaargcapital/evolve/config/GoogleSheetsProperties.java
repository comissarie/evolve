package org.vaargcapital.evolve.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.googleSheets")
public record GoogleSheetsProperties(String generalBaseUrl, String disBaseUrl) {
}
