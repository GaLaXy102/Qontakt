package app.qontakt.user;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for User administration
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/v1/user")
public class RestController {

    private final UserService userService;

    /**
     * Check if User with the given UID is logged in
     *
     * @param request  incoming HTTP request
     * @param user_uid UID of the User
     * @return true if User is logged in
     */
    public static boolean isAuthorized(HttpServletRequest request, String user_uid) {
        return user_uid.equals(request.getHeader("X-User"));
    }

    public RestController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new Visit for the given User and Lokal.", security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "User has no Authorization", content = @Content),
            @ApiResponse(responseCode = "403", description = "It is forbidden to create a Visit for another User.",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "There is an unterminated Visit for the given User UUID.", content = @Content),
            @ApiResponse(responseCode = "201", description = "The Visit with the given data was created.")
    })
    @PutMapping("/visit")
    ResponseEntity<Boolean> newVisit(@RequestParam Optional<String> user_uid, @RequestParam String local_uid, HttpServletRequest request) {
        if (user_uid.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        if (!RestController.isAuthorized(request, user_uid.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        }
        if (userService.saveVisit(user_uid.get(), local_uid, LocalDateTime.now())) {
            return ResponseEntity.created(URI.create("")).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
        }
    }

    @Operation(summary = "Get all Visits for the given User.", security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "User has no Authorization", content = @Content),
            @ApiResponse(responseCode = "403", description = "user_uid doesn't match Authorization header", content =
            @Content),
            @ApiResponse(responseCode = "200", description = "List of all Visits for the given User", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Visit.class))
            })
    })
    @GetMapping("/visit")
    ResponseEntity<List<Visit>> showVisits(@RequestParam Optional<String> user_uid, HttpServletRequest request) {
        if (user_uid.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!RestController.isAuthorized(request, user_uid.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(userService.getVisits(user_uid.get()));
    }
}
