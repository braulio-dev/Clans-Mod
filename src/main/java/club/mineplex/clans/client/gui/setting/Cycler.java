package club.mineplex.clans.client.gui.setting;

import club.mineplex.clans.client.CustomFontRenderer;
import club.mineplex.clans.client.gui.button.BoxButton;
import club.mineplex.clans.client.gui.repository.modmenu.ModBoundingBox;
import club.mineplex.clans.client.gui.repository.modmenu.ModMenuList;
import club.mineplex.clans.client.settings.IterableSetting;
import club.mineplex.clans.client.settings.IterableState;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Cycler extends SettingEntry<IterableState> {

    private static final ResourceLocation arrowLeft =
            new ResourceLocation("clansmod", "textures/mod_menu/options/arrow_left.png");
    private static final ResourceLocation arrowRight =
            new ResourceLocation("clansmod", "textures/mod_menu/options/arrow_right.png");


    private static final int ARROW_SPACING = 50;
    private static final int ARROW_SIDE_LENGTH = 10;

    public Cycler(String displayName, SettingReflect<IterableState> setting) {
        super(displayName, setting);
    }

    @Override
    public void draw(CustomFontRenderer fontRenderer, Color fontColor, float top, float left, float bottom, float right) {
        fontRenderer.drawString(this.getDisplayName(), left, top, fontColor.getRGB());
        final String state = this.getSetting().getValue().getDisplayString();
        int x = (int) (right - ARROW_SIDE_LENGTH - ARROW_SPACING / 2F + 3);
        fontRenderer.drawCenteredString(state, x, (int) top, fontColor.getRGB());
    }

    @Override
    public @NotNull List<GuiButton> createButtons(ModBoundingBox boundingBox, float top, float left,
                                                  float bottom, float right) {
        final ModMenuList list = boundingBox.getOwner().getModuleList();
        final List<GuiButton> buttons = new ArrayList<>();

        final int cyclerLeft = (int) (right - ARROW_SPACING - ARROW_SIDE_LENGTH * 2);
        final int cyclerTop = (int) (top - 2);

        final Random random = new Random();
        // Left
        buttons.add(new BoxButton(random.nextInt(1_000_000), cyclerLeft, cyclerTop, ARROW_SIDE_LENGTH, ARROW_SIDE_LENGTH) {
            @Override
            protected void drawShape(Minecraft mc, int mouseX, int mouseY) {
                drawArrow(list, arrowLeft, xPosition, yPosition);
            }

            @Override
            public void press() {
                cycle(true);
            }
        });

        // Right
        buttons.add(new BoxButton(random.nextInt(1_000_000), (int) (right - ARROW_SIDE_LENGTH), cyclerTop, ARROW_SIDE_LENGTH, ARROW_SIDE_LENGTH) {
            @Override
            protected void drawShape(Minecraft mc, int mouseX, int mouseY) {
                drawArrow(list, arrowRight, xPosition, yPosition);
            }

            @Override
            public void press() {
                cycle(false);
            }
        });

        return buttons;
    }

    private void drawArrow(ModMenuList list, ResourceLocation resource, int xPosition, int yPosition) {
        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        UtilScreen.glScissor(
                list.getBoxLeft(),
                list.getBoxTop(),
                list.getBoxRight() - list.getBoxLeft(),
                list.getBoxBottom() - list.getBoxTop()
        );

        Cycler.this.mc.getTextureManager().bindTexture(resource);
        Gui.drawScaledCustomSizeModalRect(
                xPosition,
                yPosition,
                0,
                0,
                25,
                25,
                ARROW_SIDE_LENGTH,
                ARROW_SIDE_LENGTH,
                25,
                25
        );

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
    }

    private void cycle(boolean backwards) {
        final Field field = getSetting().getField();
        final IterableSetting iterable = field.getAnnotation(IterableSetting.class);
        final List<IterableState> states = Arrays.asList(iterable.states());
        final int indexOf = states.indexOf(getSetting().getValue());

        int newIndex = indexOf + 1 > states.size() - 1 ? 0 : indexOf + 1;
        if (backwards) {
            newIndex = indexOf - 1 < 0 ? states.size() - 1 : indexOf - 1;
        }

        getSetting().setValue(states.get(newIndex));
    }
}
