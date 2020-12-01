package app.qontakt.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private final UserService userService;

    public static boolean isAuthorized(HttpServletRequest request, String user_uid) {
        return user_uid.equals(request.getHeader("X-User"));
    }

    public RestController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/api/v1/user/visit")
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
