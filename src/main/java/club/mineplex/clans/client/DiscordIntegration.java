package club.mineplex.clans.client;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.client.settings.IterableState;
import club.mineplex.clans.client.settings.repo.ClientSettings;
import club.mineplex.clans.gamestate.GameState;
import club.mineplex.clans.gamestate.mineplex.MineplexData;
import club.mineplex.clans.gamestate.mineplex.MineplexGame;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class DiscordIntegration {

    private boolean running = true;
    private long created = 0;
    private DiscordUser discordUser = null;

    private Timer updateTask = null;

    public Optional<DiscordUser> getDiscordUser() {
        return Optional.ofNullable(discordUser);
    }

    public void start() {
        this.created = System.currentTimeMillis();

        final DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(discordUser -> {
            ClansMod.logger.info("Loaded Discord Integration! [@" + discordUser.username + "#" + discordUser.discriminator + "]");
            this.update();
            DiscordIntegration.this.discordUser = discordUser;
        }).build();

        this.running = true;
        DiscordRPC.discordInitialize("798391471773319209", handlers, true);

        new Thread("Discord RPC Callback") {
            @Override
            public void run() {
                while (DiscordIntegration.this.running) {
                    DiscordRPC.discordRunCallbacks();
                }
            }
        }.start();

        if (this.updateTask == null) {
            this.updateTask = new Timer();
            this.updateTask.schedule(new TimerTask() {
                @Override
                public void run() {
                    DiscordIntegration.this.update();
                }
            }, 0, 1000L);
        }

    }

    private void shutdown() {
        this.running = false;
        DiscordRPC.discordShutdown();
        ClansMod.logger.info("Disabled Discord Integration!");
    }

    private void update() {
        if (!ClientSettings.DISCORD_RICH_STATUS && this.running) {
            this.shutdown();
            return;
        }

        if (!this.running && ClientSettings.DISCORD_RICH_STATUS) {
            this.start();
        }

        final GameState data = ClansMod.getInstance().getGameState();
        String server = data.isMultiplayer() ? "Multiplayer" : "Main Menu";

        if (data.isMultiplayer() && ClientSettings.DISCORD_SHOW_SERVER) {

            server = data.getMultiplayerData().getServerAddressThrow().getHostString();
            final IterableState displayMineplex = ClientSettings.DISCORD_SHOW_MINEPLEX_SERVER;
            if (data.isMineplex() && !displayMineplex.equals(IterableState.OFF)) {

                final MineplexData mineplexData = data.getMultiplayerData().as(MineplexData.class);
                final boolean isStaffServer = !mineplexData.getServerType().isPublic();
                String mineplexServer = mineplexData.getServer();

                if (displayMineplex.equals(IterableState.TYPE_ONLY)) {
                    mineplexServer = mineplexData.getServerType().getName();

                    if (!mineplexData.getGame().equals(MineplexGame.NONE)) {
                        mineplexServer = mineplexData.getGame().getName();
                    }
                }

                if (isStaffServer) {
                    mineplexServer = "Private Server";
                }

                server = "Mineplex " + mineplexServer;
            }

        }

        final DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder(server);
        builder.setDetails(data.isMultiplayer() ? "In-Game" : "");
        builder.setBigImage("mporange", "");
        builder.setStartTimestamps(this.created);

        DiscordRPC.discordUpdatePresence(builder.build());

    }

}
