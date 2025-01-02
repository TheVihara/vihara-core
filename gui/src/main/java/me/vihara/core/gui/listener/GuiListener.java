package me.vihara.core.gui.listener;

import lombok.NonNull;
import me.vihara.core.WrappedListener;
import me.vihara.core.WrappedPlugin;
import me.vihara.core.gui.Gui;
import me.vihara.core.gui.MultiGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;

public class GuiListener extends WrappedListener {
    public GuiListener(final @NonNull WrappedPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Gui gui = parseGui(event.getInventory());

        if (gui == null) {
            return;
        }

        gui.onOpen();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Gui gui = parseGui(event.getInventory());

        if (gui == null) {
            return;
        }

        gui.onClose();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Gui gui = parseGui(event.getInventory());

        if (gui == null) {
            return;
        }

        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory instanceof PlayerInventory
                && gui instanceof MultiGui) {
            MultiGui multiGui = (MultiGui) gui;
            multiGui.handleClick(event);
            return;
        }

        gui.handleClick(event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Gui gui = parseGui(event.getInventory());

        if (gui == null) {
            return;
        }

        gui.handleDrag(event);
    }

    private static Gui parseGui(Inventory inventory) {
        InventoryHolder holder = inventory != null ? inventory.getHolder() : null;

        if (!(holder instanceof Gui)) {
            return null;
        }

        return ((Gui) holder);
    }
}
