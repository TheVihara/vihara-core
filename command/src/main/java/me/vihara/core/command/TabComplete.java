package me.vihara.core.command;

import java.util.Collections;
import java.util.List;

public interface TabComplete {
    TabCompleter[] getTabCompletes();

    default List<String> tabComplete(String[] args) {
        TabCompleter[] tabCompletes = getTabCompletes();

        if (args.length > tabCompletes.length) {
            return Collections.emptyList();
        }
        return tabCompletes[args.length].tabComplete(args);
    }
}
