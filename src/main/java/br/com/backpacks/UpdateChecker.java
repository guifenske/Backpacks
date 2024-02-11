package br.com.backpacks;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateChecker {
    private static final String CURRENT_VERSION = Main.getMain().getPluginMeta().getVersion();

    private static final String MODRINTH_API_URL = "https://api.modrinth.com/v2/project/advancedbackpacks/version";

    public static void checkForUpdates() {
        try {
            URL url = new URL(MODRINTH_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String latestVersion = parseVersion(response.toString());
            if (!CURRENT_VERSION.equals(latestVersion)) {
                Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "An update is available! Latest version: " + latestVersion);
            }   else{
                Main.getMain().getLogger().info("You are using the latest version");
            }
        } catch (IOException e) {
            Main.getMain().debugMessage(CURRENT_VERSION);
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Could not find versions for this plugin, aborting update check.");
        }
    }

    private static String parseVersion(String response) {
        Pattern pattern = Pattern.compile("\"version_number\":\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
