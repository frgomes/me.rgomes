package me.rgomes.util.config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.rgomes.util.net.url.ResourceLookup;



/**
 * This class employs ResourceLookup in order to load configurations from
 * properties files as specified below:
 * <pre>
 *     ./config/${realm}/${mode}/application.properties
 *     ./config/${realm}/${mode}/password.properties
 * </pre>
 * <p>
 * where <i>realm</i> and <i>mode</i> are obtained from MANIFEST.MF file.
 * In case the manifest file is not found, <i>realm</i> assumes "default" and
 * <i>mode</i> assumes "debug".
 *
 * @author Richard Gomes <rgomes1997@yahoo.co.uk>
 */
public class DefaultPreferences extends AbstractPreferences {

	private static final Logger logger = LoggerFactory.getLogger(DefaultPreferences.class);

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

        // Try to read realm and mode from MANIFEST.MF
        String realm = "default";
        String mode  = "debug";
        try {
            Manifest mf = new Manifest();
            realm = mf.getAttribute("realm", realm);
            mode  = mf.getAttribute("mode", mode);
        } catch (Exception e) {
            // discard
        }

        this.props = new Properties();
		try {
            InputStream is;
            // load application.properties
            is = getInputStream(String.format("config/%s/%s/application.properties", realm, mode));
            if (is != null) props.load(is);
            // load password.properties
            is = getInputStream(String.format("config/%s/%s/password.properties", realm, mode));
            if (is != null) props.load(is);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load configuration", e);
		}
	}

	public DefaultPreferences(final DefaultPreferences parent, final String name) {
		super(parent, name);
		this.prefix = (parent.prefix.isEmpty()) ? name.concat(".") : parent.prefix.concat(name).concat(".");
		this.props = parent.props;
	}


    private InputStream getInputStream(String name) {
        try {
			logger.info(String.format("Loading configuration: '%s'", name));
            return new ResourceLookup(name).openInputStream();
        } catch (FileNotFoundException e) {
			logger.warn(String.format("Failed to load configuration '%s'", name), e);
            return null;
        }
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
}
