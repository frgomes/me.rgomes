package me.rgomes.util.net.url.handlers;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * This interface is used by our own URLStreamHandler class (do not confuse with java.net.URLStreamHandler).
 * 
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public interface ProtocolHandler {
	public void setClassLoader(ClassLoader classLoader);
	public String getProtocol();
	public URLConnection openConnection(URL url) throws IOException;
}
