package me.vihara.core.gui;

import lombok.NonNull;
import me.vihara.core.WrappedPlugin;
import me.vihara.core.feature.FeatureInfo;
import me.vihara.core.feature.Feature;
import me.vihara.core.gui.listener.GuiListener;
import me.vihara.core.gui.manager.GuiManager;

@FeatureInfo(lazy = true)
public class GuiFeature extends Feature {
    public GuiFeature(@NonNull WrappedPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        new GuiListener(plugin);
        GuiManager.INSTANCE.bootstrap();
    }

    @Override
    public void onDisable() {

    }
}
