package app.qontakt.host.lokal;

import org.hibernate.annotations.Cascade;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
public class LokalPassword implements Serializable {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private final LokalData lokal;
    private final String hashedPassword;

    public LokalPassword(LokalData lokal, String hashedPassword) {
        this.lokal = lokal;
        this.hashedPassword = hashedPassword;
    }

    protected LokalPassword() {
        this.lokal = null;
        this.hashedPassword = null;
    }

    /**
     * Getter for LokalPassword's hashedPassword
     *
     * @return hashedPassword as java.lang.String
     */
    public String getHashedPassword() {
        return this.hashedPassword;
    }
}
