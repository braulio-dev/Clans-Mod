package club.mineplex.clans.client.gui.button;

import club.mineplex.clans.client.gui.GuiButtonAbstract;

public abstract class BoxButton extends GuiButtonAbstract {

    public BoxButton(final int buttonId, final int x, final int y, final int width, final int height) {
        super(buttonId, x, y, width, height, "");
    }

    @Override
    protected boolean isHovering(final int mouseX, final int mouseY) {
        return (mouseX > this.xPosition && mouseX < this.xPosition + this.width)
                && (mouseY > this.yPosition && mouseY < this.yPosition + this.height);
    }

}
