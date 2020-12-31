package app.qontakt.host.helper;

import app.qontakt.host.lokal.LokalData;
import app.qontakt.user.Visit;
import app.qontakt.user.identity.QUserData;
import com.lowagie.text.DocumentException;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ThymeleafPdfPrinter {

    public static byte[] renderContactTracingPdf(Locale locale, LokalData lokal, Map<QUserData, List<Visit>> visits) throws DocumentException {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(new Java8TimeDialect());

        Context context = new Context();
        context.setLocale(locale);
        context.setVariable("lokal", lokal);
        List<Pair<QUserData, Visit>> visitList = ExportHelper.sortChronological(visits);
        context.setVariable("visits", visitList);
        LoggerFactory.getLogger(ThymeleafPdfPrinter.class).info("Printing [%d] datasets for %s.".formatted(visitList.size(), lokal.getLokalUid()));
        String html = templateEngine.process("visit_data", context);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(os);

        return os.toByteArray();
    }

}
