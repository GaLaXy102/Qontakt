package app.qontakt.host.uihelper;

import app.qontakt.host.lokal.LokalData;

/**
 * Class to Export Data
 */
public class LokalDataPublic extends LokalData {
    public LokalDataPublic(LokalData data) {
        super(data, false);
        this.setOwner(null);
        this.setCheckout_time(null);
    }
}
