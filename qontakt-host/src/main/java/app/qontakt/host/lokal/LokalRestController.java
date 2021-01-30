package app.qontakt.host.lokal;

import app.qontakt.user.VerificationQrCodeData;
import app.qontakt.user.Visit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Lokal administration
 */
@RestController
@RequestMapping("/api/v1/host")
public class LokalRestController {

    private final LokalService lokalService;

    public LokalRestController(LokalService lokalService) {
        this.lokalService = lokalService;
    }

    public static boolean isAuthorized(HttpServletRequest request, String userUid) {
        return userUid.equals(request.getHeader("X-User"));
    }

    public static boolean isAuthorized(HttpServletRequest request) {
        return request.getHeader("X-User") != null;
    }

    public static String calculateBaseUrl(HttpServletRequest request) {
        String scheme = request.getHeader("X-Forwarded-Proto") != null
                ? request.getHeader("X-Forwarded-Proto") : request.getScheme();
        String hostname = request.getHeader("Host");
        return scheme + "://" + hostname + request.getRequestURI().replaceFirst("api/v1/host.*$", "");
    }

    @Operation(summary = "Create a new Lokal", security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Missing Authorization header", content = @Content),
            @ApiResponse(responseCode = "403", description = "It is forbidden to create a Lokal for another owner.",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "There is a Lokal with this UUID (should really never " +
                    "happen).", content = @Content),
            @ApiResponse(responseCode = "201", description = "The Lokal was created with the returned password.")
    })
    @PostMapping("/lokal")
    public ResponseEntity<String> createLokal(@RequestBody LokalData data, HttpServletRequest request) {
        if (!LokalRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (data.getOwner() == null || !LokalRestController.isAuthorized(request, data.getOwner())) {
            // It is forbidden to create a Lokal for another owner
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            return ResponseEntity.created(URI.create("")).body(this.lokalService.createLokal(data));
        } catch (IllegalArgumentException e) {
            // There is a Lokal with this UUID (should really never happen).
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(summary = "Get all Lokals or a specific one. Level of detail depends on userUid.",
            security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Missing Authorization header", content = @Content),
            @ApiResponse(responseCode = "403", description = "userUid doesn't match Authorization header", content =
            @Content),
            @ApiResponse(responseCode = "200", description = "List of all (owned) Lokals", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LokalData.class))
            })
    })
    @GetMapping(value = "/lokal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<? extends LokalData>> getLokals(@RequestParam Optional<String> userUid,
                                                               @RequestParam Optional<String> lokalUid,
                                                               HttpServletRequest request) {
        if (!LokalRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (userUid.isPresent() && !LokalRestController.isAuthorized(request, userUid.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(this.lokalService.findAll(userUid, lokalUid));
    }

    @Operation(summary = "Get a PDF for with Qontakt Details for promotional purposes",
            security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Missing Authorization header", content = @Content),
            @ApiResponse(responseCode = "403", description = "Lokal's owner doesn't match Authorization header",
                    content = @Content),
            @ApiResponse(responseCode = "200", description = "PDF of Lokal's Data",
                    content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE))
    })
    @GetMapping(value = "/lokal/leaflet", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getLokalLeaflet(@RequestParam String lokalUid, HttpServletRequest request) {
        if (!LokalRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!this.lokalService.isAuthorized(request.getHeader("X-User"), lokalUid, Optional.empty())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        String fileName = "leaflet-"
                + lokalUid
                + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return ResponseEntity.ok().headers(headers).body(
                this.lokalService.printLeaflet(request.getLocale(), lokalUid, calculateBaseUrl(request))
        );
    }

    @Operation(summary = "Get a PDF of all sent visits", security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Missing Authorization header", content = @Content),
            @ApiResponse(responseCode = "403", description = "Lokal's owner doesn't match Authorization header",
                    content = @Content),
            @ApiResponse(responseCode = "200", description = "PDF of all sent visits",
                    content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE))
    })
    @PostMapping("/lokal/print")
    public ResponseEntity<byte[]> printVisitData(@RequestBody List<Visit> visits,
                                                 @RequestParam String lokalUid, @RequestParam Optional<String> password,
                                                 HttpServletRequest request) {
        if (!LokalRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (password.isEmpty() || !this.lokalService.isAuthorized(request.getHeader("X-User"), lokalUid, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        String fileName = "report-"
                + DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())
                + "-"
                + lokalUid
                + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return ResponseEntity.ok().headers(headers).body(this.lokalService.print(request.getLocale(), lokalUid,
                visits));
    }

    @Operation(
            summary = "Get an encrypted PDF of all visits at the given Lokal",
            security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Missing Authorization header", content = @Content),
            @ApiResponse(responseCode = "403", description = "Lokal's owner doesn't match Authorization header or " +
                    "wrong password",
                    content = @Content),
            @ApiResponse(responseCode = "200", description = "Encrypted PDF of all sent visits",
                    content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE))
    })
    @GetMapping("/lokal/print")
    public ResponseEntity<byte[]> printVisitDataEncrypted(@RequestParam String lokalUid,
                                                          @RequestParam String password,
                                                          @RequestParam String publicKeyUid,
                                                          HttpServletRequest request) {
        if (!LokalRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (password.isEmpty() || !this.lokalService.isAuthorized(request.getHeader("X-User"), lokalUid,
                Optional.of(password))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        String fileName = "report-"
                + DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())
                + "-"
                + lokalUid
                + ".pdf";
        byte[] print = this.lokalService.print(request.getLocale(), lokalUid);
        Pair<byte[], String> result = this.lokalService.encryptWithKnownKey(print, fileName, publicKeyUid);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(fileName, result.getSecond());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return ResponseEntity.ok().headers(headers).body(result.getFirst());
    }

    @Operation(summary = "Get a CSV of all sent visits", security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Missing Authorization header", content = @Content),
            @ApiResponse(responseCode = "403", description = "Lokal's owner doesn't match Authorization header",
                    content = @Content),
            @ApiResponse(responseCode = "200", description = "CSV of all sent visits",
                    content = @Content(mediaType = "text/csv"))
    })
    @PostMapping("/lokal/export")
    public ResponseEntity<byte[]> exportVisitData(@RequestBody List<Visit> visits,
                                                  @RequestParam String lokalUid, @RequestParam Optional<String> password,
                                                  HttpServletRequest request) {
        if (!LokalRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (password.isEmpty() || !this.lokalService.isAuthorized(request.getHeader("X-User"), lokalUid, password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        String fileName = "report-"
                + DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())
                + "-"
                + lokalUid
                + ".csv";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return ResponseEntity.ok().headers(headers).body(this.lokalService.export(lokalUid, visits));
    }

    @Operation(summary = "Verify Visit data (from /api/v1/user/visit/verify)", security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Missing Authorization header", content = @Content),
            @ApiResponse(responseCode = "200", description = "Verification was possible")
    })
    @GetMapping("/lokal/verify")
    ResponseEntity<Boolean> verifyQrData(String lokalUid, String qrData, HttpServletRequest request) {
        if (!LokalRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        VerificationQrCodeData data = VerificationQrCodeData.fromString(qrData);
        return ResponseEntity.ok(this.lokalService.verifyVisit(lokalUid, data));
    }
}
