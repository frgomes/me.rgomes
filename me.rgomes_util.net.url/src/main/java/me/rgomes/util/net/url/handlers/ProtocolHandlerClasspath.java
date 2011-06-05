package me.rgomes.util.net.url.handlers;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Provides <i>classpath:</i> protocol
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public class ProtocolHandlerClasspath implements ProtocolHandler {

	private ClassLoader classLoader;

	public ProtocolHandlerClasspath() {
	    final Thread t = Thread.currentThread();
	    this.classLoader = t.getContextClassLoader();
	}

    @Override
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

    @Override
	public String getProtocol() {
		return "classpath";
	}

    @Override
	public URLConnection openConnection(URL url) throws IOException {
        final URL resourceUrl = classLoader.getResource(url.getPath());
        return resourceUrl.openConnection();
	}
}
