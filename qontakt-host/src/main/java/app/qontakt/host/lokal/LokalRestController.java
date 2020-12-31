package app.qontakt.host.lokal;

import app.qontakt.user.VerificationQrCodeData;
import app.qontakt.user.Visit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @Operation(summary = "Create a new Lokal", security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Missing Authorization header", content = @Content),
            @ApiResponse(responseCode = "403", description = "It is forbidden to create a Lokal for another owner.",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "There is a Lokal with this UUID (should really never " +
                    "happen).", content = @Content),
            @ApiResponse(responseCode = "201", description = "The Lokal was created with the returned password.")
    })
    @PutMapping("/lokal")
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

    @Operation(summary = "Get all Lokals. Level of detail depends on userUid.", security =
    @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Missing Authorization header", content = @Content),
            @ApiResponse(responseCode = "403", description = "userUid doesn't match Authorization header", content =
            @Content),
            @ApiResponse(responseCode = "200", description = "List of all (owned) Lokals", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LokalData.class))
            })
    })
    @GetMapping("/lokal")
    public ResponseEntity<List<? extends LokalData>> getLokals(@RequestParam Optional<String> userUid,
                                                               HttpServletRequest request) {
        if (!LokalRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (userUid.isPresent() && !LokalRestController.isAuthorized(request, userUid.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(this.lokalService.findAll(userUid));
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
                                                 @RequestParam String lokalUid, @RequestParam String password,
                                                 HttpServletRequest request) {
        if (!LokalRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!this.lokalService.isAuthorized(request.getHeader("X-User"), lokalUid, password)) {
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
                                                  @RequestParam String lokalUid, @RequestParam String password,
                                                  HttpServletRequest request) {
        if (!LokalRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!this.lokalService.isAuthorized(request.getHeader("X-User"), lokalUid, password)) {
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
