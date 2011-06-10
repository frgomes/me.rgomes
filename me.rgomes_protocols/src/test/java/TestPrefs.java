
import java.util.prefs.Preferences;
import junit.framework.Assert;
import me.rgomes.prefs.Configuration;
import me.rgomes.prefs.DefaultPreferencesFactory;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public class TestPrefs {

    @Test
    /**
     * This test retrieves a property relative to the system root.
     * <p>
     * Example:<br/>
     * A property "name" based on the system root needs to be defined
     * in the Properties file as <i>name</i>.
     */
    public void testSystemRoot() {
        System.setProperty("java.util.prefs.PreferencesFactory", DefaultPreferencesFactory.class.getName());
        Preferences prefs = new Configuration().systemRoot();
        String prop = prefs.get("level", "test failed");
        Assert.assertTrue("ERROR".equals(prop));
        System.out.println("level="+prop);
    }

    @Test
    /**
     * This test retrieves a system property relative to the <b>package name</b>
     * of a class.
     * <p>
     * Example:<br/>
     * A property "name" based on Class com.example.Configuration needs
     * to de defined in the Properties file as <i>com.example.name</i>.
     */
    public void testSystemNodeForPackage() {
        System.setProperty("java.util.prefs.PreferencesFactory", DefaultPreferencesFactory.class.getName());
        Preferences prefs = new Configuration().systemNodeForPackage(Configuration.class);
        String prop = prefs.get("level", "test failed");
        Assert.assertTrue("WARN".equals(prop));
        System.out.println(Configuration.class.getName()+".level="+prop);
    }


//#############################################################################
//#                                                                           #
//#              !!! WARNING WARNING WARNING Will Robinson !!!                #
//#                                                                           #
//# Once userRoot and userNodeForPackage retrieve the current logged in user, #
//# the test cases shown below are provided only as a sake of example, once   #
//# our configuration file is not able to guess what your username is.        #
//#                                                                           #
//# The test cases relative to userRoot and userNodeForPackage are marked as  #
//# @Ignore because they would possibly fail due to the exposed reasons.      #
//#                                                                           #
//#############################################################################


    @Ignore
    @Test
    /**
     * This test retrieves a property relative to the user root.
     * <p>
     * Example:<br/>
     * A property "name" based on the user root needs to be defined
     * in the Properties file as <i>jsmith.name</i>, supposing that the
     * currently logged in user is "jsmith".
     */
    public void testUserRoot() {
        System.setProperty("java.util.prefs.PreferencesFactory", DefaultPreferencesFactory.class.getName());
        Preferences prefs = new Configuration().userRoot();
        String prop = prefs.get("level", "test failed");
        // The properties file assume your username is "jsmith"
        // If it is not the case... well... this is why this test is marked as @Ignore
        Assert.assertTrue("INFO".equals(prop));
        System.out.println("jsmith.level="+prop);
    }

    @Ignore
    @Test
    /**
     * This test retrieves an user property relative to the <b>package name</b>
     * of a class.
     * <p>
     * Example:<br/>
     * A property "name" based on Class com.example.Configuration needs to be
     * defined in the Properties file as <i>jsmith.com.example.name</i>, supposing
     * that the currently logged in user is "jsmith".
     */
    public void testUserNodeForPackage() {
        System.setProperty("java.util.prefs.PreferencesFactory", DefaultPreferencesFactory.class.getName());
        Preferences prefs = new Configuration().userNodeForPackage(Configuration.class);
        String prop = prefs.get("level", "test failed");
        // The properties file assume your username is "jsmith"
        // If it is not the case... well... this is why this test is marked as @Ignore
        Assert.assertTrue("DEBUG".equals(prop));
        System.out.println("jsmith." + Configuration.class.getName()+".level="+prop);
    }
}
