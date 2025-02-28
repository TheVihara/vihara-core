package me.vihara.core.command;

import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public abstract class CommandManager {
    ConcurrentHashMap<String, Command> commands = new ConcurrentHashMap<>();

    public abstract void onCommandAdd();
    public abstract void onCommandRemove();

    public void addCommand(final Command command) {
        commands.put(command.getName(), command);
        onCommandAdd();
    }

    public void removeCommand(final String commandName) {
        commands.remove(commandName);
        onCommandRemove();
    }

    public <T extends Command> T getCommand(final String commandName) {
        final Command command = commands.get(commandName);
        if (command != null) {
            return (T) command;
        }
        return null;
    }
}
