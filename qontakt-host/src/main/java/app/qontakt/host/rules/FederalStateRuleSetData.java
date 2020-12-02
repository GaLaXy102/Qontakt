package app.qontakt.host.rules;

import app.qontakt.host.uihelper.QUserDataFields;

import java.time.Period;
import java.util.List;
import java.util.Locale;

public class FederalStateRuleSetData {

    /* Shortcuts */
    public static final String DEU = Locale.GERMANY.getISO3Country();

    /* Actual rule sets */
    public static final FederalStateRuleSet DE_BW = new FederalStateRuleSet(DEU, "BW", Period.ofDays(28),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField,
                    QUserDataFields.addressLineField,
                    QUserDataFields.addressZipField,
                    QUserDataFields.addressCityField),
            List.of(),
            List.of(QUserDataFields.phoneField));
    public static final FederalStateRuleSet DE_BY = new FederalStateRuleSet(DEU, "BY", Period.ofDays(31),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField),
            List.of(QUserDataFields.addressLineField,
                    QUserDataFields.phoneField,
                    QUserDataFields.emailField),
            List.of());
    public static final FederalStateRuleSet DE_BE = new FederalStateRuleSet(DEU, "BE", Period.ofDays(28),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField,
                    QUserDataFields.addressZipField,
                    QUserDataFields.addressCityField),
            List.of(QUserDataFields.addressLineField,
                    QUserDataFields.phoneField,
                    QUserDataFields.emailField),
            List.of());
    public static final FederalStateRuleSet DE_BB = new FederalStateRuleSet(DEU, "BB", Period.ofDays(28),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField),
            List.of(QUserDataFields.addressLineField,
                    QUserDataFields.phoneField,
                    QUserDataFields.emailField),
            List.of());
    public static final FederalStateRuleSet DE_HB = new FederalStateRuleSet(DEU, "HB", Period.ofDays(21),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField),
            List.of(QUserDataFields.addressLineField,
                    QUserDataFields.phoneField,
                    QUserDataFields.emailField),
            List.of());
    public static final FederalStateRuleSet DE_HH = new FederalStateRuleSet(DEU, "HH", Period.ofDays(28),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField,
                    QUserDataFields.addressLineField,
                    QUserDataFields.addressZipField,
                    QUserDataFields.addressCityField),
            List.of(),
            List.of(QUserDataFields.phoneField));
    public static final FederalStateRuleSet DE_HE = new FederalStateRuleSet(DEU, "HE", Period.ofDays(31),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField,
                    QUserDataFields.addressLineField,
                    QUserDataFields.addressZipField,
                    QUserDataFields.addressCityField),
            List.of(),
            List.of(QUserDataFields.phoneField));
    public static final FederalStateRuleSet DE_MV = new FederalStateRuleSet(DEU, "MV", Period.ofDays(28),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField,
                    QUserDataFields.addressLineField,
                    QUserDataFields.addressZipField,
                    QUserDataFields.addressCityField),
            List.of(),
            List.of(QUserDataFields.phoneField));
    public static final FederalStateRuleSet DE_NI = new FederalStateRuleSet(DEU, "NI", Period.ofDays(21),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField,
                    QUserDataFields.addressLineField,
                    QUserDataFields.addressZipField,
                    QUserDataFields.addressCityField),
            List.of(),
            List.of(QUserDataFields.phoneField));
    public static final FederalStateRuleSet DE_NW = new FederalStateRuleSet(DEU, "NW", Period.ofDays(28),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField,
                    QUserDataFields.addressLineField,
                    QUserDataFields.addressZipField,
                    QUserDataFields.addressCityField),
            List.of(),
            List.of(QUserDataFields.phoneField));
    public static final FederalStateRuleSet DE_RP = new FederalStateRuleSet(DEU, "RP", Period.ofDays(31),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField,
                    QUserDataFields.addressLineField,
                    QUserDataFields.addressZipField,
                    QUserDataFields.addressCityField),
            List.of(),
            List.of(QUserDataFields.phoneField));
    public static final FederalStateRuleSet DE_SL = new FederalStateRuleSet(DEU, "SL", Period.ofDays(31),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField,
                    QUserDataFields.addressZipField,
                    QUserDataFields.addressCityField),
            List.of(QUserDataFields.addressLineField,
                    QUserDataFields.phoneField,
                    QUserDataFields.emailField),
            List.of());
    public static final FederalStateRuleSet DE_SN = new FederalStateRuleSet(DEU, "SN", Period.ofDays(31),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField,
                    QUserDataFields.addressZipField,
                    QUserDataFields.addressCityField),
            List.of(QUserDataFields.addressLineField,
                    QUserDataFields.phoneField,
                    QUserDataFields.emailField),
            List.of());
    public static final FederalStateRuleSet DE_ST = new FederalStateRuleSet(DEU, "ST", Period.ofDays(28),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField,
                    QUserDataFields.addressLineField,
                    QUserDataFields.addressZipField,
                    QUserDataFields.addressCityField,
                    QUserDataFields.phoneField),
            List.of(),
            List.of());
    public static final FederalStateRuleSet DE_SH = new FederalStateRuleSet(DEU, "SH", Period.ofDays(28),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField,
                    QUserDataFields.addressLineField,
                    QUserDataFields.addressZipField,
                    QUserDataFields.addressCityField),
            List.of(QUserDataFields.phoneField,
                    QUserDataFields.emailField),
            List.of());
    public static final FederalStateRuleSet DE_TH = new FederalStateRuleSet(DEU, "TH", Period.ofDays(28),
            List.of(QUserDataFields.firstNameField,
                    QUserDataFields.lastNameField),
            List.of(QUserDataFields.addressLineField,
                    QUserDataFields.phoneField),
            List.of());

    /* All elements */
    public static final List<FederalStateRuleSet> allRules = List.of(DE_BW, DE_BY, DE_BE, DE_BB, DE_HB, DE_HH, DE_HE,
            DE_MV, DE_NI, DE_NW, DE_RP, DE_SL, DE_SN, DE_ST, DE_SH, DE_TH);
}