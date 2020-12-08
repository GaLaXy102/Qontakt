package app.qontakt.host.lokal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository to save hashed passwords for Lokals
 */
@Repository
public interface LokalPasswordRepository extends CrudRepository<LokalPassword, LokalData> {
    Optional<LokalPassword> findByLokal(LokalData lokalData);
}
