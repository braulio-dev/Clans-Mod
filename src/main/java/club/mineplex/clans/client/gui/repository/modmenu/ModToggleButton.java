package club.mineplex.clans.client.gui.repository.modmenu;

import club.mineplex.clans.client.gui.button.RoundButton;
import club.mineplex.clans.util.UtilScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public abstract class ModToggleButton extends RoundButton {

    private static final ResourceLocation toggleButtonBackground =
            new ResourceLocation("clansmod", "textures/mod_menu/modules/toggle_background.png");
    private static final ResourceLocation toggleDisabled =
            new ResourceLocation("clansmod", "textures/mod_menu/modules/toggle_disabled.png");
    private static final ResourceLocation toggleEnabled =
            new ResourceLocation("clansmod", "textures/mod_menu/modules/toggle_enabled.png");

    private final ModMenuList list;

    protected ModToggleButton(final ModBoundingBox boundingBox, final int buttonId, final int x, final int y,
                           final int radiusX, final int radiusY, final double angle) {
        super(buttonId, x, y, radiusX, radiusY, angle);
        this.list = boundingBox.getOwner().getModuleList();
    }

    @Override
    protected void drawShape(final Minecraft mc, final int mouseX, final int mouseY) {
        final int buttonX = this.xPosition;
        final int buttonY = this.yPosition;
        final int buttonWidth = this.width;
        final int buttonHeight = this.height;
        final int statusSideLength = (int) (buttonHeight * (2D / 3D));
        final int statusPadding = (buttonHeight - statusSideLength) / 2;

        // Drawing button background
        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        UtilScreen.glScissor(this.list.getBoxLeft(), this.list.getBoxTop(),
                             (double) this.list.getBoxRight() - this.list.getBoxLeft(),
                             (double) this.list.getBoxBottom() - this.list.getBoxTop()
        );

        mc.getTextureManager().bindTexture(toggleButtonBackground);
        Gui.drawModalRectWithCustomSizedTexture(
                buttonX,
                buttonY,
                0,
                0,
                buttonWidth,
                buttonHeight,
                buttonWidth,
                buttonHeight
        );

        // Drawing toggle status
        mc.getTextureManager().bindTexture(isEnabled() ? toggleEnabled : toggleDisabled);
        Gui.drawModalRectWithCustomSizedTexture(
                isEnabled() ? buttonX + buttonWidth - statusPadding - statusSideLength :
                        buttonX + statusPadding,
                buttonY + statusPadding,
                0,
                0,
                statusSideLength,
                statusSideLength,
                statusSideLength,
                statusSideLength
        );

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
    }

    protected abstract boolean isEnabled();

//    @Override
//    public void press() {
//        if (!this.list.isMouseWithinBox()) {
//            return;
//        }
//
//        this.boundingBox.getCategory().setEnabled(!this.boundingBox.getCategory().isEnabled());
//        this.boundingBox.getCategory().save();
//    }

}
