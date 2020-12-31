package app.qontakt.user;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class VisitInitializer {

    private final VisitRepository visitRepository;

    public static UUID NULL_UUID = new UUID(0, 0);

    public VisitInitializer(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
        this.initialize();
    }

    /**
     * Add a sample Visit to Database
     */
    @Transactional
    void initialize() {
        Visit visit = new Visit(
                NULL_UUID.toString(),
                NULL_UUID.toString(),
                NULL_UUID.toString(),
                LocalDateTime.now().minusMinutes(35)
        );
        visit.setCheckOut(LocalDateTime.now());
        this.visitRepository.save(visit);
        LoggerFactory.getLogger(VisitInitializer.class).info("Added sample visit");
    }
}
