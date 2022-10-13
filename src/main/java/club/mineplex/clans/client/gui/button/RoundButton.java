package club.mineplex.clans.client.gui.button;

import club.mineplex.clans.client.gui.GuiButtonAbstract;

public abstract class RoundButton extends GuiButtonAbstract {

    protected final int radiusX, radiusY, centerX, centerY;
    protected final double angle;

    public RoundButton(final int buttonId, final int x, final int y, final int radiusX, final int radiusY,
                       final double angle) {
        super(buttonId, x, y, radiusX * 2, radiusY * 2, "");
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.angle = angle;
        this.centerX = x + radiusX;
        this.centerY = y + radiusY;
    }

    @Override
    protected boolean isHovering(final int mouseX, final int mouseY) {
        return (Math.pow(mouseX - this.centerX, 2 * this.angle) / Math.pow(this.radiusX, 2 * this.angle))
                + (Math.pow(mouseY - this.centerY, 2 * this.angle) / Math.pow(this.radiusY, 2 * this.angle)) <= 1D;
    }

}
