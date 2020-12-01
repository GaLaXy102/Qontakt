package app.qontakt.host.rules;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Load RuleSets into Database, update any existing data
 */
@Component
public class FederalStateRuleSetsLoader {

    private final FederalStateRuleSetRepository federalStateRuleSetRepository;

    public FederalStateRuleSetsLoader(FederalStateRuleSetRepository federalStateRuleSetRepository) {
        this.federalStateRuleSetRepository = federalStateRuleSetRepository;
        this.run();
    }

    @Transactional
    void run() {
        LoggerFactory.getLogger(FederalStateRuleSetsLoader.class).info("Loading rules from Built-in Data Source.");
        this.federalStateRuleSetRepository.saveAll(FederalStateRuleSetData.allRules);
        LoggerFactory.getLogger(FederalStateRuleSetsLoader.class).info(String.format("I currently know %d rules.",
                this.federalStateRuleSetRepository.count()));
    }
}
