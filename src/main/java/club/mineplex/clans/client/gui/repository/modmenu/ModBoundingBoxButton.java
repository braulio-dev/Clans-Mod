package club.mineplex.clans.client.gui.repository.modmenu;

import club.mineplex.clans.client.gui.button.BoxButton;
import club.mineplex.clans.util.UtilScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ModBoundingBoxButton extends BoxButton {

    private final ModBoundingBox boundingBox;
    private final ModMenuList list;
    private float hoverAlpha;
    private long lastUpdatedHover;

    public ModBoundingBoxButton(final ModBoundingBox boundingBox, final int buttonId, final int x, final int y,
                                final int width, final int height) {
        super(buttonId, x, y, width, height);
        this.boundingBox = boundingBox;
        this.list = boundingBox.getOwner().getModuleList();
    }

    @Override
    protected void drawShape(final Minecraft mc, final int mouseX, final int mouseY) {
        // Return if we aren't hovering or if there's another button being hovered
        if (!this.isMouseOver() || this.boundingBox.getOwner().getButtons().stream().filter(button -> button != this)
                                                   .anyMatch(GuiButton::isMouseOver)) {
            this.hoverAlpha = 0;
            this.lastUpdatedHover = 0;
            return;
        }

        if (this.lastUpdatedHover == 0) {
            this.lastUpdatedHover = System.currentTimeMillis();
        }

        this.hoverAlpha += (System.currentTimeMillis() - this.lastUpdatedHover) / 2000F;
        final float alpha = Math.min(25 / 255F, Math.max(0F, this.hoverAlpha));

        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GlStateManager.enableAlpha();
        UtilScreen.glScissor(this.list.getBoxLeft(), this.list.getBoxTop(),
                             (double) this.list.getBoxRight() - this.list.getBoxLeft(),
                             (double) this.list.getBoxBottom() - this.list.getBoxTop()
        );
        Gui.drawRect(
                this.xPosition,
                this.yPosition,
                this.xPosition + this.width,
                this.yPosition + this.height,
                new Color(1F, 1F, 1F, alpha).getRGB()
        );
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
        this.lastUpdatedHover = System.currentTimeMillis();
    }

    @Override
    public void press() {
        for (final GuiButton button : this.boundingBox.getOwner().getButtons()) {
            if (button != this && button.isMouseOver()) {
                // Return if the mouse is hovering over another button
                return;
            }
        }

        if (!this.list.isMouseWithinBox()) {
            return;
        }

        this.boundingBox.setExpanded(!this.boundingBox.isExpanded());
    }

}
