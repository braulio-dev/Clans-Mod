package club.mineplex.clans.client.gui.repository.modmenu;

import club.mineplex.clans.util.UtilScreen;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.util.List;

public class ModMenuList {

    private static final ResourceLocation scrollLoc =
            new ResourceLocation("clansmod", "textures/mod_menu/scrollbar/scroll.png");
    private static final ResourceLocation scrollBarLoc =
            new ResourceLocation("clansmod", "textures/mod_menu/scrollbar/scroll_bar.png");

    /* Gui */
    private final List<ModBoundingBox> modBoundingBoxes = Lists.newArrayList();
    private final GuiModMenu owner;

    /* Box Measurements */
    private final int boxLeft;
    private final int boxRight;
    private final int boxTop;
    private final int boxBottom;
    private final int scrollBarLeft;
    private final int scrollBarRight;
    private final int scrollBarTop;
    private final int scrollBarBottom;

    /* Scroll bar */
    private final int scrollBarHeight;
    private float scrollMultiplier = 0;
    private int initialClickY = -2;
    private int amountScrolled = 0;
    private int scrollProgress = 0;

    /* Box and screen vars */
    private int mouseX;
    private int mouseY;
    private int contentHeight;

    public ModMenuList(final GuiModMenu owner, final int top, final int left, final int bottom, final int right) {
        this.owner = owner;

        this.boxLeft = left;
        this.boxTop = top;
        this.boxRight = right;
        this.boxBottom = bottom;

        this.scrollBarHeight = 17;
        this.scrollBarTop = top;
        this.scrollBarLeft = right - 5;
        this.scrollBarRight = right;
        this.scrollBarBottom = bottom - this.scrollBarHeight;
    }

    protected void bindAmountScrolled() {
        this.amountScrolled =
                (int) MathHelper.clamp_float(this.amountScrolled, 0.0F, this.getMaxScrollAmount());
    }

    public int getScrollBarWidth() {
        return this.scrollBarRight - this.scrollBarLeft;
    }

    public int getAmountScrolled() {
        return (this.contentHeight * this.amountScrolled / (this.boxBottom - this.boxTop));
    }

    private int getMaxScrollAmount() {
        final int boxHeight = boxBottom - boxTop;
        return Math.max(0, (contentHeight - boxHeight) * boxHeight / Math.max(1, Math.max(this.contentHeight, boxHeight)));
    }

    public int getContentHeight() {
        return this.contentHeight;
    }

    public void setContentHeight(final int contentHeight) {
        this.contentHeight = contentHeight;
    }

    public ModBoundingBox getListEntry(final int index) {
        return this.modBoundingBoxes.get(index);
    }

    protected int getListSize() {
        return this.modBoundingBoxes.size();
    }

    public void setBoundingBoxes(final List<ModBoundingBox> boundingBoxes) {
        this.modBoundingBoxes.clear();
        this.modBoundingBoxes.addAll(boundingBoxes);
    }

    public List<ModBoundingBox> getModBoundingBoxes() {
        return this.modBoundingBoxes;
    }

    public boolean mouseClicked() {
        this.bindAmountScrolled();
        return false;
    }

    public void drawScrollbar(final int mouseX, final int mouseY) {
        this.mouseY = mouseY;
        this.mouseX = mouseX;

        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(scrollBarLoc);

        Gui.drawScaledCustomSizeModalRect(
                this.scrollBarLeft + (this.scrollBarRight - this.scrollBarLeft) / 2
                        - ((this.scrollBarRight - this.scrollBarLeft) / 2) / 2,
                this.scrollBarTop,
                0,
                0,
                5,
                591,
                (this.scrollBarRight - this.scrollBarLeft) / 2,
                this.scrollBarBottom + this.scrollBarHeight - this.scrollBarTop,
                5,
                591
        );

        final int maxScrollAmount = this.getMaxScrollAmount();
        if (maxScrollAmount != 0) {
            if (scrollProgress != amountScrolled) {
                scrollProgress += amountScrolled - scrollProgress > 0 ? 1 : -1;
            }

            Minecraft.getMinecraft().getTextureManager().bindTexture(scrollLoc);
            UtilScreen.glScissor(
                    this.scrollBarLeft,
                    this.scrollBarTop,
                    this.scrollBarRight - this.scrollBarLeft,
                    this.scrollBarBottom - this.scrollBarTop
            );

            Gui.drawScaledCustomSizeModalRect(
                    this.scrollBarLeft + (this.scrollBarRight - this.scrollBarLeft) / 2
                            - (this.scrollBarRight - this.scrollBarLeft) / 2,
                    this.scrollBarTop + (int) ((this.scrollBarBottom - this.scrollBarTop) * (float) scrollProgress
                            / maxScrollAmount),
                    0,
                    0,
                    11,
                    52,
                    (this.scrollBarRight - this.scrollBarLeft),
                    this.scrollBarHeight,
                    11,
                    52
            );
        }

        GlStateManager.popMatrix();
    }

    private boolean isMouseYWithinScrollBounds(final int mouseY) {
        return mouseY >= this.scrollBarTop && mouseY <= this.scrollBarBottom;
    }

    private boolean isMouseXWithinScrollBounds(final int mouseX) {
        return mouseX >= this.scrollBarLeft && mouseX <= this.scrollBarRight;
    }

    public boolean isMouseWithinBox() {
        return this.mouseX >= this.getBoxLeft() && this.mouseY >= this.getBoxTop() && this.mouseX <= this.getBoxRight()
                && this.mouseY <= this.getBoxBottom();
    }

    public void handleMouseInput() {
        if (Mouse.isButtonDown(0)) {
            if (this.initialClickY == -1 && this.isMouseXWithinScrollBounds(this.mouseX)
                    && this.isMouseYWithinScrollBounds(this.mouseY)) {

                this.scrollMultiplier = -1.0F;
                final int maxScrollAmount = this.boxBottom - this.boxTop;
                final int maxHeightDifference = Math.max(1, this.getContentHeight() - maxScrollAmount);

                int scrollFactor = (int) (Math.pow(maxScrollAmount, 2) / this.getContentHeight());
                scrollFactor = MathHelper.clamp_int(scrollFactor, 32, maxScrollAmount);
                this.scrollMultiplier /= (float) (maxScrollAmount - scrollFactor) / (maxHeightDifference);

            } else if (this.initialClickY >= 0) {
                this.amountScrolled -= (this.mouseY - this.initialClickY) * this.scrollMultiplier / 2.5;
            }

            this.initialClickY = this.mouseY;
        } else {
            this.initialClickY = -1;
        }

        int mouseWheel = Mouse.getEventDWheel();

        if (mouseWheel != 0 && this.isMouseWithinBox()) {
            mouseWheel = mouseWheel > 0 ? -1 : 1;

            this.amountScrolled += (float) ((double) mouseWheel * this.scrollBarHeight / 2);
        }

        this.bindAmountScrolled();
    }

    public int getBoxLeft() {
        return this.boxLeft;
    }

    public int getBoxRight() {
        return this.boxRight;
    }

    public int getBoxTop() {
        return this.boxTop;
    }

    public int getBoxBottom() {
        return this.boxBottom;
    }

    public int getScrollBarTop() {
        return this.scrollBarTop;
    }

    public int getScrollBarBottom() {
        return this.scrollBarBottom;
    }
}
