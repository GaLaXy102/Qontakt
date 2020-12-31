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
    private final LocalDateTime checkIn;
    private LocalDateTime checkOut;

    public Visit(String visitUid, String userUid, String lokalUid, LocalDateTime checkIn) {
        this.visitUid = visitUid;
        this.userUid = userUid;
        this.lokalUid = lokalUid;
        this.checkIn = checkIn;
        this.checkOut = null;
    }

    protected Visit() {
        this.visitUid = null;
        this.userUid = null;
        this.lokalUid = null;
        this.checkIn = null;
    }

    /**
     * Getter for Visit's visitUid
     *
     * @return visitUid as Java.lang.String
     */
    public String getVisitUid() {
        return visitUid;
    }

    /**
     * Getter for Visit's userUid
     *
     * @return userUid as Java.lang.String
     */
    public String getUserUid() {
        return userUid;
    }

    /**
     * Getter for Visit's lokalUid
     *
     * @return lokalUid as Java.lang.String
     */
    public String getLokalUid() {
        return lokalUid;
    }

    /**
     * Getter for Visit's check in time
     *
     * @return check in time as Java.time.LocalDateTime
     */
    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    /**
     * Getter for Visit's check out time
     *
     * @return check out time as Java.time.LocalDateTime
     */
    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    /**
     * Setter for Visit's check out time
     *
     * @param checkOut check out time as Java.time.LocalDateTime
     */
    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }
}
