package br.com.backpacks.upgrades;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface GetAutoFeed {

    List<ItemStack> getAutoFeedItems();

    Boolean isAutoFeedEnabled();

    void setAutoFeedEnabled(Boolean bool);

    void setAutoFeedItems(List<ItemStack> items);

}
