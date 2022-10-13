package club.mineplex.clans.client.gui.setting;

import club.mineplex.clans.client.CustomFontRenderer;
import club.mineplex.clans.client.gui.button.RoundButton;
import club.mineplex.clans.client.gui.repository.modmenu.ModBoundingBox;
import club.mineplex.clans.client.hud.BoxCoords;
import club.mineplex.clans.client.settings.SettingReflect;
import club.mineplex.clans.util.UtilScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// For Booleans
public class TickBox extends SettingEntry<Boolean> {

    private static final ResourceLocation resourceTickBox =
            new ResourceLocation("clansmod", "textures/mod_menu/options/tick_box.png");
    private static final ResourceLocation resourceTick =
            new ResourceLocation("clansmod", "textures/mod_menu/options/tick.png");
    private static final int BOX_PADDING = 10;
    private static final int BUTTON_SIDE_LENGTH = 10;

    public TickBox(final String displayName, final SettingReflect<Boolean> setting) {
        super(displayName, setting);
    }

    @Override
    public void draw(final CustomFontRenderer fontRenderer, final Color fontColor, final float top, final float left,
                     final float bottom, final float right) {
        fontRenderer.drawString(this.getDisplayName(), left + BUTTON_SIDE_LENGTH + BOX_PADDING, top, fontColor.getRGB());
    }

    @Override
    public @NotNull List<GuiButton> createButtons(final ModBoundingBox boundingBox, final float top, final float left,
                                                  final float bottom, final float right) {
        final BoxCoords box = boundingBox.getBox();
        final List<GuiButton> buttons = new ArrayList<>();

        final int boxTop = (int) (top + (bottom - top) / 2 - (BUTTON_SIDE_LENGTH / 2)) - 1;

        buttons.add(new RoundButton(new Random().nextInt(1000), (int) left, boxTop, BUTTON_SIDE_LENGTH / 2,
                                    BUTTON_SIDE_LENGTH / 2, 25
        ) {
            @Override
            protected void drawShape(final Minecraft mc, final int mouseX, final int mouseY) {
                GlStateManager.pushMatrix();
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                UtilScreen.glScissor(
                        box.getX(),
                        box.getY(),
                        box.getWidth(),
                        box.getHeight()
                );

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableBlend();

                TickBox.this.mc.getTextureManager().bindTexture(resourceTickBox);
                Gui.drawScaledCustomSizeModalRect(
                        this.xPosition,
                        this.yPosition,
                        0,
                        0,
                        25,
                        25,
                        BUTTON_SIDE_LENGTH,
                        BUTTON_SIDE_LENGTH,
                        25,
                        25
                );

                if ((TickBox.this.getSetting().getValue())) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    TickBox.this.mc.getTextureManager().bindTexture(resourceTick);
                    Gui.drawModalRectWithCustomSizedTexture(
                            this.xPosition,
                            this.yPosition,
                            0,
                            0,
                            BUTTON_SIDE_LENGTH,
                            BUTTON_SIDE_LENGTH,
                            BUTTON_SIDE_LENGTH,
                            BUTTON_SIDE_LENGTH
                    );
                }

                GlStateManager.disableBlend();
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                GlStateManager.color(1F, 1F, 1F, 1F);
                GlStateManager.popMatrix();
            }

            @Override
            public void press() {
                if (!boundingBox.getOwner().getModuleList().isMouseWithinBox()) {
                    return;
                }

                getSetting().setValue(!getSetting().getValue());
            }
        });

        return buttons;
    }

}
