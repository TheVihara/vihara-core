package me.vihara.core;

import lombok.NonNull;
import org.bukkit.event.Listener;

public abstract class WrappedListener implements Listener {
    public WrappedListener(final @NonNull WrappedPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
