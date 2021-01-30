package app.qontakt.host.rules;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data structure to save the rulesets in all FederalStates
 */
@Repository
public interface FederalStateRuleSetRepository extends CrudRepository<FederalStateRuleSet, FederalStateRuleSet.Code> {

    Optional<FederalStateRuleSet> findByCountryCodeAndShortName(String countryCode, String shortName);

    @Override
    Streamable<FederalStateRuleSet> findAll();

}
