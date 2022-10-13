package club.mineplex.clans.client.gui.button;

import club.mineplex.clans.client.gui.GuiButtonAbstract;

public abstract class CircularButton extends GuiButtonAbstract {

    private final int radius, centerX, centerY;

    public CircularButton(final int buttonId, final int x, final int y, final int radius) {
        super(buttonId, x, y, radius * 2, radius * 2, "");
        this.radius = radius;
        this.centerX = x + radius;
        this.centerY = y + radius;
    }

    @Override
    protected boolean isHovering(final int mouseX, final int mouseY) {
        final double mouseDistance = Math.sqrt(Math.pow(this.centerX - mouseX, 2) + Math.pow(this.centerY - mouseY, 2));
        return mouseDistance <= this.radius;
    }

}
