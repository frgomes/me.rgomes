package me.rgomes.prefs;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Returns a Properties object populated with properties read from
 * files <code>application.properties</code>, <code>persistence.properties</code> and
 * <code>password.properties</code>, loaded in this order.
 * <p>
 * If you pass a list of file names to be loaded, they will be loaded just before
 * <code>passoword.properties</code>, which guarantees that sensitive data defined in
 * this file will always override previous definitions.
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public class Configuration {

    public Properties properties() throws MalformedURLException, IOException {
        return properties((List<String>)null);
    }

    public Properties properties(String name) throws MalformedURLException, IOException {
        List<String> names = new ArrayList<String>();
        names.add(name);
        return properties(names);
    }

    public Properties properties(final List<String> names) throws MalformedURLException, IOException {
        Properties props;
        props = properties(new Properties(), "application.properties");
        props = properties(props, "persistence.properties");
        if (names != null && names.size()>0) {
            for (String name : names) {
                props = properties(props, name);
            }
        }
        props = properties(props, "password.properties");
        props = properties(props, ""); // loads/overrides properties "username", "realm" and "mode"
        return props;
    }

    private Properties properties(Properties props, String name) throws MalformedURLException, IOException {
        final String conf = "conf:".concat(name);
        // load application.properties
        final URLStreamHandler handler = new me.rgomes.protocols.conf.Handler();
        final InputStream is = new URL(null, conf, handler).openStream();
        if (is != null) props.load(is);
        return props;
    }

}
