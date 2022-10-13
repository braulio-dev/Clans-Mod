package club.mineplex.clans.client.hud;

import club.mineplex.clans.util.UtilScreen;
import club.mineplex.clans.util.object.Pair;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public interface IHudRenderer {

    void draw(int x, int y, double scaleFactor, boolean dummy);

    default void render(final int x, final int y, final double scaleFactor, boolean dummy) {
        GlStateManager.pushMatrix();

        final double translatedX = x * scaleFactor;
        final double translatedY = y * scaleFactor;

        GlStateManager.translate((x - translatedX), (y - translatedY), 0);
        GlStateManager.scale(scaleFactor, scaleFactor, 1F);

        final Pair<Double, Double> size = this.getBox().getSize();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        UtilScreen.glScissor(x, y, (int) (double) size.getKey(), (int) (double) size.getValue());
        this.draw(x, y, scaleFactor, dummy);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
    }

    HudBoundingBox getBox();

    boolean shouldRender();

    String getName();

    String getId();

}
