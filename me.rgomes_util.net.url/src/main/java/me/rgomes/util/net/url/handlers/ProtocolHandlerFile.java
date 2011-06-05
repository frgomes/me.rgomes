package me.rgomes.util.net.url.handlers;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Provides <i>file:</i> protocol which accepts relative addresses
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public class ProtocolHandlerFile implements ProtocolHandler {

	private ClassLoader classLoader;

	public ProtocolHandlerFile() {
	    final Thread t = Thread.currentThread();
	    this.classLoader = t.getContextClassLoader();
	}

    @Override
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

    @Override
	public String getProtocol() {
		return "file";
	}

    @Override
	public URLConnection openConnection(URL url) throws IOException {
		return new FileURLConnection(url);
	}
}
