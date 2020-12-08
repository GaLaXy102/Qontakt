package app.qontakt.user.identity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityStorage extends CrudRepository<QUserData, String> {

    Optional<QUserData> findByUserUid(String userUid);

}
