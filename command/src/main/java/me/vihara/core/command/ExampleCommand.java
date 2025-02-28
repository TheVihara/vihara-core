package me.vihara.core.command;

public class ExampleCommand extends Command implements TabComplete {
    public ExampleCommand() {
        super("example", "An example command!");
    }

    @Override
    public void execute(String[] args) {

    }

    @Override
    public TabCompleter[] getTabCompletes() {
        return null;
    }
}
