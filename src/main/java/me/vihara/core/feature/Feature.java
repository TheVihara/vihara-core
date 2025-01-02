package me.vihara.core.feature;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.vihara.core.WrappedPlugin;

@RequiredArgsConstructor
public abstract class Feature {
    protected final @NonNull WrappedPlugin plugin;

    public abstract void onLoad();
    public abstract void onEnable();
    public abstract void onDisable();
}
