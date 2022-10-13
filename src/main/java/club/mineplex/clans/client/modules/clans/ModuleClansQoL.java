package club.mineplex.clans.client.modules.clans;

import club.mineplex.clans.client.modules.AbstractMod;
import club.mineplex.clans.client.settings.repo.ClansSettings;
import club.mineplex.clans.events.client.CustomItemHoverEvent;
import club.mineplex.clans.gamestate.mineplex.MineplexData;
import club.mineplex.clans.gamestate.mineplex.MineplexGame;
import club.mineplex.clans.item.CustomItem;
import club.mineplex.clans.item.legendary.IChargeable;
import club.mineplex.clans.item.rune.RuneImpl;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.RuneHolder;
import club.mineplex.clans.item.legendary.AlligatorsTooth;
import club.mineplex.clans.item.legendary.HyperAxe;
import club.mineplex.clans.item.legendary.LegendaryItem;
import club.mineplex.clans.item.rune.repo.armor.ReinforcedAttribute;
import club.mineplex.clans.util.UtilText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.minecraft.util.EnumChatFormatting.*;

public class ModuleClansQoL extends AbstractMod {

    private static final Pattern DEATH_MESSAGE_PATTERN = Pattern.compile("Death> ([a-zA-Z0-9_]{3,16}) killed by ([a-zA-Z0-9_]{3,16}) with ([^\\n]+)\\.");
    private static final Pattern SUICIDE_MESSAGE_PATTERN = Pattern.compile("Death> ([a-zA-Z0-9_]{3,16}) has died\\.");

    public ModuleClansQoL() {
        super("Clans Quality of Life");
    }

    /**
     * Adds a fake item warning to a {@link LegendaryItem}'s lore that doesn't match NBT tags
     */
    @SubscribeEvent
    public void addFakeItemWarning(ItemTooltipEvent event) {
        if (!isModuleUsable() || !ClansSettings.FAKE_ITEM_WARNING) {
            return;
        }

        final ItemStack item = event.itemStack;
        final List<String> toolTip = event.toolTip;

        int pos = toolTip.size();
        for (int i = 0; i < toolTip.size(); i++) {
            final String s = toolTip.get(i);
            if (s.startsWith(DARK_PURPLE + ITALIC.toString() + WHITE + "UUID")) {
                pos = i;
                break;
            }
        }
        final int end = pos;

        if (ClansSettings.SHOW_REINFORCED_TOTAL) {
            final ItemStack[] armorInventory = Minecraft.getMinecraft().thePlayer.inventory.armorInventory;
            if (Arrays.stream(armorInventory).anyMatch(piece -> piece != null && piece.equals(item) && mod.getItemManager().read(piece).isPresent())) {
                double totalReinforced = 0;
                double maxReinforced = 1.0d * 4;
                for (ItemStack itemStack : armorInventory) {
                    final Optional<CustomItem> armorPiece = mod.getItemManager().read(itemStack);
                    if (armorPiece.isPresent()) {
                        final RuneImpl prefix = armorPiece.get().getRuneHolder().getPrefix().orElse(null);
                        if (prefix instanceof ReinforcedAttribute) {
                            totalReinforced += ((ReinforcedAttribute) prefix).getFlatReduction();
                            maxReinforced = ((ReinforcedAttribute) prefix).getValues()[0].getRange().getMax() * armorInventory.length;
                        }
                    }
                }

                String extraInfo = "";
                if (ClansSettings.ENHANCED_RUNED_ITEMS_DISPLAY) {
                    double percentage = totalReinforced / maxReinforced * 100;
                    EnumChatFormatting color = percentage < 30 ? RED : percentage < 50 ? GOLD : percentage < 70 ? YELLOW : percentage < 90 ? DARK_GREEN : GREEN;;
                    extraInfo = GRAY + " (" + color + String.format("%.1f%%", percentage) + GRAY + ")";
                }

                toolTip.add(end, RESET + " ");
                toolTip.add(end, String.format(
                        "%s%sTotal Reinforced: %s%s-%.1f%s",
                        GOLD,
                        BOLD,
                        RESET,
                        WHITE,
                        totalReinforced,
                        extraInfo
                ));
            }
        }

        final Optional<CustomItem> customItem = mod.getItemManager().read(item);
        if (!customItem.isPresent()) {
            final Optional<LegendaryItem.Type> type = LegendaryItem.fromType(item.getItem());
            type.ifPresent(legendaryType -> {
                toolTip.add(end, RESET.toString());
                toolTip.add(end, EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + "THIS IS A FAKE ITEM: " + legendaryType);
            });
        }

    }

