package br.com.backpacks;

import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateChecker {
    private static final String CURRENT_VERSION = Main.getMain().getDescription().getVersion();

    private static final String MODRINTH_API_URL = "https://api.modrinth.com/v2/project/advancedbackpacks/version";

    public static void checkForUpdates() {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MODRINTH_API_URL))
                .GET()
                .build();

        String latestVersion;

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            latestVersion = parseVersion(response.body());
        } catch (IOException | InterruptedException e) {
            return;
        }

        if (latestVersion == null || latestVersion.equals("null")) {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Could not find versions for this plugin, aborting update check.");
            return;
        }
        if (!CURRENT_VERSION.equals(latestVersion)) {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "An update is available! Latest version: " + latestVersion);
        } else {
            Main.getMain().getLogger().info("You are using the latest version");
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
