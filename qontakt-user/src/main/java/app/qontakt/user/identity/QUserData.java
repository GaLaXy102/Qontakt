package app.qontakt.user.identity;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class QUserData {

    @Id
    @Schema(hidden = true)
    private String userUid;
    @Schema(example = "Sam")
    private String firstName;
    @Schema(example = "Muster")
    private String lastName;
    @Schema(example = "sam.muster@qontakt.me")
    private String email;
    @Schema(example = "Musterstraße 42")
    private String homeAddress;
    @Schema(example = "01337")
    private String homeZip;
    @Schema(example = "Musterstadt")
    private String homeCity;
    @Schema(example = "0123-456789")
    private String telephoneNumber;

    public QUserData(String userUid, String firstName, String lastName, String email, String homeAddress,
                     String homeZip, String homeCity, String telephoneNumber) {
        this.userUid = userUid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.homeAddress = homeAddress;
        this.homeZip = homeZip;
        this.homeCity = homeCity;
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * Generate a new Lokal with an optional overwritten UUID
     */
    public QUserData(QUserData old, boolean overwriteUUID) {
        this.userUid = overwriteUUID ? UUID.randomUUID().toString() : old.userUid;
        this.firstName = old.firstName;
        this.lastName = old.lastName;
        this.email = old.email;
        this.homeAddress = old.homeAddress;
        this.homeZip = old.homeZip;
        this.homeCity = old.homeCity;
        this.telephoneNumber = old.telephoneNumber;
    }

    protected QUserData() {
    }

    /**
     * Getter for QUserData's userUid
     *
     * @return userUid as java.lang.String
     */
    public String getUserUid() {
        return this.userUid;
    }

    /**
     * Getter for QUserData's firstName
     *
     * @return firstName as java.lang.String
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Getter for QUserData's lastName
     *
     * @return lastName as java.lang.String
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Getter for QUserData's email
     *
     * @return email as java.lang.String
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Getter for QUserData's homeAddress
     *
     * @return homeAddress as java.lang.String
     */
    public String getHomeAddress() {
        return this.homeAddress;
    }

    /**
     * Getter for QUserData's homeZip
     *
     * @return homeZip as java.lang.String
     */
    public String getHomeZip() {
        return this.homeZip;
    }

    /**
     * Getter for QUserData's homeCity
     *
     * @return homeCity as java.lang.String
     */
    public String getHomeCity() {
        return this.homeCity;
    }

    /**
     * Getter for QUserData's telephoneNumber
     *
     * @return telephoneNumber as java.lang.String
     */
    public String getTelephoneNumber() {
        return this.telephoneNumber;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void setHomeZip(String homeZip) {
        this.homeZip = homeZip;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
