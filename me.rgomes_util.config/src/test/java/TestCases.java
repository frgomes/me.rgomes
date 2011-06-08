
import java.util.prefs.Preferences;
import junit.framework.Assert;
import me.rgomes.util.config.Configuration;
import me.rgomes.util.config.DefaultPreferencesFactory;
import org.junit.Test;

/**
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
public class TestCases {

    @Test
    public void testSystemRoot() {
        System.setProperty("java.util.prefs.PreferencesFactory", DefaultPreferencesFactory.class.getName());
        Preferences prefs = new Configuration().systemRoot();
        String prop = prefs.get("level", null);
        Assert.assertTrue("ERROR".equals(prop));
        System.out.println("level="+prop);
    }

    @Test
    public void testSystemNodeForPackage() {
        System.setProperty("java.util.prefs.PreferencesFactory", DefaultPreferencesFactory.class.getName());
        Preferences prefs = new Configuration().systemNodeForPackage(Configuration.class);
        String prop = prefs.get("level", null);
        Assert.assertTrue("WARN".equals(prop));
        System.out.println(Configuration.class.getName()+".level="+prop);
    }



    @Test
    public void testUserRoot() {
        System.setProperty("java.util.prefs.PreferencesFactory", DefaultPreferencesFactory.class.getName());
        Preferences prefs = new Configuration().userRoot();
        String prop = prefs.get("level", null);
        Assert.assertTrue("INFO".equals(prop));
        System.out.println("demo.level="+prop);
    }

    @Test
    public void testUserNodeForPackage() {
        System.setProperty("java.util.prefs.PreferencesFactory", DefaultPreferencesFactory.class.getName());
        Preferences prefs = new Configuration().userNodeForPackage(Configuration.class);
        String prop = prefs.get("level", null);
        Assert.assertTrue("DEBUG".equals(prop));
        System.out.println(Configuration.class.getName()+".demo.level="+prop);
    }
}
