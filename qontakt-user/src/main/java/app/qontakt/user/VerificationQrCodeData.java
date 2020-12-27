package app.qontakt.user;

import java.time.Instant;
import java.util.regex.Pattern;

public class VerificationQrCodeData {
    private static final String uidPattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";
    private static final String longPattern = "[0-9]{1,%d}".formatted(String.valueOf(Long.MAX_VALUE).length());
    public static Pattern regexPattern = Pattern.compile("///" + uidPattern + "/" + uidPattern + "//" + longPattern + "///");

    private final String userUid;
    private final String visitUid;
    private final Instant created;

    public VerificationQrCodeData(String userUid, String visitUid, Instant created) {
        this.userUid = userUid;
        this.visitUid = visitUid;
        this.created = created;
    }

    /**
     * Create Verification String
     * @return "///userUid/visitUid//creationTimestamp///"
     */
    @Override
    public String toString() {
        return "///" + this.userUid + "/" + this.visitUid + "//" + created.getEpochSecond() + "///";
    }

    /**
     * Create object from a String
     * @param data Verification String matching "///userUid/visitUid//creationTimestamp///"
     * @return created object
     */
    public static VerificationQrCodeData fromString(String data) {
        if (!data.matches(regexPattern.pattern())) {
            throw new IllegalArgumentException("Invalid data!");
        }
        String userUid = data.substring(3, 39);
        String visitUid = data.substring(40, 76);
        String created = data.substring(78, data.length() - 3);
        return new VerificationQrCodeData(userUid, visitUid, Instant.ofEpochSecond(Long.parseLong(created)));
    }

    /**
     * Getter for VerificationQrCodeData's userUid
     *
     * @return userUid as java.lang.String
     */
    public String getUserUid() {
        return this.userUid;
    }

    /**
     * Getter for VerificationQrCodeData's visitUid
     *
     * @return visitUid as java.lang.String
     */
    public String getVisitUid() {
        return this.visitUid;
    }

    /**
     * Getter for VerificationQrCodeData's created
     *
     * @return created as java.time.Instant
     */
    public Instant getCreated() {
        return this.created;
    }
}
