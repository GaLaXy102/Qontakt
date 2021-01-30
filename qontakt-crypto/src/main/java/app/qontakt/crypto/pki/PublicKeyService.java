package app.qontakt.crypto.pki;

import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Component
public class PublicKeyService {

    private final RsaPublicKeyRepository rsaPublicKeyRepository;

    public PublicKeyService(RsaPublicKeyRepository rsaPublicKeyRepository) {
        this.rsaPublicKeyRepository = rsaPublicKeyRepository;
    }

    /**
     * Get a RSA PubKey by its id
     *
     * @param id UUID of key
     * @return PublicKey
     * @throws IllegalArgumentException when there is no such key
     */
    public PublicKey getRsaKey(String id) {
        return this.rsaPublicKeyRepository.findById(id).map(SavedRSAPublicKey::toPubKey)
                .orElseThrow(() -> new IllegalArgumentException("No such key"));
    }

    /**
     * Get all enabled Public Keys
     *
     * @return List of found Keys
     */
    public List<SavedRSAPublicKey> getAllRsaKeys() {
        return this.rsaPublicKeyRepository.findAllByEnabledTrue().toList();
    }

    /**
     * Map all available Public Key IDs to their name
     *
     * @return List(PublicKeyController.PublicKeyDataPublic)
     */
    public List<PublicKeyController.PublicKeyDataPublic> getRsaKeysWithFriendlyNamesById() {
        return this.rsaPublicKeyRepository.findAllByEnabledTrue()
                .map(k -> new PublicKeyController.PublicKeyDataPublic(k.getId(), k.getName()))
                .toList();
    }

    /**
     * Add an RSA Key to Public Keys
     *
     * @param publicKey Public Key to add
     * @param name      friendly name of Public Key
     */
    @Transactional
    public void addRSAPublicKey(RSAPublicKey publicKey, String name) {
        this.rsaPublicKeyRepository.save(new SavedRSAPublicKey(publicKey, name));
    }

}
