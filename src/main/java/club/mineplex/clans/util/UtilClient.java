package club.mineplex.clans.util;

import club.mineplex.clans.util.object.ConnectionBuilder;
import club.mineplex.clans.util.object.DelayedTask;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Slot;

import java.net.URI;

public class UtilClient {
    private UtilClient() {
    }

    public static void openWebLink(final String url) {
        try {
            final Class<?> oclass = Class.forName("java.awt.Desktop");
            final Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{new URI(url)});
        } catch (final Exception e) {
            System.err.println("Couldn\\'t open link");
            e.printStackTrace();
        }
    }

    public static void openGuiScreen(final GuiScreen screen) {
        new DelayedTask(() -> Minecraft.getMinecraft().displayGuiScreen(screen));
    }

    public static void playSound(final String sound, final float volume, final float pitch) {
        Minecraft.getMinecraft().thePlayer.playSound(sound, volume, pitch);
    }

    public static boolean isModFeatureAllowed(final String moduleId) {
        final ConnectionBuilder builder = new ConnectionBuilder("http://api.mineplex.club/clansmod/features");
        builder.send();
        builder.skipRedirects();

        try {
            final Gson gson = new Gson();
            final JsonArray array = gson.fromJson(builder.getResponseString(), JsonArray.class);

            for (final JsonElement jsonElement : array) {
                final JsonObject object = jsonElement.getAsJsonObject();

                if (object.get("name").getAsString().equals(moduleId)) {
                    return object.get("enabled").getAsBoolean();
                }

            }

        } catch (final JsonSyntaxException e) {
            e.printStackTrace();
            return true;
        }

        return true;
    }

    public static Slot getSlotInHotbar(final int slot) {
        int slot2 = Minecraft.getMinecraft().thePlayer.inventoryContainer.inventorySlots.size() + slot;
        slot2 -= 9;

        return Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(slot2);
    }


}
