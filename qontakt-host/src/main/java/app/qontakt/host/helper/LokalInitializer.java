package app.qontakt.host.helper;

import app.qontakt.host.lokal.LokalData;
import app.qontakt.host.lokal.LokalDataRepository;
import app.qontakt.host.lokal.LokalPassword;
import app.qontakt.host.lokal.LokalPasswordRepository;
import app.qontakt.host.rules.FederalStateRuleSet;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.UUID;

@Component
public class LokalInitializer {

    private final LokalDataRepository lokalDataRepository;
    private final LokalPasswordRepository lokalPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    public static UUID NULL_UUID = new UUID(0, 0);

    public LokalInitializer(LokalDataRepository lokalDataRepository, LokalPasswordRepository lokalPasswordRepository,
                            PasswordEncoder passwordEncoder) {
        this.lokalDataRepository = lokalDataRepository;
        this.lokalPasswordRepository = lokalPasswordRepository;
        this.passwordEncoder = passwordEncoder;
        if (this.lokalDataRepository.count() == 0) {
            this.initialize();
        }
    }

    /**
     * Add a sample Lokal to Database
     */
    @Transactional
    void initialize() {
        UUID uuid = UUID.randomUUID();
        LokalData lokal = new LokalData(
                uuid.toString(),
                "Sample Lokal",
                "Sample Address",
                new Point(0, 0),
                NULL_UUID.toString(),
                "qdpr@qontakt,me",
                LocalTime.MIDNIGHT,
                new FederalStateRuleSet.Code("DEU", "SN")
        );
        // this.lokalDataRepository.save(lokal); This is done implicitly ↓
        this.lokalPasswordRepository.save(new LokalPassword(lokal, passwordEncoder.encode(NULL_UUID.toString())));
        LoggerFactory.getLogger(LokalInitializer.class).info("Added sample Lokal " + uuid.toString());
    }
}
