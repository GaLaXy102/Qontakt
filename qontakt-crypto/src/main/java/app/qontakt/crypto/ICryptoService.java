package app.qontakt.crypto;

import org.apache.commons.lang3.tuple.Pair;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface ICryptoService {

    /**
     * Read the private key from a .pem file
     * @param pemFile Inputstream of .pem file
     * @param password Passphrase for private key
     * @return read private key
     */
    PrivateKey readPrivateKey(InputStream pemFile, String password);

    /**
     * Read the public key from a .pem file
     * @param pemFile Inputstream of .pem file
     * @return read public key
     */
    PublicKey readPublicKey(InputStream pemFile);

    /**
     * Encrypt data
     * @param inData data to encrypt
     * @param publicKey public key to use
     * @return encrypted AES key and AES encrypted data
     */
    Pair<byte[], byte[]> encrypt(byte[] inData, PublicKey publicKey);

    /**
     * Decrypt data
     * @param inData data to decrypt
     * @param aesKeyEnc AES Key encrypted with private key
     * @param privateKey private key to use
     * @return decrypted data
     */
    byte[] decrypt(byte[] inData, byte[] aesKeyEnc, PrivateKey privateKey);
}
