package app.qontakt.host.helper;

/**
 * JSON Strings for QUserData
 */
public enum QUiData {
    CHECK_IN {
        final String uiName = "checkIn";
    },
    CHECK_OUT {
        final String uiName = "checkOut";
    },
    FIRST_NAME {
        final String uiName = "firstName";
    },
    LAST_NAME {
        final String uiName = "lastName";
    },
    ADDRESS_LINE {
        final String uiName = "homeAddress";
    },
    ADDRESS_ZIP {
        final String uiName = "homeZip";
    },
    ADDRESS_CITY {
        final String uiName = "homeCity";
    },
    PHONE_NUMBER {
        final String uiName = "telephoneNumber";
    },
    EMAIL {
        final String uiName = "email";
    };

    String uiName;
}
