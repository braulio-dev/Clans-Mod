package club.mineplex.clans.util;

import club.mineplex.clans.util.object.DelayedTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class UtilScreen {
    private UtilScreen() {
    }

    public static void glScissor(final double x, double y, final double width, final double height) {

        y += height;

        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

        final Minecraft mc = Minecraft.getMinecraft();

        GL11.glScissor((int) ((x * mc.displayWidth) / scaledResolution.getScaledWidth()),
                       (int) (((scaledResolution.getScaledHeight() - y) * mc.displayHeight)
                               / scaledResolution.getScaledHeight()),
                       (int) (width * mc.displayWidth / scaledResolution.getScaledWidth()),
                       (int) (height * mc.displayHeight / scaledResolution.getScaledHeight())
        );
    }


    public static void displayGuiScreen(final GuiScreen guiScreen) {
        new DelayedTask(() -> Minecraft.getMinecraft().displayGuiScreen(guiScreen));
    }

    public static int getHueFromColor(final Color color) {
        final int r = color.getRed();
        final int g = color.getGreen();
        final int b = color.getBlue();

        final float min = Math.min(Math.min(r, g), b);
        final float max = Math.max(Math.max(r, g), b);

        if (max == min) {
            return 0;
        }

        float hue;
        if (max == r) {
            hue = (g - b) / (max - min);
        } else if (max == g) {
            hue = 2F + (b - r) / (max - min);
        } else {
            hue = 4F + (r - g) / (max - min);
        }

        hue *= 60;

        return Math.round(hue < 0 ? hue + 360 : hue);
    }

    /**
     * @param x
     * @param y
     * @param width
     * @param height
     * @param cornerRadius
     * @param color
     * @author danterus
     */
    public static void drawRoundedRect(final int x, final int y, final int width, final int height,
                                       final int cornerRadius, final Color color) {
        GlStateManager.pushMatrix();
        Gui.drawRect(x, y + cornerRadius, x + cornerRadius, y + height - cornerRadius, color.getRGB());
        Gui.drawRect(x + cornerRadius, y, x + width - cornerRadius, y + height, color.getRGB());
        Gui.drawRect(x + width - cornerRadius, y + cornerRadius, x + width, y + height - cornerRadius, color.getRGB());
        GlStateManager.popMatrix();

        drawArc(x + cornerRadius, y + cornerRadius, cornerRadius, 0, 90, color);
        drawArc(x + width - cornerRadius, y + cornerRadius, cornerRadius, 270, 360, color);
        drawArc(x + width - cornerRadius, y + height - cornerRadius, cornerRadius, 180, 270, color);
        drawArc(x + cornerRadius, y + height - cornerRadius, cornerRadius, 90, 180, color);
    }

    /**
     * @param x
     * @param y
     * @param radius
     * @param startAngle
     * @param endAngle
     * @param color
     * @author danterus
     */
    private static void drawArc(final int x, final int y, final int radius, final int startAngle,
                                final int endAngle, final Color color) {

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255,
                       (float) color.getAlpha() / 255
        );

        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();

        worldRenderer.begin(6, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x, y, 0).endVertex();

        for (int i = (int) (startAngle / 360.0 * 100); i <= (int) (endAngle / 360.0 * 100); i++) {
            final double angle = (Math.PI * 2 * i / 100) + Math.toRadians(180);
            worldRenderer.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0).endVertex();
        }

        Tessellator.getInstance().draw();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawColorPickerButton(final int x, final int y, final int sideLength) {
        final Color[] colors = {
                new Color(255, 255, 255, 255),
                new Color(59, 59, 59, 255)
        };

        for (int i = colors.length - 1; i >= 0; i--) {
            final Color color = colors[i];
            GlStateManager.pushMatrix();
            Gui.drawRect(x + i, y + i, x + sideLength + i, y + sideLength + i, color.getRGB());
            GlStateManager.popMatrix();
        }
    }

    public static void drawColorPickerSlider(final int x, final int y, final int width, final int height) {
        final Color[] colors = {
                new Color(255, 255, 255, 255),
                new Color(59, 59, 59, 255)
        };

        for (int i = colors.length - 1; i >= 0; i--) {
            final Color color = colors[i];
            GlStateManager.pushMatrix();
            Gui.drawRect(x + i, y + i, x + width + i, y + height + i, color.getRGB());
            GlStateManager.popMatrix();
        }
    }

}
