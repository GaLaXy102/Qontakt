package app.qontakt.crypto;

import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

@Component
public class RSACryptoService {

    public RSACryptoService() {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Read the private RSA key from a .pem file
     * @param pemFile Inputstream of .pem file
     * @param password Passphrase for private key
     * @return read private key
     */
    public static RSAPrivateKey readPrivateKey(InputStream pemFile, String password) {
        PEMParser pemParser = new PEMParser(new InputStreamReader(pemFile));
        PrivateKeyInfo keyInfo;
        try {
            Object keyPair = pemParser.readObject();
            if (keyPair instanceof PEMEncryptedKeyPair) {
                if (password == null || password.isEmpty()) {
                    throw new SecurityException("No password provided");
                }
                keyInfo = ((PEMEncryptedKeyPair) keyPair).decryptKeyPair(
                        new JcePEMDecryptorProviderBuilder().build(password.toCharArray())
                ).getPrivateKeyInfo();
            } else {
                keyInfo = ((PEMKeyPair) keyPair).getPrivateKeyInfo();
            }
            return (RSAPrivateKey) new JcaPEMKeyConverter().getPrivateKey(keyInfo);
        } catch (IOException | ClassCastException e) {
            throw new SecurityException("Error reading key data");
        }
    }

    /**
     * Read the public RSA key from a .pem file
     * @param pemFile Inputstream of .pem file
     * @return read public key
     */
    public static RSAPublicKey readPublicKey(InputStream pemFile) {
        PEMParser pemParser = new PEMParser(new InputStreamReader(pemFile));
        try {
            return (RSAPublicKey) new JcaPEMKeyConverter().getPublicKey((SubjectPublicKeyInfo) pemParser.readObject());
        } catch (IOException | ClassCastException e) {
            throw new SecurityException("Error reading key data");
        }
    }

    /**
     * Encrypt data using AES and RSA
     * @param inData data to encrypt
     * @param publicKey public RSA key to use
     * @return RSA encrypted AES key and AES encrypted data
     */
    public Pair<byte[], byte[]> encrypt(byte[] inData, PublicKey publicKey) {
        try {
            SecretKey aesKey = KeyGenerator.getInstance("AES").generateKey();
            Cipher cipherForKey = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
            cipherForKey.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] aesKeyEncBytes = cipherForKey.doFinal(aesKey.getEncoded());
            Cipher cipherForText = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipherForText.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] cipherText = cipherForText.doFinal(inData);
            return Pair.of(aesKeyEncBytes, cipherText);

        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
            LoggerFactory.getLogger(RSACryptoService.class).error("Something is wrong with your JRE. There is no RSA support with BouncyCastle.");
            throw new RuntimeException();
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("Invalid public key.");
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalArgumentException("Error during encryption");
        }
    }

    /**
     * Decrypt data using RSA
     * @param inData data to decrypt
     * @param aesKeyEnc RSA encrypted AES Key
     * @param privateKey private key to use
     * @return decrypted data
     */
    public byte[] decrypt(byte[] inData, byte[] aesKeyEnc, PrivateKey privateKey) {
        try {
            Cipher cipherForKey = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
            cipherForKey.init(Cipher.DECRYPT_MODE, privateKey);
            SecretKey aesKey = new SecretKeySpec(cipherForKey.doFinal(aesKeyEnc), "AES");
            Cipher cipherForText = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipherForText.init(Cipher.DECRYPT_MODE, aesKey);
            return cipherForText.doFinal(inData);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
            LoggerFactory.getLogger(RSACryptoService.class).error("Something is wrong with your JRE. There is no RSA support with BouncyCastle.");
            throw new RuntimeException();
        } catch (InvalidKeyException e) {
            throw new SecurityException("Invalid private key.");
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalArgumentException("Error during encryption");
        }
    }
}
