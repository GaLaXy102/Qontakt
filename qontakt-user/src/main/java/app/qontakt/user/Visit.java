package app.qontakt.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Visit {
    @Id
    private final String visitUid;
    private final String userUid;
    private final String lokalUid;
    private final LocalDateTime start;
    private LocalDateTime end;

    public Visit(String userUid, String lokalUid, LocalDateTime start) {
        this.visitUid = UUID.randomUUID().toString();
        this.userUid = userUid;
        this.lokalUid = lokalUid;
        this.start = start;
        this.end = null;
    }

    protected Visit() {
        this.visitUid = null;
        this.userUid = null;
        this.lokalUid = null;
        this.start = null;
    }

    /**
     * Getter for Visit's visit_uid
     *
     * @return visit_uid as Java.lang.String
     */
    public String getVisitUid() {
        return visitUid;
    }

    /**
     * Getter for Visit's user_uid
     *
     * @return user_uid as Java.lang.String
     */
    public String getUserUid() {
        return userUid;
    }

    /**
     * Getter for Visit's lokal_uid
     *
     * @return lokal_uid as Java.lang.String
     */
    public String getLokalUid() {
        return lokalUid;
    }

    /**
     * Getter for Visit's start time
     *
     * @return start time as Java.time.LocalDateTime
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Getter for Visit's end time
     *
     * @return end time as Java.time.LocalDateTime
     */
    public LocalDateTime getEnd() {
        return end;
    }
}
