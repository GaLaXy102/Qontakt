package app.qontakt.host;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class QontaktHostApplication {

    public static void main(String[] args) {
        SpringApplication.run(QontaktHostApplication.class, args);
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
                )
                .addServersItem(new Server().url("https://localhost"))
                .addServersItem(new Server().url("https://staging.qontakt.me"))
                .info(new Info()
                        .title("Qontakt Host Service")
                        .contact(new Contact()
                                .name("Galaxy102")
                                .email("services@galaxion.de")
                                .url("https://www.galaxy102.de"))
                        .license(new License().name("MIT Licence")))
                ;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
