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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for User administration
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Check if User with the given UID is logged in or the given lokal is authorized
     *
     * @param request  incoming HTTP request
     * @param lokalUid  UID of the Lokal
     * @param user_uid UID of the User
     * @return true if User is logged in or Lokal Header matches
     */
    public static boolean isAuthorized(HttpServletRequest request, String lokalUid, Optional<String> user_uid) {
        return lokalUid.equals(request.getHeader("X-Lokal"))
                || user_uid.map(s -> s.equals(request.getHeader("X-User"))).orElse(false);
    }

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

    public static boolean isAuthorized(HttpServletRequest request) {
        return request.getHeader("X-User") != null;
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
    ResponseEntity<Boolean> newVisit(@RequestParam String user_uid, @RequestParam String lokal_uid, HttpServletRequest request) {
        if (!UserRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!UserRestController.isAuthorized(request, user_uid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        if (userService.saveVisit(user_uid, lokal_uid, LocalDateTime.now())) {
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
    ResponseEntity<List<Visit>> showVisits(@RequestParam String user_uid, HttpServletRequest request) {
        if (!UserRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!UserRestController.isAuthorized(request, user_uid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(userService.getVisits(user_uid));
    }

    @Operation(summary = "Get all Visits at a given Lokal for the given User.", security = @SecurityRequirement(name =
            "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "User has no Authorization", content = @Content),
            @ApiResponse(responseCode = "403", description = "user_uid doesn't match Authorization header", content =
            @Content),
            @ApiResponse(responseCode = "200", description = "List of all Visits for the given User at the given Lokal",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Visit.class))
                    })
    })
    @GetMapping("/visit/{lokal_uid}")
    ResponseEntity<List<Visit>> getVisitsForLokal(@RequestParam Optional<String> user_uid, @PathVariable String lokal_uid,
                                                  HttpServletRequest request) {
        if (!UserRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!UserRestController.isAuthorized(request, lokal_uid, user_uid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(this.userService.getVisits(lokal_uid, user_uid));
    }

    @Operation(summary = "Delete a single Visit.", security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Missing Authorization Header", content = @Content),
            @ApiResponse(responseCode = "403", description = "user_uid doesn't match Authorization header or Visit " +
                    "with UID visit_uid doesn't belong to specified user_uid.", content = @Content),
            @ApiResponse(responseCode = "200", description = "true -> Visit deleted; false -> No such Visit")
    })
    @DeleteMapping("/visit")
    ResponseEntity<Boolean> deleteSingleVisit(@RequestParam String user_uid, @RequestParam String visit_uid,
                                              HttpServletRequest request) {
        if (!UserRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!UserRestController.isAuthorized(request, user_uid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            return ResponseEntity.ok(this.userService.deleteVisit(user_uid, visit_uid));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @Operation(summary = "Get Verification string for current visit", security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "User has no Authorization", content = @Content),
            @ApiResponse(responseCode = "403", description = "user_uid doesn't match Authorization header", content =
            @Content),
            @ApiResponse(responseCode = "400", description = "User has no current Visit", content = @Content),
            @ApiResponse(responseCode = "200", description = "Current Visit data")
    })
    @GetMapping("/visit/verify")
    ResponseEntity<String> getCurrentVisitVerificationString(@RequestParam String user_uid, HttpServletRequest request) {
        if (!UserRestController.isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!UserRestController.isAuthorized(request, user_uid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            return ResponseEntity.ok(this.userService.calculateCurrentVisitVerificationString(user_uid));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
