package club.mineplex.clans.client.gui.repository;

import club.mineplex.clans.client.gui.GuiModScreen;
import club.mineplex.clans.client.hud.HudBoundingBox;
import club.mineplex.clans.client.hud.HudManager;
import club.mineplex.clans.client.hud.HudPosition;
import club.mineplex.clans.client.hud.IHudRenderer;
import club.mineplex.clans.util.object.Pair;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;

public class GuiHudManager extends GuiModScreen {

    private static final int ARROWS_SHIFT_BY = 1;
    private static final int ARROWS_SHIFT_BY_MULTIPLIED = 10;

    private final Collection<IHudRenderer> renderers;

    private IHudRenderer selectedRenderer = null;
    private int prevMouseX = -1;
    private int prevMouseY = -1;
    private boolean isResizing = false;

    public GuiHudManager() {
        this.renderers = HudManager.getInstance().getRenderers();
    }

    private void drawRenderers() {
        final int boxColor = new Color(1F, 1F, 1F).getRGB();
        final int selectedBoxColor = new Color(230, 198, 96).getRGB();

        for (final IHudRenderer renderer : this.renderers) {
            if (!renderer.shouldRender()) {
                continue;
            }
            final boolean isSelected = this.selectedRenderer == renderer;
            final int color = isSelected ? selectedBoxColor : boxColor;

            final ScaledResolution resolution = new ScaledResolution(this.mc);
            final HudBoundingBox box = renderer.getBox();
            final HudPosition position = box.getPosition();
            final Pair<Double, Double> size = box.getSize();

            position.rescaleWindow(resolution.getScaledWidth(), resolution.getScaledHeight());
            renderer.render(position.getX(), position.getY(), box.getScaleFactor(), true);

            // Drawing outline box
            final int left = position.getX() - 1;
            final int bottom = (int) (position.getY() + size.getValue() + 1);
            final int right = (int) (position.getX() + size.getKey() + 1);
            final int top = position.getY() - 1;

            this.drawHorizontalLine(left, right, top, color);
            this.drawHorizontalLine(left, right, bottom, color);
            this.drawVerticalLine(left, top, bottom, color);
            this.drawVerticalLine(right, top, bottom, color);

            // Drawing resize square
            Gui.drawRect(right - 3, bottom - 3, right + 3, bottom + 3, color);
        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        // Mouse button wasnt left click, so no purpose on doing anything
        if (mouseButton != 0) {
            return;
        }

        this.prevMouseX = mouseX;
        this.prevMouseY = mouseY;

        // Looping through all renderers to see if the mouse click was within any box's bounds
        for (final IHudRenderer renderer : this.renderers) {
            final HudBoundingBox box = renderer.getBox();
            final HudPosition position = box.getPosition();
            final Pair<Double, Double> size = box.getSize();

            final int boxLeft = position.getX() - 1;
            final int boxTop = position.getY() - 1;
            final double boxRight = position.getX() + size.getKey() + 1;
            final double boxBottom = position.getY() + size.getValue() + 1;

            // Checking if the mouse isn't in the size bar
            if (this.isResizing(mouseX, mouseY, renderer)) {
                this.selectedRenderer = renderer;
                this.isResizing = true;
                return;
            }

            // Mouse is within the box's bounds, therefore we're selecting it
            if (mouseX >= boxLeft && mouseX <= boxRight && mouseY >= boxTop && mouseY <= boxBottom) {
                this.selectedRenderer = renderer;
                return;
            }

        }

        // Since the user has clicked somewhere where there isn't a renderer, we unselect the current selected, if any
        this.selectedRenderer = null;
    }

    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton,
                                  final long timeSinceLastClick) {

        // We're returning  if the selected renderer is null, otherwise there's no point in listening for drag
        if (this.selectedRenderer == null) {
            return;
        }

        // Returning if it wasn't a left click
        if (clickedMouseButton != 0) {
            return;
        }

        // Handle resize
        if (this.isResizing) {
            final HudBoundingBox box = this.selectedRenderer.getBox();
            final Pair<Integer, Integer> defaultSize = box.getDefaultSize();
            final HudPosition position = box.getPosition();
            final int defaultRight = position.getX() + defaultSize.getKey();
            final int defaultLeft = position.getY() + defaultSize.getValue();

            box.setScaleFactor(((double) mouseX / defaultRight + (double) mouseY / defaultLeft) / 2D);
            return;
        }

        this.shiftRendererBy(mouseX - this.prevMouseX, mouseY - this.prevMouseY);

        this.prevMouseX = mouseX;
        this.prevMouseY = mouseY;
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            this.isResizing = false;
        }
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        // Returning if the selected renderer is null, otherwise there isn't a point in listening for keys
        if (this.selectedRenderer == null) {
            return;
        }

        final int shiftBy = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)
                ? ARROWS_SHIFT_BY_MULTIPLIED
                : ARROWS_SHIFT_BY;

        // TODO: Make it so if you hold down arrow keys it continuously shifts
        switch (keyCode) {
            case Keyboard.KEY_DOWN:
                this.shiftRendererBy(0, shiftBy);
                break;
            case Keyboard.KEY_UP:
                this.shiftRendererBy(0, -shiftBy);
                break;
            case Keyboard.KEY_RIGHT:
                this.shiftRendererBy(shiftBy, 0);
                break;
            case Keyboard.KEY_LEFT:
                this.shiftRendererBy(-shiftBy, 0);
                break;
        }

    }

    private void shiftRendererBy(final int xShift, final int yShift) {
        if (this.selectedRenderer == null) {
            return;
        }

        final HudBoundingBox box = this.selectedRenderer.getBox();
        final HudPosition position = box.getPosition();

        final int x = (int) Math.max(0, Math.min(this.width - box.getSize().getKey(), position.getX() + xShift));
        final int y = (int) Math.max(0, Math.min(this.height - box.getSize().getValue(), position.getY() + yShift));

        position.setPosition(x, y);
    }

    private boolean isResizing(final int mouseX, final int mouseY, final IHudRenderer renderer) {
        if (renderer == null) {
            return false;
        }

        final HudBoundingBox box = renderer.getBox();
        final HudPosition position = box.getPosition();
        final Pair<Double, Double> size = box.getSize();

        final int boxRight = (int) (position.getX() + size.getKey() + 1);
        final int boxBottom = (int) (position.getY() + size.getValue() + 1);

        return mouseX >= boxRight - 3 && mouseX <= boxRight + 3 && mouseY >= boxBottom - 3 && mouseY <= boxBottom + 3;
    }

    @Override
    public void onGuiClosed() {
        HudManager.getInstance().saveRenderers();
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();

        // Rendering text
        final int fontColor = new Color(191, 191, 191).getRGB();
        this.drawCenteredString(this.fontRendererObj,
                                "Click and hold to drag",
                                this.width / 2,
                                this.height - 90 - this.fontRendererObj.FONT_HEIGHT,
                                fontColor
        );
        this.drawCenteredString(this.fontRendererObj,
                                "Click and release to select",
                                this.width / 2,
                                this.height - 75 - this.fontRendererObj.FONT_HEIGHT,
                                fontColor
        );
        this.drawCenteredString(this.fontRendererObj,
                                "Use arrow keys to move selected",
                                this.width / 2,
                                this.height - 60 - this.fontRendererObj.FONT_HEIGHT,
                                fontColor
        );
        this.drawCenteredString(this.fontRendererObj,
                                "and hold SHIFT to move faster",
                                this.width / 2,
                                this.height - 50 - this.fontRendererObj.FONT_HEIGHT,
                                fontColor
        );

        // Rendering boxes
        this.drawRenderers();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
