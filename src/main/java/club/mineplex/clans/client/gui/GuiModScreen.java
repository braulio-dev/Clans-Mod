package club.mineplex.clans.client.gui;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.gamestate.GameState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public abstract class GuiModScreen extends GuiScreen {

    protected final GameState gameState = ClansMod.getInstance().getGameState();
    protected final GuiScreen previous;

    public GuiModScreen(final GuiScreen previous) {
        this.previous = previous;
    }

    public GuiModScreen() {
        this.previous = null;
    }

    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {

        if (button.id == 200 && button.enabled) {
            if (this.previous != null) {

                this.mc.displayGuiScreen(this.previous);

            } else {

                try {
                    this.mc.thePlayer.closeScreen();
                } catch (final NullPointerException e) {
                    this.mc.displayGuiScreen(new GuiMainMenu());
                }

            }
            return;
        }

        for (final GuiButton guiButton : this.buttonList) {
            if (guiButton == button) {
                continue;
            }

            if (guiButton.isMouseOver() && guiButton instanceof GuiButtonAbstract
                    && ((GuiButtonAbstract) guiButton).shouldOverlapOtherButtons()) {
                return; // Return if there's two buttons overlapping each other
            }
        }

    }

    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton,
                                  final long timeSinceLastClick) {
        for (final GuiButton guiButton : this.buttonList) {
            if (!(guiButton instanceof GuiButtonAbstract)) {
                continue;
            }

            final GuiButtonAbstract button = (GuiButtonAbstract) guiButton;
            button.mouseDrag(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final GuiButton guiButton : this.buttonList) {
            if (!(guiButton instanceof GuiButtonAbstract)) {
                continue;
            }

            final GuiButtonAbstract button = (GuiButtonAbstract) guiButton;
            if (mouseButton == 0) { // Is left click
                button.onScreenLeftClick(mouseX, mouseY);
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE && this.previous != null) {
            this.mc.displayGuiScreen(this.previous);
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

}
