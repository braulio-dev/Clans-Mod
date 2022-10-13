package club.mineplex.clans;

import club.mineplex.clans.util.object.ConnectionBuilder;
import club.mineplex.core.clansmod.model.Release;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarFile;

public class Client {

    private static final boolean IS_DEV = Boolean.parseBoolean(System.getProperty("clansmod.dev", Boolean.toString(false)));
    private static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("clansmod.debug", Boolean.toString(false)));

    private static final String API_ENDPOINT = IS_DEV ? "http://localhost:9090" : "https://api.mineplex.club";

    private final boolean isObfuscated;
    private final ClansMod mod;
    private final List<Release> releases = new ArrayList<>();

    private final String version;
    private boolean isUpdating;
    private boolean checkedForUpdates;

    Client(ClansMod mod) {
        this.mod = mod;
        this.isObfuscated = !((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"));
        this.version = IS_DEV ? "DEV" : ClansMod.getMod().version();
        if (debugEnabled()) {
            LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            ctx.getConfiguration().getLoggerConfig(LogManager.ROOT_LOGGER_NAME).setLevel(Level.DEBUG);
            ctx.updateLoggers();
            ClansMod.logger.info("Debug mode enabled!");
        }
    }

    public String getVersion() {
        return version;
    }

    public boolean isObfuscated() {
        return this.isObfuscated;
    }

    public boolean isDev() {
        return "DEV".equals(version) || IS_DEV;
    }

    public boolean debugEnabled() {
        return DEBUG;
    }

    public boolean isUpdating() {
        return isUpdating;
    }

    public boolean hasCheckedForUpdates() {
        return checkedForUpdates;
    }

    /**
     * Check for mod updates
     * @return true if there is an update, false otherwise.
     */
    public boolean checkForUpdates() {
        if (checkedForUpdates) {
            return false;
        }

        // If we are in a dev environment, we only update if the clansmod.forceUpdate property isn't present
        if (isDev() && !Boolean.parseBoolean(System.getProperty("clansmod.forceUpdate", Boolean.toString(false)))) {
            ClansMod.logger.info("Running in dev environment, skipping update check.");
            return false; // Development environment
        }

        try {
            releases.clear();
            final ConnectionBuilder builder = new ConnectionBuilder(API_ENDPOINT + "/clansmod/versions");
            builder.send();
            builder.skipRedirects();
            releases.addAll(new ObjectMapper().readValue(builder.getResponseString(), new TypeReference<List<Release>>(){}));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (releases.isEmpty()) {
            ClansMod.logger.error("API did not return any mod releases.");
            return false; // There aren't any versions saved in the API, no point in updating
        }

        final Release latest = this.releases.get(0);


        // Getting the release equivalent to this client
        final Optional<Release> currentOpt = releases.stream()
                                                  .filter(release -> release.getName().equals(version))
                                                  .findFirst();

        // We update the mod if we don't have an associated version or if the latest version is newer to ours
        if (!currentOpt.isPresent() || latest.compareVersion(currentOpt.get()).equals(Release.Comparison.NEWER)) { // We're outdated
            ClansMod.logger.info("Attempting to update to version: " + latest.getName());

            // Downloading on a different thread
            isUpdating = true;
            new Thread(() -> {
                if (new UpdateHandler(latest).update()) {
                    ClansMod.logger.info("Update successful.");
                } else {
                    ClansMod.logger.error("Update failed.");
                }
                isUpdating = false;
                checkedForUpdates = true;
            }).start();
            return true;
        }

        return false;
    }

    public String getLatestVersion() {
        return releases.isEmpty() ? "N/A" : releases.get(0).getName();
    }

    static class UpdateHandler {

        private final Release release;
        private final File mcFolder;

        protected UpdateHandler(Release toDownload) {
            this.release = toDownload;
            this.mcFolder = Minecraft.getMinecraft().mcDataDir;
        }

        /**
         * Update the mod
         * @return true if the update was successful, false otherwise.
         */
        protected boolean update() {
            try {
                // We read the incoming latest download
                URL url = new URL(API_ENDPOINT + "/clansmod/download");
                ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());

                // We write the download to the current mod file
                final File modFile = getCurrentModFile();
                final File toReplace = modFile == null ? new File(new File(mcFolder, "mods"), "ClansMod-1.8.9-" + release.getName() + ".jar") : modFile;
                FileOutputStream fileOutputStream = new FileOutputStream(toReplace);
                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        private File getCurrentModFile() {
            final File modsFolder = new File(mcFolder, "mods");

            try {
                final File[] files = modsFolder.listFiles((dir, name) -> name.endsWith(".jar"));

                if (files != null) {
                    for (File file : files) {
                        try (JarFile jar = new JarFile(file)) {
                            if (jar.getJarEntry("club/mineplex/clans/ClansMod.class") != null) {
                                return file;
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

}
