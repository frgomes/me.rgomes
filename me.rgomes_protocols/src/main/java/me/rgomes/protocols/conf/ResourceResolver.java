/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.rgomes.protocols.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public class ResourceResolver {

    private final String username;
    private final String realm;
    private final String mode;

    public ResourceResolver() throws IOException {
        this.username = System.getProperty("user.name", "anonymous");

        // Try to read default realm and mode from MANIFEST.MF
        String mrealm = username();
        String mmode = "debug";
        try {
            Manifest mf = new Manifest();
            mrealm = mf.getAttribute("realm", mrealm);
            mmode = mf.getAttribute("mode", mmode);
        } catch (Exception e) {
            // discard
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
        final String path = String.format("conf/%s/%s/%s", realm, mode, name);
        return new Resource(path).openInputStream();
    }

}
