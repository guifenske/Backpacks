package br.com.Backpacks.menus;

import br.com.Backpacks.Main;
import de.tr7zw.nbtapi.NBTItem;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class backpackConfig implements Listener {

    private static HashMap<Player, Integer> currentUuidBackpack = new HashMap<>();
    private static HashMap<Player, String> currentTypeBackpack = new HashMap<>();
    private static HashMap<Player, UUID> currentOwnerUuid = new HashMap<>();
    private static ConcurrentHashMap<Player, File> currentYaml = new ConcurrentHashMap<>();

    public static Inventory createConfigBackpack(Player player, Integer uuidback, String type, UUID uuid){
        currentTypeBackpack.put(player, type);
        currentUuidBackpack.put(player, uuidback);
        currentOwnerUuid.put(player, uuid);

        ItemStack nameItem = new ItemStack(Material.NAME_TAG);
        ItemMeta nameMeta = nameItem.getItemMeta();
        nameMeta.setDisplayName("Rename Backpack");
        nameItem.setItemMeta(nameMeta);

        ItemStack upgradesItem = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta upgradesMeta = upgradesItem.getItemMeta();
        upgradesMeta.setDisplayName("Backpack Upgrades");
        upgradesItem.setItemMeta(upgradesMeta);

        ItemStack open = new ItemStack(Material.GREEN_WOOL);
        ItemMeta openMeta = open.getItemMeta();
        openMeta.setDisplayName("Opened");
        open.setItemMeta(openMeta);

        ItemStack lock = new ItemStack(Material.RED_WOOL);
        ItemMeta lockMeta = lock.getItemMeta();
        lockMeta.setDisplayName("Locked");
        lock.setItemMeta(lockMeta);

        Inventory inventory = Bukkit.createInventory(player, 54, "Configuration of " + Bukkit.getPlayer(uuid).getName());

        inventory.setItem(22, nameItem);
        inventory.setItem(31, upgradesItem);

        File playerfile = new File(Main.back.getDataFolder() + "/" + uuid + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerfile);

        currentYaml.put(player, playerfile);

        if(config.getBoolean("config." + uuidback + ".isOpen")) inventory.setItem(24, open);
        else inventory.setItem(24, lock);

        return inventory;
    }

    private static int getMaxUpgradesPerType(String type) {
        return switch (type) {
            case "is_leatherbackpack" -> 1;
            case "is_goldbackpack" -> 3;
            case "is_ironbackpack" -> 4;
            case "is_lapisbackpack" -> 5;
            case "is_amethystbackpack" -> 6;
            case "is_diamondbackpack" -> 7;
            case "is_netheritebackpack" -> 9;
            default -> 0;
        };

    }

    private static Inventory upgradesMenu(Player player){
        Inventory inventory = Bukkit.createInventory(player, 9, "Backpack Upgrades");

        isInConfigSection.put(player, true);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName("This slot is blocked, try upgrading your backpack");
        barrier.setItemMeta(barrierMeta);

        FileConfiguration config = YamlConfiguration.loadConfiguration(currentYaml.get(player));

        if(getMaxUpgradesPerType(currentTypeBackpack.get(player)) != 0)    for (int i = 0; i < 9 - getMaxUpgradesPerType(currentTypeBackpack.get(player)); i++)   inventory.setItem(i, barrier);

        if(!config.isSet("config." + currentUuidBackpack.get(player) + ".upgrades")) return inventory;

        Set<String> list = config.getConfigurationSection("config." + currentUuidBackpack.get(player) + ".upgrades").getKeys(false);

        for (String s : list)  inventory.setItem(Integer.parseInt(s), config.getItemStack("config." + currentUuidBackpack.get(player) + ".upgrades." + s));


        return inventory;
    }

    private static HashMap<Player, Boolean> isInConfigSection = new HashMap<>();

    @EventHandler
    private void onClickConfigEvent(InventoryClickEvent event) {
        if(!currentOwnerUuid.containsKey((Player) event.getWhoClicked())) return;
        if(event.getClickedInventory() == null) return;
        if(event.getCurrentItem() == null) return;
        if(!event.getClickedInventory().getType().equals(InventoryType.CHEST)) return;

        if(event.getSlot() == 24){
            event.setCancelled(true);
            FileConfiguration config = YamlConfiguration.loadConfiguration(currentYaml.get((Player) event.getWhoClicked()));

            Boolean setar;

            if(event.getClickedInventory().getItem(24).getType().equals(Material.GREEN_WOOL)){
                config.set("config." + currentUuidBackpack.get((Player) event.getWhoClicked()) + ".isOpen", false);
                setar = false;
            }   else{
                config.set("config." + currentUuidBackpack.get((Player) event.getWhoClicked()) + ".isOpen", true);
                setar = true;
            }

            Main.back.saveConfigAsync(config, currentYaml.get((Player) event.getWhoClicked()));

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(setar){
                        ItemStack open = new ItemStack(Material.GREEN_WOOL);
                        ItemMeta openMeta = open.getItemMeta();
                        openMeta.setDisplayName("Opened");
                        open.setItemMeta(openMeta);

                        event.getClickedInventory().setItem(24, open);
                    }   else {
                        ItemStack lock = new ItemStack(Material.RED_WOOL);
                        ItemMeta lockMeta = lock.getItemMeta();
                        lockMeta.setDisplayName("Locked");
                        lock.setItemMeta(lockMeta);

                        event.getClickedInventory().setItem(24, lock);
                    }
                }
            }.runTaskLater(Main.back, 1L);
        } else if (event.getSlot() == 22) {
            event.setCancelled(true);
            anvil((Player) event.getWhoClicked());
        }   else if(event.getSlot() == 31) {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().openInventory(upgradesMenu((Player) event.getWhoClicked()));
        }
    }

    @EventHandler
    private void onCloseUpgradesMenu(InventoryCloseEvent event) {
        if(!currentOwnerUuid.containsKey(event.getPlayer())) return;
        if(!isInConfigSection.containsKey(event.getPlayer())) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(currentYaml.get(event.getPlayer()));
        for(int i = 0; i < event.getInventory().getSize(); i++){
            if(event.getInventory().getItem(i) == null){
                config.set("config." + currentUuidBackpack.get((Player) event.getPlayer()) + ".upgrades." + i, null);
                continue;
            }

            if(event.getInventory().getItem(i).getType() == Material.BARRIER) continue;

            NBTItem item = new NBTItem(event.getInventory().getItem(i));
            if(!item.hasKey("backpack_tags")) continue;
            if(!item.getCompound("backpack_tags").hasTag("upgrade")) continue;

            config.set("config." + currentUuidBackpack.get((Player) event.getPlayer()) + ".upgrades." + i, event.getInventory().getItem(i));
        }

        Main.back.saveConfigAsync(config, currentYaml.get((Player) event.getPlayer()));

        isInConfigSection.remove(event.getPlayer());
    }

    private void anvil(Player player){

        FileConfiguration config = YamlConfiguration.loadConfiguration(currentYaml.get(player));

        new AnvilGUI.Builder()
                .onClose(stateSnapshot -> {
                    currentUuidBackpack.remove(player);
                    currentTypeBackpack.remove(player);
                    currentOwnerUuid.remove(player);
                    isInConfigSection.remove(player);
                    currentYaml.remove(player);
                })

                .onClick((slot, stateSnapshot) -> { // Either use sync or async variant, not both
                    if(slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();

                    config.set("config." + currentUuidBackpack.get(player) + ".name", stateSnapshot.getText());

                    if(backpackMenu.actiontypeback.get(player).equals("chestplate")){
                        ItemStack item = player.getInventory().getChestplate();
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(stateSnapshot.getText());
                        item.setItemMeta(meta);

                        player.getInventory().setChestplate(item);
                    }   else{
                        ItemStack item;

                        if(backpackMenu.handUsed.get(player).equals("HAND")){
                            item = player.getInventory().getItemInMainHand();
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(stateSnapshot.getText());
                            item.setItemMeta(meta);

                            player.getInventory().setItemInMainHand(item);
                        }
                        else{
                            item = player.getInventory().getItemInOffHand();
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(stateSnapshot.getText());
                            item.setItemMeta(meta);

                            player.getInventory().setItemInOffHand(item);
                        }

                        backpackMenu.handUsed.remove(player);
                        backpackMenu.actiontypeback.remove(player);

                    }

                    Main.back.saveConfigAsync(config, currentYaml.get(player));

                    return Arrays.asList(AnvilGUI.ResponseAction.close());

                })
                .text("Type here the new name")
                .title("Enter the new name of your backpack.")
                .plugin(Main.back)
                .open(player);
    }
}
