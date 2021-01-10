package app.qontakt.crypto;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;

public class RSACryptoServiceTests {

    private RSACryptoService testable;

    @BeforeEach
    void setUp() {
        testable = new RSACryptoService();
    }

    @Test
    void testPasswordlessIsIdentity() {
        byte[] data = new byte[1048576];
        new Random().nextBytes(data);
        PrivateKey privateKey = testable.readPrivateKey(new ByteArrayInputStream(RSACryptoServiceTestData.privateKeyString.getBytes()), null);
        PublicKey publicKey = testable.readPublicKey(new ByteArrayInputStream(RSACryptoServiceTestData.publicKeyString.getBytes()));
        Pair<byte[], byte[]> aesKeyEncAndCipherText = testable.encrypt(data, publicKey);
        byte[] decrypted = testable.decrypt(aesKeyEncAndCipherText.getRight(), aesKeyEncAndCipherText.getLeft(), privateKey);
        Assertions.assertArrayEquals(data, decrypted);
    }

    @Test
    void testPasswordIsIdentity() {
        byte[] data = new byte[1048576];
        new Random().nextBytes(data);
        PrivateKey privateKey = testable.readPrivateKey(new ByteArrayInputStream(RSACryptoServiceTestData.encryptedPrivateKeyString.getBytes()),
                RSACryptoServiceTestData.passphrase);
        PublicKey publicKey = testable.readPublicKey(new ByteArrayInputStream(RSACryptoServiceTestData.encryptedPublicKeyString.getBytes()));
        Pair<byte[], byte[]> aesKeyEncAndCipherText = testable.encrypt(data, publicKey);
        byte[] decrypted = testable.decrypt(aesKeyEncAndCipherText.getRight(), aesKeyEncAndCipherText.getLeft(), privateKey);
        Assertions.assertArrayEquals(data, decrypted);
    }

    @Test
    void testWrongPassword() {
        Assertions.assertThrows(SecurityException.class,
                () -> testable.readPrivateKey(new ByteArrayInputStream(RSACryptoServiceTestData.encryptedPrivateKeyString.getBytes()),
                RSACryptoServiceTestData.passphrase + "la"));
    }
}
