package app.qontakt.host.uihelper;

import app.qontakt.host.lokal.LokalData;
import app.qontakt.host.rules.FederalStateRuleSet;
import app.qontakt.host.rules.FederalStateRuleSetData;
import app.qontakt.user.Visit;
import app.qontakt.user.identity.QUserData;
import com.lowagie.text.DocumentException;
import org.springframework.data.util.Pair;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ThymeleafPdfPrinter {

    /**
     * Chronologically sort Visits
     * @param data Map<QUserData, List<Visit>>
     * @return List<Pair<QUserData, Visit>> sorted by time of visit
     */
    private static List<Pair<QUserData, Visit>> sortChronological(Map<QUserData, List<Visit>> data) {
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

    public static byte[] renderContactTracingPdf(Locale locale, LokalData lokal, Map<QUserData, List<Visit>> visits) throws DocumentException {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setLocale(locale);
        context.setVariable("lokal", lokal);
        context.setVariable("visits", ThymeleafPdfPrinter.sortChronological(visits));
        String html = templateEngine.process("visit_data", context);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(os);

        return os.toByteArray();
    }
}
