package app.qontakt.crypto;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QontaktCryptoApplication {

    public static void main(String[] args) {
        SpringApplication.run(QontaktCryptoApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("https://localhost"))
                .addServersItem(new Server().url("https://staging.qontakt.me"))
                .info(new Info()
                        .title("Qontakt Crypto Service")
                        .contact(new Contact()
                                .name("Galaxy102")
                                .email("services@galaxion.de")
                                .url("https://www.galaxy102.de"))
                        .license(new License().name("MIT Licence")))
                ;
    }
}
