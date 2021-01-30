package app.qontakt.crypto.pki;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RsaPublicKeyRepository extends CrudRepository<SavedRSAPublicKey, String> {
    Streamable<SavedRSAPublicKey> findAllByEnabledTrue();
    Optional<SavedRSAPublicKey> findById(String id);
}
