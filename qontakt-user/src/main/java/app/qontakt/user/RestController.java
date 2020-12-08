package app.qontakt.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
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

    /**
     * Add a new Visit for the given User and Lokal
     *
     * @param user_uid  UID of the User
     * @param local_uid UID of the Lokal
     * @param request incoming HTTP request
     * @return true if creation of Visit is successful and User has no unterminated Visits
     */
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
}
