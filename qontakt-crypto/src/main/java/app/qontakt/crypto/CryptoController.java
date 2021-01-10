package app.qontakt.crypto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/crypto")
public class CryptoController {

    private final RSACryptoService cryptoService;

    public CryptoController(RSACryptoService cryptoService) {
        this.cryptoService = cryptoService;
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
            RSAPublicKey publicKey = this.cryptoService.readPublicKey(publicKeyPem.getInputStream());
            if (publicKey.getModulus().bitLength() < 4096) {
                // Key too short
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
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
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
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
            RSAPrivateKey privateKey = this.cryptoService.readPrivateKey(
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
