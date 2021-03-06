package app.qontakt.host.helper;

import com.openhtmltopdf.extend.FSStream;

import java.net.URI;
import java.net.URISyntaxException;

// From https://github.com/aleks-shbln/separate-resources-example/blob/master/src/main/java/io/ashabalin/ohtp/separateresources/pdf/ClassPathStreamFactory.java
public class ClassPathStreamFactory implements com.openhtmltopdf.extend.FSStreamFactory {

    @Override
    public FSStream getUrl(String uri) {
        try {
            final URI fullUri = new URI(uri);
            return new ClassPathStream(fullUri.getPath());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

}
