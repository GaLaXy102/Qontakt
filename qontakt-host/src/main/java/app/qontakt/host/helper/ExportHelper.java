package app.qontakt.host.helper;

import app.qontakt.user.Visit;
import app.qontakt.user.identity.QUserData;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ExportHelper {

    /**
     * Chronologically sort Visits
     *
     * @param data Map<QUserData, List<Visit>>
     * @return List<Pair < QUserData, Visit>> sorted by time of visit
     */
    static List<Pair<QUserData, Visit>> sortChronological(Map<QUserData, List<Visit>> data) {
        List<Pair<QUserData, Visit>> result = new ArrayList<>(data.size());
        data.keySet().forEach(user ->
                data.get(user)
                        .stream()
                        .map(v -> Pair.of(user, v))
                        .forEach(result::add)
        );
        result.sort(Comparator.comparing(p -> p.getSecond().getCheckIn()));
        return result;
    }

}
