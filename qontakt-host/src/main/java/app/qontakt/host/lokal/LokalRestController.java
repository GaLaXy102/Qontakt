package app.qontakt.host.lokal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Lokal administration
 */
@RestController
@RequestMapping("/api/v1/host")
public class LokalRestController {

    public static boolean isAuthorized(HttpServletRequest request, String user_uid) {
        return user_uid.equals(request.getHeader("X-User"));
    }

    public static boolean isAuthorized(HttpServletRequest request) {
        return request.getHeader("X-User") != null;
    }

    private final LokalService lokalService;

    public LokalRestController(LokalService lokalService) {
        this.lokalService = lokalService;
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

    @Operation(summary = "Get all Lokals. Level of detail depends on user_uid.", security =
    @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Missing Authorization header", content = @Content),
            @ApiResponse(responseCode = "403", description = "user_uid doesn't match Authorization header", content =
                    @Content),
            @ApiResponse(responseCode = "200", description = "List of all (owned) Lokals", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LokalData.class))
            })
    })
    @GetMapping("/lokal")
    public ResponseEntity<List<? extends LokalData>> getLokals(@RequestParam Optional<String> user_uid,
                                                     HttpServletRequest request) {
        if (!LokalRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (user_uid.isPresent() && !LokalRestController.isAuthorized(request, user_uid.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(this.lokalService.findAll(user_uid));
    }
}
