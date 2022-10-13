package club.mineplex.clans.client.modules;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.util.UtilText;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public abstract class ModCommand extends CommandBase {

    private final String name;
    private final String usage;
    private boolean mineplexOnly = false;

    protected ModCommand(final String name, final String usage) {
        this.name = name;
        this.usage = usage;
    }

    abstract void run(ICommandSender sender, String[] args);

    @Override
    public String getCommandName() {
        return this.name;
    }

    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return this.usage;
    }

    @Override
    public boolean canCommandSenderUseCommand(final ICommandSender sender) {
        return true;
    }

    protected void setMineplexOnly(final boolean mineplexOnly) {
        this.mineplexOnly = mineplexOnly;
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args) {

        if (this.mineplexOnly && !ClansMod.getInstance().getGameState().isMineplex()) {
            UtilText.sendPlayerMessageWithPrefix("Error", "This command only works in Mineplex!");
            return;
        }

        this.run(sender, args);

    }

}
