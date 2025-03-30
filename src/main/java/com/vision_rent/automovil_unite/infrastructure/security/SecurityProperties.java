package com.vision_rent.automovil_unite.infrastructure.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Propiedades de configuración para la seguridad de la aplicación.
 */
@Component
@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class SecurityProperties {

    private String secret;
    private long expiration;
    private long refreshExpiration;
    private String header;
    private String prefix;

}