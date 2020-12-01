package app.qontakt.user;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserService {
    public UserService() {
    }

    public boolean saveVisit(String user_uid, String local_uid, LocalDateTime time) {
        return false;
    }


}
