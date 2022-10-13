package club.mineplex.clans.client.settings;

import net.minecraft.util.EnumChatFormatting;

import static net.minecraft.util.EnumChatFormatting.*;

public enum IterableState {

    // Toggleable
    // Pausable
    ENABLED(GREEN + "Enabled"),
    PAUSED(YELLOW + "Paused"),
    DISABLED(RED + "Disabled"),

    // Enable / Disable
    ON(GREEN + "On"),
    OFF(RED + "Off"),

    // Custom
    IN_CLANSHUB(YELLOW + "In ClansHub"),
    TYPE_ONLY(YELLOW + "Type Only"),
    ICON_ONLY(YELLOW + "Icon Only"),
    NUMBER(YELLOW + "Number");

    private final String displayString;

    IterableState(final String displayString) {
        this.displayString = displayString;
    }

    public static IterableState[] toggleable() {
        return new IterableState[]{
                ENABLED,
                DISABLED
        };
    }

    public static IterableState[] pausable() {
        return new IterableState[]{
                ENABLED,
                PAUSED,
                DISABLED
        };
    }

    public String getDisplayString() {
        return this.displayString;
    }

    public String getDisplayStringStripped() {
        return EnumChatFormatting.getTextWithoutFormattingCodes(this.displayString);
    }

}
