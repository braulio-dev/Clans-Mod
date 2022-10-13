package club.mineplex.clans.client.gui.repository.modmenu;

import club.mineplex.clans.client.CustomFontRenderer;
import club.mineplex.clans.client.gui.GuiButtonAbstract;
import club.mineplex.clans.client.gui.button.BoxButton;
import club.mineplex.clans.client.hud.BoxCoords;
import club.mineplex.clans.client.settings.SettingCategory;
import club.mineplex.clans.util.UtilScreen;
import lombok.NonNull;
import lombok.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.*;

public class ModBoundingBox {

    private static final int TEXT_PADDING = 13;
    private static final int TEXT_MARGIN = 10;
    private static final int ICON_SIDE_LENGTH = 16;

    private final Random random = new Random();

    private final CustomFontRenderer nameRenderer;
    private final Minecraft mc;
    private final SettingCategory category;

    private final GuiModMenu owner;
    private final ArrayList<GuiButton> buttons = new ArrayList<>();
    private int boxHeight;
    private boolean updateButtons;
    private boolean expanded;
    private boolean wasExpanded = !this.expanded;

    private BoxCoords previousBox = new BoxCoords(0, 0, 0, 0);

    private int previousYTop = 0;
    private float extraModuleBoxHeight = 0F;

    protected ModBoundingBox(final GuiModMenu owner, final SettingCategory category,
                             final CustomFontRenderer nameRenderer) {
        this.category = category;
        this.mc = Minecraft.getMinecraft();
        this.owner = owner;
        this.nameRenderer = nameRenderer;
    }

    public int getBoxHeight() {
        return this.boxHeight;
    }

    protected void draw(final int top, final int left, final int right, final int mouseY, final int mouseX) {
        final String name = this.category.getName().toUpperCase();

        final int stringHeight = (int) this.nameRenderer.getHeight(name);
        final int headerHeight = stringHeight + TEXT_MARGIN * 2;
        float expandedBoxHeight = 0;

        final List<BoundingBoxElement> entries = new ArrayList<>();
        this.category.getSettings().forEach(setting -> {
            if (setting.getGuiEntry() != null) {
                entries.add(setting.getGuiEntry());
            }
        });

        final Collection<SettingCategory> children = getAllChildren();
        children.forEach(child -> {
            entries.add(new CategoryHeader(child));
            child.getSettings().forEach(setting -> {
                if (setting.getGuiEntry() != null) {
                    entries.add(setting.getGuiEntry());
                }
            });
        });

        if (this.isExpanded()) {
            for (final BoundingBoxElement element : entries) {
                expandedBoxHeight += 8 + TEXT_MARGIN;
//                expandedBoxHeight += element.getHeight(GuiModMenu.TEXT_RENDERER) + TEXT_MARGIN;
            }
        }

        final ModMenuList list = this.owner.getModuleList();
        int toCut = (int) Math.max(0, (top + extraModuleBoxHeight) - list.getBoxBottom());
        this.extraModuleBoxHeight = this.isExpanded()
                ? Math.min(expandedBoxHeight + 8, this.extraModuleBoxHeight + 2F)
                : Math.max(0F, this.extraModuleBoxHeight - 2F - toCut);

        // Drawing bounding box
        GlStateManager.pushMatrix();
        this.mc.getTextureManager().bindTexture(GuiModMenu.MODULE_BOX);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.01F);
        GlStateManager.blendFunc(770, 771);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        previousBox = new BoxCoords(
                Math.max(Math.min(list.getBoxRight(), left), list.getBoxLeft()),
                Math.max(Math.min(top, list.getBoxBottom()), list.getBoxTop()),
                Math.min(list.getBoxRight() - list.getBoxLeft(), right - left),
                Math.min(Math.min(list.getBoxBottom() - top, list.getBoxBottom() - list.getBoxTop()), (int) (headerHeight + this.extraModuleBoxHeight))
        );

