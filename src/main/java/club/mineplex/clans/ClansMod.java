package club.mineplex.clans;

import club.mineplex.clans.client.DiscordIntegration;
import club.mineplex.clans.client.gui.GuiEvents;
import club.mineplex.clans.client.hud.HudManager;
import club.mineplex.clans.client.keybind.KeyBindingManager;
import club.mineplex.clans.client.modules.AbstractMod;
import club.mineplex.clans.client.modules.champions.ModuleChampions;
import club.mineplex.clans.client.modules.champions.ModuleChampionsQoL;
import club.mineplex.clans.client.modules.clans.*;
import club.mineplex.clans.client.modules.mineplex.ModuleMineplexServerHandler;
import club.mineplex.clans.client.settings.Config;
import club.mineplex.clans.client.settings.repo.ChampionsSettings;
import club.mineplex.clans.client.settings.repo.ClansSettings;
import club.mineplex.clans.client.settings.repo.ClientSettings;
import club.mineplex.clans.client.settings.repo.DropSettings;
import club.mineplex.clans.command.GearCommand;
import club.mineplex.clans.gamestate.GameState;
import club.mineplex.clans.gamestate.controller.GenericController;
import club.mineplex.clans.gamestate.controller.ListenerServerConnection;
import club.mineplex.clans.gamestate.controller.MineplexController;
import club.mineplex.clans.gamestate.controller.ServerController;
import club.mineplex.clans.item.ItemFactory;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;

@Mod(modid = "clansmod", useMetadata = true)
public class ClansMod {

    @Mod.Instance("clansmod")
    private static ClansMod instance;
    public static final Logger logger = LogManager.getLogger("ClansMod");

    private final Map<Class<? extends AbstractMod>, AbstractMod> modules = new HashMap<>();
    private final File configFolder = new File(Loader.instance().getConfigDir(), "clansmod");
    private final Config config = new Config(new File(this.configFolder, "config.json"));

    private final ItemFactory itemFactory = new ItemFactory();
    private Client client;
    private GameState gameState;
    private Configuration rootConfiguration;
    private List<ServerController<?>> serverControllers;
    final DiscordIntegration discord = new DiscordIntegration();

    public static ClansMod getInstance() {
        return instance;
    }

    public static Mod getMod() {
        return ClansMod.class.getAnnotation(Mod.class);
    }

    private void registerEvents(final Object... events) {
        for (final Object event : events) {
            MinecraftForge.EVENT_BUS.register(event);
        }
    }

    private void registerModules(final AbstractMod... modules) {
        for (final AbstractMod module : modules) {
            this.modules.put(module.getClass(), module);
            MinecraftForge.EVENT_BUS.register(module);
        }
    }

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        this.rootConfiguration = new Configuration(event.getSuggestedConfigurationFile());
        this.rootConfiguration.load();
        KeyBindingManager.getInstance().setup();
    }

    @EventHandler
    public void init(final FMLInitializationEvent event) {
        if (!this.configFolder.exists()) {
            this.configFolder.mkdir();
        }

        this.config.registerClass(ClansSettings.class);
        this.config.registerClass(ChampionsSettings.class);
        this.config.registerClass(ClientSettings.class);
        this.config.registerClass(DropSettings.class);
        this.config.load();
        this.config.save();

        this.gameState = GameState.getInstance(this);

        this.serverControllers = Arrays.asList(
                new GenericController(),
                new MineplexController()
        );
        serverControllers.forEach(MinecraftForge.EVENT_BUS::register);

        this.registerEvents(
                this,
                new GuiEvents(this),
                new ListenerServerConnection(),
                HudManager.getInstance()
        );

        ClientCommandHandler.instance.registerCommand(new GearCommand());


        // Enable modules
        this.registerModules(
                new ModuleMineplexServerHandler(),
                new ModuleDropPrevention(),
                new ModuleSlotLock(),
                new ModuleEnhancedMounts(),
                new ModuleMapFix(),
                new ModuleResourcepack(),
                new ModuleChampions(),
                new ModuleClansQoL(),
                new ModuleChampionsQoL()
        );

        for (AbstractMod module : getModules()) {
            module.init();
        }

        HudManager.getInstance().loadRenderers();

        this.discord.start();
        this.client = new Client(this);
    }

    public List<AbstractMod> getModules() {
        return new ArrayList<>(this.modules.values());
    }

    public <T extends AbstractMod> Optional<T> getModule(final Class<T> moduleClass) {
        return Optional.ofNullable(moduleClass.cast(this.modules.get(moduleClass)));
    }

    public <T extends AbstractMod> T getModuleThrow(final Class<T> moduleClass) {
        return this.getModule(moduleClass)
                   .orElseThrow(() -> new RuntimeException("Invalid module class: " + moduleClass.getName()));
    }

    public ItemFactory getItemManager() {
        return itemFactory;
    }

    public File getConfigFolder() {
        return configFolder;
    }

    public Config getConfig() {
        return config;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Configuration getRootConfiguration() {
        return rootConfiguration;
    }

    public List<ServerController<?>> getServerControllers() {
        return serverControllers;
    }

    public Client getClient() {
        return client;
    }

}
