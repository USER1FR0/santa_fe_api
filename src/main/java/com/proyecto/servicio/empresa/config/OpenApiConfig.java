package com.proyecto.servicio.empresa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

@Configuration
@CrossOrigin(origins = "*", methods = {RequestMethod.POST})
public class OpenApiConfig {

    @Value("${openapi.url.dev}")
    private String url;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(getInfo())
                .addServersItem(new Server().url(url));
    }

    private Info getInfo() {
        Info info = new Info();
        info.setTitle("Proyecto Santa Fe");
        info.setVersion("v1");
        info.setDescription("Documentación de la API del Proyecto Santa Fe (sin autenticación JWT)");
        return info;
    }
}
