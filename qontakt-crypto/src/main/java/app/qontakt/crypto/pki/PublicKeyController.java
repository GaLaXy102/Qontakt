package app.qontakt.crypto.pki;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/api/v1/crypto/pki")
public class PublicKeyController {
    private final PublicKeyService publicKeyService;

    public PublicKeyController(PublicKeyService publicKeyService) {
        this.publicKeyService = publicKeyService;
    }

    public static class PublicKeyDataPublic implements Serializable {

        public final String id;
        public final String name;

        PublicKeyDataPublic(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Operation(summary = "Encrypt a file using AES over RSA")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Map of all Key UUID with their friendly Name")
    })
    @GetMapping("/keys")
    public ResponseEntity<List<PublicKeyDataPublic>> getAllPublicKeys() {
        return ResponseEntity.ok(this.publicKeyService.getRsaKeysWithFriendlyNamesById());
    }
}
