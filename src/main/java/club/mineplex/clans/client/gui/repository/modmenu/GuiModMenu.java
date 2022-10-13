package club.mineplex.clans.client.gui.repository.modmenu;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.client.CustomFontRenderer;
import club.mineplex.clans.client.gui.GuiModScreen;
import club.mineplex.clans.client.settings.SettingCategory;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GuiModMenu extends GuiModScreen {

    protected static final ResourceLocation VIGNETTE =
            new ResourceLocation("clansmod", "textures/mod_menu/background/vignette.png");
    protected static final ResourceLocation TITLE_BAR =
            new ResourceLocation("clansmod", "textures/mod_menu/title/title_bar.png");
    protected static final ResourceLocation MODULE_BOX =
            new ResourceLocation("clansmod", "textures/mod_menu/modules/module_box.png");

    protected static final CustomFontRenderer TITLE_RENDERER =
            new CustomFontRenderer(CustomFontRenderer.Font.OPEN_SANS_BOLD, 20);
    protected static final CustomFontRenderer SUBTITLE_RENDERER =
            new CustomFontRenderer(CustomFontRenderer.Font.OPEN_SANS_BOLD, 17);
    protected static final CustomFontRenderer TEXT_RENDERER =
            new CustomFontRenderer(CustomFontRenderer.Font.OPEN_SANS_BOLD, 15);


    private static final int MODULE_BOX_PADDING = 3;

    private final CustomFontRenderer titleRenderer;
    private final CustomFontRenderer modNameRenderer;

    private ModMenuList moduleList;

    private int scrolledProgress = 0;

    public GuiModMenu(final GuiScreen previous) {
        super(previous);
        titleRenderer = TITLE_RENDERER;
        modNameRenderer = SUBTITLE_RENDERER;
    }

    public GuiModMenu() {
        this(null);
    }

    @Override
    public void initGui() {
        this.moduleList = new ModMenuList(
                this,
                this.height / 4 + MODULE_BOX_PADDING,
                this.width / 4,
                this.height / 4 * 3,
                (this.width / 4) + this.width / 2
        );

        final List<ModBoundingBox> boxes = Arrays.stream(SettingCategory.values())
                                                 .filter(SettingCategory::isRoot)
                                                 .map(category -> new ModBoundingBox(this, category, modNameRenderer))
                                                 .collect(Collectors.toCollection(LinkedList::new));

        this.moduleList.setBoundingBoxes(boxes);
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        ClansMod.getInstance().getConfig().save();
        ClansMod.getInstance().getRootConfiguration().save();
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();

        this.renderVignette();
        final int moduleListHeight = this.renderModuleBoxes(mouseX, mouseY);
        this.moduleList.setContentHeight(moduleListHeight);
        this.renderScrollBar(mouseX, mouseY);
        this.renderTitle();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void renderScrollBar(final int mouseX, final int mouseY) {
        this.moduleList.drawScrollbar(mouseX, mouseY);
    }

    private void renderVignette() {

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.01F);
        GlStateManager.blendFunc(770, 771);
        this.mc.getTextureManager().bindTexture(VIGNETTE);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

    }

    private int renderModuleBoxes(final int mouseX, final int mouseY) {

        final int xPosition = this.moduleList.getBoxLeft();
        final int yPosition = this.moduleList.getBoxTop();
        final int boxWidth = this.moduleList.getBoxRight() - this.moduleList.getBoxLeft() - 10;
        int yPadding = 0;

        if (scrolledProgress != moduleList.getAmountScrolled()) {
            scrolledProgress += moduleList.getAmountScrolled() - scrolledProgress > 0 ? 1 : -1;
        }

        // Drawing boxes
        for (final ModBoundingBox box : this.moduleList.getModBoundingBoxes()) {
            box.draw(yPosition + yPadding - scrolledProgress, xPosition, xPosition + boxWidth, mouseY, mouseX);
            yPadding += box.getBoxHeight() + MODULE_BOX_PADDING;
        }

        // Drawing top fade DOWN
        GlStateManager.pushMatrix();
        this.drawGradientRect(
                xPosition,
                this.moduleList.getBoxTop(),
                xPosition + boxWidth,
                this.moduleList.getBoxTop() + 4 + MODULE_BOX_PADDING * 2,
                new Color(0, 0, 0, 255).getRGB(),
                new Color(0, 0, 0, 0).getRGB()
        );

        if (this.moduleList.getContentHeight() >= this.moduleList.getBoxBottom() - this.moduleList.getBoxTop()) {
            // Drawing bottom fade DOWN
            this.drawGradientRect(
                    xPosition,
                    this.moduleList.getBoxBottom(),
                    xPosition + boxWidth,
                    this.moduleList.getBoxBottom() + 4,
                    new Color(0, 0, 0, 255).getRGB(),
                    new Color(0, 0, 0, 0).getRGB()
            );

            // Drawing bottom fade UP
            this.drawGradientRect(
                    xPosition,
                    this.moduleList.getBoxBottom() - 4 - MODULE_BOX_PADDING * 2,
                    xPosition + boxWidth,
                    this.moduleList.getBoxBottom(),
                    new Color(0, 0, 0, 0).getRGB(),
                    new Color(0, 0, 0, 255).getRGB()
            );
        }
        GlStateManager.popMatrix();

        return yPadding;
    }

    private void renderTitle() {
        final String title = ClansMod.getMod().name() + " Settings";
        final int x = this.width / 4;
        final int y = this.height / 4;

        this.mc.getTextureManager().bindTexture(TITLE_BAR);
        Gui.drawModalRectWithCustomSizedTexture(x, y - 3, 0, 0, this.width / 2, 3, (float) this.width / 4 * 3, 3);
        titleRenderer.drawString(title, x, y - titleRenderer.getHeight(title) - 3 - 3,
                                 new Color(255, 255, 255, 255).getRGB()
        );
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.moduleList.handleMouseInput();
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.moduleList.mouseClicked();
    }

    protected List<GuiButton> getButtons() {
        return this.buttonList;
    }

    public ModMenuList getModuleList() {
        return this.moduleList;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
