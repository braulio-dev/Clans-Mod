package club.mineplex.clans.client.modules.mineplex;

import club.mineplex.clans.client.modules.AbstractMod;
import club.mineplex.clans.events.MineplexServerSwitchEvent;
import club.mineplex.clans.events.client.PacketEvent;
import club.mineplex.clans.gamestate.GameState;
import club.mineplex.clans.gamestate.mineplex.MineplexData;
import club.mineplex.clans.gamestate.mineplex.MineplexGame;
import net.minecraft.network.play.server.*;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModuleMineplexServerHandler extends AbstractMod {

    private static final Pattern MINEPLEX_SWITCHED_SERVER_PATTERN =
            Pattern.compile(".*> .* sent from ([A-z]+-[\\d]+) to ([A-z]+-[\\d]+)\\.");
    private static final Pattern MINEPLEX_TAB_PATTERN =
            Pattern.compile("^Mineplex Network {3}(.*-\\d*)$");

    private final ExecutorService threadService = Executors.newSingleThreadExecutor();
    private final LinkedBlockingQueue<String> updateQueue = new LinkedBlockingQueue<>();

    public ModuleMineplexServerHandler() {
        super("Mineplex Server Handler");
        this.start();
    }

    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent e) {
        final String message = e.message.getUnformattedText();

        if (!message.matches(MINEPLEX_SWITCHED_SERVER_PATTERN.pattern())) {
            return;
        }

        final Matcher matcher = MINEPLEX_SWITCHED_SERVER_PATTERN.matcher(message);
        if (!matcher.find()) {
            return;
        }

        this.update(matcher.group(2));
    }

    @SubscribeEvent
    public void onHeaderFooter(final PacketEvent.Receive.Post e) {
        if (!(e.getPacket() instanceof S47PacketPlayerListHeaderFooter)) {
            return;
        }

        final String header = EnumChatFormatting.getTextWithoutFormattingCodes(((S47PacketPlayerListHeaderFooter) e.getPacket()).getHeader().getUnformattedText());
        final Matcher matcher = MINEPLEX_TAB_PATTERN.matcher(header);
        if (!matcher.find()) {
            for (MineplexGame value : MineplexGame.values()) {
                if (header.contains(value.getName())) {
                    final MineplexData newData = gameState.getMultiplayerData().as(MineplexData.class);
                    newData.setGame(value);
                    break;
                }
            }
        } else {
            final String server = matcher.group(1);
            this.update(server);
        }
    }

    @SubscribeEvent
    public void onActionBar(final ClientChatReceivedEvent e) {
        if (e.type != 2) { // Skipping if it's not action bar
            return;
        }
        if (!gameState.isMineplex() || gameState.getMultiplayerData().as(MineplexData.class).getGame() == MineplexGame.NONE) {
            return;
        }

        final String text = e.message.getUnformattedText();
        if (text.matches("^§a§lStart!$")) {
            gameState.getMultiplayerData().as(MineplexData.class).setGameStatus(MineplexGame.Status.IN_GAME);
        } else if (text.matches("^Game Start§r §a(?:▌|§c)+§r \\d\\.\\d Seconds$")) {
            gameState.getMultiplayerData().as(MineplexData.class).setGameStatus(MineplexGame.Status.STARTING);
        }

    }

    @SubscribeEvent
    public void onGameEnd(final ClientChatReceivedEvent e) {

        if (!gameState.isMineplex() || gameState.getMultiplayerData().as(MineplexData.class).getGame() == MineplexGame.NONE) {
            return;
        }

        final String text = e.message.getUnformattedText();
        if (!text.matches("^ (?:\\w+ won the game!|Nobody won the game...|\\d\\w\\w Place \\w+(?:, \\w+)*)$")) {
            return;
        }

        gameState.getMultiplayerData().as(MineplexData.class).setGameStatus(MineplexGame.Status.IN_LOBBY);
    }

    private void start() {
        this.threadService.execute(() -> new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                final String server = ModuleMineplexServerHandler.this.updateQueue.poll();
                if (server == null) {
                    return;
                }

                final GameState gameState = ModuleMineplexServerHandler.this.gameState;

                // Returning if we're updating to the same server
                if (server.equals(gameState.getMultiplayerData().as(MineplexData.class).getServer())) {
                    return;
                }

                final MineplexData newData = gameState.getMultiplayerData().as(MineplexData.class);
                newData.setServer(server);

                final MineplexServerSwitchEvent switchEvent = new MineplexServerSwitchEvent(
                        gameState.getMultiplayerData().as(MineplexData.class),
                        newData
                );
                MinecraftForge.EVENT_BUS.post(switchEvent);

            }
        }, 0L, 1_000L));
    }

    private void update(final String server) {
        this.updateQueue.add(server);
    }

}
