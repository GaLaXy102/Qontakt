package app.qontakt.host.helper;

import app.qontakt.host.lokal.LokalData;

/**
 * Class to Export Data
 */
public class LokalDataPublic extends LokalData {
    public LokalDataPublic(LokalData data) {
        super(data, false);
        this.setOwner(null);
        this.setCheckoutTime(null);
    }
}
