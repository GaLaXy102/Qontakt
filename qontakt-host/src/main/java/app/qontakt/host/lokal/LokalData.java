package app.qontakt.host.lokal;

import app.qontakt.host.rules.FederalStateRuleSet;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.geo.Point;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalTime;
import java.util.UUID;

@Entity
public class LokalData {

    @Id
    @Schema(hidden = true)
    private final String lokalUid;
    @Schema(example = "Zur Fröhlichen Reblaus")
    private String name;
    @Schema(example = "Weinstraße 3, 01069 Dresden")
    private String address;
    private Point coordinates;
    @Schema(example = "UID-of-Owner")
    private String owner;
    @Schema(example = "gdpr@qontakt.me")
    private String gdprContact;
    @Schema(type = "string", example = "12:34:56")
    private LocalTime checkoutTime;
    private FederalStateRuleSet.Code federalState;

    public LokalData(String lokalUid, String name, String address, Point coordinates, String owner, String gdprContact,
                     LocalTime checkoutTime, FederalStateRuleSet.Code federalState) {
        this.lokalUid = lokalUid;
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.owner = owner;
        this.gdprContact = gdprContact;
        this.checkoutTime = checkoutTime;
        this.federalState = federalState;
    }

    /**
     * Generate a new Lokal with an optional overwritten UUID
     */
    public LokalData(LokalData old, boolean overwriteUUID) {
        this.lokalUid = overwriteUUID ? UUID.randomUUID().toString() : old.lokalUid;
        this.name = old.name;
        this.address = old.address;
        this.coordinates = old.coordinates;
        this.owner = old.owner;
        this.gdprContact = old.gdprContact;
        this.checkoutTime = old.checkoutTime;
        this.federalState = old.federalState;
    }

    public LokalData() {
        this.lokalUid = null;
        this.name = null;
        this.address = null;
        this.coordinates = null;
        this.owner = null;
        this.gdprContact = null;
        this.checkoutTime = null;
        this.federalState = null;
    }

    /**
     * Getter for LokalData's lokalUid
     *
     * @return lokalUid as java.lang.String
     */
    public String getLokalUid() {
        return this.lokalUid;
    }

    /**
     * Getter for LokalData's name
     *
     * @return name as java.lang.String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for LokalData's address
     *
     * @return address as java.lang.String
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Getter for LokalData's coordinates
     *
     * @return coordinates as org.springframework.data.geo.Point
     */
    public Point getCoordinates() {
        return this.coordinates;
    }

    /**
     * Getter for LokalData's owner
     *
     * @return owner as java.lang.String
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * Getter for LokalData's gdpr_contact
     *
     * @return gdpr_contact as java.lang.String
     */
    public String getGdprContact() {
        return this.gdprContact;
    }

    /**
     * Getter for LokalData's checkout_time
     *
     * @return checkout_time as java.time.LocalTime
     */
    public LocalTime getCheckoutTime() {
        return this.checkoutTime;
    }

    /**
     * Getter for LokalData's fed_state
     *
     * @return fed_state as app.qontakt.host.rules.FederalStateRuleSet.Code
     */
    public FederalStateRuleSet.Code getFederalState() {
        return this.federalState;
    }

    /* Setters for Subclasses and update Method */
    protected void setName(String name) {
        this.name = name;
    }

    protected void setAddress(String address) {
        this.address = address;
    }

    protected void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    protected void setOwner(String owner) {
        this.owner = owner;
    }

    protected void setGdprContact(String gdpr_contact) {
        this.gdprContact = gdpr_contact;
    }

    protected void setCheckoutTime(LocalTime checkout_time) {
        this.checkoutTime = checkout_time;
    }

    protected void setFederalState(FederalStateRuleSet.Code fed_state) {
        this.federalState = fed_state;
    }
}