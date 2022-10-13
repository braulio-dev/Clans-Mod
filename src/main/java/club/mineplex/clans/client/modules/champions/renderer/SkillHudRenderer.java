package club.mineplex.clans.client.modules.champions.renderer;

import club.mineplex.clans.client.cooldown.Cooldown;
import club.mineplex.clans.client.hud.HudBoundingBox;
import club.mineplex.clans.client.modules.champions.ModuleChampions;
import club.mineplex.clans.client.modules.champions.SkillCooldown;
import club.mineplex.clans.client.settings.IterableState;
import club.mineplex.clans.client.settings.repo.ChampionsSettings;
import club.mineplex.core.mineplex.champions.ChampionsKit;
import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IActivatable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static club.mineplex.core.mineplex.champions.ChampionsSkill.Type;
import static club.mineplex.core.mineplex.champions.ChampionsSkill.Type.*;

public class SkillHudRenderer extends ChampionsRenderer {

    private static final EnumMap<Type, ResourceLocation> SKILL_ICONS = new EnumMap<>(Type.class);
    private static final ResourceLocation DEFAULT_ICON = new ResourceLocation("textures/items/emerald.png");
    private static final int CIRCLE_RADIUS = 20;
    private static final int CIRCLE_PADDING = (CIRCLE_RADIUS * 2) + 5;

    private static final Color DEFAULT_BACKGROUND = new Color(70, 70, 70, 117);
    private static final Color PREPARED_BACKGROUND = new Color(253, 203, 110, 117);
    private static final Color ACTIVATED_BACKGROUND = new Color(255, 241, 79, 117);

    private static final Color PREPARED_RING = new Color(253, 203, 110);
    private static final Color ACTIVATED_RING = new Color(255, 247, 64);
    private static final Color DEFAULT_RING = new Color(0, 148, 255);

    private static final EnumMap<Type, SkillCooldown> dummyCooldowns = new EnumMap<>(Type.class);

    static {
        SKILL_ICONS.put(SWORD, new ResourceLocation("textures/items/gold_sword.png"));
        SKILL_ICONS.put(AXE, new ResourceLocation("textures/items/iron_axe.png"));
        SKILL_ICONS.put(BOW, new ResourceLocation("textures/items/bow_standby.png"));
        SKILL_ICONS.put(PASSIVE_A, new ResourceLocation("textures/items/dye_powder_red.png"));
        SKILL_ICONS.put(PASSIVE_B, new ResourceLocation("textures/items/dye_powder_orange.png"));
        SKILL_ICONS.put(GLOBAL_PASSIVE, new ResourceLocation("textures/items/dye_powder_yellow.png"));

        dummyCooldowns.put(SWORD, new SkillCooldown(null, 15));
        dummyCooldowns.put(AXE, new SkillCooldown(null, 7) {
            @Override
            public float getPercentageLeft() {
                return 0.75F;
            }
            @Override
            public float getTimeLeft() {
                return 7;
            }
        });
        dummyCooldowns.put(BOW, new SkillCooldown(null, 13));
        dummyCooldowns.put(PASSIVE_A, new SkillCooldown(null, 4) {
            @Override
            public float getPercentageLeft() {
                return 0.50F;
            }
            @Override
            public float getTimeLeft() {
                return 4;
            }
        });
        dummyCooldowns.put(PASSIVE_B, new SkillCooldown(null, 11));
        dummyCooldowns.put(GLOBAL_PASSIVE, new SkillCooldown(null, 0) {
            @Override
            public float getPercentageLeft() {
                return -0.1F;
            }
            @Override
            public float getTimeLeft() {
                return 0;
            }
        });
    }

    private final HudBoundingBox box = HudBoundingBox.builder().size(275, 43).build();

    public SkillHudRenderer(final ModuleChampions championsModule) {
        super(championsModule,
              "Skill Recharge",
              "skill-recharge"
        );
    }

