package dev.neur0pvp.neur0flow;

import com.github.retrooper.packetevents.PacketEvents;
import dev.neur0pvp.neur0flow.command.MainCommand;
import dev.neur0pvp.neur0flow.command.generic.AbstractPlayerSelectorParser;
import dev.neur0pvp.neur0flow.command.generic.BuilderCommand;
import dev.neur0pvp.neur0flow.command.subcommand.*;
import dev.neur0pvp.neur0flow.event.Event;
import dev.neur0pvp.neur0flow.event.EventBus;
import dev.neur0pvp.neur0flow.event.OptimizedEventBus;
import dev.neur0pvp.neur0flow.listener.packetevents.*;
import dev.neur0pvp.neur0flow.manager.ConfigManager;
import dev.neur0pvp.neur0flow.permission.PermissionChecker;
import dev.neur0pvp.neur0flow.scheduler.SchedulerAdapter;
import dev.neur0pvp.neur0flow.sender.Sender;
import dev.neur0pvp.neur0flow.world.PlatformServer;
import lombok.Getter;
import org.incendo.cloud.CommandManager;
import org.kohsuke.github.GitHub;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

// Base class
@Getter
public abstract class Base {
    public static Logger LOGGER;
    public static Base INSTANCE;
    protected final EventBus eventBus = new OptimizedEventBus();
    private final Platform platform;
    protected PlatformServer platformServer;
    protected SchedulerAdapter scheduler;
    protected ConfigManager configManager;
    protected CommandManager<Sender> commandManager;
    protected AbstractPlayerSelectorParser<Sender> playerSelectorParser;

    protected Base() {
        this.platform = detectPlatform();
        INSTANCE = this;
    }

