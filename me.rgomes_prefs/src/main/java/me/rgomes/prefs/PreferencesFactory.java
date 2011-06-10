package me.rgomes.prefs;


/**
 * This is a PreferencesFactory which instantiates our DefaultPreferences class.
 *
 * @author Richard Gomes <rgomes1997@yahoo.co.uk>
 */
public class PreferencesFactory implements java.util.prefs.PreferencesFactory {

    // This variable is intentionally package-protected
    static final String USERNAME = System.getProperty("user.name", "anonymous");

    @Override
	public java.util.prefs.Preferences systemRoot() {
		return new me.rgomes.prefs.Preferences();
	}

    @Override
	public java.util.prefs.Preferences userRoot() {
		return new me.rgomes.prefs.Preferences(new me.rgomes.prefs.Preferences(), USERNAME);
	}

}
