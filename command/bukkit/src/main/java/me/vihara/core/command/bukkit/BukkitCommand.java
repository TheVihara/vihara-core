package me.vihara.core.command.bukkit;

import me.vihara.core.command.Command;
import org.bukkit.command.CommandSender;

public abstract class BukkitCommand extends Command {
    public BukkitCommand(String name, String description) {
        super(name, description);
    }

    public abstract void execute(CommandSender sender, String[] args);

    @Override
    public void execute(String[] args) {

    }
}
