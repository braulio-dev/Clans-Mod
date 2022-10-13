package club.mineplex.clans.client.modules.champions.renderer;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.client.hud.IHudRenderer;
import club.mineplex.clans.client.modules.champions.ModuleChampions;
import club.mineplex.clans.gamestate.GameState;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ChampionsRenderer implements IHudRenderer {

    protected final ModuleChampions championsModule;
    private final String name;
    private final String id;
    protected final Minecraft mc = Minecraft.getMinecraft();
    GameState gameState = ClansMod.getInstance().getGameState();

    protected abstract boolean isEnabled();

    @Override
    public boolean shouldRender() {
        return this.isEnabled() && this.championsModule.isEnabled();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getId() {
        return this.id;
    }

}
