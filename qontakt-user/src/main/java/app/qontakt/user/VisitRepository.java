package app.qontakt.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository to save data of Visits
 */
@Repository
public interface VisitRepository extends CrudRepository<Visit, String> {
    /**
     * Find all Visits with a given User
     *
     * @param user_uid UID of the User
     * @return Streamable of all Visits belonging to the given User
     */
    Streamable<Visit> findAllByUserUid(String user_uid);

    /**
     * Get a single Visit
     * @param visit_uid UID of the Visit
     * @return Optional of eventually found Visit
     */
    Optional<Visit> findByVisitUid(String visit_uid);

    /**
     * Find an unterminated Visit for a given User
     *
     * @param user_uid UID of the User
     * @return unterminated Visit belonging to the given User
     */
    Optional<Visit> findByUserUidAndCheckOutIsNull(String user_uid);

    /**
     * Find all Visits for a given Lokal
     *
     * @param lokal_uid UID of the Lokal
     * @return Streamable of all Visits belonging to the given Lokal
     */
    Streamable<Visit> findAllByLokalUid(String lokal_uid);
}
