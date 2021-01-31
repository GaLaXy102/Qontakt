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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @param lokalUid UID of the Lokal
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

    public static boolean isAuthorizedUser(HttpServletRequest request) {
        return request.getHeader("X-User") != null || request.getHeader("X-Lokal") != null;
    }

    public static boolean isAuthorizedLokal(HttpServletRequest request) {
        return request.getHeader("X-Lokal") != null;
    }

    @Operation(
            summary = "Get user UID belonging to a given request",
            security = @SecurityRequirement(name = "user-header")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UserUid or nothing if not authenticated")
    })
    @GetMapping("/whoami")
    public ResponseEntity<String> whoami(HttpServletRequest request){
        return ResponseEntity.ok(request.getHeader("X-User"));
    }

    @Operation(
            summary = "Create a new Visit for the given User and Lokal.",
            security = @SecurityRequirement(name = "user-header")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "User has no Authorization", content = @Content),
            @ApiResponse(responseCode = "403", description = "It is forbidden to create a Visit for another User.",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "There is an unterminated Visit for the given User UUID.",
                    content = @Content),
            @ApiResponse(responseCode = "201", description = "The Visit with the given data was created.")
    })
    @PostMapping("/visit")
    ResponseEntity<Boolean> newVisit(@RequestParam String user_uid, @RequestParam String lokal_uid, HttpServletRequest request) {
        if (!UserRestController.isAuthorizedUser(request)) {
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

    @Operation(
            summary = "Get all Visits or a specific one for the given User.",
            security = @SecurityRequirement(name = "user-header")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "User has no Authorization", content = @Content),
            @ApiResponse(responseCode = "403", description = "user_uid doesn't match Authorization header or Visit's associated user",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "VisitUid was specified, but there is no such visit",
                    content = @Content),
            @ApiResponse(responseCode = "200", description = "List of all Visits for the given User",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Visit.class))
            })
    })
    @GetMapping("/visit")
    ResponseEntity<List<Visit>> showVisits(@RequestParam String user_uid, @RequestParam Optional<String> visitUid, HttpServletRequest request) {
        if (!UserRestController.isAuthorizedUser(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!UserRestController.isAuthorized(request, user_uid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            return ResponseEntity.ok(userService.getVisitsForUser(user_uid, visitUid));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @Operation(
            summary = "Get all Visits at a given Lokal for the given User.",
            security = {
                    @SecurityRequirement(name = "user-header"),
                    @SecurityRequirement(name = "lokal-header")
            }
    )
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
        if (!UserRestController.isAuthorizedUser(request) && !UserRestController.isAuthorizedLokal(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!UserRestController.isAuthorized(request, lokal_uid, user_uid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(this.userService.getVisitsForLokal(lokal_uid, user_uid));
    }

    @Operation(
            summary = "Delete a single Visit.",
            security = {
                    @SecurityRequirement(name = "user-header"),
                    @SecurityRequirement(name = "user-header")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Missing Authorization Header (Lokal and User",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "user_uid doesn't match Authorization header or Visit " +
                    "with UID visit_uid doesn't belong to specified user_uid or lokal_uid in Header.",
                    content = @Content),
            @ApiResponse(responseCode = "200", description = "true -> Visit deleted; false -> No such Visit")
    })
    @DeleteMapping("/visit")
    ResponseEntity<Boolean> deleteSingleVisit(@RequestParam String user_uid, @RequestParam String visit_uid,
                                              HttpServletRequest request) {
        if (!(UserRestController.isAuthorizedUser(request) && UserRestController.isAuthorizedLokal(request))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!UserRestController.isAuthorized(request, this.userService.getVisitsForUser(user_uid, Optional.of(visit_uid)).get(0).getLokalUid(), Optional.of(user_uid))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            return ResponseEntity.ok(this.userService.deleteVisit(user_uid, visit_uid));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @Operation(
            summary = "Get Verification string for current visit",
            security = @SecurityRequirement(name = "user-header")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "User has no Authorization", content = @Content),
            @ApiResponse(responseCode = "403", description = "user_uid doesn't match Authorization header",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "User has no current Visit", content = @Content),
            @ApiResponse(responseCode = "200", description = "Current Visit data")
    })
    @GetMapping("/verify")
    ResponseEntity<String> getCurrentVisitVerificationString(@RequestParam String user_uid, HttpServletRequest request) {
        if (!UserRestController.isAuthorizedUser(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!UserRestController.isAuthorized(request, user_uid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            return ResponseEntity.ok(this.userService.calculateCurrentVisitVerificationString(user_uid).toString());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(
            summary = "Close a single Visit",
            security = @SecurityRequirement(name = "user-header")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "User has no Authorization", content = @Content),
            @ApiResponse(responseCode = "403", description = "user_uid doesn't match Authorization header or Visit " +
                    "with UID visit_uid doesn't belong to specified user_uid.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Visit is already terminated.", content = @Content),
            @ApiResponse(responseCode = "200", description = "true -> Visit terminated; false -> No such Visit")
    })
    @PutMapping("/visit")
    ResponseEntity<Boolean> closeVisit(@RequestParam String user_uid, @RequestParam String visit_uid, HttpServletRequest request) {
        if (!UserRestController.isAuthorizedUser(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!UserRestController.isAuthorized(request, user_uid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            return ResponseEntity.ok(this.userService.closeVisit(user_uid, visit_uid));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
