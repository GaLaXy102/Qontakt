package app.qontakt.host.helper;

import app.qontakt.host.lokal.LokalData;
import app.qontakt.host.rules.FederalStateRuleSet;
import app.qontakt.user.Visit;
import app.qontakt.user.identity.QUserData;
import com.lowagie.text.DocumentException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;
import org.springframework.data.util.Pair;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Base64;
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

    private static String generateQrCodePngBase64(String data) {
        return Base64.getEncoder().encodeToString(QRCode.from(data).to(ImageType.PNG).withSize(500, 500).stream().toByteArray());
    }

    public static byte[] renderLeaflet(Locale locale, LokalData lokalData, String baseurl) throws IOException {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(new Java8TimeDialect());
        Context context = new Context();
        context.setLocale(locale);
        context.setVariable("lokal", lokalData);
        context.setVariable("lokalqr", generateQrCodePngBase64(lokalData.getLokalUid()));
        context.setVariable("qontaktqr", generateQrCodePngBase64(baseurl));
        String html = templateEngine.process("lokal_data", context);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        new PdfRendererBuilder()
                .useFastMode()
                .useProtocolsStreamImplementation(new ClassPathStreamFactory(), "classpath")
                .withHtmlContent(html, "classpath:/templates/")
                .toStream(os)
                .run();
        return os.toByteArray();
    }
}
