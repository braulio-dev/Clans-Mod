package club.mineplex.clans.client.modules.champions;

import club.mineplex.clans.client.hud.HudManager;
import club.mineplex.clans.client.modules.AbstractMod;
import club.mineplex.clans.client.modules.champions.cache.ChampionsBuildCache;
import club.mineplex.clans.client.modules.champions.cache.ChampionsRechargeCache;
import club.mineplex.clans.client.modules.champions.renderer.ManaRenderer;
import club.mineplex.clans.client.modules.champions.renderer.SkillHudRenderer;
import club.mineplex.clans.gamestate.mineplex.MineplexData;
import club.mineplex.clans.gamestate.mineplex.MineplexGame;
import club.mineplex.core.mineplex.champions.ChampionsBuild;
import club.mineplex.core.mineplex.champions.ChampionsKit;
import club.mineplex.core.mineplex.champions.ChampionsSkill;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import java.util.Optional;

@Value
@EqualsAndHashCode(callSuper = true)
public class ModuleChampions extends AbstractMod {

    SkillHudRenderer skillRechargeRenderer;
    ManaRenderer manaRenderer;
    ChampionsRechargeCache rechargeCache;
    @Getter(AccessLevel.PRIVATE)
    ChampionsBuildCache buildCache;

    public ModuleChampions() {
        super("Champions Utilities");
        this.skillRechargeRenderer = new SkillHudRenderer(this);
        this.manaRenderer = new ManaRenderer(this);
        this.rechargeCache = new ChampionsRechargeCache(this);
        this.buildCache = new ChampionsBuildCache(this);

        HudManager.getInstance().registerRenderer(skillRechargeRenderer);
        HudManager.getInstance().registerRenderer(manaRenderer);

        MinecraftForge.EVENT_BUS.register(rechargeCache);
        MinecraftForge.EVENT_BUS.register(buildCache);
    }

    public boolean hasBoosterWeapon(final ChampionsSkill.Type skillType) {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return false;
        }

        return (skillType == ChampionsSkill.Type.AXE && player.inventory.hasItem(Items.golden_axe))
                || (skillType == ChampionsSkill.Type.SWORD && player.inventory.hasItem(
                Items.golden_sword));
    }

    public ChampionsBuild getEquippedBuild() {
        return this.buildCache.get();
    }

    public Optional<ChampionsKit> getPlayerKit(final EntityPlayerSP player) {
        final ItemStack[] armorInventory = player.inventory.armorInventory;

        for (final ClassArmor classArmor : ClassArmor.values()) {
            if (classArmor.matches(armorInventory[3], armorInventory[2], armorInventory[1], armorInventory[0])) {
                return Optional.of(classArmor.getKit());
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean isModuleUsable() {
        if (!gameState.isMineplex()) {
            return false;
        }

        final MineplexGame game = gameState.getMultiplayerData().as(MineplexData.class).getGame();
        return game.equals(MineplexGame.CHAMPIONS_CTF)
                || game.equals(MineplexGame.CHAMPIONS_DOMINATE)
                || game.equals(MineplexGame.CLANS);
    }

}
