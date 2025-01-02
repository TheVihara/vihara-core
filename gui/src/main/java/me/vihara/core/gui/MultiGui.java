package me.vihara.core.gui;

import lombok.NonNull;
import me.vihara.core.gui.type.ClickAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class MultiGui extends Gui {
    private static final Map<UUID, ItemStack[]> storedInventories = new HashMap<>();
    protected final ClickAction[] playerInventoryActions;

    public MultiGui(@NonNull Player player, @NonNull String title, int rows) {
        super(player, title, rows);
        this.playerInventoryActions = new ClickAction[36]; // Player inventory size (excluding armor slots)
        storeAndClearPlayerInventory(player);
    }

    private void storeAndClearPlayerInventory(Player player) {
        ItemStack[] contents = player.getInventory().getContents().clone();
        storedInventories.put(player.getUniqueId(), contents);

        player.getInventory().clear();
        player.updateInventory();
    }

    @Override
    public void handleClick(@NonNull InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getClickedInventory() == null) {
            return;
        }

        if (event.getClickedInventory().getHolder() == this) {
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < actions.length && actions[slot] != null) {
                actions[slot].execute(event);
            }
        } else if (event.getClickedInventory().getHolder() == player.getInventory().getHolder()) {
            int slot = event.getSlot();
            if (slot >= 0 && slot < playerInventoryActions.length && playerInventoryActions[slot] != null) {
                playerInventoryActions[slot].execute(event);
            }
        }
    }

    @Override
    public void onClose() {
        restorePlayerInventory();
    }

    private void restorePlayerInventory() {
        ItemStack[] contents = storedInventories.remove(player.getUniqueId());
        if (contents != null) {
            player.getInventory().setContents(contents);
            player.updateInventory();
        }
    }

    protected void setPlayerInventoryAction(int slot, ClickAction action) {
        if (slot >= 0 && slot < playerInventoryActions.length) {
            playerInventoryActions[slot] = action;
        }
    }

    protected void setPlayerInventoryItem(int slot, ItemStack item, ClickAction action) {
        if (slot >= 0 && slot < playerInventoryActions.length) {
            player.getInventory().setItem(slot, item);
            playerInventoryActions[slot] = action;
        }
    }

    public static void cleanup(UUID playerUuid) {
        storedInventories.remove(playerUuid);
    }
}