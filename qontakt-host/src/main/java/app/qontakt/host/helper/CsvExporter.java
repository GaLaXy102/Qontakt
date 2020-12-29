package app.qontakt.host.helper;

import app.qontakt.host.lokal.LokalData;
import app.qontakt.user.Visit;
import app.qontakt.user.identity.QUserData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.util.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CsvExporter {

    public static byte[] exportToCsv(LokalData lokal, Map<QUserData, List<Visit>> visits) {
        List<Pair<QUserData, Visit>> sorted = ExportHelper.sortChronological(visits);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            CSVPrinter csv = CSVFormat.DEFAULT.withHeader(QUiData.class).print(new OutputStreamWriter(out));
            out.writeBytes(
                    "# %s, %s, %s; %s\n".formatted(
                            lokal.getName(),
                            lokal.getAddress(),
                            lokal.getGdprContact(),
                            lokal.getFederalState().toString()
                    ).getBytes(StandardCharsets.UTF_8)
            );
            List<List<String>> printable = sorted.stream().map(p -> {
                String checkIn = p.getSecond().getCheckIn().toString();
                String checkOut = p.getSecond().getCheckOut().toString();
                String firstName = p.getFirst().getFirstName();
                String lastName = p.getFirst().getLastName();
                String addressLine = p.getFirst().getHomeAddress();
                String addressZip = p.getFirst().getHomeZip();
                String addressCity = p.getFirst().getHomeCity();
                String phoneNumber = p.getFirst().getTelephoneNumber();
                String email = p.getFirst().getEmail();
                return List.of(checkIn, checkOut, firstName, lastName, addressLine, addressZip, addressCity, phoneNumber, email);
            }).collect(Collectors.toList());
            csv.printRecords(printable);
            csv.close(true);
        } catch (IOException e) {
            // This exception should not happen in this use case.
        }
        return out.toByteArray();
    }

}
