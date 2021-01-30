package app.qontakt.user;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QontaktUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(QontaktUserApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("user-header",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-User")
                        )
                        .addSecuritySchemes("lokal-header",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-Lokal")
                        )
                )
                .addServersItem(new Server().url("https://staging.qontakt.me"))
                .addServersItem(new Server().url("https://staging.localhost"))
                .info(new Info()
                        .title("Qontakt User Service")
                        .contact(new Contact()
                                .name("Ylvion")
                                .email("services@galaxion.de"))
                        .license(new License().name("MIT Licence"))
                );
    }

}
