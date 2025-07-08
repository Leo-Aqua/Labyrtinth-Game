import java.util.Properties;
import java.io.*;

public class Settings {
    private static  Properties props = new Properties();
    private static final File file = new File("config.properties");

    static {
        
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                props.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Set defaults
            props.setProperty("gridSize", "8");
            save(); // Save defaults
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static void set(String key, String value) {
        props.setProperty(key, value);
    }

    public static void save() {
        try (FileOutputStream out = new FileOutputStream(file)) {
            props.store(out, "Game Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
