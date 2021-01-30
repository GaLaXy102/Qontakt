package app.qontakt.crypto;

import app.qontakt.crypto.pki.PublicKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/crypto")
public class CryptoController {

    private final RSACryptoService cryptoService;
    private final PublicKeyService publicKeyService;

    public CryptoController(RSACryptoService cryptoService, PublicKeyService publicKeyService) {
        this.cryptoService = cryptoService;
        this.publicKeyService = publicKeyService;
    }

    @Operation(summary = "Encrypt a file using AES over RSA")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Invalid key data", content = @Content),
            @ApiResponse(responseCode = "403", description = "Key too short", content = @Content),
            @ApiResponse(responseCode = "400", description = "Malformed request", content = @Content),
            @ApiResponse(responseCode = "200", description = "Encryption successful. " +
                    "The first len(pkModulus) bytes are the encrypted AES Key, the rest is the encrypted file content.",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
    })
    @PostMapping(value = "/encrypt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> encrypt(@RequestPart("data") MultipartFile data, @RequestPart("key") MultipartFile publicKeyPem) {
        try {
            RSAPublicKey publicKey = RSACryptoService.readPublicKey(publicKeyPem.getInputStream());
            if (publicKey.getModulus().bitLength() < 4096) {
                // Key too short
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            return getEncryptedFileResponse(data, publicKey);
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @Operation(summary = "Encrypt a file using AES over RSA with saved RSA key")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Invalid key data or unknown key", content = @Content),
            @ApiResponse(responseCode = "400", description = "Malformed request", content = @Content),
            @ApiResponse(responseCode = "200", description = "Encryption successful. " +
                    "The first len(pkModulus) bytes are the encrypted AES Key, the rest is the encrypted file content.",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
    })
    @PostMapping(value = "/encrypt/{keyUid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> encrypt(@RequestPart("data") MultipartFile data, @PathVariable String keyUid) {
        PublicKey publicKey;
        try {
            publicKey = this.publicKeyService.getRsaKey(keyUid);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try {
            return getEncryptedFileResponse(data, publicKey);
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    private ResponseEntity<byte[]> getEncryptedFileResponse(MultipartFile data, PublicKey publicKey) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Pair<byte[], byte[]> encKeyAndData = this.cryptoService.encrypt(data.getBytes(), publicKey);
        out.writeBytes(encKeyAndData.getLeft());
        out.writeBytes(encKeyAndData.getRight());
        String inFileName = data.getOriginalFilename();
        String fileName =  inFileName == null ? "Qontakt-encrypted.dat" : inFileName + ".qenc";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return ResponseEntity.ok().headers(headers).body(out.toByteArray());
    }

    @Operation(summary = "Decrypt a file using AES over RSA")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Invalid or encrypted key data", content = @Content),
            @ApiResponse(responseCode = "400", description = "Malformed request", content = @Content),
            @ApiResponse(responseCode = "200", description = "Decryption successful.", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
    })
    @PostMapping(value = "/decrypt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> decrypt(
            @RequestPart("data") MultipartFile data,
            @RequestPart("key") MultipartFile privateKeyPem, @RequestParam Optional<String> passwordForPrivateKey) {
        try {
            RSAPrivateKey privateKey = RSACryptoService.readPrivateKey(
                    privateKeyPem.getInputStream(),
                    passwordForPrivateKey.orElse(null)
            );
            // We do not fail on Modulus length because to ensure any data can be decrypted, even though the key might
            // not be supported any longer, but was supported for encryption.
            int modLength = privateKey.getModulus().bitLength() / 8;
            ByteArrayInputStream inData = new ByteArrayInputStream(data.getBytes());
            byte[] aesKeyEnc = inData.readNBytes(modLength);
            byte[] encData = inData.readAllBytes();
            String inFileName = data.getOriginalFilename();
            String fileName = inFileName == null ? "Qontakt-decrypted.dat" : inFileName.replaceAll("\\.qenc$", "");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
            headers.setContentDispositionFormData(fileName, fileName);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return ResponseEntity.ok().headers(headers).body(this.cryptoService.decrypt(encData, aesKeyEnc, privateKey));

        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
