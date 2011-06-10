
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
public class TestProtocols {

    @Ignore
    @Test
    public void testFile() throws MalformedURLException, IOException {
        System.out.println(System.getProperty("java.protocol.handler.pkgs"));
        String path = "file://conf/global.properties";
        System.out.println(path);
        System.out.println("=============================================");

        // System.setProperty("java.protocol.handler.pkgs", "me.rgomes.protocols");

        URL url = new URL(path);
        dump(url.openStream());
    }

    @Ignore
    @Test
    public void testConf() throws MalformedURLException, IOException {
        System.out.println(System.getProperty("java.protocol.handler.pkgs"));
        String path = "conf:application.properties";
        System.out.println(path);
        System.out.println("=============================================");
        URLStreamHandler handler = new me.rgomes.protocols.conf.Handler();

        // System.setProperty("java.protocol.handler.pkgs", "me.rgomes.protocols");

        URL url = new URL(path);
        dump(url.openStream());
    }

    @Ignore
    @Test
    public void testClasspath() throws MalformedURLException, IOException {
        System.out.println(System.getProperty("java.protocol.handler.pkgs"));
        String path = "classpath:me/rgomes/protocols/conf/package.html";
        System.out.println(path);
        System.out.println("=============================================");

        // System.setProperty("java.protocol.handler.pkgs", "me.rgomes.protocols");

        URL url = new URL(path);
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
