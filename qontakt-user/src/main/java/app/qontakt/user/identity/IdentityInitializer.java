package app.qontakt.user.identity;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

@Component
public class IdentityInitializer {

    private final IdentityStorage identityStorage;

    public static UUID NULL_UUID = new UUID(0, 0);

    public IdentityInitializer(IdentityStorage identityStorage) {
        this.identityStorage = identityStorage;
        this.initialize();
    }

    /**
     * Add a sample Identity to Database
     */
    @Transactional
    void initialize() {
        this.identityStorage.save(
                new QUserData(
                        NULL_UUID.toString(),
                        "Sample",
                        "User",
                        "sample.user@qontakt.me",
                        "Sample Street, 42",
                        "1337",
                        "Sample City",
                        "+1 (234) 567-8901"
                )
        );
        LoggerFactory.getLogger(IdentityInitializer.class).info("Added sample identity");
    }
}
