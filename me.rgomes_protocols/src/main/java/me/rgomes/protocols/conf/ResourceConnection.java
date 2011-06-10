/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.rgomes.protocols.conf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

import me.rgomes.protocols.Constants;

/**
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
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

}
