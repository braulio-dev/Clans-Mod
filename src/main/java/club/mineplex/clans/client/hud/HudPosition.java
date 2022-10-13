package club.mineplex.clans.client.hud;

import org.apache.commons.lang3.Validate;

public class HudPosition {

    private double positionalFactorX;
    private double positionalFactorY;

    private int screenWidth;
    private int screenHeight;

    public HudPosition(final double positionalFactorX, final double positionalFactorY) {
        this.positionalFactorX = positionalFactorX;
        this.positionalFactorY = positionalFactorY;
    }

    public void rescaleWindow(final int width, final int height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }

    public void fromFactors(final double positionalFactorX, final double positionalFactorY) {
        this.positionalFactorX = positionalFactorX;
        this.positionalFactorY = positionalFactorY;
    }

    public double getPositionalFactorX() {
        return this.positionalFactorX;
    }

    public double getPositionalFactorY() {
        return this.positionalFactorY;
    }

    public int getX() {
        return (int) (this.screenWidth * this.positionalFactorX);
    }

    private void setX(final int x) {
        Validate.isTrue(x >= 0, "'x' Value must be positive or 0");
        this.positionalFactorX = this.screenWidth == 0 ? 0 : (double) x / this.screenWidth;
    }

    public int getY() {
        return (int) (this.screenHeight * this.positionalFactorY);
    }

    private void setY(final int y) {
        Validate.isTrue(y >= 0, "'y' Value must be positive or 0");
        this.positionalFactorY = this.screenHeight == 0 ? 0 : (double) y / this.screenHeight;
    }

    public void setPosition(final int x, final int y) {
        this.setX(x);
        this.setY(y);
    }

}
