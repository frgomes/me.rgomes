/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.rgomes.prefs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.NodeChangeListener;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

/**
 * This interface exposes abstract methods declared by class java.util.prefs.Preferences
 * <p>
 * This package does not use this interface. It is provided only as the sake of convenience.
 *
 * @author Richard Gomes <rgomes1997@yahoo.co.uk>
 */
public interface Preferences_ {

    String absolutePath();

    void addNodeChangeListener(NodeChangeListener ncl);

    void addPreferenceChangeListener(PreferenceChangeListener pcl);

    String[] childrenNames() throws BackingStoreException;

    void clear() throws BackingStoreException;

    void exportNode(OutputStream os) throws IOException, BackingStoreException;

    void exportSubtree(OutputStream os) throws IOException, BackingStoreException;

    void flush() throws BackingStoreException;

    String get(String key, String def);

    boolean getBoolean(String key, boolean def);

    byte[] getByteArray(String key, byte[] def);

    double getDouble(String key, double def);

    float getFloat(String key, float def);

    int getInt(String key, int def);

    long getLong(String key, long def);

    boolean isUserNode();

    String[] keys() throws BackingStoreException;

    String name();

    Preferences node(String pathName);

    boolean nodeExists(String pathName) throws BackingStoreException;

    Preferences parent();

    void put(String key, String value);

    void putBoolean(String key, boolean value);

    void putByteArray(String key, byte[] value);

    void putDouble(String key, double value);

    void putFloat(String key, float value);

    void putInt(String key, int value);

    void putLong(String key, long value);

    void remove(String key);

    void removeNode() throws BackingStoreException;

    void removeNodeChangeListener(NodeChangeListener ncl);

    void removePreferenceChangeListener(PreferenceChangeListener pcl);

    void sync() throws BackingStoreException;

    String toString();

}
