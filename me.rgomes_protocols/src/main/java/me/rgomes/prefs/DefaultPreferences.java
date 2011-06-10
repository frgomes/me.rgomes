package me.rgomes.prefs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;
import me.rgomes.protocols.conf.ResourceResolver;




/**
 * Returns Preferences retrieved by the <i>conf:</i> protocol as below:
 * <pre>
 *     conf:application.properties
 *     conf:password.properties
 * <p>
 * In a nutshell, the <i>conf:</i> allows transparent access to
 * path <i>conf/${realm}/${mode}</i>, does not matter if this path can be found in
 * as a subfolder of the application folder, as a subfolder packaged inside a .jar file
 * or as a subfolder mapped under the Eclipse build path.
 * <p>
 * There are 2 ways to define variables <i>realm</i> and <i>mode</i>:
 * <ul>
 * <li>
 * Entries in MANIFEST.MF
 * </li>
 * <li>
 * Properties present in file conf/global.properties
 * </li>
 * </ul>
 * <p>
 * Once <i>realm</i> and <i>mode</i> are determined, properties are then read from the files
 * listed below, if they exist:
 * <ul>
 * <i>conf/${realm}/${mode}/application.properties</i> - this file is intended to contain
 * all properties an application needs
 * <li>
 * </li>
 * <li>
 * <i>conf/${realm}/${mode}/password.properties</i> - properties defined in this file override
 * properties loaded from application.properties. This feature allows sensitive information to be provided
 * by production support personnel, overriding configurations provided by developers.
 * </li>
 * </ul>
 * <p>
 * <b>NOTES:<b>
 * <ul>
 * <li>
 * Only properties <i>debug</i> and <i>mode</i> are taken from file <i>etc/global.properties</i>. All other
 * properties are discarded.
 * </li>
 * <li>
 * Do not confuse <i>realm<b> with <i>username</i>. You can specify a realm as an entry in the
 * MANIFEST.MF file or force it in file <i>conf/global.properties</i>. If you do not specify
 * anything, your <i>realm<i> is obtained by <code>System.getProperty("user.name", "anonymous")</code>.
 * </li>
 * </ul>
 *
 * @author Richard Gomes <rgomes1997@yahoo.co.uk>
 */
public class DefaultPreferences extends AbstractPreferences {

	/**
	 * The data in this node.
	 */
	private final Properties props;

	/**
	 * Prefix property names during retrieval
	 */
	private final String prefix;

	/**
	 * Create the root node for the file-based preferences.
	 */
	public DefaultPreferences() {
		super(null, "");
		this.prefix = "";
        this.props = new Properties();

        try {
            final ResourceResolver resolver = OnDemandLoader.instance.getResolver();
            InputStream is;

            // load application.properties
            is = resolver.getInputStream("application.properties");
            if (is != null) props.load(is);

            // load password.properties
            is = resolver.getInputStream("password.properties");
            if (is != null) props.load(is);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load etc/uration", e);
		}
	}

	public DefaultPreferences(final DefaultPreferences parent, final String name) {
		super(parent, name);
		this.prefix = (parent.prefix.isEmpty()) ? name.concat(".") : parent.prefix.concat(name).concat(".");
		this.props = parent.props;
	}


    @Override
	public boolean isUserNode() {
		// For now file preferences are always user nodes.
		return true;
	}

    @Override
	protected String[] childrenNamesSpi() throws BackingStoreException {
		// Does not support children nodes
		return new String[0];
	}

    @Override
	protected AbstractPreferences childSpi(String name) {
		return new DefaultPreferences(this, name);
	}

    @Override
	protected String[] keysSpi() throws BackingStoreException {
		return (String[]) props.keySet().toArray(new String[0]);
	}

    @Override
	protected String getSpi(String key) {
		return props.getProperty(prefix.concat(key));
	}

    @Override
	protected void putSpi(String key, String value) {
		props.put(key, value);
	}

    @Override
	protected void removeSpi(String key) {
		// removal os keys is prohibited due to security reasons
		throw new UnsupportedOperationException();
	}

    @Override
	protected void flushSpi() throws BackingStoreException {
		throw new BackingStoreException("Backing store is read-only");
	}

    @Override
	protected void syncSpi() throws BackingStoreException {
		flushSpi();
	}

    @Override
	protected void removeNodeSpi() throws BackingStoreException {
		// We can simply delegate.
		flushSpi();
	}


    //
    // private inner classes
    //

    private static class OnDemandLoader {
        public static final OnDemandLoader instance = new OnDemandLoader();

        private ResourceResolver getResolver() throws IOException {
            return new ResourceResolver();
        }
    }

}