    @SubscribeEvent
    public void onCustomItemHover(CustomItemHoverEvent event) {
        final CustomItem cItem = event.getCustomItem();
        final List<String> toolTip = event.getTooltip();
        final RuneHolder attributes = cItem.getRuneHolder();

        if (ClansSettings.ENHANCED_RUNED_ITEMS_DISPLAY) {
            double overallPercentage = 0;
            for (RuneImpl attribute : attributes.asSet()) {
                final String description = attribute.getDescription();

                for (int i = 0; i < toolTip.size(); i++) {
                    final String s = toolTip.get(i);

                    if (s.contains(description)) {
                        double percentage = 0;
                        for (ValueRangeDisplay<?> value : attribute.getValues()) {
                            double toAdd =
                                    (value.getValue().doubleValue() - value.getRange().getMin()) / value.getRange()
                                                                                                        .gap();
                            if (value.isReverse()) {
                                toAdd = 1 - toAdd;
                            }
                            
                            percentage += toAdd * 100;
                        }

                        percentage /= attribute.getValues().length;
                        overallPercentage += percentage;

                        EnumChatFormatting color = percentage < 30 ? RED : percentage < 50 ? GOLD :
                                percentage < 70 ? YELLOW : percentage < 90 ? DARK_GREEN : GREEN;
                        toolTip.set(i, s + " " + GRAY + "(" + color + String.format("%.1f%%", percentage) + GRAY + ")");
                    }
                }
            }

            if (!attributes.asSet().isEmpty()) {
                overallPercentage /= attributes.asSet().size();
                EnumChatFormatting color = overallPercentage < 30 ? RED : overallPercentage < 50 ? GOLD :
                        overallPercentage < 70 ? YELLOW : overallPercentage < 90 ? DARK_GREEN : GREEN;
                String name = toolTip.get(0) + " " + GRAY + "(" + color + String.format("%.1f%%", overallPercentage) + GRAY + ")";

                toolTip.set(0, name);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLegendHover(CustomItemHoverEvent event) {
        final CustomItem cItem = event.getCustomItem();
        final List<String> toolTip = event.getTooltip();

        if (cItem instanceof HyperAxe) {
            final HyperAxe hyperAxe = (HyperAxe) cItem;

            int pos = toolTip.size();
            for (int i = 0; i < toolTip.size(); i++) {
                final String s = toolTip.get(i);
                if (s.endsWith("Dash")) {
                    pos = i;
                    break;
                }
            }

            toolTip.add(pos, RESET + " ");
            toolTip.add(pos, String.format(
                    "%sGrants Speed %s%s%s for %s%.1f%s Seconds",
                    WHITE,
                    YELLOW,
                    UtilText.toRoman(hyperAxe.getSpeedAmount() + 1),
                    WHITE,
                    YELLOW,
                    hyperAxe.getSpeedDuration() / 20d,
                    WHITE
            ));
            return;
        }

        if (cItem instanceof AlligatorsTooth) {
            final AlligatorsTooth alligatorsTooth = (AlligatorsTooth) cItem;

            int pos = toolTip.size();
            for (int i = 0; i < toolTip.size(); i++) {
                final String s = toolTip.get(i);
                if (s.endsWith("Gator Stroke")) {
                    pos = i;
                    break;
                }
            }

            toolTip.add(pos, RESET + " ");
            toolTip.add(pos, String.format(
                    "%sHas a %s%.2f%%%s speed boost", // "%sHas a speed boost of %s%.1f%s Blocks"
                    WHITE,
                    YELLOW,
                    (alligatorsTooth.getSwimSpeed() - AlligatorsTooth.BOOST_GEN.getMin()) / AlligatorsTooth.BOOST_GEN.gap() * 100D, // alligatorsTooth.getSwimSpeed() * 8.75,
                    WHITE
            ));
            return;
        }
    }


    /**
     * Enhances death messages to include the clan relation color on names
     */
    @SubscribeEvent
    public void enhanceDeathMessage(ClientChatReceivedEvent event) {
        if (!isModuleUsable() || !ClansSettings.ENHANCED_DEATH_MESSAGES) {
            return;
        }

        final IChatComponent message = event.message;
        final String text = message.getUnformattedText();
        Matcher matcher = DEATH_MESSAGE_PATTERN.matcher(text);
        if (!matcher.matches()) {
            matcher = SUICIDE_MESSAGE_PATTERN.matcher(text);
            if (!matcher.matches()) {
                return;
            }
        }

        IChatComponent builder = new ChatComponentText("");
        final String victim = matcher.group(1);
        final Optional<String> attacker = matcher.groupCount() > 1 ? Optional.of(matcher.group(2)) : Optional.empty();

        final List<IChatComponent> siblings = message.getSiblings();
        for (IChatComponent sibling : siblings) {
            if (sibling.getUnformattedText().equals(victim)) {
                final Optional<IChatComponent> victimName = getPlayerDisplayName(victim);
                if (victimName.isPresent()) {
                    builder.appendSibling(victimName.get());
                    continue;
                }
            }

            if (attacker.isPresent() && sibling.getUnformattedText().equals(attacker.get())) {
                final Optional<IChatComponent> attackerName = getPlayerDisplayName(attacker.get());
                if (attackerName.isPresent()) {
                    builder.appendSibling(attackerName.get());
                    continue;
                }
            }

            builder.appendSibling(sibling);
        }

        event.message = builder;
    }

    private Optional<IChatComponent> getPlayerDisplayName(String playerName) {
        final Collection<NetworkPlayerInfo> infoMap = mc.getNetHandler().getPlayerInfoMap();
        for (NetworkPlayerInfo playerInfo : infoMap) {
            if (playerInfo.getGameProfile().getName().equals(playerName)) {

                final ChatComponentText builder = new ChatComponentText("");
                final ScorePlayerTeam playerTeam = playerInfo.getPlayerTeam();
                if (playerTeam != null) {
                    String prefix = playerTeam.getColorPrefix();
                    final int index = playerTeam.getColorPrefix().indexOf(' ') - 2;
                    if (index >= 0) {
                        prefix = prefix.substring(index);
                    }

                    prefix = prefix.replace(" ", "");
                    builder.appendSibling(new ChatComponentText(prefix + playerInfo.getGameProfile().getName()));
                } else {
                    builder.appendSibling(new ChatComponentText(playerInfo.getGameProfile().getName()));
                }

                return Optional.of(builder);
            }
        }

        return Optional.empty();
    }

    @Override
    protected boolean isModuleUsable() {
        return gameState.isMineplex()
                && gameState.getMultiplayerData().as(MineplexData.class).getGame().equals(
                MineplexGame.CLANS);
    }
}
