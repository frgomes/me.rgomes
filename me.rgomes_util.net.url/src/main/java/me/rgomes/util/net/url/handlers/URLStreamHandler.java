package me.rgomes.util.net.url.handlers;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

/**
 * This class extends legacy <i>java.net.URLStreamHandler</i>, adding <i>classpath:</i> protocol and
 * a <i>file:</i> protocol which allows relative addresses.
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public class URLStreamHandler extends java.net.URLStreamHandler {

	private static final ProtocolHandler[] providedProtocolHandlers = {
		new ProtocolHandlerClasspath(),
		new ProtocolHandlerFile()
	};

	private final List<ProtocolHandler> protocolHandlers;

    public URLStreamHandler() {
        this.protocolHandlers = Arrays.asList(providedProtocolHandlers);
	}

    public URLStreamHandler addProtocolHandler(ProtocolHandler handler) {
    	this.protocolHandlers.add(handler);
    	return this;
    }

	public URLStreamHandler setClassLoader(ClassLoader classLoader) {
        for (ProtocolHandler handler : protocolHandlers) {
        	handler.setClassLoader(classLoader);
        }
        return this;
	}

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
    	String protocol = url.getProtocol();
    	for (ProtocolHandler handler : protocolHandlers) {
    		if (protocol.equals(handler.getProtocol())) {
    			return handler.openConnection(url);
    		}
        }
    	// fallback to handle default protocols, like http, https, ftp, file and jar
    	return new URL(null, url.toString()).openConnection();
    }

}
