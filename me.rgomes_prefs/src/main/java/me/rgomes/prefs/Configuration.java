package me.rgomes.prefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.NodeChangeListener;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

/**
 * Returns a Preferences object populated with properties read from
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public class Configuration {

    public Preferences systemRoot() {
        System.setProperty("java.util.prefs.PreferencesFactory", me.rgomes.prefs.PreferencesFactory.class.getName());
        try {
            Preferences prefs = Preferences.systemRoot();
            return new PreferencesHelper(prefs);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load system preferences", e);
        }
    }

    public Preferences userRoot() {
        System.setProperty("java.util.prefs.PreferencesFactory", me.rgomes.prefs.PreferencesFactory.class.getName());
        try {
            Preferences prefs = Preferences.userRoot();
            return new PreferencesHelper(prefs);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load user preferences", e);
        }
    }

    public Preferences systemNodeForPackage(java.lang.Class<?> klass) {
        System.setProperty("java.util.prefs.PreferencesFactory", me.rgomes.prefs.PreferencesFactory.class.getName());
        try {
            Preferences prefs = Preferences.systemNodeForPackage(klass);
            return new PreferencesHelper(prefs);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Cannot load system preferences for class %s", klass.getName()));
        }
    }

    public Preferences userNodeForPackage(java.lang.Class<?> klass) {
        System.setProperty("java.util.prefs.PreferencesFactory", me.rgomes.prefs.PreferencesFactory.class.getName());
        try {
            Preferences prefs = Preferences.userNodeForPackage(klass);
            return new PreferencesHelper(prefs);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Cannot load user preferences for class %s", klass.getName()));
        }
    }

    public void importPreferences(InputStream is) throws IOException, InvalidPreferencesFormatException {
        throw new IOException("Backing store is read-only");
    }


    //
    // private inner classes
    //

    private class PreferencesHelper extends Preferences implements Preferences_ {

        private final Preferences delegate;

        private PreferencesHelper(final Preferences prefs) {
            this.delegate = prefs;
        }


        //
        // Override abstract methods of Preferences
        //

        @Override
        public String toString() {
            return delegate.toString();
        }

        @Override
        public void sync() throws BackingStoreException {
            delegate.sync();
        }

        @Override
        public void removePreferenceChangeListener(PreferenceChangeListener pcl) {
            delegate.removePreferenceChangeListener(pcl);
        }

        @Override
        public void removeNodeChangeListener(NodeChangeListener ncl) {
            delegate.removeNodeChangeListener(ncl);
        }

        @Override
        public void removeNode() throws BackingStoreException {
            delegate.removeNode();
        }

        @Override
        public void remove(String key) {
            delegate.remove(key);
        }

        @Override
        public void putLong(String key, long value) {
            delegate.putLong(key, value);
        }

        @Override
        public void putInt(String key, int value) {
            delegate.putInt(key, value);
        }

        @Override
        public void putFloat(String key, float value) {
            delegate.putFloat(key, value);
        }

        @Override
        public void putDouble(String key, double value) {
            delegate.putDouble(key, value);
        }

        @Override
        public void putByteArray(String key, byte[] value) {
            delegate.putByteArray(key, value);
        }

        @Override
        public void putBoolean(String key, boolean value) {
            delegate.putBoolean(key, value);
        }

        @Override
        public void put(String key, String value) {
            delegate.put(key, value);
        }

        @Override
        public Preferences parent() {
            return delegate.parent();
        }

        @Override
        public boolean nodeExists(String pathName) throws BackingStoreException {
            return delegate.nodeExists(pathName);
        }

        @Override
        public Preferences node(String pathName) {
            return delegate.node(pathName);
        }

        @Override
        public String name() {
            return delegate.name();
        }

        @Override
        public String[] keys() throws BackingStoreException {
            return delegate.keys();
        }

        @Override
        public boolean isUserNode() {
            return delegate.isUserNode();
        }

        @Override
        public long getLong(String key, long def) {
            return delegate.getLong(key, def);
        }

        @Override
        public int getInt(String key, int def) {
            return delegate.getInt(key, def);
        }

        @Override
        public float getFloat(String key, float def) {
            return delegate.getFloat(key, def);
        }

        @Override
        public double getDouble(String key, double def) {
            return delegate.getDouble(key, def);
        }

        @Override
        public byte[] getByteArray(String key, byte[] def) {
            return delegate.getByteArray(key, def);
        }

        @Override
        public boolean getBoolean(String key, boolean def) {
            return delegate.getBoolean(key, def);
        }

        @Override
        public String get(String key, String def) {
            return delegate.get(key, def);
        }

        @Override
        public void flush() throws BackingStoreException {
            delegate.flush();
        }

        @Override
        public void exportSubtree(OutputStream os) throws IOException, BackingStoreException {
            delegate.exportSubtree(os);
        }

        @Override
        public void exportNode(OutputStream os) throws IOException, BackingStoreException {
            delegate.exportNode(os);
        }

        @Override
        public void clear() throws BackingStoreException {
            delegate.clear();
        }

        @Override
        public String[] childrenNames() throws BackingStoreException {
            return delegate.childrenNames();
        }

        @Override
        public void addPreferenceChangeListener(PreferenceChangeListener pcl) {
            delegate.addPreferenceChangeListener(pcl);
        }

        @Override
        public void addNodeChangeListener(NodeChangeListener ncl) {
            delegate.addNodeChangeListener(ncl);
        }

        @Override
        public String absolutePath() {
            return delegate.absolutePath();
        }

        /*
        public static Preferences systemRoot() {
            return delegate.systemRoot();
        }

        public static Preferences systemNodeForPackage(java.lang.Class<?> c) {
            return delegate.systemNodeForPackage(c);
        }

        public static Preferences userRoot() {
            return delegate.userRoot();
        }

        public static Preferences userNodeForPackage(java.lang.Class<?> c) {
            return delegate.userNodeForPackage(c);
        }

        public static void importPreferences(InputStream is) throws IOException, InvalidPreferencesFormatException {
            delegate.importPreferences(is);
        }
        */
    }



}
