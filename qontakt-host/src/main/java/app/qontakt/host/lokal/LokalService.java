package app.qontakt.host.lokal;

import app.qontakt.host.uihelper.LokalDataPublic;
import app.qontakt.host.uihelper.ThymeleafPdfPrinter;
import app.qontakt.user.Visit;
import app.qontakt.user.identity.QUserData;
import com.lowagie.text.DocumentException;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Handle all transactions concerning Lokals
 */
@Component
public class LokalService {

    private final LokalDataRepository lokalDataRepository;
    private final LokalPasswordRepository lokalPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Initialize Component to handle all transactions concerning Lokals
     *
     * @param lokalDataRepository     Autowired JPA repository
     * @param lokalPasswordRepository Autowired JPA repository
     * @param passwordEncoder         Autowired PasswordEncoder
     */
    public LokalService(LokalDataRepository lokalDataRepository, LokalPasswordRepository lokalPasswordRepository,
                        PasswordEncoder passwordEncoder) {
        this.lokalDataRepository = lokalDataRepository;
        this.lokalPasswordRepository = lokalPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a Lokal with the given data
     *
     * @param data dataset to save
     * @return Password if creation successful
     */
    @Transactional
    public String createLokal(LokalData data) {
        if (data.getLokalUid() == null) {
            data = new LokalData(data, true);
        }
        if (this.lokalDataRepository.existsById(data.getLokalUid())) {
            throw new IllegalArgumentException("Lokal exists already!");
        }
        String password = UUID.randomUUID().toString();
        // Only one save transaction must be done because of the 1-to-1 relationship
        this.lokalPasswordRepository.save(new LokalPassword(data, passwordEncoder.encode(password)));
        return password;
    }

    /**
     * Check whether a user with a given UID is entitled to administer a Lokal with a given UID
     *
     * @param userUid  User UID
     * @param lokalUid Lokal UID
     * @param password  Lokal Password
     * @return true if and only if the user is authorized to administer the Lokal
     */
    public boolean isAuthorized(String userUid, String lokalUid, String password) {
        Optional<LokalData> foundLokal = this.lokalDataRepository.findById(lokalUid);
        if (foundLokal.isEmpty()) {
            // This Lokal does not exist.
            return false;
        }
        Optional<LokalPassword> foundPassword = this.lokalPasswordRepository.findByLokal(foundLokal.get());
        if (foundPassword.isEmpty()) {
            // Lokal exists but has no password.
            throw new IllegalStateException("This Lokal has no password!");
        }
        return foundLokal.get().getOwner().equals(userUid)
                && this.passwordEncoder.matches(password, foundPassword.get().getHashedPassword());
    }

    /**
     * Find all Lokals with the possiblity to restrict to a given user, yielding more information
     *
     * @param userUid userUid of Owner
     * @return List of all Lokals, with missing checkoutTime and owner information when requesting all Lokals
     */
    public List<? extends LokalData> findAll(Optional<String> userUid) {
        if (userUid.isEmpty()) {
            return this.lokalDataRepository.findAll().map(LokalDataPublic::new).toList();
        } else {
            return this.lokalDataRepository.findAllByOwner(userUid.get()).toList();
        }
    }

    /**
     * Get the identity data associated with a given userUid from our identity service
     *
     * @param userUid userUid to get information of
     * @return Dataset of found user
     */
    private QUserData getUserData(String userUid) {
        WebClient client = WebClient.create("http://q-user-service:8080");
        String remote = "/api/v1/user/identity";
        return client.get()
                .uri(uriBuilder -> uriBuilder.path(remote).queryParam("userUid", userUid).build())
                .accept(MediaType.APPLICATION_JSON)
                .header("X-User", userUid)  // Spoofing User Header to allow access to identity data
                .retrieve()
                .bodyToMono(QUserData.class)
                .block();

    }

    /**
     * Print Data to PDF
     *
     * @param locale   Locale of Document to generate
     * @param lokalUid Uid of Lokal for which the report is
     * @param visits   List of Visit data
     * @return PDF document
     */
    public byte[] print(Locale locale, String lokalUid, List<Visit> visits) {
        // Existence already checked
        LokalData lokalData = this.lokalDataRepository.findById(lokalUid).orElse(null);
        // Map to User-specific data
        Map<QUserData, List<Visit>> filledDataset = new HashMap<>();
        visits.stream()
                .filter(visit -> lokalUid.equals(visit.getLokalUid()))
                .forEach(v -> {
                            QUserData userData = this.getUserData(v.getUserUid());
                            List<Visit> visitList = filledDataset.getOrDefault(userData, new LinkedList<>());
                            visitList.add(v);
                            filledDataset.put(userData, visitList);
                        }
                );
        try {
            return ThymeleafPdfPrinter.renderContactTracingPdf(locale, lokalData, filledDataset);
        } catch (DocumentException e) {
            return new ByteArrayOutputStream().toByteArray();
        }
    }
}
