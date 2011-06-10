package me.rgomes.protocols.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is intended to solve the burden related to looking up resources from
 * different locations depending on the environment the application is running on.
 * <p/>
 * This is the search order:
 * <p/>
 * <ul>
 * <li>
 * Relative to the current working directory.
 * </li>
 * <li>
 * Relative to the classpath.
 * </li>
 * <li>
 * Folders ./target/classes and ./target/test-classes are considered, once these folder locations where Eclipse put
 * generated classes and resources. When an application is packaged, resources end up in a .jar file, which will be
 * certainly part of the classpath (otherwise nothing will work!) but whilst in development mode, these folders need
 * to be included.
 * </li>
 * </ul>
 *
 * @author Richard Gomes
 */
public class Resource {

   private final String relativePath;

   public Resource(final String relativePath) {
       this.relativePath = relativePath;
   }

   public InputStream openInputStream() throws IOException  {
       InputStream is;
       // first try to find under the current directory
       is = openInputStream(relativePath);
       if (is==null) {
           // if not found, try to find in the .JAR file itself
           is = openInputStreamAsResource(relativePath);
           if (is==null) {
               // if not found, try to find on mounted directories in the CLASSPATH (Eclipse mode)
               is = openInputStream(relativePath, true);
           } else {
               // definitely not found!
               throw new FileNotFoundException(String.format("Cannot find %s", relativePath));
           }
       }
       return is;
   }

   // Try to find resource in the current directory
   private InputStream openInputStream(final String relativePath) throws IOException {
       return openInputStream(relativePath, false);
   }

   // Try to find resource in the .JAR file itself
   private InputStream openInputStreamAsResource(final String relativePath) {
       return this.getClass().getResourceAsStream(relativePath);
   }

   // Try to find resource in mounted directories in the CLASSPATH (Eclipse mode) when debugging
   // Otherwise: Try to find resource in the current directory
   private InputStream openInputStream(final String relativePath, final boolean debug) throws IOException {
       final String cwd = System.getProperty("user.dir");
       if (debug) {
           // Try to find resource in the current directory
           final InputStream is = openInputStream(cwd, ".");
           if (is!=null) return is;
       } else {
           // Try to find resource in mounted directories in the CLASSPATH (Eclipse mode)
           for (final String folder : new String[] { "target/classes", "target/test-classes" }) {
               final InputStream is = openInputStream(cwd, folder);
               if (is!=null) return is;
           }
       }
       return null;
   }

   private InputStream openInputStream(final String cwd, final String folder) throws IOException {
       final StringBuilder sb = new StringBuilder();
       sb.append(cwd).append('/').append(folder).append('/').append(relativePath);
       final File file = new File(sb.toString());
       return file.isFile() ? new FileInputStream(file) : null;
   }

}
