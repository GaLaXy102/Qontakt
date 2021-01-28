package app.qontakt.user.identity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/user/identity")
public class IdentityRestController {

    private final IdentityService identityService;

    public IdentityRestController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Operation(summary = "Create a new Identity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Supplied Identity data is invalid.", content = @Content),
            @ApiResponse(responseCode = "409", description = "There is an Identity with this UUID (should really " +
                    "never happen).", content = @Content),
            @ApiResponse(responseCode = "201", description = "The Identity was created.")
    })
    @PostMapping("")
    public ResponseEntity<String> createUser(@RequestBody QUserData data) {
        try {
            String uid = this.identityService.create(data);
            return ResponseEntity.created(URI.create("?user_uid=%s".formatted(uid))).body(uid);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Update an existing Identity", security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Supplied identity data is invalid.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing Authorization header", content = @Content),
            @ApiResponse(responseCode = "403", description = "It is forbidden to update data of another user.",
                    content = @Content),
            @ApiResponse(responseCode = "200", description = "true -> data updated; false -> no such user")
    })
    @PutMapping("")
    public ResponseEntity<Boolean> updateUser(@RequestBody QUserData data, HttpServletRequest request) {
        if (!app.qontakt.user.UserRestController.isAuthorizedUser(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (data.getUserUid() == null) {
            data.setUserUid(request.getHeader("X-User"));
        }
        if (!app.qontakt.user.UserRestController.isAuthorized(request, data.getUserUid())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            return ResponseEntity.ok(this.identityService.update(data));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Get Identity", security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Missing Authorization header", content = @Content),
            @ApiResponse(responseCode = "403", description = "It is forbidden to get data of another user.",
                    content = @Content),
            @ApiResponse(responseCode = "200", description = "null -> no such user; else user's Identity")
    })
    @GetMapping("")
    public ResponseEntity<QUserData> getUser(@RequestParam String userUid, HttpServletRequest request) {
        if (!app.qontakt.user.UserRestController.isAuthorizedUser(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!app.qontakt.user.UserRestController.isAuthorized(request, userUid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(this.identityService.get(userUid));
    }

    @Operation(summary = "Delete Identity", security = @SecurityRequirement(name = "user-header"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Missing Authorization header", content = @Content),
            @ApiResponse(responseCode = "403", description = "It is forbidden to update data of another user.",
                    content = @Content),
            @ApiResponse(responseCode = "419", description = "There are visits associated with this userUid that must" +
                    " be deleted first.", content = @Content),
            @ApiResponse(responseCode = "200", description = "true -> data deleted; false -> no such user")
    })
    @DeleteMapping("")
    public ResponseEntity<Boolean> deleteUser(@RequestParam String userUid, HttpServletRequest request) {
        if (!app.qontakt.user.UserRestController.isAuthorizedUser(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!app.qontakt.user.UserRestController.isAuthorized(request, userUid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            return ResponseEntity.ok(this.identityService.delete(userUid));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }
}
