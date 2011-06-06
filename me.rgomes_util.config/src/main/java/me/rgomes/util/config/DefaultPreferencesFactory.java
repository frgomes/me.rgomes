package me.rgomes.util.config;

import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

/**
 * This is a PreferencesFactory which instantiates our DefaultPreferences class.
 *
 * @author Richard Gomes <rgomes1997@yahoo.co.uk>
 */
public class DefaultPreferencesFactory implements PreferencesFactory {

	public Preferences userRoot() {
		return new DefaultPreferences();
	}

	public Preferences systemRoot() {
		return new DefaultPreferences();
	}
}
