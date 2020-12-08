package app.qontakt.host.lokal;

import app.qontakt.host.uihelper.LokalDataPublic;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        if (data.getLocal_uid() == null) {
            data = new LokalData(data, true);
        }
        if (this.lokalDataRepository.existsById(data.getLocal_uid())) {
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
     * @param user_uid  User UID
     * @param lokal_uid Lokal UID
     * @param password  Lokal Password
     * @return true if and only if the user is authorized to administer the Lokal
     */
    public boolean isAuthorized(String user_uid, String lokal_uid, String password) {
        Optional<LokalData> foundLokal = this.lokalDataRepository.findById(lokal_uid);
        if (foundLokal.isEmpty()) {
            // This Lokal does not exist.
            return false;
        }
        Optional<LokalPassword> foundPassword = this.lokalPasswordRepository.findByLokal(foundLokal.get());
        if (foundPassword.isEmpty()) {
            // Lokal exists but has no password.
            throw new IllegalStateException("This Lokal has no password!");
        }
        return foundLokal.get().getOwner().equals(user_uid)
                && this.passwordEncoder.matches(password, foundPassword.get().getHashedPassword());
    }

    /**
     * Find all Lokals with the possiblity to restrict to a given user, yielding more information
     * @param user_uid user_uid of Owner
     * @return List of all Lokals, with missing checkoutTime and owner information when requesting all Lokals
     */
    public List<? extends LokalData> findAll(Optional<String> user_uid) {
        if (user_uid.isEmpty()) {
            return this.lokalDataRepository.findAll().map(LokalDataPublic::new).toList();
        } else {
            return this.lokalDataRepository.findAllByOwner(user_uid.get()).toList();
        }
    }
}
