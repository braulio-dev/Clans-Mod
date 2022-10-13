package club.mineplex.clans.client.gui.setting;

import club.mineplex.clans.client.CustomFontRenderer;
import club.mineplex.clans.client.gui.button.BoxButton;
import club.mineplex.clans.client.gui.button.RoundButton;
import club.mineplex.clans.client.gui.repository.modmenu.ModBoundingBox;
import club.mineplex.clans.client.hud.BoxCoords;
import club.mineplex.clans.client.settings.SettingReflect;
import club.mineplex.clans.util.UtilScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ColorPicker extends SettingEntry<Color> {

    private static final long serialVersionUID = -2616110411004189089L;
    private static final ResourceLocation transparentTexture =
            new ResourceLocation("clansmod", "textures/mod_menu/options/transparent.png");
    private static final ResourceLocation resourceColorBox =
            new ResourceLocation("clansmod", "textures/mod_menu/options/color_picker.png");

    private final int buttonSideLength = 10;
    private final int colorPickerWidth = 220;
    private final int colorPickerHeight = 120;
    private final int buttonSidePadding = 10;
    private final int elementHeight = 100;
    private final int elementWidth = 30;
    private final int yPadding = (this.colorPickerHeight - this.elementHeight) / 2;
    private boolean isChoosingColor = false;
    private int mouseX, mouseY;
    private float hue, alpha;
    private int hueY = -1;
    private int alphaY = -1;
    private int colorX = -1, colorY = -1;

    public ColorPicker(final String displayName, final SettingReflect<Color> setting) {
        super(displayName, setting);
    }

    private void init() {
        final double accuracyThreshold = 0.005;

        final Color currentColor = this.getColor();
        final float currentRed = currentColor.getRed() / 255F;
        final float currentGreen = currentColor.getGreen() / 255F;
        final float currentBlue = currentColor.getBlue() / 255F;

        this.hue = UtilScreen.getHueFromColor(this.getSetting().getValue()) / 360F;
        this.alpha = this.getSetting().getValue().getAlpha() / 255f;

        final Color goal = this.getBaseColor();
        final int width = this.elementWidth * 4;
        final int height = this.elementHeight;

        // Setting default color picker button location
        if (this.colorX == -1 && this.colorY == -1) {
            for (int i = 0; i <= width; i++) {
                for (int level = 0; level <= height; level++) {
                    final float possibleFactor = ((height - level) * 1F / height);

                    final float possibleRed =
                            Math.min(1F, Math.max(0, (goal.getRed() / 255F) + (i * 1F / width))) * possibleFactor;
                    final float possibleGreen =
                            Math.min(1F, Math.max(0, (goal.getGreen() / 255F) + (i * 1F / width))) * possibleFactor;
                    final float possibleBlue =
                            Math.min(1F, Math.max(0, (goal.getBlue() / 255F) + (i * 1F / width))) * possibleFactor;

                    if (this.comparedDecimalThreshold(currentRed, possibleRed, accuracyThreshold)
                            && this.comparedDecimalThreshold(currentGreen, possibleGreen, accuracyThreshold)
                            && this.comparedDecimalThreshold(currentBlue, possibleBlue, accuracyThreshold)) {
                        this.colorX = i;
                        this.colorY = level;
                    }

                }
            }
        }

        // Setting default hue picker button location
        // Setting default alpha picker button location
        if (this.alphaY == -1 && this.hueY == -1) {
            for (int i = 0; i <= height; i++) {
                final float col = 1F - ((float) i / height);
                final Color alphaColor = new Color(0, 0, 0, col);

                if (this.comparedDecimalThreshold(this.hue, col, accuracyThreshold)) {
                    this.hueY = i;
                }

                if (this.alphaY == -1 && this.comparedDecimalThreshold(this.alpha, alphaColor.getAlpha() / 255F,
                                                                       accuracyThreshold
                )) {
                    this.alphaY = i;
                }
            }
        }

    }

    @Override
    public void draw(final CustomFontRenderer fontRenderer, final Color fontColor, final float top, final float left,
                     final float bottom, final float right) {
        fontRenderer.drawString(this.getDisplayName(), left, top, fontColor.getRGB());
        fontRenderer.drawString(this.getHexadecimal(), right - 80 + this.buttonSideLength + this.buttonSideLength / 2F,
                                top, fontColor.getRGB()
        );
    }

    @Override
    public void resetDrawing() {
        this.isChoosingColor = false;
    }

    @Override
    public @NotNull List<GuiButton> createButtons(final ModBoundingBox boundingBox, final float top, final float left,
                                                  final float bottom, final float right) {
        final BoxCoords box = boundingBox.getBox();
        final LinkedList<GuiButton> buttons = new LinkedList<>();

        final int boxTop = (int) (top + (bottom - top) / 2 - (this.buttonSideLength / 2));
        final int buttonLeft = (int) (right - 80);

        buttons.add(new RoundButton(new Random().nextInt(1000), buttonLeft, boxTop, this.buttonSideLength / 2,
                                    this.buttonSideLength / 2, 25
        ) {
            @Override
            protected void drawShape(final Minecraft mc, final int mouseX, final int mouseY) {

                GlStateManager.pushMatrix();
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);
                UtilScreen.glScissor(
                        box.getX(),
                        box.getY(),
                        box.getWidth(),
                        box.getHeight()
                );

                ColorPicker.this.mc.getTextureManager().bindTexture(resourceColorBox);
                Gui.drawScaledCustomSizeModalRect(
                        this.xPosition,
                        this.yPosition,
                        0,
                        0,
                        25,
                        25,
                        ColorPicker.this.buttonSideLength,
                        ColorPicker.this.buttonSideLength,
                        25,
                        25
                );

                Gui.drawRect(
                        this.xPosition + 1,
                        this.yPosition + 1,
                        this.xPosition + ColorPicker.this.buttonSideLength - 1,
                        this.yPosition + ColorPicker.this.buttonSideLength - 1,
                        ColorPicker.this.getSetting().getValue().getRGB()
                );

                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                GlStateManager.enableDepth();
                GlStateManager.popMatrix();
                GlStateManager.color(1F, 1F, 1F, 1F);

            }

            @Override
            public void press() {
                ColorPicker.this.isChoosingColor = true;
                boundingBox.updateButtons();
            }
        });

        if (this.isChoosingColor) {

            this.init();
            int colorPickerX = buttonLeft - 5, colorPickerY = (int) (top + this.buttonSideLength + 3);

            final int width = boundingBox.getOwner().width;
            final int height = boundingBox.getOwner().height;
            if (colorPickerX + this.colorPickerWidth > width) {
                colorPickerX = width - this.colorPickerWidth;
            }

            if (colorPickerY + this.colorPickerHeight > height) {
                colorPickerY = height - this.colorPickerHeight;
            }

            buttons.add(new BoxButton(new Random().nextInt(1000), colorPickerX, colorPickerY, this.colorPickerWidth,
                                      this.colorPickerHeight
            ) {

                @Override
                protected void drawShape(final Minecraft mc, final int mouseX, final int mouseY) {
                    GlStateManager.pushMatrix();
                    GlStateManager.pushAttrib();
                    GlStateManager.disableBlend();
                    UtilScreen.drawRoundedRect(this.xPosition, this.yPosition, this.width, this.height, 6,
                                               new Color(0, 0, 0, 150)
                    );

                    // Color Picker backgrounds
                    final Color colorPickerBg = new Color(50, 50, 50, 175);
                    UtilScreen.drawRoundedRect(
                            this.xPosition + ColorPicker.this.yPadding - 3,
                            this.yPosition + ColorPicker.this.yPadding - 2,
                            ColorPicker.this.elementWidth * 4 + 6,
                            ColorPicker.this.elementHeight + 4,
                            3,
                            colorPickerBg
                    );

                    UtilScreen.drawRoundedRect(
                            this.xPosition + ColorPicker.this.yPadding + ColorPicker.this.elementWidth * 4
                                    + ColorPicker.this.buttonSidePadding - 3,
                            this.yPosition + ColorPicker.this.yPadding - 2,
                            ColorPicker.this.elementWidth + 6,
                            ColorPicker.this.elementHeight + 4,
                            3,
                            colorPickerBg
                    );

                    UtilScreen.drawRoundedRect(
                            this.xPosition + ColorPicker.this.yPadding + ColorPicker.this.elementWidth * 4
                                    + ColorPicker.this.elementWidth + ColorPicker.this.buttonSidePadding * 2 - 3,
                            this.yPosition + ColorPicker.this.yPadding - 2,
                            ColorPicker.this.elementWidth + 6,
                            ColorPicker.this.elementHeight + 4,
                            3,
                            colorPickerBg
                    );

                    GlStateManager.color(1f, 1f, 1f, 1f);
                    GlStateManager.popAttrib();
                    GlStateManager.popMatrix();

                    GlStateManager.pushMatrix();
                    GlStateManager.disableLighting();
                    GlStateManager.disableFog();
                    GlStateManager.enableBlend();

                    GlStateManager.disableTexture2D();
                    GlStateManager.shadeModel(GL11.GL_SMOOTH);

                    // Color Pickers
                    ColorPicker.this.drawVerticalColorBox(
                            this.xPosition + ColorPicker.this.yPadding,
                            this.yPosition + ColorPicker.this.yPadding,
                            ColorPicker.this.elementWidth * 4,
                            ColorPicker.this.elementHeight
                    );

                    ColorPicker.this.drawVerticalHueBox(
                            this.xPosition + ColorPicker.this.yPadding + ColorPicker.this.elementWidth * 4
                                    + ColorPicker.this.buttonSidePadding,
                            this.yPosition + ColorPicker.this.yPadding,
                            ColorPicker.this.elementWidth,
                            ColorPicker.this.elementHeight
                    );

                    ColorPicker.this.drawVerticalAlphaBox(
                            this.xPosition + ColorPicker.this.yPadding + ColorPicker.this.elementWidth * 4
                                    + ColorPicker.this.elementWidth + ColorPicker.this.buttonSidePadding * 2,
                            this.yPosition + ColorPicker.this.yPadding,
                            ColorPicker.this.elementWidth,
                            ColorPicker.this.elementHeight
                    );

                    GlStateManager.enableBlend();
                    GlStateManager.enableTexture2D();
                    GlStateManager.color(1F, 1F, 1F, 1F);
                    GlStateManager.popMatrix();

                    this.drawGradientRect(
                            this.xPosition + ColorPicker.this.yPadding + ColorPicker.this.elementWidth * 4
                                    + ColorPicker.this.elementWidth + ColorPicker.this.buttonSidePadding * 2,
                            this.yPosition + ColorPicker.this.yPadding,
                            (this.xPosition + ColorPicker.this.yPadding + ColorPicker.this.elementWidth * 4
                                    + ColorPicker.this.elementWidth + ColorPicker.this.buttonSidePadding * 2)
                                    + ColorPicker.this.elementWidth,
                            this.yPosition + ColorPicker.this.yPadding + ColorPicker.this.elementHeight,
                            new Color(ColorPicker.this.getColor().getRed(), ColorPicker.this.getColor().getGreen(),
                                      ColorPicker.this.getColor().getBlue(), 255
                            ).getRGB(),
                            new Color(ColorPicker.this.getColor().getRed(), ColorPicker.this.getColor().getGreen(),
                                      ColorPicker.this.getColor().getBlue(), 0
                            ).getRGB()
                    );

                    GlStateManager.pushMatrix();

                    // Drawing button indicators
                    UtilScreen.drawColorPickerButton(
                            this.xPosition + ColorPicker.this.yPadding + ColorPicker.this.colorX - 1,
                            this.yPosition + ColorPicker.this.yPadding + ColorPicker.this.colorY - 1,
                            3
                    );

                    UtilScreen.drawColorPickerSlider(
                            this.xPosition + ColorPicker.this.yPadding + ColorPicker.this.elementWidth * 4
                                    + ColorPicker.this.buttonSidePadding,
                            this.yPosition + ColorPicker.this.yPadding + ColorPicker.this.hueY - 1,
                            ColorPicker.this.elementWidth,
                            3
                    );

                    UtilScreen.drawColorPickerSlider(
                            this.xPosition + ColorPicker.this.yPadding + ColorPicker.this.elementWidth * 4
                                    + ColorPicker.this.elementWidth + ColorPicker.this.buttonSidePadding * 2,
                            this.yPosition + ColorPicker.this.yPadding + ColorPicker.this.alphaY - 1,
                            ColorPicker.this.elementWidth,
                            3
                    );

                    GlStateManager.color(1f, 1f, 1f, 1f);
                    GlStateManager.popMatrix();

                }

                @Override
                public void mouseDrag(final int mouseX, final int mouseY) {
                    ColorPicker.this.mouseX = mouseX;
                    ColorPicker.this.mouseY = mouseY;
                }

                @Override
                public void onScreenLeftClick(final int mouseX, final int mouseY) {
                    // Close if the player clicked outside the color picker
                    if (!this.isHovering(mouseX, mouseY)) {
                        ColorPicker.this.isChoosingColor = false;
                        boundingBox.updateButtons();
                    }

                    ColorPicker.this.mouseX = mouseX;
                    ColorPicker.this.mouseY = mouseY;
                }

                @Override
                public boolean shouldOverlapOtherButtons() {
                    return true;
                }
            });

        }

        return buttons;
    }

    public Color getColor() {
        return this.getSetting().getValue();
    }

    public Color getBaseColor() {
        return Color.getHSBColor(this.hue, 1F, 1F);
    }

    public String getHexadecimal() {
        return String.format("#%08X", this.getColor().getRGB());
    }

    private void drawVerticalColorBox(final int x, final int y, final int width, final int height) {
        final Color goal = this.getBaseColor();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer renderer = tessellator.getWorldRenderer();

        final int relativeX = this.mouseX - x;
        final int relativeY = this.mouseY - y;

        for (int i = 0; i <= width; i++) {
            final int col = i * 255 / width;
            final Color progressive =
                    new Color(Math.min(255, goal.getRed() + col), Math.min(255, goal.getGreen() + col),
                              Math.min(255, goal.getBlue() + col), 255
                    );

            final boolean isMouseIn =
                    relativeX > 0 && relativeY > 0 && relativeX < width && relativeY < height && i == relativeY;
            if (isMouseIn || (this.colorX == i)) {

                if (isMouseIn) {
                    this.colorX = relativeX;
                    this.colorY = relativeY;
                }

                final float factor = ((height - (this.colorY)) * 1F / height);

                final float red =
                        Math.min(1F, Math.max(0, (goal.getRed() / 255F) + ((this.colorX) * 1F / width))) * factor;
                final float green =
                        Math.min(1F, Math.max(0, (goal.getGreen() / 255F) + (this.colorX) * 1F / width)) * factor;
                final float blue =
                        Math.min(1F, Math.max(0, (goal.getBlue() / 255F) + (this.colorX) * 1F / width)) * factor;

                final Color toSet = new Color(red, green, blue, this.alpha);
                this.getSetting().setValue(toSet);
            }

            final int xTranslated = x + i;
            renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            renderer.pos(xTranslated, y, 0D)
                    .color(progressive.getRed(), progressive.getGreen(), progressive.getBlue(), 255).endVertex();
            renderer.pos(xTranslated, y + height, 0D).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
        }
    }

    private boolean comparedDecimalThreshold(final double decimal1, final double decimal2, final double threshold) {
        return (decimal2 - decimal1) < threshold && (decimal2 - decimal1) > -threshold;
    }

    private void drawVerticalHueBox(final int x, final int y, final int width, final int height) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer renderer = tessellator.getWorldRenderer();

        final int relativeX = this.mouseX - x;
        final int relativeY = this.mouseY - y;

        for (int i = 0; i <= height; i++) {
            final float col = 1F - ((float) i / height);
            final Color bridge = Color.getHSBColor(col, 1F, 1F);

            if (relativeX > 0 && relativeY > 0 && relativeX < width && relativeY < height && i == relativeY) {
                this.hue = col;
                this.hueY = i;
            }

            GlStateManager.color(bridge.getRed() / 255F, bridge.getBlue() / 255F, bridge.getGreen() / 255F, 255F);
            renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            renderer.pos(x + width, y + height - i, 0D).endVertex();
            renderer.pos(x, y + height - i, 0D).endVertex();
            tessellator.draw();
        }
    }

    private void drawVerticalAlphaBox(final int x, final int y, final int width, final int height) {

        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.pushAttrib();

        this.mc.getTextureManager().bindTexture(transparentTexture);
        Gui.drawModalRectWithCustomSizedTexture(
                x,
                y,
                0,
                0,
                width,
                height,
                16,
                16
        );

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();


        final int relativeX = this.mouseX - x;
        final int relativeY = this.mouseY - y;

        for (int i = 0; i <= height; i++) {
            final float col = 1F - ((float) i / height);

            if (relativeX > 0 && relativeY > 0 && relativeX < width && relativeY < height && i == relativeY) {
                this.alpha = col;

                final Color current = this.getColor();
                final Color toSet =
                        new Color(current.getRed() / 255F, current.getGreen() / 255F, current.getBlue() / 255F,
                                  this.alpha
                        );
                this.getSetting().setValue(toSet);

                this.alphaY = relativeY;
            }

        }

    }

}