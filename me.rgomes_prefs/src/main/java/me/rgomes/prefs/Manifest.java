package me.rgomes.prefs;

import java.io.IOException;
import java.net.URL;

/**
 * Extends legacy java.util.jar.Manifest, providing additional
 * getters for common attributes. Also employs ResourceLookup under the covers in
 * order to provide easy access to the MANIFEST.MF file.
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public class Manifest extends java.util.jar.Manifest {

    public Manifest() throws IOException {
        // Employ conf: protocol
        super(new URL(null, "META-INF/MANIFEST.MF", new me.rgomes.protocols.conf.Handler()).openStream());
    }

    public String getAttribute(final String name) {
        String result = getMainAttributes().getValue(name);
        if (result==null) throw new NullPointerException(String.format("attribute '%s' not found", name));
        return result;
    }

    public String getAttribute(final String name, final String value) {
        String result = getMainAttributes().getValue(name);
        return result == null ? value : result;
    }

    public String getSpecificationVendor() {
        return getAttribute("Specification-Vendor");
    }

    public String getSpecificationTitle() {
        return getAttribute("Specification-Title");
    }

    public String getSpecificationVersion() {
        return getAttribute("Specification-Version");
    }

    public String getImplementationVendor() {
        return getAttribute("Implementation-Vendor");
    }

    public String getImplementationTitle() {
        return getAttribute("Implementation-Title");
    }

    public String getImplementationVersion() {
        return getAttribute("Implementation-Version");
    }

    public String getImplementationBuild() {
        return getAttribute("Implementation-Build");
    }

    public String getVersion() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getSpecificationVendor()).append(':');
        sb.append(getSpecificationTitle()).append(':');
        sb.append(getSpecificationVersion());
        sb.append(" (");
        sb.append(getImplementationVendor()).append(':');
        sb.append(getImplementationTitle()).append(':');
        sb.append(getImplementationVersion()).append('.');
        sb.append(getImplementationBuild()).append(')');
        return sb.toString();
    }

}
