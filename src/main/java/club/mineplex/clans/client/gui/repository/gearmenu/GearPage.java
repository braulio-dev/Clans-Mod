package club.mineplex.clans.client.gui.repository.gearmenu;

import club.mineplex.clans.client.gui.GuiModScreen;
import club.mineplex.clans.client.gui.button.RoundButton;
import club.mineplex.clans.item.legendary.LegendaryItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GearPage extends GuiModScreen {

    @Override
    public void initGui() {
        this.buttonList.add(new RoundButton(1, this.width / 2 - 10, this.height / 20 + 10 + 25 - 10, 20, 20, 130) {

            @Override
            protected void drawShape(Minecraft mc, int mouseX, int mouseY) {
                final RenderItem renderer = GearPage.this.itemRender;
                final Tessellator tessellator = Tessellator.getInstance();
                final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                final int radius = radiusX;
                final int ringWidth = radius / 6;
                int x = centerX;
                int y = centerY;

                GlStateManager.pushMatrix();
                GL11.glEnable(3042);
                GL11.glDisable(3553);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);

                // Background
                GL11.glColor4f(1F, 1F, 1F, 1F);
                Color color = hovered ? new Color(79, 232, 255, 117) : new Color(70, 70, 70, 117);
                worldRenderer.begin(6, DefaultVertexFormats.POSITION);
                worldRenderer.pos(x, y, 0).endVertex();

                GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
                for (int i = 0; i <= 360; ++i) {
                    final double sin = Math.sin(Math.toRadians(i));
                    final double cos = Math.cos(Math.toRadians(i));
                    worldRenderer.pos(x + sin * radius, y + cos * radius, 0.0).endVertex();
                }

                tessellator.draw();

                // Edges
                Color primaryColor = new Color(92, 92, 92);
                for (double i = 0; i <= 360; i += 0.4) {
                    GL11.glColor4f(
                            primaryColor.getRed() / 255F,
                            primaryColor.getGreen() / 255F,
                            primaryColor.getBlue() / 255F,
                            1F
                    );

                    worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
                    final double cos = Math.cos(Math.toRadians(i + 90));
                    final double sin = Math.sin(Math.toRadians(i + 90));

                    worldRenderer.pos(
                            x + cos * (radius - ringWidth - 1),
                            y - sin * (radius - ringWidth - 1),
                            0
                    ).endVertex();
                    worldRenderer.pos(
                            x + cos * radius,
                            y - sin * radius,
                            0
                    ).endVertex();

                    tessellator.draw();
                }

                GL11.glColor4f(1F, 1F, 1F, 1F);
                GL11.glEnable(3553);
                GL11.glDisable(3042);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.popMatrix();

                renderer.zLevel = 100.0F;
                GL11.glColor4f(1F, 1F, 1F, 1F);
                final ItemStack item = new ItemStack(LegendaryItem.Type.HYPER_AXE.getType());
                renderer.renderItemAndEffectIntoGUI(item, x + 2 - radius / 2, y + 2 - radius / 2);
                renderer.renderItemOverlays(mc.fontRendererObj, item, x + 2 - radius / 2, y + 2 - radius / 2);
                renderer.zLevel = 0.0F;
            }
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        final int fontColor = new Color(191, 191, 191).getRGB();
        this.drawCenteredString(this.fontRendererObj,
                                "Choose an item",
                                this.width / 2,
                                this.height / 20 + 10 - this.fontRendererObj.FONT_HEIGHT,
                                fontColor
        );

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}