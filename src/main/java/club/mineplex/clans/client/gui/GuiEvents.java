package club.mineplex.clans.client.gui;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.client.gui.repository.GuiVersionBlock;
import club.mineplex.clans.client.gui.repository.modmenu.GuiModMenu;
import club.mineplex.clans.util.UtilClient;
import club.mineplex.clans.util.UtilScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiEvents {

    GuiModMenu modMenu = new GuiModMenu();

    private final ClansMod mod;

    public GuiEvents(ClansMod mod) {
        this.mod = mod;
    }

    // Check if the client has the latest version, and block the first instance of loading in
    // if we're updating
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (!(event.gui instanceof GuiMainMenu)) {
            return;
        }

        if (!mod.getClient().hasCheckedForUpdates() && ClansMod.getInstance().getClient().checkForUpdates()) {
            UtilClient.openGuiScreen(new GuiVersionBlock());
        }
    }

    @SubscribeEvent
    public void guiEvent(final GuiScreenEvent.InitGuiEvent.Post event) {
        final GuiScreen gui = event.gui;

        if (gui instanceof GuiOptions) {
            event.buttonList.add(new GuiButton(210, gui.width / 2 - 100, gui.height / 6 + 192,
                                               I18n.format("clansmod.gui.open", ClansMod.getMod().name()
                                               )
            ));
        }
    }

    @SubscribeEvent
    public void onButtonClick(final GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (!event.button.enabled) {
            return;
        }

        switch (event.button.id) {
            case 210:

                // Opening main mod gui
                if (event.gui instanceof GuiOptions) {
                    Minecraft.getMinecraft().displayGuiScreen(modMenu = new GuiModMenu(event.gui));
                }

                break;
        }

    }

    @SubscribeEvent
    public void onButtonClickPre(final GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.button instanceof GuiButtonDefault) {
            ((GuiButtonDefault) event.button).press();
        }
    }

}