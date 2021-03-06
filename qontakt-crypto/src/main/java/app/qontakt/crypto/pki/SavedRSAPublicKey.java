package app.qontakt.crypto.pki;

import app.qontakt.crypto.RSACryptoService;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.UUID;

@Entity
public class SavedRSAPublicKey {

    @Id
    private final String id;
    private boolean enabled;
    private final String name;
    @Column(length = 1270)
    private final String modulus;           // Save as string to prevent precision issues
    @Column(length = 1270)
    private final String publicExponent;    // Save as string to prevent precision issues

    /**
     * Savable entity containing all information to reconstruct a RSA Public Key
     * @param modulus Modulus of Public Key
     * @param publicExponent Public Exponent of Public Key
     * @param name Friendly name of key
     */
    public SavedRSAPublicKey(BigInteger modulus, BigInteger publicExponent, String name) {
        this.id = UUID.randomUUID().toString();
        this.enabled = true;
        this.modulus = modulus.toString();
        this.publicExponent = publicExponent.toString();
        this.name = name;
    }

    /**
     * Savable entity containing all information to reconstruct a RSA Public Key
     * @param publicKey Public Key
     * @param name Friendly name of key
     */
    public SavedRSAPublicKey(RSAPublicKey publicKey, String name) {
        this.id = UUID.randomUUID().toString();
        this.enabled = true;
        this.modulus = publicKey.getModulus().toString();
        this.publicExponent = publicKey.getPublicExponent().toString();
        this.name = name;
    }

    protected SavedRSAPublicKey() {
        this.id = null;
        this.modulus = null;
        this.publicExponent = null;
        this.name = null;
    }

    public PublicKey toPubKey() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            BigInteger modulus = new BigInteger(this.modulus);
            BigInteger publicExponent = new BigInteger(this.publicExponent);
            return keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LoggerFactory.getLogger(RSACryptoService.class).error("Something is wrong with your JRE. There is no RSA " +
                    "support.");
            throw new RuntimeException();
        }
    }

    public void disable() {
        this.enabled = false;
    }

    /**
     * Getter for SavedRSAPublicKey's id
     *
     * @return id as java.lang.String
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for SavedRSAPublicKey's enabled
     *
     * @return enabled as boolean
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Getter for SavedRSAPublicKey's name
     *
     * @return name as java.lang.String
     */
    public String getName() {
        return this.name;
    }
}
