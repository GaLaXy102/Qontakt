package app.qontakt.host.lokal;

import app.qontakt.host.helper.CsvExporter;
import app.qontakt.host.helper.LokalDataPublic;
import app.qontakt.host.helper.ThymeleafPdfPrinter;
import app.qontakt.host.rules.FederalStateRuleSetService;
import app.qontakt.user.VerificationQrCodeData;
import app.qontakt.user.Visit;
import app.qontakt.user.identity.QUserData;
import com.lowagie.text.DocumentException;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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

    /**
     * Verfication timeout for verifyVisit in seconds
     */
    public static final int visitVerificationTimeout = 30;
    /**
     * Timeout in seconds for which a visit is considered valid
     */
    public static final int visitTimeout = 60 * 60 * 24;

    private final LokalDataRepository lokalDataRepository;
    private final LokalPasswordRepository lokalPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final FederalStateRuleSetService federalStateRuleSetService;
    private final Map<String, LocalDateTime> lastPerformedAutoCheckout;
    private final Map<String, LocalDateTime> lastPerformedAutoDelete;

    /**
     * Initialize Component to handle all transactions concerning Lokals
     *
     * @param lokalDataRepository        Autowired JPA repository
     * @param lokalPasswordRepository    Autowired JPA repository
     * @param passwordEncoder            Autowired PasswordEncoder
     * @param federalStateRuleSetService Autowired FederalStateRuleSetService
     */
    public LokalService(LokalDataRepository lokalDataRepository, LokalPasswordRepository lokalPasswordRepository,
                        PasswordEncoder passwordEncoder, FederalStateRuleSetService federalStateRuleSetService) {
        this.lokalDataRepository = lokalDataRepository;
        this.lokalPasswordRepository = lokalPasswordRepository;
        this.passwordEncoder = passwordEncoder;
        this.federalStateRuleSetService = federalStateRuleSetService;
        this.lastPerformedAutoCheckout = new HashMap<>();
        this.lastPerformedAutoDelete = new HashMap<>();
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
     * @param password Lokal Password
     * @return true if and only if the user is authorized to administer the Lokal
     */
    public boolean isAuthorized(String userUid, String lokalUid, Optional<String> password) {
        Optional<LokalData> foundLokal = this.lokalDataRepository.findById(lokalUid);
        if (foundLokal.isEmpty()) {
            // This Lokal does not exist.
            return false;
        }
        if (password.isEmpty()) {
            return foundLokal.get().getOwner().equals(userUid);
        }
        Optional<LokalPassword> foundPassword = this.lokalPasswordRepository.findByLokal(foundLokal.get());
        if (foundPassword.isEmpty()) {
            // Lokal exists but has no password.
            throw new IllegalStateException("This Lokal has no password!");
        }
        return foundLokal.get().getOwner().equals(userUid)
                && this.passwordEncoder.matches(password.get(), foundPassword.get().getHashedPassword());
    }

    /**
     * Find all Lokals or a specific one with the possiblity to restrict to a given user, yielding more information
     *
     * @param userUid  userUid of Owner
     * @param lokalUid lokalUid of Lokal
     * @return List of all Lokals, with missing checkoutTime and owner information when requesting all Lokals
     */
    public List<? extends LokalData> findAll(Optional<String> userUid, Optional<String> lokalUid) {
        if (userUid.isEmpty()) {
            return this.lokalDataRepository.findAll()
                    .filter(lokalData -> lokalUid.isEmpty() || lokalData.getLokalUid().equals(lokalUid.get()))
                    .map(LokalDataPublic::new)
                    .toList();
        } else {
            return this.lokalDataRepository.findAllByOwner(userUid.get())
                    .filter(lokalData -> lokalUid.isEmpty() || lokalData.getLokalUid().equals(lokalUid.get()))
                    .toList();
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
     * Get the visit data associated with a given visitUid from our user service
     *
     * @param visitUid visitUid to get information of
     * @param userUid  userUid of visitUid
     * @return Dataset of found Visit
     */
    private Visit getVisitData(String userUid, String visitUid) {
        WebClient client = WebClient.create("http://q-user-service:8080");
        String remote = "/api/v1/user/visit";
        return client.get()
                .uri(uriBuilder -> uriBuilder.path(remote).queryParam("user_uid", userUid).queryParam("visitUid", visitUid).build())
                .accept(MediaType.APPLICATION_JSON)
                .header("X-User", userUid)
                .retrieve()
                .bodyToFlux(Visit.class)
                .blockFirst();
    }

    /**
     * Print a Leaflet to promote the usage of Qontakt
     *
     * @param locale   Locale of Document to generate
     * @param lokalUid Uid of Lokal for which the leaflet is
     * @param baseurl  Base URL of this Qontakt instance, e.g. https://qontakt.me/
     * @return PDF document
     */
    public byte[] printLeaflet(Locale locale, String lokalUid, String baseurl) {
        LokalData lokalData = this.lokalDataRepository.findById(lokalUid).orElseThrow(() -> new IllegalArgumentException("No such Lokal"));
        try {
            return ThymeleafPdfPrinter.renderLeaflet(locale, lokalData, baseurl);
        } catch (IOException e) {
            return new ByteArrayOutputStream().toByteArray();
        }
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
        LokalData lokalData = this.lokalDataRepository.findById(lokalUid).orElseThrow(() -> new IllegalArgumentException("No such Lokal"));
        // Map to User-specific data
        Map<QUserData, List<Visit>> filledDataset = this.prepareExportData(lokalUid, visits);
        try {
            return ThymeleafPdfPrinter.renderContactTracingPdf(locale, lokalData, filledDataset);
        } catch (DocumentException e) {
            return new ByteArrayOutputStream().toByteArray();
        }
    }

    /**
     * Get Visits at Lokal and print Data to PDF
     *
     * @param locale   Locale of Document to generate
     * @param lokalUid Uid of Lokal for which the report is
     * @return PDF document
     */
    public byte[] print(Locale locale, String lokalUid) {
        List<Visit> visits = getVisitFlux(lokalUid)
                .collectList()
                .block();
        return this.print(locale, lokalUid, visits);
    }

    private Flux<Visit> getVisitFlux(String lokalUid) {
        WebClient client = WebClient.create("http://q-user-service:8080");
        String remote = "/api/v1/user/visit/" + lokalUid;
        return client.get()
                .uri(uriBuilder -> uriBuilder.path(remote).build())
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Lokal", lokalUid)
                .retrieve()
                .bodyToFlux(Visit.class);
    }

    /**
     * Perform encryption with q-crypto-service
     *
     * @param data         data to encrypt
     * @param filename     filename of data to encrypt
     * @param publicKeyUid Uid of key to use
     * @return encrypted data and corresponding filename
     */
    public Pair<byte[], String> encryptWithKnownKey(byte[] data, String filename, String publicKeyUid) {
        WebClient client = WebClient.create("http://q-crypto-service:8080");
        String remote = "/api/v1/crypto/encrypt/" + publicKeyUid;
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("data", data).filename(filename);
        Mono<ByteArrayResource> buffer = client.post()
                .uri(uriBuilder -> uriBuilder.path(remote).build())
                .syncBody(builder.build())
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(ByteArrayResource.class);
        return Pair.of(buffer.block().getByteArray(), filename + ".qenc");
    }

    /**
     * Export Data to CSV
     *
     * @param lokalUid Uid of Lokal for which the report is
     * @param visits   List of Visit data
     * @return CSV document
     */
    public byte[] export(String lokalUid, List<Visit> visits) {
        LokalData lokalData = this.lokalDataRepository.findById(lokalUid).orElseThrow(() -> new IllegalArgumentException("No such Lokal"));
        // Map to User-specific data
        Map<QUserData, List<Visit>> filledDataset = this.prepareExportData(lokalUid, visits);
        return CsvExporter.exportToCsv(lokalData, filledDataset);
    }

    /**
     * Map data from a list of Visits to a Map of User to Visits
     *
     * @param lokalUid lokalUid (used for filtering as security mechanism)
     * @param visits   List of Visits to export
     * @return prepared Data
     */
    private Map<QUserData, List<Visit>> prepareExportData(String lokalUid, List<Visit> visits) {
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
        return filledDataset;
    }

    /**
     * A verification is issued if
     * - the Timestamp is not older than 30 seconds
     * - the Visit is not older than 24 hours
     * - the Visit matches the Lokal
     *
     * @param lokalUid Uid of Lokal
     * @param data     Data to verify
     * @return true if and only if all all above conditions are met
     */
    public boolean verifyVisit(String lokalUid, VerificationQrCodeData data) {
        Visit visit;
        try {
            visit = this.getVisitData(data.getUserUid(), data.getVisitUid());
        } catch (Exception e) {
            return false;
        }
        Instant checkIn = visit.getCheckIn().atZone(ZoneId.systemDefault()).toInstant();
        System.out.println();
        return data.getCreated().isBefore(Instant.now()) && data.getCreated().isAfter(Instant.now().minusSeconds(visitVerificationTimeout))
                && checkIn.isBefore(Instant.now()) && checkIn.isAfter(Instant.now().minusSeconds(visitTimeout))
                && visit.getLokalUid().equals(lokalUid);
    }

    void deleteSingleVisit(Visit visit) {
        LoggerFactory.getLogger(LokalService.class).info("Deleting Visit " + visit.getVisitUid());
        WebClient client = WebClient.create("http://q-user-service:8080");
        String remote = "/api/v1/user/visit";
        client.delete()
                .uri(uriBuilder -> uriBuilder.path(remote)
                        .queryParam("user_uid", visit.getUserUid())
                        .queryParam("visit_uid", visit.getVisitUid())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .header("X-User", visit.getUserUid())
                .header("X-Lokal", visit.getLokalUid())
                .retrieve()
                .bodyToMono(Boolean.class)
                .subscribe(success -> {
                    LoggerFactory.getLogger(LokalService.class).info("Delete " + visit.getVisitUid() + " " + (success ? "successful" : "failed"));
                });
    }

    /**
     * Delete all visits for all Lokals which are not permitted to be kept by federal state retention policy
     */
    public void performDeleteVisitsByPolicy() {
        LocalDateTime now = LocalDateTime.now();
        this.lokalDataRepository.findAll().stream().parallel()
                .map(lokalData ->
                        Pair.of(lokalData.getLokalUid(),
                                this.federalStateRuleSetService.getByCode(lokalData.getFederalState()).getRetainPeriod()))
                .peek(pair -> {
                    LoggerFactory.getLogger(LokalService.class).info("AutoDelete for Lokal " + pair.getFirst());
                })
                .map(pair -> Pair.of(pair.getSecond(), this.getVisitFlux(pair.getFirst())))
                .map(pair -> pair.getSecond().filter(visit -> visit.getCheckIn().plusDays(pair.getFirst().getDays()).isBefore(now)))
                .forEach(visitFlux -> visitFlux.subscribe(this::deleteSingleVisit));
    }

    void checkoutSingleVisit(Visit visit) {
        LoggerFactory.getLogger(LokalService.class).info("Checking Out Visit " + visit.getVisitUid());
        WebClient client = WebClient.create("http://q-user-service:8080");
        String remote = "/api/v1/user/visit";
        client.put()
                .uri(uriBuilder -> uriBuilder.path(remote)
                        .queryParam("user_uid", visit.getUserUid())
                        .queryParam("visit_uid", visit.getVisitUid())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .header("X-User", visit.getUserUid())
                .retrieve()
                .bodyToMono(Boolean.class)
                .subscribe(success -> {
                    LoggerFactory.getLogger(LokalService.class).info("Checkout " + visit.getVisitUid() + " " + (success ? "successful" : "failed"));
                });
    }

    /**
     * Checkout all unclosed visits for all Lokals
     */
    public void performCheckoutVisitsByPolicy() {
        LocalDateTime midnight = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime now = LocalDateTime.now();
        this.lokalDataRepository.findAll().stream().parallel()
                // remove lokals that have had their auto-checkout today
                .filter(lokalData ->
                        !(lastPerformedAutoCheckout.containsKey(lokalData.getLokalUid())
                                && lastPerformedAutoCheckout.get(lokalData.getLokalUid()).isAfter(midnight)))
                .map(lokalData ->
                        Pair.of(lokalData.getLokalUid(),
                                lokalData.getCheckoutTime()))
                // remove lokals that have their auto-checkout later today
                .filter(pair -> pair.getSecond().isBefore(LocalTime.from(now)))
                .peek(pair -> {
                    LoggerFactory.getLogger(LokalService.class).info("AutoCheckout for Lokal " + pair.getFirst());
                    this.lastPerformedAutoCheckout.put(pair.getFirst(), now);
                })
                .map(pair -> Pair.of(pair.getSecond(), this.getVisitFlux(pair.getFirst())))
                .map(pair -> pair.getSecond().filter(visit -> visit.getCheckOut() == null))
                .forEach(visitFlux -> visitFlux.subscribe(this::checkoutSingleVisit));
    }
}
