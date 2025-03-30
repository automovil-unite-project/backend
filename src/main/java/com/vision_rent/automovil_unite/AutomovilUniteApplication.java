package com.vision_rent.automovil_unite;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "API Automovil Unite",
				version = "1.0",
				description = "Documentación de la API con Swagger"
		)
)
@SpringBootApplication
public class AutomovilUniteApplication {
	public static void main(String[] args) {
		SpringApplication.run(AutomovilUniteApplication.class, args);
	}
}