    @Override
    public void draw(final int x, final int y, final double scaleFactor, boolean dummy) {

        final Optional<ChampionsKit> kitOpt = championsModule.getPlayerKit(Minecraft.getMinecraft().thePlayer);
        if (!dummy && !kitOpt.isPresent()) {
            return;
        }

        final EnumMap<Type, SkillCooldown> clonedMap = new EnumMap<>(
                !dummy ? this.championsModule.getRechargeCache().getRechargeMap(kitOpt.get()) : dummyCooldowns
        );

        final boolean iconOnly = ChampionsSettings.SKILL_RECHARGE_RENDERER.equals(IterableState.ICON_ONLY);

        int horizontalPadding = CIRCLE_RADIUS + 2;
        final int yPosition = y + 1;

        for (final ChampionsSkill.Type type : ChampionsSkill.Type.values()) {
            final SkillCooldown cooldown = clonedMap.getOrDefault(type, null);
            boolean isArrowPrepared = type == BOW && this.championsModule.getRechargeCache().isArrowPrepared();
            if (cooldown == null && !isArrowPrepared) {
                continue;
            }

            final Optional<ChampionsSkill> skill = cooldown == null ? Optional.empty() : cooldown.getSkill();
            final boolean isActivatable = skill.isPresent() && skill.get() instanceof IActivatable;
            final boolean hasStarted = cooldown != null && cooldown.hasStarted();

            Color color = !hasStarted && isActivatable ? ACTIVATED_BACKGROUND : (isArrowPrepared ? PREPARED_BACKGROUND : DEFAULT_BACKGROUND);

            float percentageLeft = cooldown == null ? -1 : cooldown.getPercentageLeft();
            if (!hasStarted && isActivatable) {
                percentageLeft = (cooldown.getAbsoluteTimeLeft() - cooldown.getTimeLeft()) / cooldown.getActivateAfter();
            }

            final String timeLeft = String.format("%.1f", cooldown == null ? 0F : cooldown.getTimeLeft());

            GL11.glEnable(3042);
            GL11.glDisable(3553);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);

            GL11.glColor4f(1F, 1F, 1F, 1F);
            final Tessellator tessellator = Tessellator.getInstance();

            final int centerX = x + horizontalPadding;
            final int centerY = yPosition + (CIRCLE_RADIUS);


            this.drawBackground(tessellator, centerX, centerY, CIRCLE_RADIUS - (CIRCLE_RADIUS / 5), color);
            if (cooldown != null) {
                if (!hasStarted && isActivatable) {
                    this.drawRing(tessellator, centerX, centerY, CIRCLE_RADIUS, percentageLeft, CIRCLE_RADIUS / 5, ACTIVATED_RING);
                } else {
                    this.drawRing(tessellator, centerX, centerY, CIRCLE_RADIUS, percentageLeft, CIRCLE_RADIUS / 5,
                                  DEFAULT_RING
                    );
                }
            } else {
                this.drawRing(tessellator, centerX, centerY, CIRCLE_RADIUS, 1F, CIRCLE_RADIUS / 5, PREPARED_RING);
            }

            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

            final ResourceLocation location = SKILL_ICONS.getOrDefault(type, DEFAULT_ICON);
            Minecraft.getMinecraft().getTextureManager().bindTexture(location);
            Gui.drawScaledCustomSizeModalRect(
                    centerX - CIRCLE_RADIUS,
                    centerY - CIRCLE_RADIUS,
                    0,
                    0,
                    16,
                    16,
                    CIRCLE_RADIUS * 2,
                    CIRCLE_RADIUS * 2,
                    16,
                    16
            );

            if (percentageLeft >= 0 && !iconOnly) {
                if (!(isActivatable && !hasStarted)){
                    final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
                    fontRenderer.drawString(
                            timeLeft,
                            centerX - (fontRenderer.getStringWidth(timeLeft) / 2F),
                            ((float) centerY) + CIRCLE_RADIUS - (fontRenderer.FONT_HEIGHT),
                            Color.WHITE.getRGB(),
                            true
                    );
                }
            }

            horizontalPadding += CIRCLE_PADDING;
        }
    }

    private void drawBackground(final Tessellator tessellator, final int xPos, final int yPos, final int radius, Color color) {
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(6, DefaultVertexFormats.POSITION);
        worldRenderer.pos(xPos, yPos, 0).endVertex();

        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
        for (int i = 0; i <= 360; ++i) {
            final double sin = Math.sin(Math.toRadians(i));
            final double cos = Math.cos(Math.toRadians(i));
            worldRenderer.pos(xPos + sin * radius, yPos + cos * radius, 0.0).endVertex();
        }

        tessellator.draw();
    }

    private void drawRing(final Tessellator tessellator,
                          final int xPos,
                          final int yPos,
                          final int radius,
                          final float percentageLeft,
                          final int ringWidth,
                          Color primaryColor) {

        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        // Ring for time left and passed
        final Color secondaryColor = percentageLeft < 0 ? new Color(36, 255, 46) : new Color(129, 202, 255);
        for (double i = 0; i <= 360; i += 0.4) {

            GL11.glColor4f(
                    primaryColor.getRed() / 255F,
                    primaryColor.getGreen() / 255F,
                    primaryColor.getBlue() / 255F,
                    1F
            );
            if (i >= (360 * percentageLeft)) {
                GL11.glColor4f(
                        secondaryColor.getRed() / 255F,
                        secondaryColor.getGreen() / 255F,
                        secondaryColor.getBlue() / 255F,
                        1F
                );
            }

            worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            final double cos = Math.cos(Math.toRadians(i + 90));
            final double sin = Math.sin(Math.toRadians(i + 90));

            worldRenderer.pos(
                    xPos + cos * (radius - ringWidth),
                    yPos - sin * (radius - ringWidth),
                    0
            ).endVertex();
            worldRenderer.pos(
                    xPos + cos * radius,
                    yPos - sin * radius,
                    0
            ).endVertex();

            tessellator.draw();
        }

    }

    @Override
    public HudBoundingBox getBox() {
        return this.box;
    }

    @Override
    protected boolean isEnabled() {
        return !ChampionsSettings.SKILL_RECHARGE_RENDERER.equals(IterableState.OFF);
    }

}
