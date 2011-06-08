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
 * This class employs ResourceLookup for loading configurations from properties files.
 * <p>
 * The first step consists in obtaining the default <i>realm</i> and default <i>mode</i>.
 * The default <i>realm</i> is obtained from current logged in user or "anonymous" is
 * assumed in case it cannot be obtained.
 * <pre>
 *     System.getProperty("user.name", "anonymous")
 * </pre>
 * The default <i>mode</i> is "debug".
 * <p>
 * If <i>realm</i> and/or <i>mode</i> are found as entries in the MANIFEST.MF, their
 * values override values previously found in the previous step. This feature permits that
 * defaults values are defined by an automated build process.
 * <p>
 * If <i>config/global.properties</i> file exists and <i>realm</i> and/or <i>mode</i> are
 * found as entries in this file, these values override values previously found in previous
 * steps. This feature permits that these values can be forced by the user.
 * <p>
 * Once <i>realm</i> and <i>mode</i> are determined, additional properties are read from
 * these files, if they exist:
 * <pre>
 *     ./config/${realm}/${mode}/application.properties
 * </pre>
 * This file is intended to contain all properties an application needs. These properties can
 * be organised hierarchically.
  * <pre>
 *     ./config/${realm}/${mode}/password.properties
 * </pre>
 * Properties defined in password.properties may override values previously loaded
 * from application.properties. This feature allows sensitive information to be provided
 * by production support personnel, overriding configurations provided by developers.
 * <p>
 * <b>NOTES:<b>
 * <ul>
 * <li>
 * File <i>config/global.properties</i> can contain as much properties as user wishes, in spite
 * this is not a good practice.
 * </li>
 * <li>
 * Do not confuse <i>realm<b> with <i>username</i>. You can specify a realm as an entry in the
 * MANIFEST.MF file or force it in file <i>config/global.properties</i>. You you do not specify
 * anything, your <i>realm<i> is obtained by <code>System.getProperty("user.name", "anonymous")</code>
 * which tried to obtain your <i>username</i>. If all fails, your <i>realm</i> is assumed as
 * <code>anonymous</code>.
 * </li>
 * </ul>
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

        // Try to read default realm and mode from MANIFEST.MF
        String mrealm = DefaultPreferencesFactory.USERNAME;
        String mmode  = "debug";
        try {
            Manifest mf = new Manifest();
            mrealm = mf.getAttribute("realm", mrealm);
            mmode  = mf.getAttribute("mode",  mmode);
        } catch (Exception e) {
            // discard
        }

        this.props = new Properties();
		try {
            InputStream is;

            // Try to read realm and mode from config/global.properties
            is = getInputStream("config/global.properties");
            if (is != null) props.load(is);
            String realm = props.getProperty("realm", mrealm);
            String mode  = props.getProperty("mode",  mmode);

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
