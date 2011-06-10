/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.rgomes.protocols.file;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public class Handler extends java.net.URLStreamHandler {

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new FileURLConnection(u);
    }

}
