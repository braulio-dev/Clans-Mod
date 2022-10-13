package club.mineplex.clans.util;

import club.mineplex.clans.util.object.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilText {

    public static final char COLOR_CHAR = '\u00A7';
    private static final Pattern COLOR_PATTERN = Pattern.compile("&[^\\s]");

    private UtilText() {
    }

    public static String[] splitLinesToArray(String[] strings, LineFormat lineFormat)
    {
        ArrayList<String> lineList = splitLines(strings, lineFormat);

        String[] lineArray = new String[lineList.size()];
        lineArray = lineList.toArray(lineArray);

        return lineArray;
    }

    public static ArrayList<String> splitLines(String[] strings, LineFormat lineFormat)
    {
        ArrayList<String> lines = new ArrayList<String>();

        for (String s : strings)
        {
            lines.addAll(splitLine(s, lineFormat));
        }

        return lines;
    }

    public static String[] splitLineToArray(String string, LineFormat lineFormat)
    {
        return splitLinesToArray(string.split("\n"), lineFormat);
    }

    public static ArrayList<String> splitLine(String string, int lineLength)
    {
        ArrayList<String> strings = new ArrayList<String>();

        // Ignore lines with #
        if (string.startsWith("#"))
        {
            strings.add(string.substring(1, string.length()));
            return strings;
        }

        // Empty
        if (string.equals("") || string.equals(" "))
        {
            strings.add(" ");
            return strings;
        }

        String current = "";
        int currentLength = 0;
        String[] split = string.split(" ");
        String colors = "";

        for (int i = 0; i < split.length; i++)
        {
            String word = split[i];
            int wordLength = word.length();

            if (currentLength + wordLength + 4 > lineLength && !current.isEmpty())
            {
                strings.add(current);
                current = colors + word;
                currentLength = wordLength + 1;
                continue;
            }

            if (i != 0)
            {
                current += " ";
                currentLength += 4;
            }

            current += word;
            currentLength += wordLength;

            outer:
            for (int i1 = 0; i1 < current.length(); i1++) {
                if (current.charAt(i1) == '\u00a7') {
                    String colorString = "\u00a7" + current.charAt(i1 + 1);
                    for (EnumChatFormatting value : EnumChatFormatting.values()) {
                        if (colorString.equals(colorString.toString())) {
                            colors = value.toString();
                            break outer;
                        }
                    }
                }
            }

        }

        if (!current.isEmpty())
        {
            strings.add(current);
        }

        return strings;
    }

    public static ArrayList<String> splitLine(String string, LineFormat lineFormat)
    {
        return splitLine(string, lineFormat.getLength());
    }

    public static void sendPlayerMessage(final String text) {
        final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return;
        }

        final StringBuilder str = new StringBuilder();
        final Matcher matcher = COLOR_PATTERN.matcher(text);

        final List<Pair<Pair<EnumChatFormatting, Integer>, Pair<Integer, Integer>>> indexes = new ArrayList<>();
        while (matcher.find()) {
            final int position = matcher.group().length();

            EnumChatFormatting color;
            try {
                final char found = matcher.group().substring(1).charAt(0);

                switch (found) {
                    case '9':
                        color = EnumChatFormatting.BLUE;
                        break;
                    case '8':
                        color = EnumChatFormatting.DARK_GRAY;
                        break;
                    case '7':
                        color = EnumChatFormatting.GRAY;
                        break;
                    case '6':
                        color = EnumChatFormatting.GOLD;
                        break;
                    case '5':
                        color = EnumChatFormatting.DARK_PURPLE;
                        break;
                    case '4':
                        color = EnumChatFormatting.DARK_RED;
                        break;
                    case '3':
                        color = EnumChatFormatting.DARK_AQUA;
                        break;
                    case '2':
                        color = EnumChatFormatting.DARK_GREEN;
                        break;
                    case '1':
                        color = EnumChatFormatting.DARK_BLUE;
                        break;
                    case 'a':
                        color = EnumChatFormatting.GREEN;
                        break;
                    case 'b':
                        color = EnumChatFormatting.AQUA;
                        break;
                    case 'c':
                        color = EnumChatFormatting.RED;
                        break;
                    case 'd':
                        color = EnumChatFormatting.LIGHT_PURPLE;
                        break;
                    case 'e':
                        color = EnumChatFormatting.YELLOW;
                        break;
                    case 'f':
                        color = EnumChatFormatting.WHITE;
                        break;
                    case 'k':
                        color = EnumChatFormatting.OBFUSCATED;
                        break;
                    case 'l':
                        color = EnumChatFormatting.BOLD;
                        break;
                    case 'm':
                        color = EnumChatFormatting.STRIKETHROUGH;
                        break;
                    case 'n':
                        color = EnumChatFormatting.UNDERLINE;
                        break;
                    case 'o':
                        color = EnumChatFormatting.ITALIC;
                        break;

                    default:
                        color = EnumChatFormatting.RESET;
                        break;
                }

            } catch (final NumberFormatException e) {
                color = EnumChatFormatting.RESET;
            }

            indexes.add(
                    new Pair<>(
                            new Pair<>(color, position),
                            new Pair<>(matcher.start(), matcher.end())
                    )
            );
        }

        if (indexes.size() > 0) {

            /* Initial color is default */
            if (indexes.get(0).getValue().getKey() > 0) {
                str.append(text, 0, indexes.get(0).getValue().getKey());
            }

            /* Coloring */
            for (int i = 0; i < indexes.size(); i++) {
                final Pair<Integer, Integer> bounds = indexes.get(i).getValue();
                final int nextBound = indexes.size() > i + 1 ? indexes.get(i + 1).getValue().getKey() : text.length();

                final String toAppend =
                        text.substring(bounds.getKey(), nextBound).substring(indexes.get(i).getKey().getValue());
                str.append(indexes.get(i).getKey().getKey()).append(toAppend);
            }

        }

        final IChatComponent component = new ChatComponentText(str.length() > 0 ? str.toString() : text);
        player.addChatMessage(component);
    }

    /**
     * @param prefix The prefix of the message.
     * @param text   The content of the message.
     *               <p>
     *               Example: Death> Player1 killed by Player2.
     */
    public static void sendPlayerMessageWithPrefix(final String prefix, String text) {
        text = text.replaceAll("&r", "&7");
        UtilText.sendPlayerMessage("&9" + prefix + "> &7" + text);
    }

    public static String prettifyString(final String text) {
        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            final String spaced = text.replaceAll("_", " ");
            final String c = Character.toString(spaced.charAt(i)).toLowerCase();
            str.append(i == 0 || spaced.charAt(i - 1) == ' ' ? c.toUpperCase() : c);
        }
        return str.toString();
    }

    public static String toRoman(int input) {
        int[] values = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romanDigits = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };

        StringBuffer result = new StringBuffer();

        int i = 0;
        while (input > 0) {
            while (input >= values[i]) {
                result.append(romanDigits[i]);
                input -= values[i];
            }

            i++;
        }

        return result.toString();
    }
}
