package me.rgomes.vertex.lang;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class ThreadLocalProperties {

    private final Properties delegate;

    public ThreadLocalProperties() {
        this.delegate = props.get();
    }

    //
    // public methods
    //

    public void clear() {
        delegate.clear();
    }
    @Override
    public Object clone() {
        return delegate.clone();
    }
    public boolean contains(final Object value) {
        return delegate.contains(value);
    }
    public boolean containsKey(final Object key) {
        return delegate.containsKey(key);
    }
    public boolean containsValue(final Object value) {
        return delegate.containsValue(value);
    }
    public Enumeration<Object> elements() {
        return delegate.elements();
    }
    public Set<Entry<Object, Object>> entrySet() {
        return delegate.entrySet();
    }
    @Override
    public boolean equals(final Object o) {
        return delegate.equals(o);
    }
    public Object get(final Object key) {
        return delegate.get(key);
    }
    public String getProperty(final String key, final String defaultValue) {
        return delegate.getProperty(key, defaultValue);
    }
    public String getProperty(final String key) {
        return delegate.getProperty(key);
    }
    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
    public boolean isEmpty() {
        return delegate.isEmpty();
    }
    public Set<Object> keySet() {
        return delegate.keySet();
    }
    public Enumeration<Object> keys() {
        return delegate.keys();
    }
    public void list(final PrintStream out) {
        delegate.list(out);
    }
    public void list(final PrintWriter out) {
        delegate.list(out);
    }
    public void load(final InputStream inStream) throws IOException {
        delegate.load(inStream);
    }
    public void load(final Reader reader) throws IOException {
        delegate.load(reader);
    }
    public void loadFromXML(final InputStream in) throws IOException, InvalidPropertiesFormatException {
        delegate.loadFromXML(in);
    }
    public Enumeration<?> propertyNames() {
        return delegate.propertyNames();
    }
    public Object put(final Object key, final Object value) {
        return delegate.put(key, value);
    }
    public void putAll(final Map<? extends Object, ? extends Object> t) {
        delegate.putAll(t);
    }
    public Object remove(final Object key) {
        return delegate.remove(key);
    }
    @SuppressWarnings("deprecation")
    public void save(final OutputStream out, final String comments) {
        delegate.save(out, comments);
    }
    public Object setProperty(final String key, final String value) {
        return delegate.setProperty(key, value);
    }
    public int size() {
        return delegate.size();
    }
    public void store(final OutputStream out, final String comments) throws IOException {
        delegate.store(out, comments);
    }
    public void store(final Writer writer, final String comments) throws IOException {
        delegate.store(writer, comments);
    }
    public void storeToXML(final OutputStream os, final String comment, final String encoding) throws IOException {
        delegate.storeToXML(os, comment, encoding);
    }
    public void storeToXML(final OutputStream os, final String comment) throws IOException {
        delegate.storeToXML(os, comment);
    }
    public Set<String> stringPropertyNames() {
        return delegate.stringPropertyNames();
    }
    @Override
    public String toString() {
        return delegate.toString();
    }
    public Collection<Object> values() {
        return delegate.values();
    }


    //
    // private static inner classes
    //

    //
    // Employs a ThreadLocal object in order to keep thread dependent data.
    //
    // In spite <code>props</code> seems to be static and, for this reason, should return the same contents
    // whatever piece of code makes use of it, in fact, what happens is that ThreadLocal internally organises
    // data using a thread id (or something like this) as a key in order to obtain the actual thread dependent
    // data. When the user code calls the <code>get()</code> method (without any argument), ThreadLocal returns
    // the actual Properties object allocated for that thread because the thread id (or something like this)
    // was employed to obtain the corresponding Properties object for that specific caller thread.
    // [Richard Gomes]
    //
    private static final ThreadLocal<Properties> props = new ThreadLocal<Properties>() {
        @Override
        public Properties initialValue() {
            return new Properties();
        }
    };

}


