package ru.job4j.urlshortcut.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${urlshortcut.openapi.dev-url:http://localhost:8080}")
    private String devUrl;

    @Value("${urlshortcut.openapi.prod-url:https://urlshortcut.example.com}")
    private String prodUrl;

    @Bean
    public OpenAPI urlShortcutOpenAPI() {
        var devServer = new Server()
                .url(devUrl)
                .description("Server URL in development environment");
        var prodServer = new Server()
                .url(prodUrl)
                .description("Server URL in production environment");
        var contact = new Contact()
                .name("URL Shortcut API support")
                .email("support@urlshortcut.example.com")
                .url("https://urlshortcut.example.com");
        var license = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");
        var info = new Info()
                .title("URL Shortcut API")
                .version("1.0")
                .description("REST API for registering sites, converting URLs, redirecting by codes and viewing statistics.")
                .termsOfService("https://urlshortcut.example.com/terms")
                .contact(contact)
                .license(license);
        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }

}
