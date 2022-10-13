package club.mineplex.clans.client.gui.repository;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.client.gui.GuiModScreen;

import java.awt.*;

public class GuiVersionBlock extends GuiModScreen {

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) {
        // IGNORE
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();

        final String title = String.format(
                "Your %s has started to update (%s ➜ %s)",
                "Clans Mod", // TODO: Implement variable
                ClansMod.getInstance().getClient().getVersion(),
                ClansMod.getInstance().getClient().getLatestVersion()
        );

        this.fontRendererObj.drawStringWithShadow(
                title,
                (float) this.width / 2 - (this.fontRendererObj.getStringWidth(title) / 2F),
                this.height / 2F - 20,
                0xFFFFFF
        );

        final String secondLine = "When the bar below turns green, please restart your game";
        this.fontRendererObj.drawStringWithShadow(
                secondLine,
                (float) this.width / 2 - (this.fontRendererObj.getStringWidth(secondLine) / 2F),
                this.height / 2F,
                0xFFFFFF
        );

        final Color outlineColor = new Color(122, 122, 122);
        for (int i = 0; i < 2; i++) {
            drawHorizontalLine(
                    this.width / 4,
                    this.width / 2,
                    this.height / 2 + 10 + i,
                    outlineColor.getRGB()
            );
        }

        final Color color = ClansMod.getInstance().getClient().isUpdating() ? new Color(250, 90, 90) : new Color(89, 255, 111);
        drawGradientRect(
                this.width / 4,
                this.height / 2 + 10 + 2 + 1,
                this.width / 4 * 3,
                this.height / 2 + 10 + 2 + 1 + 11,
                color.getRGB(),
                color.getRGB()
        );

        for (int i = 0; i < 2; i++) {
            drawHorizontalLine(
                    this.width / 4,
                    this.width / 2,
                    this.height / 2 + 10 + 2 + 1 + 11 + 1 + i,
                    outlineColor.getRGB()
            );
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
