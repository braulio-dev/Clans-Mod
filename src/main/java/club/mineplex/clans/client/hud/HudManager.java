package club.mineplex.clans.client.hud;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.client.gui.repository.GuiHudManager;
import lombok.NonNull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collection;

public class HudManager {

    private static final HudManager INSTANCE = new HudManager();
    private final Collection<IHudRenderer> renderers = new ArrayList<>();

    private HudManager() {
    }

    public static HudManager getInstance() {
        return INSTANCE;
    }

    public void registerRenderer(@NonNull final IHudRenderer renderer) {
        this.renderers.add(renderer);
        MinecraftForge.EVENT_BUS.register(renderer);
    }

    public Collection<IHudRenderer> getRenderers() {
        return new ArrayList<>(this.renderers);
    }

    public void saveRenderers() {
        for (final IHudRenderer renderer : this.renderers) {
            final ConfigCategory category = ClansMod.getInstance()
                                                    .getRootConfiguration()
                                                    .getCategory("hud." + renderer.getId());

            final double positionalFactorX = renderer.getBox().getPosition().getPositionalFactorX();
            final double positionalFactorY = renderer.getBox().getPosition().getPositionalFactorY();
            final double scaleFactor = renderer.getBox().getScaleFactor();

            // Position
            category.put("positionalFactorX",
                         new Property("positionalFactorX", Double.toString(positionalFactorX), Property.Type.DOUBLE)
            );
            category.put("positionalFactorY",
                         new Property("positionalFactorY", Double.toString(positionalFactorY), Property.Type.DOUBLE)
            );
            category.put("scaleFactor",
                         new Property("scaleFactor", Double.toString(scaleFactor), Property.Type.DOUBLE)
            );
        }

        ClansMod.getInstance().getRootConfiguration().save();
        ClansMod.getInstance().getRootConfiguration().load();
    }

    public void loadRenderers() {
        ClansMod.getInstance().getRootConfiguration().load();

        for (final IHudRenderer renderer : this.renderers) {
            final ConfigCategory category = ClansMod.getInstance()
                                                    .getRootConfiguration()
                                                    .getCategory("hud." + renderer.getId());

            // Position
            final double factorX = category.getOrDefault("positionalFactorX",
                                                         new Property("positionalFactorX", "0.0", Property.Type.DOUBLE)
            ).getDouble();
            final double factorY = category.getOrDefault("positionalFactorY",
                                                         new Property("positionalFactorY", "0.0", Property.Type.DOUBLE)
            ).getDouble();
            final double scaleFactor =
                    category.getOrDefault("scaleFactor", new Property("scaleFactor", "0.0", Property.Type.DOUBLE))
                            .getDouble();

            renderer.getBox().setScaleFactor(scaleFactor);
            renderer.getBox().getPosition().fromFactors(factorX, factorY);
        }
    }

    private void render(@NonNull final ScaledResolution resolution) {
        for (final IHudRenderer renderer : this.renderers) {
            if (!renderer.shouldRender()) {
                continue;
            }

            final HudBoundingBox box = renderer.getBox();
            box.getPosition().rescaleWindow(resolution.getScaledWidth(), resolution.getScaledHeight());
            renderer.render(box.getPosition().getX(), box.getPosition().getY(), box.getScaleFactor(), false);
        }
    }

    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            // Skipping rendering if the screen being rendered is the HUD manager
            if (!(Minecraft.getMinecraft().currentScreen instanceof GuiHudManager)) {
                this.render(event.resolution);
            }
        }
    }

}