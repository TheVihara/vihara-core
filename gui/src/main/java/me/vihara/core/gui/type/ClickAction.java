package me.vihara.core.gui.type;

import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface ClickAction {
    void execute(final @NonNull InventoryClickEvent event);
}
