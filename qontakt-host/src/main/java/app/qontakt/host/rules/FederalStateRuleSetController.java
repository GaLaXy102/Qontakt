package app.qontakt.host.rules;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/host/rules")
public class FederalStateRuleSetController {

    private final FederalStateRuleSetService federalStateRuleSetService;

    public FederalStateRuleSetController(FederalStateRuleSetService federalStateRuleSetService) {
        this.federalStateRuleSetService = federalStateRuleSetService;
    }

    @Operation(summary = "Get a Map countryCode -> federalStateCode for all states, where the rules are known")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Map of known mappings")
    })
    @GetMapping("/known")
    public ResponseEntity<Map<String, List<String>>> getKnownStates() {
        return ResponseEntity.ok(this.federalStateRuleSetService.getKnownRuleSetsMap());
    }
}
