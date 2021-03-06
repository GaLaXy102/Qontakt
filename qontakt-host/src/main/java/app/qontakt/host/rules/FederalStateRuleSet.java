package app.qontakt.host.rules;

import app.qontakt.host.helper.QUiData;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.time.Period;
import java.util.List;
import java.util.Objects;

@Entity
@IdClass(FederalStateRuleSet.Code.class)
public class FederalStateRuleSet {

    /* Primary Key */
    public static class Code implements Serializable {
        @Schema(example = "DEU")
        private final String countryCode;
        @Schema(example = "SN")
        private final String shortName;

        public Code(String countryCode, String shortName) {
            this.countryCode = countryCode;
            this.shortName = shortName;
        }

        protected Code() {
            this.countryCode = null;
            this.shortName = null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Code code = (Code) o;
            return Objects.equals(countryCode, code.countryCode) &&
                    Objects.equals(shortName, code.shortName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(countryCode, shortName);
        }

        /**
         * Getter for Code's countryCode
         *
         * @return countryCode as java.lang.String
         */
        public String getCountryCode() {
            return this.countryCode;
        }

        /**
         * Getter for Code's shortName
         *
         * @return shortName as java.lang.String
         */
        public String getShortName() {
            return this.shortName;
        }

        @Override
        public String toString() {
            return this.shortName + ", " + this.countryCode;
        }
    }

    /* Actual data structure */
    @Id
    private final String countryCode;
    @Id
    private final String shortName;
    private final Period retainPeriod;
    @ElementCollection
    private final List<QUiData> requiredFields;
    @ElementCollection
    private final List<QUiData> anyRequiredFields;
    @ElementCollection
    private final List<QUiData> allowedFields;

    /**
     * Create a new Data Set for Rules of a Federal State
     *
     * @param countryCode       ISO 3166 ALPHA2 code of country, e.g. DEU for Germany
     * @param shortName         Short name of the Federal State, e.g. BY for Bavaria in Germany
     * @param retainPeriod      Retain period for visits
     * @param requiredFields    List of required fields from QUserData
     * @param anyRequiredFields List of "any or" required fields from QUserData
     * @param allowedFields     List of optionally accepted fields from QUserData
     */
    public FederalStateRuleSet(String countryCode, String shortName, Period retainPeriod,
                               List<QUiData> requiredFields, List<QUiData> anyRequiredFields,
                               List<QUiData> allowedFields) {
        this.countryCode = countryCode;
        this.shortName = shortName;
        this.retainPeriod = retainPeriod;
        this.requiredFields = requiredFields;
        this.anyRequiredFields = anyRequiredFields;
        this.allowedFields = allowedFields;
    }

    /**
     * Default no-arg constructor for JPA
     */
    protected FederalStateRuleSet() {
        this.countryCode = null;
        this.shortName = null;
        this.retainPeriod = null;
        this.requiredFields = null;
        this.anyRequiredFields = null;
        this.allowedFields = null;
    }

    /**
     * Getter for FederalStateRuleSet's countryCode
     *
     * @return countryCode as java.lang.String
     */
    public String getCountryCode() {
        return this.countryCode;
    }

    /**
     * Getter for FederalStateRuleSet's shortName
     *
     * @return shortName as java.lang.String
     */
    public String getShortName() {
        return this.shortName;
    }

    /**
     * Getter for FederalStateRuleSet's retainPeriod
     *
     * @return retainPeriod as java.time.Period
     */
    public Period getRetainPeriod() {
        return this.retainPeriod;
    }

    /**
     * Getter for FederalStateRuleSet's requiredFields
     *
     * @return requiredFields as java.util.List<app.qontakt.host.helper.QUserDataFields>
     */
    public List<QUiData> getRequiredFields() {
        return this.requiredFields;
    }

    /**
     * Getter for FederalStateRuleSet's anyRequiredFields
     *
     * @return anyRequiredFields as java.util.List<app.qontakt.host.helper.QUserDataFields>
     */
    public List<QUiData> getAnyRequiredFields() {
        return this.anyRequiredFields;
    }

    /**
     * Getter for FederalStateRuleSet's allowedFields
     *
     * @return allowedFields as java.util.List<app.qontakt.host.helper.QUserDataFields>
     */
    public List<QUiData> getAllowedFields() {
        return this.allowedFields;
    }
}