    private Platform detectPlatform() {
        final Map<String, Platform> platforms = Collections.unmodifiableMap(new HashMap<String, Platform>() {{
            put("io.papermc.paper.threadedregions.RegionizedServer", Platform.FOLIA);
            put("org.bukkit.Bukkit", Platform.BUKKIT);
        }});

        return platforms.entrySet().stream()
                .filter(entry -> isClassPresent(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unknown platform!"));
    }

    private boolean isClassPresent(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public abstract Logger getLogger();

    public abstract File getDataFolder();

    public abstract void load();

    public void enable() {
        LOGGER = getLogger();
        saveDefaultConfig();
        initializePacketEvents();
        registerCommonListeners();
        registerPlatformListeners();
        registerCommands();
        initializeScheduler();
        configManager.loadConfig(false);
        checkForUpdates();
    }

    public abstract void initializeScheduler();

    public void initializePacketEvents() {
        PacketEvents.getAPI().getSettings()
                .checkForUpdates(false)
                .debug(false);

        PacketEvents.getAPI().init();
    }

    protected void registerCommonListeners() {
        PacketEvents.getAPI().getEventManager().registerListeners(
                new AttributeChangeListener(),
                new PingSendListener(),
                new PingReceiveListener(),
                new PacketPlayerJoinQuit(),
                new ClientBrandListener()
        );
        Event.setEventBus(eventBus);
    }

    protected abstract void registerPlatformListeners();

    protected void registerCommands() {
        List<BuilderCommand> list = Arrays.asList(
                new MainCommand(),
                new ReloadCommand(),
                new PingCommand(),
                new StatusCommand(),
                new ToggleOffGroundSubcommand(),
                new ToggleCommand()
        );
        list.forEach(command -> command.register(commandManager));
    }

    protected abstract String getVersion();

    protected void checkForUpdates() {
        getLogger().info("Checking for updates...");

        scheduler.runTaskAsynchronously(() -> {
            try {
                GitHub github = GitHub.connectAnonymously();
                String latestVersion = github.getRepository("neur0pvp/neur0flow")
                        .getLatestRelease()
                        .getTagName();

                String currentVersion = getVersion();

                int comparisonResult = compareVersions(currentVersion, latestVersion);

                if (comparisonResult < 0) {
                    LOGGER.warning("You are running an older version. A new update is available for download at: https://github.com/neur0pvp/neur0flow/releases/latest");
                    configManager.setUpdateAvailable(true);
                } else if (comparisonResult > 0) {
                    if (currentVersion.contains("-dev"))
                        LOGGER.info("You are running a development build newer than the latest release.");
                    else {
                        LOGGER.info("You are running a version newer than the latest release.");
                    }
                } else {
                    LOGGER.info("You are running the latest release.");
                }

                if (configManager.isUpdateAvailable() && configManager.isAutoUpdate()) {
                    LOGGER.info("Updating...");
                    byte[] bytes = downloadLatestRelease(github);
                    updatePlugin(bytes);

                    if (hasRestartScript())
                        scheduler.runTask(this::restartServer);
                }
            } catch (Exception e) {
                LOGGER.severe("Failed to check for updates: " + e.getMessage());
            }
        });
    }

    private void updatePlugin(byte[] pluginBytes) throws URISyntaxException {
        File file = new File(getJarURL().toURI());

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(pluginBytes);
            LOGGER.info("Successfully updated the plugin!");
        } catch (Exception e) {
            LOGGER.severe("Failed to update: " + e.getMessage());
        }
    }

    private byte[] downloadLatestRelease(GitHub github) {
        try {
            return github.getRepository("neur0pvp/neur0flow")
                    .getLatestRelease()
                    .getAssets().stream()
                    .filter(asset -> asset.getName().endsWith(".jar")
                            && asset.getName().contains("bukkit"))
                    .findFirst()
                    .map(asset -> {
                        try (InputStream inputStream = new URL(asset.getBrowserDownloadUrl()).openStream()) {
                            inputStream.reset();
                            byte[] bytes = new byte[inputStream.available()];
                            DataInputStream dataInputStream = new DataInputStream(inputStream);
                            dataInputStream.readFully(bytes);
                            return bytes;
                        } catch (Exception e) {
                            LOGGER.severe("Failed to download latest release: " + e.getMessage());
                            return null;
                        }
                    }).orElse(null);
        } catch (Exception e) {
            LOGGER.severe("Failed to locate latest release: " + e.getMessage());
            return null;
        }
    }

    private int compareVersions(String version1, String version2) {
        String[] v1Parts = version1.split("[-.]");
        String[] v2Parts = version2.split("[-.]");

        int length = Math.min(v1Parts.length, v2Parts.length);

        for (int i = 0; i < length; i++) {
            int comparison = compareVersionPart(v1Parts[i], v2Parts[i]);
            if (comparison != 0) {
                return comparison;
            }
        }

        // If we're here, all compared parts are equal
        if (v1Parts.length != v2Parts.length) {
            return compareSpecialVersions(v1Parts, v2Parts);
        }

        return 0; // Versions are equal
    }

    private int compareVersionPart(String part1, String part2) {
        try {
            int v1 = Integer.parseInt(part1);
            int v2 = Integer.parseInt(part2);
            return Integer.compare(v1, v2);
        } catch (NumberFormatException e) {
            // If parts are not numbers, compare them based on dev < snapshot < release
            return compareSpecialPart(part1, part2);
        }
    }

    private int compareSpecialPart(String part1, String part2) {
        if (part1.equals(part2)) return 0;
        if (part1.startsWith("dev")) return part2.startsWith("dev") ? 0 : -1;
        if (part2.startsWith("dev")) return 1;
        if (part1.equals("SNAPSHOT")) return -1;
        if (part2.equals("SNAPSHOT")) return 1;
        return part1.compareTo(part2);
    }

    private int compareSpecialVersions(String[] v1Parts, String[] v2Parts) {
        if (v1Parts.length > v2Parts.length) {
            String specialPart = v1Parts[v2Parts.length];
            if (specialPart.startsWith("dev")) return -1;
            if (specialPart.equals("SNAPSHOT")) return -1;
            return 1; // Assume it's a release version part
        } else {
            String specialPart = v2Parts[v1Parts.length];
            if (specialPart.startsWith("dev")) return 1;
            if (specialPart.equals("SNAPSHOT")) return 1;
            return -1; // Assume it's a release version part
        }
    }

    public abstract void saveDefaultConfig();

    public abstract PermissionChecker getPermissionChecker();

    public abstract float getTickRate();

    public abstract URL getJarURL();

    public abstract void restartServer();

    public abstract boolean hasRestartScript();
}


