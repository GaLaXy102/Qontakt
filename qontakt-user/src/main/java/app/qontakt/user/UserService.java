package app.qontakt.user;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Handle all transactions concerning Users
 */
@Component
public class UserService {
    private final VisitRepository visitRepository;

    public UserService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    /**
     * Check if User with the given UID has unterminated Visits
     *
     * @param user_uid UID of the User
     * @return true if User has unterminated Visits
     */
    private boolean hasOpenVisit(String user_uid) {
        return !this.visitRepository.findAllByUserUidAndEndIsNull(user_uid).isEmpty();
    }

    /**
     * Create a new Visit for the given User and Lokal
     *
     * @param user_uid  UID of the User
     * @param local_uid UID of the Lokal
     * @param time      current time as start of Visit
     * @return true if creation of Visit is successful and User has no unterminated Visits
     */
    public boolean saveVisit(String user_uid, String local_uid, LocalDateTime time) {
        //to be dealt with in Frontend
        if (hasOpenVisit(user_uid)) {
            return false;
        }
        try {
            visitRepository.save(new Visit(user_uid, local_uid, time));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get all Visits for the given User
     *
     * @param user_uid UID of the User
     * @return List of Visits if the User already has some
     */
    public List<Visit> getVisits(String user_uid) {
        return visitRepository.findAllByUserUid(user_uid).toList();
    }


}
