package club.mineplex.clans.client.modules.champions.renderer;

import club.mineplex.clans.client.gui.setting.TickBox;
import club.mineplex.clans.client.hud.HudBoundingBox;
import club.mineplex.clans.client.modules.champions.ModuleChampions;
import club.mineplex.clans.client.settings.IterableState;
import club.mineplex.clans.client.settings.repo.ChampionsSettings;
import club.mineplex.core.mineplex.champions.ChampionsKit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Optional;

/**
 * @author Rey
 * @created 21/09/2022
 * @project Clans Mod
 */
public class ManaRenderer extends ChampionsRenderer {

    private static final ResourceLocation manaBarTexture = new ResourceLocation("clansmod", "textures/gui/manabar.png");

    private final HudBoundingBox box = HudBoundingBox.builder().size(17, 71).build();

    public ManaRenderer(ModuleChampions championsModule) {
        super(championsModule, "Mana Renderer", "mana-renderer");
    }

    @Override
    public void draw(int x, int y, double scaleFactor, boolean dummy) {
        final Optional<ChampionsKit> playerKit = championsModule.getPlayerKit(mc.thePlayer);
        if (!playerKit.isPresent() || playerKit.get() != ChampionsKit.MAGE) {
            return;
        }

        int yPosition = y + 1;
        int xPosition = x + 3;

        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(manaBarTexture);

        Gui.drawScaledCustomSizeModalRect(
                xPosition,
                yPosition,
                11,
                0,
                11,
                69,
                11,
                69,
                22,
                69
        );

        final int manaRelation = (int) (mc.thePlayer.experience * 69);
        Gui.drawModalRectWithCustomSizedTexture(
                xPosition,
                yPosition + 69 - manaRelation,
                0,
                69F - manaRelation,
                11,
                manaRelation,
                22,
                69
        );

        int percentage = Math.round((mc.thePlayer.experience * 100));

        final FontRenderer fontRenderer = mc.fontRendererObj;
        final String percentageText = String.format("%d%%", percentage);

        if (ChampionsSettings.MANA_RENDERER.equals(IterableState.ON)) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            fontRenderer.drawString(
                    percentageText,
                    box.getDefaultSize().getKey() / 2F + x - fontRenderer.getStringWidth(percentageText) / 2F + 1,
                    box.getDefaultSize().getValue() / 2F + y - fontRenderer.FONT_HEIGHT / 2F - 1F,
                    new Color(255 - percentage / 2, 140 + percentage, 140 + percentage / 2).getRGB(),
                    true
            );
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
        }

        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public HudBoundingBox getBox() {
        return box;
    }

    @Override
    protected boolean isEnabled() {
        return !ChampionsSettings.MANA_RENDERER.equals(IterableState.OFF);
    }

}
