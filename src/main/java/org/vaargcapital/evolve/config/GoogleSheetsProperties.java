package org.vaargcapital.evolve.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.google-sheets")
public record GoogleSheetsProperties(String generalBaseUrl, String disBaseUrl) {
}
