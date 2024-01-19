package br.com.backpacks.events.custom;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BackpackCookItemEvent extends Event implements Cancellable {

    public void setResult(ItemStack result) {
        this.result = result;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;

    public ItemStack getSource() {
        return source;
    }

    private ItemStack source;

    public ItemStack getResult() {
        return result;
    }

    private ItemStack result;
    private ItemStack fuel;
    public BackpackCookItemEvent(@NotNull ItemStack source, @NotNull ItemStack result, @NotNull ItemStack fuel){
        this.fuel = fuel;
        this.source = source;
        this.result = result;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
