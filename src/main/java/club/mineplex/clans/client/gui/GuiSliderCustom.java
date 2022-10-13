package club.mineplex.clans.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiSliderCustom extends GuiButton {

    private final float min;
    private final float max;
    private final float increment;
    public boolean dragging;
    private float sliderValue;

    public GuiSliderCustom(final int buttonId, final int x, final int y, final int width, final int height) {
        this(buttonId, x, y, width, height, 0.0F, 1.0F, 1.0F);
    }

    public GuiSliderCustom(final int buttonId, final int x, final int y, final int width, final int height, final float min,
                           final float max,
                           final float increment) {
        super(buttonId, x, y, width, height, "");

        this.increment = increment;
        this.min = min;
        this.max = max;
        this.init();
    }

    private void init() {
        this.sliderValue = this.normalizeValue(this.getValue());
    }

    protected abstract float getValue();

    protected abstract void setValue(float value);

    protected abstract String getDisplayString();

    @Override
    protected int getHoverState(final boolean mouseOver) {
        return 0;
    }

    @Override
    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible && this.enabled) {
            if (this.dragging) {
                this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
                this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
                final float f = this.denormalizeValue(this.sliderValue);
                this.setValue(f);
                this.sliderValue = this.normalizeValue(f);
                this.displayString = this.getDisplayString();
            }

            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)),
                                       this.yPosition, 0, 66, 4, 20
            );
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4,
                                       this.yPosition, 196, 66, 4, 20
            );
        }
    }

    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY) && this.enabled) {
            this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
            this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
            this.setValue(this.denormalizeValue(this.sliderValue));
            this.displayString = this.getDisplayString();
            this.dragging = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        this.dragging = false;
    }

    private float normalizeValue(final float value) {
        return MathHelper.clamp_float((this.snapToStepClamp(value) - this.min) / (this.max - this.min),
                                      0.0F, 1.0F
        );
    }

    private float denormalizeValue(final float value) {
        return this.snapToStepClamp(
                this.min + (this.max - this.min) * MathHelper.clamp_float(value, 0.0F, 1.0F));
    }

    private float snapToStepClamp(float value) {
        if (this.increment > 0.0F) {
            value = this.increment * (float) Math.round(value / this.increment);
        }

        return MathHelper.clamp_float(value, this.min, this.max);
    }
}