package me.vihara.core.command;

import java.util.List;

public abstract class TabCompleter {
    public abstract List<String> tabComplete(String[] args);
}
