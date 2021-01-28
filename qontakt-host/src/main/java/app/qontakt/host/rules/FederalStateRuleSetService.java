package app.qontakt.host.rules;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service to handle Rule Sets
 */
@Component
public class FederalStateRuleSetService {

    private final FederalStateRuleSetRepository federalStateRuleSetRepository;

    public FederalStateRuleSetService(FederalStateRuleSetRepository federalStateRuleSetRepository) {
        this.federalStateRuleSetRepository = federalStateRuleSetRepository;
    }

    /**
     * Calculate a mapping of countryCode to federal states where Qontakt knows the rules
     * @return Map from Country to FederalState
     */
    public Map<String, List<String>> getKnownRuleSetsMap() {
        return this.federalStateRuleSetRepository.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(FederalStateRuleSet::getCountryCode,
                                Collectors.mapping(FederalStateRuleSet::getShortName, Collectors.toList())
                        )
                );
    }
}

