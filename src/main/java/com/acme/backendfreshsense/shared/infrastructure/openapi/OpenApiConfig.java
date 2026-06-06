package com.acme.backendfreshsense.shared.infrastructure.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI freshSenseOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("FreshSense API")
                        .description("""
                                API REST de FreshSense — plataforma IoT de gestión de inventario alimenticio.

                                **Autenticación:** La mayoría de endpoints requieren un JWT válido.\s
                                Tras hacer login o registro, el token se almacena en una cookie HttpOnly `authToken` \
                                (enviada automáticamente por el navegador).

                                Para probar desde Swagger UI: haz login, copia el valor del campo `token` \
                                de la respuesta y pégalo en el botón **Authorize** (esquema Bearer).
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Freshlabs")
                                .email("contact@freshlabs.pe"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT obtenido en POST /api/accounts/login o /api/accounts/register. " +
                                             "En producción viaja como cookie HttpOnly; en Swagger UI ingresa aquí el valor del campo `token`.")));
    }
}
