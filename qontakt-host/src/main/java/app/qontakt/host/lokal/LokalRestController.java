package app.qontakt.host.lokal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

/**
 * REST Controller for Lokal administration
 */
@RestController
@RequestMapping("/api/v1/host")
public class LokalRestController {

    public static boolean isAuthorized(HttpServletRequest request, String user_uid) {
        return user_uid.equals(request.getHeader("X-User"));
    }

    private final LokalService lokalService;

    public LokalRestController(LokalService lokalService) {
        this.lokalService = lokalService;
    }

    @Operation(summary = "Create a new Lokal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "It is forbidden to create a Lokal for another owner."),
            @ApiResponse(responseCode = "409", description = "There is a Lokal with this UUID (should really never " +
                    "happen).", content = @Content),
            @ApiResponse(responseCode = "201", description = "The Lokal was created with the returned password.")
    })
    @PutMapping("/lokal")
    public ResponseEntity<String> createLokal(@RequestBody LokalData data, HttpServletRequest request) {
        if (!LokalRestController.isAuthorized(request, data.getOwner())) {
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
}
