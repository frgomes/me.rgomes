package me.rgomes.prefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This class provides utilities methods which loads properties files
 * employing the <code>conf:</code> protocol.
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public class Configuration {

    private final Properties props;

    /**
     * Creates a configuration from properties files read from
     * <code>application.properties</code> and <code>password.properties</code>, loaded
     * in this order, employing the <code>conf:</code> protocol.
     */
    public Configuration() throws MalformedURLException, IOException {
        Properties p = new Properties();
        p = properties(p, "application.properties");
        p = properties(p, "password.properties");
        p = properties(p, ""); // loads/overrides properties "username", "realm" and "mode"
        this.props = p;
    }

    /**
     * Returns a Properties populated from a file retrieved by the <code>conf:</code> protocol.
     * <p>
     * <b>Notes:</b> The <code>conf:</code> is always forced and should never be mentioned.
     */
    public Configuration(String name) throws MalformedURLException, IOException {
        final Properties p = properties(new Properties(), name);
        this.props = p;
    }

    /**
     * Returns Properties populated from a list of files retrieved by the <code>conf:</code> protocol.
     * <p>
     * <b>Note:</b> The <code>conf:</code> is always forced and should never be mentioned.
     */
    public Configuration(final List<String> names) throws MalformedURLException, IOException {
        Properties p = new Properties();
        if (names != null && names.size()>0) {
            for (String name : names) {
                p = properties(p, name);
            }
        }
        this.props = p;
    }

    public Properties asProperties() {
        return this.props;
    }

    public Map<String,String> asUnmodifiableMap() {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Map<String, String> map = new HashMap(this.props);
        return Collections.<String,String>unmodifiableMap(map);
    }

    private Properties properties(Properties props, String name) throws MalformedURLException, IOException {
        final String conf = "conf:".concat(name);
        // load application.properties
        final URLStreamHandler handler = new me.rgomes.protocols.conf.Handler();
        final InputStream is = new URL(null, conf, handler).openStream();
        if (is != null) props.load(is);
        return props;
    }

    public void dump() throws IOException {
        dump(java.lang.System.out, asUnmodifiableMap());
    }

    public void dump(final PrintStream ps) {
        dump(ps, asUnmodifiableMap());
    }

    private void dump(final PrintStream ps, Map<String,String> map) {
        try {
            for (final String key : map.keySet()) {
                String value = map.get(key);
                ps.println(String.format("%s=%s", key, value));
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot dump preferences", e);
        }
    }

}
