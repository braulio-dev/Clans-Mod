package club.mineplex.clans.client.keybind.repository;

import club.mineplex.clans.client.keybind.ModKeybind;
import club.mineplex.clans.client.settings.repo.ClientSettings;
import club.mineplex.clans.util.UtilClient;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import static net.minecraft.util.EnumChatFormatting.*;

public class KeybindRenderDistance extends ModKeybind {

    private static final EnumChatFormatting[] colors = {RED, GOLD, YELLOW, GREEN, DARK_GREEN};
    private static final int[] renderDistances = {2, 4, 6, 8, 10};

    private int index = -1;

    public KeybindRenderDistance() {
        super("render-cycle.desc", Keyboard.KEY_K);
        final int currentDistance = Minecraft.getMinecraft().gameSettings.renderDistanceChunks;

        for (int i = 0; i < renderDistances.length; i++) {
            final int option = renderDistances[i];
            if (option == currentDistance) {
                this.index = i;
            }
        }

        if (this.index == -1) {
            this.index = 2;
        }
    }

    @Override
    public void onPress() {
        if (!ClientSettings.CYCLE_RENDER_DISTANCE_KEY) {
            return;
        }

        final int previous = this.index;
        this.index = this.index + 1 > renderDistances.length - 1 ? 0 : this.index + 1;
        Minecraft.getMinecraft().gameSettings.renderDistanceChunks = renderDistances[this.index];
        Minecraft.getMinecraft().gameSettings.saveOptions();

        this.sendNotification(previous, this.index);
    }

    private void sendNotification(final int previous, final int next) {
        final String subtitleText = colors[previous] + String.valueOf(renderDistances[previous])
                + WHITE + " ➜ " + colors[next] + renderDistances[next];

        Minecraft.getMinecraft().ingameGUI.displayTitle(
                GRAY + "Changed Render Distance",
                null,
                0,
                30,
                3
        );

        Minecraft.getMinecraft().ingameGUI.displayTitle(
                null,
                subtitleText,
                0,
                30,
                3
        );

        UtilClient.playSound("note.pling", 1F, 2F);
    }

}
