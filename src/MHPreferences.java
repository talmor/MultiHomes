import java.io.IOException;

public class MHPreferences {
    public static int        maxHomes = 2;
    public static PropertiesFile file                      = new PropertiesFile("multihomes.properties");

    public static void initialize() {
        maxHomes = file.getInt("maxHomes", 2);
    }

}
