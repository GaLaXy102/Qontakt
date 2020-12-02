package app.qontakt.host.lokal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

/**
 * Repository to save data of Lokals
 */
@Repository
public interface LokalDataRepository extends CrudRepository<LokalData, String> {

    /**
     * Find all Lokals with a given owner
     *
     * @param owner user id of owner, external
     * @return Streamable of all found Lokals belonging to the given owner
     */
    Streamable<LokalData> findAllByOwner(String owner);

}
