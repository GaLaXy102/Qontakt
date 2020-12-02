package app.qontakt.host.lokal;

import app.qontakt.host.rules.FederalStateRuleSet;
import org.springframework.data.geo.Point;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalTime;
import java.util.UUID;

@Entity
public class LokalData {

    @Id
    private final String local_uid;
    private final String name;
    private final String address;
    private final Point coordinates;
    private final String owner;
    private final String gdpr_contact;
    private final LocalTime checkout_time;
    private final FederalStateRuleSet.Code fed_state;

    public LokalData(String name, String address, Point coordinates, String owner, String gdpr_contact,
                     LocalTime checkout_time, FederalStateRuleSet.Code fed_state) {
        this.local_uid = null;
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.owner = owner;
        this.gdpr_contact = gdpr_contact;
        this.checkout_time = checkout_time;
        this.fed_state = fed_state;
    }

    /**
     * Generate a new Lokal with an overwritten UUID
     */
    LokalData(LokalData old) {
        this.local_uid = UUID.randomUUID().toString();
        this.name = old.name;
        this.address = old.address;
        this.coordinates = old.coordinates;
        this.owner = old.owner;
        this.gdpr_contact = old.gdpr_contact;
        this.checkout_time = old.checkout_time;
        this.fed_state = old.fed_state;
    }

    public LokalData() {
        this.local_uid = null;
        this.name = null;
        this.address = null;
        this.coordinates = null;
        this.owner = null;
        this.gdpr_contact = null;
        this.checkout_time = null;
        this.fed_state = null;
    }

    /**
     * Getter for LokalData's local_uid
     *
     * @return local_uid as java.lang.String
     */
    public String getLocal_uid() {
        return this.local_uid;
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
    public String getGdpr_contact() {
        return this.gdpr_contact;
    }

    /**
     * Getter for LokalData's checkout_time
     *
     * @return checkout_time as java.time.LocalTime
     */
    public LocalTime getCheckout_time() {
        return this.checkout_time;
    }

    /**
     * Getter for LokalData's fed_state
     *
     * @return fed_state as app.qontakt.host.rules.FederalStateRuleSet.Code
     */
    public FederalStateRuleSet.Code getFed_state() {
        return this.fed_state;
    }
}