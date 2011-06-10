
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import org.junit.Ignore;
import org.junit.Test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public class TestHandlers {

    @Test
    public void testFile() throws MalformedURLException, IOException {
        String path = "file://conf/global.properties";
        System.out.println(path);
        System.out.println("=============================================");
        URLStreamHandler handler = new me.rgomes.protocols.file.Handler();
        URL url = new URL(null, path, handler);
        dump(url.openStream());
    }

    @Test
    public void testConf() throws MalformedURLException, IOException {
        String path = "conf:application.properties";
        System.out.println(path);
        System.out.println("=============================================");
        URLStreamHandler handler = new me.rgomes.protocols.conf.Handler();
        URL url = new URL(null, path, handler);
        dump(url.openStream());
    }

    @Ignore
    @Test
    public void testClasspath() throws MalformedURLException, IOException {
        String path = "classpath:me/rgomes/protocols/conf/package.html";
        System.out.println(path);
        System.out.println("=============================================");
        URLStreamHandler handler = new me.rgomes.protocols.classpath.Handler();
        URL url = new URL(null, path, handler);
        dump(url.openStream());
    }

    private void dump(final InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
    }

}