        UtilScreen.glScissor(
                getBox().getX(),
                getBox().getY(),
                getBox().getWidth(),
                getBox().getHeight()
        );

        Gui.drawModalRectWithCustomSizedTexture(
                left,
                top,
                0,
                0,
                right - left,
                (int) (headerHeight + this.extraModuleBoxHeight),
                (float) right - left,
                headerHeight + this.extraModuleBoxHeight
        );

        // TODO: Display module icon rather than default when icons are made
        // Drawing module icon
        final int iconPadding = (headerHeight - ICON_SIDE_LENGTH) / 2;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);;
        category.bindIconTexture();
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(
                left + iconPadding,
                top + iconPadding,
                0,
                0,
                ICON_SIDE_LENGTH,
                ICON_SIDE_LENGTH,
                16,
                16
        );
        GlStateManager.disableBlend();

        // Drawing module name
        this.nameRenderer.drawString(
                name,
                (int) (left + iconPadding * 2.5 + ICON_SIDE_LENGTH),
                (top + iconPadding) + (ICON_SIDE_LENGTH / 2F) - (this.nameRenderer.getHeight(name) / 2),
                new Color(255, 255, 255, 255).getRGB()
        );

        int minimumBoxHeight = (int) Math.min(expandedBoxHeight, (top + extraModuleBoxHeight) - list.getBoxBottom());
        final boolean isExpanding =
                (this.expanded != this.wasExpanded && this.extraModuleBoxHeight >= minimumBoxHeight);
        final boolean createButtons =
                this.buttons.isEmpty() || isExpanding || this.previousYTop != top || this.updateButtons;

        if (createButtons) {
            this.updateButtons = false;
            this.removeAllButtons();
        }

        // Drawing module settings
        drawModuleSettings(
                top,
                left,
                right,
                headerHeight,
                entries,
                iconPadding,
                createButtons
        );

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();

        // Creating buttons if they haven't been initialized yet
        if (createButtons) {
            final ArrayList<GuiButtonAbstract> createdButtons = this.createButtons(
                    left,
                    top,
                    right,
                    top + headerHeight,
                    iconPadding
            );

            this.buttons.addAll(createdButtons);
            this.owner.getButtons().addAll(this.buttons);
        }

        if (this.expanded != this.wasExpanded && this.extraModuleBoxHeight >= expandedBoxHeight) {
            this.wasExpanded = this.expanded;
        }

        this.getButtons().forEach(button -> button.drawButton(this.mc, mouseX, mouseY));

        this.previousYTop = top;
        this.boxHeight = (int) (headerHeight + this.extraModuleBoxHeight);
    }

    private void drawModuleSettings(int top,
                                    int left,
                                    float right,
                                    int headerHeight,
                                    List<BoundingBoxElement> entries,
                                    int iconPadding,
                                    boolean createButtons) {
        if (this.isExpanded()) {
            int expandedHeightIndex = 0;
            final Color settingFontColor = new Color(150, 150, 150, 255);
            final Color headerFontColor = new Color(255, 255, 255, 255);

            for (final BoundingBoxElement element : entries) {

                final int topOffset = (int) (element.getHeight(GuiModMenu.TEXT_RENDERER) + TEXT_MARGIN);

                final float settingTop = top + TEXT_MARGIN + headerHeight + (float) expandedHeightIndex;
                final float settingLeft = left + iconPadding * 2.5F + ICON_SIDE_LENGTH;
                final float settingBottom = settingTop - TEXT_MARGIN + topOffset;

                GlStateManager.color(1F, 1F, 1F, 1F);
                element.draw(
                        GuiModMenu.TEXT_RENDERER,
                        element instanceof CategoryHeader ? headerFontColor : settingFontColor,
                        settingTop,
                        settingLeft,
                        settingBottom,
                        right - 10
                );

                if (createButtons) {
                    this.buttons.addAll(element.createButtons(
                            this,
                            settingTop,
                            settingLeft,
                            settingBottom,
                            right - 10
                    ));
                }

                expandedHeightIndex += topOffset;
            }

        } else {
            // Removing mod settings that are opened after unexpanding module
            for (final BoundingBoxElement element : entries) {
                element.resetDrawing();
            }
        }
    }

    public void removeAllButtons() {
        this.owner.getButtons().removeAll(this.buttons);
        this.buttons.clear();
    }

    private ArrayList<GuiButtonAbstract> createButtons(final int left, final int top, final int right,
                                                       final int bottom, final int sidePadding) {
        final ArrayList<GuiButtonAbstract> buttonList = new ArrayList<>();

        // Drawing expand overlay button
        final BoxButton expandButton = new ModBoundingBoxButton(
                this,
                random.nextInt(10_000_000),
                left,
                top,
                right - left,
                bottom - top
        );

        buttonList.add(expandButton);
        return buttonList;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public void setExpanded(final boolean expanded) {
        this.wasExpanded = this.expanded;
        this.expanded = expanded;

        if (expanded) {
            for (final ModBoundingBox otherBox : this.owner.getModuleList().getModBoundingBoxes()) {
                if (otherBox == this) {
                    continue;
                }

                otherBox.setExpanded(false);
            }
        }
    }

    public void updateButtons() {
        this.updateButtons = true;
    }

    protected ArrayList<GuiButton> getButtons() {
        return this.buttons;
    }

    public GuiModMenu getOwner() {
        return this.owner;
    }

    public SettingCategory getCategory() {
        return this.category;
    }

    public BoxCoords getBox() {
        return previousBox;
    }

    private Collection<SettingCategory> getAllChildren() {
        List<SettingCategory> categories = new ArrayList<>(Arrays.asList(category.getChildren()));
        List<SettingCategory> toLoop = categories;
        boolean nestedChildren;
        do {
            nestedChildren = false;
            List<SettingCategory> toSet = new ArrayList<>();
            for (SettingCategory settingCategory : toLoop) {
                final SettingCategory[] children = settingCategory.getChildren();
                if (children.length > 0) {
                    nestedChildren = true;
                    toSet.addAll(Arrays.asList(children));
                }
            }

            toLoop = toSet;
            categories.addAll(toLoop);
        } while(nestedChildren);
        return categories;
    }

    public interface BoundingBoxElement {

        void draw(final CustomFontRenderer fontRenderer, Color fontColor, final float top, final float left,
                  final float bottom, final float right);

        @NonNull
        List<GuiButton> createButtons(final ModBoundingBox boundingBox, final float top, final float left,
                                      final float bottom, final float right);

        float getHeight(CustomFontRenderer renderer);

        default void resetDrawing() {
            // Ignored
        }

    }

    @Value
    public static class CategoryHeader implements BoundingBoxElement {

        SettingCategory category;

        @Override
        public void draw(CustomFontRenderer fontRenderer, Color fontColor, float top, float left,
                         float bottom, float right) {
            final String text = category.getName();
            final int textWidth = (int) fontRenderer.getWidth(text);

            float topLine = top + (bottom - top) / 2 - 1;
            float bottomLine = top + 2;

            Gui.drawRect(
                    (int) left,
                    (int) topLine,
                    (int) (left + (right - left) / 2 - textWidth / 2F) - 10,
                    (int) bottomLine,
                    fontColor.getRGB()
            );

            Gui.drawRect(
                    (int) (left + (right - left) / 2 + textWidth / 2F) + 10,
                    (int) topLine,
                    (int) right,
                    (int) bottomLine ,
                    fontColor.getRGB()
            );

            fontRenderer.drawCenteredString(text, (int) (left + (right - left) / 2), (int) top, fontColor.getRGB());
        }

        @Override
        public @NonNull List<GuiButton> createButtons(ModBoundingBox boundingBox, float top,
                                                      float left, float bottom, float right) {
            return Collections.emptyList();
        }

        @Override
        public float getHeight(CustomFontRenderer renderer) {
            return GuiModMenu.TEXT_RENDERER.getHeight(category.getName());
        }

    }


}
