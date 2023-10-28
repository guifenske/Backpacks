package br.com.Backpacks.events;

import br.com.Backpacks.Main;
import br.com.Backpacks.crafting.recipesUtil;
import br.com.Backpacks.menus.backpackMenu;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.Set;

public class playerEvents implements Listener {
    @EventHandler
    public void onUnlockChest(PlayerRecipeDiscoverEvent e) {
        switch (e.getRecipe().getKey()){
            case "leather_chestplate":
                e.getPlayer().discoverRecipe(recipesUtil.leatherchestkey);
                break;
            case "gold_chestplate":
                e.getPlayer().discoverRecipe(recipesUtil.goldChestKey);
                break;
            case "iron_chestplate":
                e.getPlayer().discoverRecipe(recipesUtil.ironChestKey);
                break;
            case "diamond_chestplate":
                e.getPlayer().discoverRecipe(recipesUtil.diamondChestkey);
                break;
            case "netherite_chestplate":
                e.getPlayer().discoverRecipe(recipesUtil.netheriteChestkey);
                break;
            case "chest":
                e.getPlayer().discoverRecipe(recipesUtil.leatherkey);
                e.getPlayer().discoverRecipe(recipesUtil.upgraderkey);
                e.getPlayer().discoverRecipe(recipesUtil.leatherUpgraderKey);
                e.getPlayer().discoverRecipe(recipesUtil.goldUpgraderKey);
                e.getPlayer().discoverRecipe(recipesUtil.ironUpgraderKey);
                e.getPlayer().discoverRecipe(recipesUtil.lapisUpgraderKey);
                e.getPlayer().discoverRecipe(recipesUtil.amethystUpgraderKey);
                break;
        }

       }

