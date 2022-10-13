package club.mineplex.clans.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;

public abstract class GuiButtonAbstract extends GuiButtonDefault {

    protected GuiButtonAbstract(final int buttonId, final int x, final int y, final int widthIn, final int heightIn,
                                final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            this.hovered = this.isHovering(mouseX, mouseY);
            this.drawShape(mc, mouseX, mouseY);
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    @Override
    public final boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        return this.enabled && this.visible && this.isHovering(mouseX, mouseY);
    }

    public void onScreenLeftClick(final int mouseX, final int mouseY) {

    }

    public void mouseDrag(final int mouseX, final int mouseY) {

    }

    public boolean shouldOverlapOtherButtons() {
        return false;
    }

    @Override
    public void playPressSound(final SoundHandler soundHandlerIn) {

    }

    protected abstract boolean isHovering(final int mouseX, final int mouseY);

    protected abstract void drawShape(final Minecraft mc, final int mouseX, final int mouseY);

}

