package app.qontakt.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Visit {
    @Id
    private final String visit_uid;
    private final String user_uid;
    private final String lokal_uid;
    private final LocalDateTime start;
    private LocalDateTime end;

    public Visit(String user_uid, String lokal_uid, LocalDateTime start) {
        this.visit_uid = UUID.randomUUID().toString();
        this.user_uid = user_uid;
        this.lokal_uid = lokal_uid;
        this.start = start;
        this.end = null;
    }

    /**
     * Getter for Visit's visit_uid
     *
     * @return visit_uid as Java.lang.String
     */
    public String getVisit_uid() {
        return visit_uid;
    }

    /**
     * Getter for Visit's user_uid
     *
     * @return user_uid as Java.lang.String
     */
    public String getUser_uid() {
        return user_uid;
    }

    /**
     * Getter for Visit's lokal_uid
     *
     * @return lokal_uid as Java.lang.String
     */
    public String getLokal_uid() {
        return lokal_uid;
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
