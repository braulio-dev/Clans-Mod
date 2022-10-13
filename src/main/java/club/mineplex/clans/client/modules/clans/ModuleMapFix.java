package club.mineplex.clans.client.modules.clans;

import club.mineplex.clans.client.modules.AbstractMod;
import club.mineplex.clans.client.settings.repo.ClansSettings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModuleMapFix extends AbstractMod {

    public ModuleMapFix() {
        super("Clans Map Fix");
    }

    @SubscribeEvent
    public void onWorldChange(final WorldEvent.Load event) {
        if (!this.gameState.isMineplex() || !ClansSettings.FIX_CLANS_MAP) {
            return;
        }

        Minecraft.getMinecraft().entityRenderer.getMapItemRenderer().clearLoadedMaps();
    }

}
