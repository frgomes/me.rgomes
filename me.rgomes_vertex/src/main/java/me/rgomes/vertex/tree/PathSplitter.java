package me.rgomes.vertex.tree;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class is responsible for processing an string and bring back its full path as recommended by W3C HTML 4.01 specification.
 * Doing so, a certain name which which contains spaces, special characters, unicode characters, etc can be represented only by
 * ASCII characters.
 * <p>
 * Suppression of leading and trailing white spaces is optional and enabled by default.
 * For instance: "/ top  / medium level  /  bottom  " can become equivalent to "/top/medium level/bottom".
 * <p>
 * A slash is the separator between path elements.
 * If an element needs to contain a slash itself, a slash must appear twice. For instance: "Concept//X" stands for "Concept/X".
 * Doing so, it's possible to have path elements which contain a slash in a full path, for instance: "/Concept//XYZ/Concept//X"
 * stands for a path element "Concept/XYZ" followed by a path element "Concept/X".
 * <p>
 * <b>Limitations:</b>
 * <p>
 * It's not possible to have a slash in the beginning of a path element, except the first element.
 * It happens because this construction is ambiguous with a slash in the end of a path element.
 * For instance: "/aaa///bbb" stands for
 * <ul>
 * <li>first path element "aaa/" followed by second path element "bbb"</li>
 * and not
 * <li>first path element "aaa" followed by second path element "/bbb"</li>
 * </ul>
 *
 * @see URLEncoder
 * @see <a href="http://www.w3.org/TR/html4/">HTML 4.01 specification</a>
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public class PathSplitter {

	private static final Pattern pattern = Pattern.compile(Matcher.quoteReplacement("(?<!\\)/"));

	private final String path;

	public PathSplitter(final String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		final List<String> list = splitString();
		final StringBuilder sb = new StringBuilder();
		for (final String s : list) {
			sb.append('/').append(escapes(s));
		}
		return sb.toString();
	}


	public String toURL() {
		final List<String> list = splitURL();
		final StringBuilder sb = new StringBuilder();
		for (final String s : list) {
			sb.append('/').append(encode(escapes(s)));
		}
		return sb.toString();
	}

	public List<String> splitString() {
		return split(false);
	}

	public List<String> splitURL() {
		return split(true);
	}


	//
	// public static methods
	//

	public static String escapes(final String s) {
		final String result = s
			.replaceAll(Matcher.quoteReplacement("/"), Matcher.quoteReplacement("\\/"))
			.replaceAll(Matcher.quoteReplacement("\\"), Matcher.quoteReplacement("\\\\"));
		// System.out.println("escapes -> " + result);
		return result;
	}

	public static String noEscapes(final String s) {
		final String result = s
			.replaceAll(Matcher.quoteReplacement("\\/"), "/")
			.replaceAll(Matcher.quoteReplacement("\\\\"), Matcher.quoteReplacement("\\"));
		// System.out.println("noEscapes -> " + result);
		return result;
	}

	public static String encode(final String s) {
        try {
            return URLEncoder.encode(s, "UTF-8"); // see http://tools.ietf.org/html/rfc3986
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

	public static String decode(final String s) {
        try {
            return URLDecoder.decode(s, "UTF-8"); // see http://tools.ietf.org/html/rfc3986
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


	//
    // private methods
    //

	private List<String> split(final boolean asURL) {
    	final String[] parts = pattern.split(path);
    	final List<String> list = new ArrayList<String>();
    	int index = -1;
    	for (final String part : parts) {
    		if (part.length()==0) continue;
			String s = noEscapes(part.trim());
			if (asURL) s = encode(s);
			list.add(s);
			index++;
    	}
    	return list;
	}

}
