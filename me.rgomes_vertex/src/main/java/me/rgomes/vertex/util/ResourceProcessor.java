package me.rgomes.vertex.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This class offers a generic processor for local and remote resources.
 * <p>
 * Resources may be passed by name, by {@link File} or by {@link URL}.
 * Local resources may be both files or directories.
 * In case a directory is specified, it is scanned recursively until
 * all its files are processed.
 * <p>
 * <b>Note:</b>
 * This class currently does not call {@link ResourceLookup}
 * <p>
 * TODO: Study the possibility of tweaking the thread's class loader instead.<br/>
 * http://www.javafaq.nu/java-example-code-895.html<br/>
 * http://java.sun.com/products/jndi/tutorial/beyond/misc/classloader.html
 *
 * @see ResourceLookup
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public abstract class ResourceProcessor {

    protected abstract void process(final InputStream is);

    /**
     * Process a resource by its name
     * <p>
     * A resource name may be its name in the file system of any URL.
     *
     * @param source
     */
    public void process(final String source) {
        if (source==null || source.equals("-")) {
        	process(System.in);
        } else {
            URI uri;
            try {
                uri = new URI(source);
            } catch (final URISyntaxException e) {
                throw new RuntimeException(e);
            }
            if (uri.isAbsolute()) {
                URL url;
                try {
                    url = uri.toURL();
                } catch (final MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                process(url);
            } else {
            	process(new File(source));
            }
        }
    }

    /**
     * Process a resource URL
     * <p>
     * This method simply processes the {@link InputStream} associated to such URL
     *
     * @param url
     */
    public void process(final URL url) {
        try {
        	process(url.openStream());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Process a resource File
     * <p>
     * Resources may be both files or directories.
     * In case a directory is specified, it is scanned recursively until
     * all its files are processed.
     *
     * @param file
     */
    //TODO: offer an option for filtering files
    private void process(final File file) {
        if (file.isDirectory()) {
            final String files[] = file.list();
            for (final String entry : files) {
            	process(new File(file, entry));
            }
        } else {
            try {
            	process(new FileInputStream(file));
            } catch (final FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
