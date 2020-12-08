package app.qontakt.user.identity;

import app.qontakt.user.VisitRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Handle all Transactions concerning Identities (except login, logout)
 */
@Component
public class IdentityService {

    private final IdentityStorage identityStorage;
    private final VisitRepository visitRepository;

    public static boolean isValid(QUserData data) {
        // Validation to follow later
        return true;
    }

    public IdentityService(IdentityStorage identityStorage, VisitRepository visitRepository) {
        this.identityStorage = identityStorage;
        this.visitRepository = visitRepository;
    }

    /**
     * Update a user's Identity
     * @param data User Identity (QUserData)
     * @return true if update successful, false otherwise
     */
    @Transactional
    public boolean update(QUserData data) {
        if (!IdentityService.isValid(data)) {
            throw new IllegalArgumentException("Invalid data.");
        }
        if (this.identityStorage.findByUserUid(data.getUserUid()).isEmpty()) {
            return false;
        }
        this.identityStorage.save(data);
        return true;
    }

    /**
     * Create a user's Identity
     * @param data User Identity (QUserData)
     * @return UUID of created User
     */
    @Transactional
    public String create(QUserData data) {
        if (data.getUserUid() == null) {
            data = new QUserData(data, true);
        }
        if (!IdentityService.isValid(data)) {
            throw new IllegalArgumentException("Invalid data.");
        }
        if (this.identityStorage.existsById(data.getUserUid())) {
            throw new IllegalStateException("User exists already.");
        }
        this.identityStorage.save(data);
        return data.getUserUid();
    }

    /**
     * Get Identity from User UID
     * @param userUid User UID
     * @return Identity of User with given UID
     */
    public QUserData get(String userUid) {
        return this.identityStorage.findByUserUid(userUid).orElse(null);
    }

    /**
     * Delete Identity for User with given UID
     * @param userUid User UID
     * @return true if delete successful; false if there is no Identity for the given UID
     * @throws IllegalStateException There are non-expired visits with the given data
     */
    public boolean delete(String userUid) {
        if (!this.visitRepository.findAllByUserUid(userUid).isEmpty()) {
            throw new IllegalStateException("There are non-expired visits.");
        }
        if (this.identityStorage.findByUserUid(userUid).isEmpty()) {
            return false;
        }
        this.identityStorage.deleteById(userUid);
        return true;
    }
}
