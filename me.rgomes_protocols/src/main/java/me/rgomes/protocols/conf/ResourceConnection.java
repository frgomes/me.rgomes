package me.rgomes.protocols.conf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLStreamHandler;
import java.util.Properties;
import java.util.jar.Manifest;

import me.rgomes.protocols.Constants;


public class ResourceConnection extends URLConnection {

    private final String path;
    private InputStream  is;

    public ResourceConnection(final URL url) throws MalformedURLException, IOException {
        super(url);

        String s = url.getHost().concat(url.getPath());
        if (Boolean.getBoolean(Constants.DECODE_URL)) {
            s = URLDecoder.decode(s, "UTF-8");
        }

        // Convert the url '/' to the os file separator
        this.path = s.replace('/', File.separatorChar).replace('|', ':');

        doOutput = false;
    }

    @Override
    public void connect() throws IOException {
        if (connected) return;

        final ResourceResolver resolver = OnDemandLoader.instance.getResolver();
        this.is = resolver.getInputStream(path);

        connected = true;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (!connected) connect();
        return is;
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


    //
    // private inner classes
    //

    private static class ResourceResolver {

        private final String username;
        private final String realm;
        private final String mode;

        public ResourceResolver() throws IOException {
            this.username = System.getProperty("user.name", "anonymous");

            // These are the defauls
            String drealm = username();
            String dmode = "debug";
            // Try to read default realm and mode from MANIFEST.MF
            String mrealm;
            String mmode;
            try {
                URLStreamHandler handler = new me.rgomes.protocols.conf.Handler();
                Manifest mf = new Manifest(new URL(null, "META-INF/MANIFEST.MF", handler).openStream());
                mrealm = mf.getMainAttributes().getValue("realm");
                mmode  = mf.getMainAttributes().getValue("mode");
                if (mrealm == null) mrealm = drealm;
                if (mmode  == null) mmode  = dmode;
            } catch (Exception e) {
                mrealm = drealm;
                mmode  = dmode;
            }
            // Try to read realm and mode from config/global.properties
            final InputStream is = new Resource("conf/global.properties").openInputStream();
            final Properties props = new Properties();
            if (is != null) {
                props.load(is);
            }
            this.realm = props.getProperty("realm", mrealm);
            this.mode = props.getProperty("mode", mmode);
        }

        public String username() {
            return username;
        }

        public String getMode() {
            return mode;
        }

        public String getRealm() {
            return realm;
        }

        public InputStream getInputStream(String name) throws IOException {
            if (name!=null && name.length()>0) {
                final String path = String.format("conf/%s/%s/%s", realm, mode, name);
                return new Resource(path).openInputStream();
            } else {
                byte[] bytes = String.format("username=%s\nrealm=%s\nmode=%s\n", username, realm, mode).getBytes();
                return new ByteArrayInputStream(bytes);
            }
        }

    }


    /**
     * This class is intended to solve the burden related to looking up resources from
     * different locations depending on the environment the application is running on.
     * <p/>
     * This is the search order:
     * <p/>
     * <ul>
     * <li>
     * Relative to the current working directory.
     * </li>
     * <li>
     * Relative to the classpath.
     * </li>
     * <li>
     * Folders ./target/classes and ./target/test-classes are considered, once these folder locations where Eclipse put
     * generated classes and resources. When an application is packaged, resources end up in a .jar file, which will be
     * certainly part of the classpath (otherwise nothing will work!) but whilst in development mode, these folders need
     * to be included.
     * </li>
     * </ul>
     *
     * @author Richard Gomes
     */
    private static class Resource {

        private final String relativePath;

        public Resource(final String relativePath) {
            this.relativePath = relativePath;
        }

        public InputStream openInputStream() throws IOException  {
            InputStream is;
            // first try to find under the current directory
            is = openInputStream(relativePath);
            if (is==null) {
                // if not found, try to find in the .JAR file itself
                is = openInputStreamAsResource(relativePath);
                if (is==null) {
                    // if not found, try to find on mounted directories in the CLASSPATH (Eclipse mode)
                    is = openInputStream(relativePath, true);
                } else {
                    // definitely not found!
                    throw new FileNotFoundException(String.format("Cannot find %s", relativePath));
                }
            }
            return is;
        }

        // Try to find resource in the current directory
        private InputStream openInputStream(final String relativePath) throws IOException {
            return openInputStream(relativePath, false);
        }

        // Try to find resource in the .JAR file itself
        private InputStream openInputStreamAsResource(final String relativePath) {
            return this.getClass().getResourceAsStream(relativePath);
        }

        // Try to find resource in mounted directories in the CLASSPATH (Eclipse mode) when debugging
        // Otherwise: Try to find resource in the current directory
        private InputStream openInputStream(final String relativePath, final boolean debug) throws IOException {
            final String cwd = System.getProperty("user.dir");
            if (debug) {
                // Try to find resource in the current directory
                final InputStream is = openInputStream(cwd, ".");
                if (is!=null) return is;
            } else {
                // Try to find resource in mounted directories in the CLASSPATH (Eclipse mode)
                for (final String folder : new String[] { "target/classes", "target/test-classes" }) {
                    final InputStream is = openInputStream(cwd, folder);
                    if (is!=null) return is;
                }
            }
            return null;
        }

        private InputStream openInputStream(final String cwd, final String folder) throws IOException {
            final StringBuilder sb = new StringBuilder();
            sb.append(cwd).append('/').append(folder).append('/').append(relativePath);
            final File file = new File(sb.toString());
            return file.isFile() ? new FileInputStream(file) : null;
        }

    }

}
