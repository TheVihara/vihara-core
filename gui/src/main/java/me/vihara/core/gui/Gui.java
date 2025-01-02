package me.vihara.core.gui;

import lombok.NonNull;
import me.vihara.core.HeartBeat;
import me.vihara.core.gui.type.ClickAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class Gui implements InventoryHolder {
    ClickAction[] actions;
    protected Inventory inventory;
    protected Player player;
    int rows;

    public Gui(final @NonNull Player player, final @NonNull String title, final int rows) {
        this.player = player;
        this.rows = rows;
        this.actions = new ClickAction[rows * 9];
        this.inventory = Bukkit.createInventory(this, rows * 9, title);

        this.open();

        HeartBeat.runSync(() -> {
            if (isOpened()) {
                player.updateInventory();
            }
        });
    }

    public final void open() {
        player.openInventory(inventory);
    }

    protected abstract void draw();
    protected abstract void refresh();
    public abstract void onOpen();
    public abstract void onClose();

    public void handleClick(final @NonNull InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getClickedInventory().getHolder() != this) {
            return;
        }

        ClickAction action = actions[event.getRawSlot()];
        if (action != null) {
            action.execute(event);
        }
    }

    public void handleDrag(final @NonNull InventoryDragEvent event) {
        event.setCancelled(true);
    }

    public final boolean isOpened() {
        return player.getOpenInventory().getTopInventory().getHolder() == this;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