    @EventHandler
    public void onCraftBack(CraftItemEvent event) {
        if (event.getRecipe().getResult().equals(recipesUtil.checkBackRecipes(event.getRecipe().getResult()))) {
            if (event.getRecipe().getResult().equals(recipesUtil.leatherItem)) {
                ItemStack resultItem = event.getRecipe().getResult();
                NBTItem resultNbt = new NBTItem(resultItem);
                NBTCompound result = resultNbt.getCompound("backpack_tags");
                result.setUUID("ownerUuid", event.getWhoClicked().getUniqueId());
                File playerfile = new File(Main.back.getDataFolder() + "/" + event.getView().getPlayer().getUniqueId() + ".yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(playerfile);


                if(!config.isSet("backpacks")) {
                    config.set("backpacks", 1);
                    config.set("backpacks.1", backpackMenu.backpackType(result));
                    config.set("config.1.isOpen", true);
                    result.setInteger("uuidback", 1);

                }else{
                    if(!result.hasTag("uuidback")){
                        Set<String> lista = config.getConfigurationSection("backpacks").getKeys(false);
                        for (String index : lista) {
                            int indextrue = Integer.parseInt(index) + 1;
                            if (config.getString("backpacks." + indextrue) == null) {
                                config.set("backpacks." + indextrue, backpackMenu.backpackType(result));
                                config.set("config." + indextrue + ".isOpen", true);
                                result.setInteger("uuidback", indextrue);
                                break;
                            }
                        }
                    }
                }

               Main.back.saveConfigAsync(config, playerfile);

                event.getInventory().setResult(resultNbt.getItem());
                return;
            }
            for (ItemStack item : event.getInventory().getMatrix()) {
                if (item != null) {
                    if (item.getType().equals(Material.CHEST)) {
                        NBTItem nbtItem = new NBTItem(item);
                        if (nbtItem.hasKey("backpack_tags")) {
                            NBTCompound compound = nbtItem.getCompound("backpack_tags");
                            if (compound.hasTag("uuidback")) {
                                File playerfile = new File(Main.back.getDataFolder() + "/" + event.getView().getPlayer().getUniqueId() + ".yml");
                                FileConfiguration config = YamlConfiguration.loadConfiguration(playerfile);
                                ItemStack resultItem = event.getRecipe().getResult();
                                NBTItem resultNbt = new NBTItem(resultItem);
                                NBTCompound result = resultNbt.getCompound("backpack_tags");
                                result.setInteger("uuidback", compound.getInteger("uuidback"));
                                result.setUUID("ownerUuid", compound.getUUID("ownerUuid"));

                                Set<String> list = null;
                                Set<String> list2 = null;

                                if(config.getConfigurationSection("backpacks." + compound.getInteger("uuidback") + "." + backpackMenu.backpackType(compound) + ".0") != null) {
                                    list = config.getConfigurationSection("backpacks." + compound.getInteger("uuidback") + "." + backpackMenu.backpackType(compound) + ".0").getKeys(false);
                                }

                                if(config.getConfigurationSection("backpacks." + compound.getInteger("uuidback") + "." + backpackMenu.backpackType(compound) + ".1") != null) {
                                    list2 = config.getConfigurationSection("backpacks." + compound.getInteger("uuidback") + "." + backpackMenu.backpackType(compound) + ".1").getKeys(false);
                                }

                                if(list != null) {
                                    ItemStack[] items = new ItemStack[list.size()];
                                    for (int i = 0; i < list.size(); i++)
                                        items[i] = config.getItemStack("backpacks." + compound.getInteger("uuidback") + "." + backpackMenu.backpackType(compound) + ".0." + i);

                                    config.set("backpacks." + compound.getInteger("uuidback"), null);

                                    for (int i = 0; i < items.length; i++)
                                        config.set("backpacks." + compound.getInteger("uuidback") + "." + backpackMenu.backpackType(result) + ".0." + i, items[i]);

                                    config.set("config." + compound.getInteger("uuidback") + ".isOpen", true);
                                    result.setInteger("uuidback", compound.getInteger("uuidback"));
                                }

                                if(list2 != null) {
                                    ItemStack[] items2 = new ItemStack[list2.size()];
                                    for (int i = 0; i < list2.size(); i++)
                                        items2[i] = config.getItemStack("backpacks." + compound.getInteger("uuidback") + "." + backpackMenu.backpackType(compound) + ".1." + i);

                                    config.set("backpacks." + compound.getInteger("uuidback"), null);

                                    for (int i = 0; i < items2.length; i++)
                                        config.set("backpacks." + compound.getInteger("uuidback") + "." + backpackMenu.backpackType(result) + ".1." + i, items2[i]);

                                    config.set("config." + compound.getInteger("uuidback") + ".isOpen", true);
                                    result.setInteger("uuidback", compound.getInteger("uuidback"));
                                }

                               Main.back.saveConfigAsync(config, playerfile);

                                event.getInventory().setResult(resultNbt.getItem());
                                break;
                            }
                        } else {
                            event.getWhoClicked().sendMessage("§cYou don't have a backpack!");
                            event.setCancelled(true);
                            break;
                        }
                    }
                }
            }

        } else if (event.getRecipe().getResult().equals(recipesUtil.checkChestBack(event.getRecipe().getResult()))) {
            for (ItemStack item : event.getInventory().getMatrix()) {
                if (item != null) {
                    if (item.getType().equals(Material.CHEST)) {
                        NBTItem nbtItem = new NBTItem(item);
                        if (nbtItem.hasKey("backpack_tags")) {
                                NBTCompound compound = nbtItem.getCompound("backpack_tags");
                                ItemStack resultItem = event.getRecipe().getResult();
                                NBTItem resultNbt = new NBTItem(resultItem);
                                NBTCompound result = resultNbt.getCompound("backpack_tags");
                                File playerfile = new File(Main.back.getDataFolder() + "/" + event.getView().getPlayer().getUniqueId() + ".yml");
                                FileConfiguration config = YamlConfiguration.loadConfiguration(playerfile);

                            if(!config.isSet("backpacks")) {
                                config.set("backpacks", 1);
                                config.set("backpacks.1", backpackMenu.backpackType(compound));
                                config.set("config.1.isOpen", true);
                                result.setInteger("uuidback", 1);

                            }   else{
                                if(!compound.hasTag("uuidback")){;
                                    Set<String> lista = config.getConfigurationSection("backpacks").getKeys(false);
                                    for (String index : lista) {
                                        int indextrue = Integer.parseInt(index) + 1;
                                        if (config.getString("backpacks." + indextrue) == null) {
                                            config.set("backpacks." + indextrue, backpackMenu.backpackType(compound));
                                            config.set("config." + indextrue + ".isOpen", true);
                                            result.setInteger("uuidback", indextrue);
                                            break;
                                        }
                                    }
                                }
                                else if(compound.hasTag("uuidback"))  result.setInteger("uuidback", compound.getInteger("uuidback"));
                            }

                            Main.back.saveConfigAsync(config, playerfile);

                                result.setUUID("ownerUuid", compound.getUUID("ownerUuid"));
                                result.setBoolean(backpackMenu.backpackType(compound), true);

                                event.getInventory().setResult(resultNbt.getItem());

                        } else {
                            event.getWhoClicked().sendMessage("§cYou don't have a backpack!");
                            event.setCancelled(true);
                        }

                        break;
                    }   else if(recipesUtil.checkArmor(item) != null){
                        NBTItem nbtItem = new NBTItem(item);
                        if (nbtItem.hasKey("backpack_tags")) {
                            event.getWhoClicked().sendMessage("§cHey, you cannot put a BackChestplate!");
                            event.setCancelled(true);
                            break;
                        }
                    }
                }
            }
        }   else if(event.getRecipe().getResult().equals(recipesUtil.checkUpgrader(event.getRecipe().getResult()))){
            for (ItemStack item : event.getInventory().getMatrix()) {
                if (item != null) {
                    if (item.getType().equals(Material.CHEST)) {
                        NBTItem nbtItem = new NBTItem(item);
                        if (nbtItem.hasKey("backpack_tags")) {
                            event.setCancelled(true);
                            event.getWhoClicked().sendMessage("§cTake care, you would lose your backpack :(");
                            break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void InteractWithOthersBackpack(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Player)) return;


        Player playerLookedAt = (Player) e.getRightClicked();
        if (playerLookedAt.getInventory().getChestplate() == null) return;


        NBTItem item = new NBTItem(playerLookedAt.getInventory().getChestplate());

        if (!item.hasKey("backpack_tags")) return;


        NBTCompound list = item.getCompound("backpack_tags");

        File filePlayer = new File(Main.back.getDataFolder() + "/" + list.getUUID("ownerUuid") + ".yml");
        FileConfiguration configPlayer = YamlConfiguration.loadConfiguration(filePlayer);

        if (!configPlayer.getBoolean("config." + list.getInteger("uuidback") + ".isOpen")) return;

        Player player = e.getPlayer();

        /*
        float teste = player.getBodyYaw();
        float teste2 = playerLookedAt.getBodyYaw();
         */

        Vector playerInteractedDirection = player.getEyeLocation().getDirection();
        Vector playerLookedAtDirection = playerLookedAt.getEyeLocation().getDirection();

        double angleTolerance = Math.toRadians(70);

        double angle = playerInteractedDirection.angle(playerLookedAtDirection);
        if (angle < angleTolerance) player.openInventory(backpackMenu.createBackpack(player, backpackMenu.backpackName(list), list.getInteger("uuidback"), backpackMenu.backpackType(list), list.getUUID("ownerUuid")));

    }

    @EventHandler
    private void UncraftItem(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;

        if (e.getView().getTopInventory().getType().equals(InventoryType.DROPPER)){

            if (e.getView().getOriginalTitle().equals("Uncrafter of ChestBackPack")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ItemStack itemStack = null;

                        for (int i = 0; i < e.getView().getTopInventory().getSize(); i++) {

                            if (e.getView().getTopInventory().getItem(i) == null) continue;

                            if (recipesUtil.checkArmor(e.getView().getTopInventory().getItem(i)) == null)     continue;

                            itemStack = e.getView().getTopInventory().getItem(i);
                            break;
                        }

                        if (itemStack == null) return;
                        NBTItem nbtItem = new NBTItem(itemStack);
                        if(!nbtItem.hasKey("backpack_tags")) return;

                        ItemStack itemStack1 = new ItemStack(itemStack.getType());
                        NBTItem nbtItem1 = new NBTItem(new ItemStack(Material.CHEST));

                        ItemMeta meta = nbtItem1.getItem().getItemMeta();
                        meta.setDisplayName(backpackMenu.backpackName(nbtItem.getCompound("backpack_tags")));
                        nbtItem1.getItem().setItemMeta(meta);

                        e.getView().getTopInventory().clear();
                        e.getView().getTopInventory().setItem(1, itemStack1);


                        nbtItem1.addCompound("backpack_tags");
                        nbtItem1.getCompound("backpack_tags").setUUID("ownerUuid", nbtItem.getCompound("backpack_tags").getUUID("ownerUuid"));

                        if (nbtItem.getCompound("backpack_tags").hasTag("uuidback"))    nbtItem1.getCompound("backpack_tags").setInteger("uuidback", nbtItem.getCompound("backpack_tags").getInteger("uuidback"));

                        nbtItem1.getCompound("backpack_tags").setBoolean(backpackMenu.backpackType(nbtItem.getCompound("backpack_tags")), true);

                        e.getView().getTopInventory().setItem(4, nbtItem1.getItem());
                    }
                }.runTaskLater(Main.back, 1L);
            }
        }
    }

    @EventHandler
   private void smithing(PrepareSmithingEvent event){
        if(event.getInventory().getInputEquipment() == null || event.getInventory().getInputMineral() == null) return;
        if(event.getInventory().getInputEquipment().getType().equals(Material.DIAMOND_CHESTPLATE)) {
            if(event.getInventory().getInputTemplate() == null) return;

            NBTItem nbtItem = new NBTItem(event.getInventory().getInputEquipment());
            if(!nbtItem.hasKey("backpack_tags")) return;

            NBTCompound compound = nbtItem.getCompound("backpack_tags");
            ItemStack backpack = recipesUtil.netheriteChestItem;
            NBTItem resultNbt = new NBTItem(backpack);

            resultNbt.getCompound("backpack_tags").setUUID("ownerUuid", compound.getUUID("ownerUuid"));
            resultNbt.getCompound("backpack_tags").setInteger("uuidback", compound.getInteger("uuidback"));
            resultNbt.getCompound("backpack_tags").setBoolean(backpackMenu.backpackType(compound), true);
            event.setResult(resultNbt.getItem());
        }
    }

    @EventHandler
    private void onBreakChestplate(PlayerItemBreakEvent e) {
       if(recipesUtil.checkArmor(e.getBrokenItem())==null) return;
       NBTItem nbtItem = new NBTItem(e.getBrokenItem());
       if(!nbtItem.hasKey("backpack_tags")) return;
       NBTCompound compound = nbtItem.getCompound("backpack_tags");
       ItemStack backpack = new ItemStack(Material.CHEST);
       ItemMeta meta = backpack.getItemMeta();
       meta.setDisplayName(backpackMenu.backpackName(compound));
       backpack.setItemMeta(meta);

       NBTItem resultNbt = new NBTItem(backpack);
       resultNbt.addCompound("backpack_tags");
       resultNbt.getCompound("backpack_tags").setUUID("ownerUuid", compound.getUUID("ownerUuid"));
       resultNbt.getCompound("backpack_tags").setInteger("uuidback", compound.getInteger("uuidback"));
       resultNbt.getCompound("backpack_tags").setBoolean(backpackMenu.backpackType(compound), true);

        if(e.getPlayer().getInventory().firstEmpty() != -1){
            e.getPlayer().sendMessage(ChatColor.RED + "Your chestplate has been broken, so your backpack is in your inventory.");
            e.getPlayer().getInventory().addItem(resultNbt.getItem());
            return;
        }

        e.getPlayer().sendMessage(ChatColor.RED + "You don't have free space in your inventory, so your backpack has been dropped.");
        e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), resultNbt.getItem());
    }
}