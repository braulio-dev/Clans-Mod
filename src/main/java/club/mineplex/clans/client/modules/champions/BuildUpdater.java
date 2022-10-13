package club.mineplex.clans.client.modules.champions;

import club.mineplex.clans.util.UtilText;
import club.mineplex.clans.util.object.Pair;
import club.mineplex.core.mineplex.champions.ChampionsBuild;
import club.mineplex.core.mineplex.champions.ChampionsKit;
import club.mineplex.core.mineplex.champions.ChampionsSkill;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public abstract class BuildUpdater {

    private static final Pattern SKILL_UPDATE_PATTERN =
            Pattern.compile("(Axe|Bow|Sword|PassiveA|PassiveB|GlobalPassive): ([a-zA-Z ]+) (?:Lvl)?(\\d+)?");

    private final Pattern startBuildPattern;
    private final Pattern endBuildPattern;
    private final Collection<IChatComponent> messages = new ArrayList<>();
    private final ModuleChampions championsModule;
    @Getter
    protected boolean isInvisible = false;
    private ChampionsBuild build = null;
    @Getter
    private boolean building;

    public void addChatMessage(@NonNull final IChatComponent text) {
        Validate.validState(this.isBuilding(), "An update is not currently being built by this instance");
        this.messages.add(text);
    }

    public void attemptBuild(@NonNull final IChatComponent text) {
        Validate.validState(!this.building, "An update is currently being built by this instance already");
        final String rawText = text.getUnformattedText();
        final Matcher matcher = this.startBuildPattern.matcher(rawText);
        this.building = matcher.matches();
        if (this.building) {
            final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            if (player == null) {
                return;
            }

            final Optional<ChampionsKit> playerKitOpt = this.championsModule.getPlayerKit(player);
            if (!playerKitOpt.isPresent()) {
                return;
            }

            this.build = new ChampionsBuild(player.getUniqueID(), playerKitOpt.get());
        }

        this.building = this.build != null;
    }

    public Pair<Boolean, ChampionsBuild> attemptEnd(@NonNull final IChatComponent text) {
        Validate.isTrue(this.building, "An update is not currently being built by this instance");

        final String rawText = text.getUnformattedText();
        final Matcher matcher = this.endBuildPattern.matcher(rawText);
        this.building = !matcher.matches();

        if (this.building) {
            return new Pair<>(false, null);
        }

        for (final IChatComponent message : this.messages) {
            this.handleMessage(message, this.build);
        }

        this.messages.clear();
        final Pair<Boolean, ChampionsBuild> toReturn = new Pair<>(true, this.build);
        this.build = null;
        this.isInvisible = false;

        return toReturn;
    }

    private void handleMessage(final IChatComponent text, final ChampionsBuild build) {
        final String rawText = text.getUnformattedText();

        final Matcher matcher = SKILL_UPDATE_PATTERN.matcher(rawText);
        if (!matcher.matches() || build == null) {
            return;
        }

        ChampionsSkill.Type type = null;
        for (final ChampionsSkill.Type value : ChampionsSkill.Type.values()) {
            final String prettified = UtilText.prettifyString(value.name()).replace(" ", "");
            if (prettified.equals(matcher.group(1))) {
                type = value;
                break;
            }

        }

        if (type == null) {
            return;
        }

        try {
            final ChampionsSkill skill = ChampionsSkill.ofName(matcher.group(2));
            final int level = Integer.parseInt(matcher.group(3));

            this.build.setSkill(type, skill, level);
        } catch (final NumberFormatException e) {
            this.build.removeSkill(type);
        } catch (final IllegalArgumentException e) {
            this.build = null;
        }
    }

    public static class SkillCommand extends BuildUpdater {

        public SkillCommand(final ModuleChampions moduleChampions) {
            super(Pattern.compile("------------------------------------------"),
                  Pattern.compile("------------------------------------------"),
                  moduleChampions
            );
        }

        public void setInvisible(final boolean invisible) {
            this.isInvisible = invisible;
        }

    }

    public static class Automatic extends BuildUpdater {

        public Automatic(final ModuleChampions moduleChampions) {
            super(Pattern.compile("Skill> Listing Class Skills:"),
                  Pattern.compile("Class> You equipped (.*)\\."),
                  moduleChampions
            );
        }

    }

}