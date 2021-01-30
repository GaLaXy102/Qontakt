package app.qontakt.crypto.pki;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Load PublicKeys into Database, don't touch if there is any existing data
 */
@Component
public class PublicKeyLoader {

    private final RsaPublicKeyRepository rsaPublicKeyRepository;

    public PublicKeyLoader(RsaPublicKeyRepository rsaPublicKeyRepository) {
        this.rsaPublicKeyRepository = rsaPublicKeyRepository;
        this.run();
    }

    @Transactional
    void run() {
        if (this.rsaPublicKeyRepository.count() > 0) {
            LoggerFactory.getLogger(PublicKeyLoader.class).info("Apparently I'm in production. Thus I will not load " +
                    "any keys");
        } else {
            LoggerFactory.getLogger(PublicKeyLoader.class).info("Loading keys from Built-in Data Source.");
            this.rsaPublicKeyRepository.saveAll(PublicKeyData.allRsaPublicKeys);
        }
        LoggerFactory.getLogger(PublicKeyLoader.class).info(String.format("I currently know %d keys.",
                this.rsaPublicKeyRepository.count()));
    }
}
