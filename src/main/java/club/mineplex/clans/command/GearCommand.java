package club.mineplex.clans.command;

import club.mineplex.clans.client.gui.repository.gearmenu.GearPage;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GearCommand extends CommandBase {

    private final GearPage page;

    public GearCommand() {
        page = new GearPage();
    }

    @Override
    public String getCommandName() {
        return "gear";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/gear";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        // TODO: fix /gear
//        Minecraft.getMinecraft().displayGuiScreen(page);
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/gear");
    }
}
