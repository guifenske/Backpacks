package br.com.Backpacks.menus;

import br.com.Backpacks.Main;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class backpackMenu implements Listener {

    public static HashMap<Player, String> actiontypeback = new HashMap<>();
    public static HashMap<Player, String> handUsed = new HashMap<>();

    private static HashMap<Player, Integer> currentUuidBackpack = new HashMap<>();
    private static HashMap<Player, Integer> currentSizeBackpack = new HashMap<>();
    private static HashMap<Player, String> currentTypeBackpack = new HashMap<>();
    private static HashMap<Player, String> currentNameBackpack = new HashMap<>();
    private static HashMap<Player, UUID> currentOwnerUuid = new HashMap<>();
    private static HashMap<Player, Integer> currentMultiplier = new HashMap<>();

    private static ConcurrentHashMap<Player, File> currentYaml = new ConcurrentHashMap<>();

    private static HashMap<Player, Integer> clickInArrows = new HashMap<>();



    @EventHandler
    private void onClickEvent(InventoryClickEvent event) {
        if (!currentOwnerUuid.containsKey((Player) event.getWhoClicked())) return;
        if (event.getClickedInventory() == null) return;
        if (!event.getInventory().getType().equals(InventoryType.CHEST)) return;

        Player player = (Player) event.getWhoClicked();

        if (event.getRawSlot() == currentSizeBackpack.get(player) - 1) {
            event.getWhoClicked().openInventory(backpackConfig.createConfigBackpack(player,
                    currentUuidBackpack.get(player),
                    currentTypeBackpack.get(player),
                    currentOwnerUuid.get(player)));
            return;
        }

        if (currentSizeBackpack.get(player) == 54 || secondPageSize.containsKey(player)) {
            if (event.getRawSlot() == currentSizeBackpack.get(player) - 2) {
                switch (currentPage.get(player)) {
                    case 0:
                        clickInArrows.put(player, 1);
                        player.closeInventory();
                        player.openInventory(secondPage(player));
                        clickInArrows.remove(player);
                        break;
                    case 1:
                        clickInArrows.put(player, 1);
                        player.closeInventory();
                        player.openInventory(createBackpack(player,
                                currentNameBackpack.get(player),
                                currentUuidBackpack.get(player),
                                currentTypeBackpack.get(player),
                                currentOwnerUuid.get(player)));
                        clickInArrows.remove(player);
                        break;
                }
                return;
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = (Player) event.getWhoClicked();

                HashMap<ItemStack, Integer> quantity = new HashMap<>();

                //itera sobre os itens, faz a soma total de cada item por tipo;
                for(int i = 0; i < event.getInventory().getSize() - useless_Slots(player); i++) {
                    ItemStack itemStack = event.getInventory().getItem(i);

                    if(itemStack == null) continue;
                    int amount = itemStack.getAmount();

                    if(quantity.containsKey(get_itemStack_wtht_lore(itemStack))) {
                        if(itemStack.getItemMeta().hasLore()) {
                            quantity.put(get_itemStack_wtht_lore(itemStack), quantity.get(get_itemStack_wtht_lore(itemStack)) + check_lore(itemStack));
                            event.getWhoClicked().sendMessage("1. " + quantity.get(get_itemStack_wtht_lore(itemStack)).toString());
                            event.getInventory().setItem(i, null);
                            continue;
                        }

                        quantity.put(get_itemStack_wtht_lore(itemStack), quantity.get(get_itemStack_wtht_lore(itemStack)) + amount);
                        event.getInventory().setItem(i, null);
                        continue;
                    }

                    if(itemStack.getItemMeta().hasLore()) {
                        quantity.put(get_itemStack_wtht_lore(itemStack), check_lore(itemStack));
                        event.getInventory().setItem(i, null);
                        continue;
                    }

                    quantity.put(get_itemStack_wtht_lore(itemStack), amount);
                    event.getWhoClicked().sendMessage(quantity.get(get_itemStack_wtht_lore(itemStack)).toString());
                    event.getInventory().setItem(i, null);
                }

                HashMap<ItemStack, Integer> times_toApply = new HashMap<>();
                HashMap<ItemStack, Integer> restoStack = new HashMap<>();

                for(ItemStack item : quantity.keySet()) {
                    if(quantity.get(item) > item.getMaxStackSize() * currentMultiplier.get(player)) {
                        times_toApply.put(item, quantity.get(item) / (item.getMaxStackSize() * currentMultiplier.get(player)));

                        int resto = quantity.get(item) % (item.getMaxStackSize() * currentMultiplier.get(player));
                        if(resto != 0)  restoStack.put(item, resto);

                    }   else times_toApply.put(item, 1);
                }

                int index = 0;

                for(ItemStack item : times_toApply.keySet()){
                    int times_to_apply = times_toApply.get(item);
                    int resto = 0;
                    if(restoStack.containsKey(item)) resto = restoStack.get(item);
                    if(item.getItemMeta().hasLore()) {
                        if(quantity.get(item) <= item.getMaxStackSize() * currentMultiplier.get(player)) item.getItemMeta().getLore().add("Total: " + quantity.get(item));
                        else item.getItemMeta().getLore().add("Total: " + item.getMaxStackSize() * currentMultiplier.get(player));
                    }   else{
                        ItemMeta itemMeta = item.getItemMeta();
                        List<String> lore = new ArrayList<>();
                        if(quantity.get(item) <= item.getMaxStackSize() * currentMultiplier.get(player)) lore.add("Total: " + quantity.get(item));
                        else lore.add("Total: " + item.getMaxStackSize() * currentMultiplier.get(player));

                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                    }

                    for (int i = 0; i < times_to_apply; i++)   event.getInventory().setItem(index + i, item);
                    if(resto != 0) {
                        //o resto pode ser maior que 64, ent adicionar a lore quando necessário
                        item.setAmount(resto);
                        if(resto <= item.getMaxStackSize()) event.getInventory().setItem(index + times_to_apply, item);
                        else{
                            ItemMeta itemMeta = item.getItemMeta();
                            List<String> lore = new ArrayList<>();

                            lore.add("Total: " + resto);
                            event.getWhoClicked().sendMessage("tempo restante: " + resto);

                            itemMeta.setLore(lore);
                            item.setItemMeta(itemMeta);
                            event.getInventory().setItem(index + times_to_apply, item);
                        }
                        index += times_to_apply + 1;
                        continue;
                    }

                    index += times_to_apply;

                }

                quantity.clear();
                restoStack.clear();
                times_toApply.clear();

            }
        }.runTask(Main.back);
    }

    private int check_lore(ItemStack itemStack) {
        int _quantity_lore = 0;
        List<String> lore = itemStack.getItemMeta().getLore();

        for(String s : lore) {
            if(!s.contains("Total: ")) continue;

            String string = s.substring(s.indexOf("Total: "));
            StringBuilder _quantity_string = new StringBuilder();

            for(Character c : string.toCharArray())     if(Character.isDigit(c)) _quantity_string.append(c);

            _quantity_lore = Integer.parseInt(_quantity_string.toString());
            break;
        }

        return _quantity_lore;
    }

    private ItemStack get_itemStack_wtht_lore(ItemStack item){
        if(item.getItemMeta().hasLore()) {
            ItemMeta itemMeta = item.getItemMeta();
            List<String> lore = itemMeta.getLore();
            ItemStack itemStack = new ItemStack(item.getType());

            for (String s : lore) {
                if (!s.contains("Total: ")) continue;
                lore.remove(s);
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                break;
            }

            itemStack.setAmount(1);

            return itemStack.asOne();
        }
        item.setAmount(1);
        return item.asOne();
    }

    @EventHandler
    public void backpackCloseEvent(InventoryCloseEvent event) {
        if (!event.getInventory().getType().equals(InventoryType.CHEST)) return;
        if (!currentOwnerUuid.containsKey(event.getPlayer())) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(currentYaml.get(event.getPlayer()));
        Player player = (Player) event.getPlayer();

        for (int i = 0; i < event.getInventory().getSize() - useless_Slots(player); i++) {
            config.set("backpacks." + currentUuidBackpack.get(player) + "." + currentTypeBackpack.get(player) + "." + currentPage.get(player) + "." + i, event.getInventory().getItem(i));
        }

        Main.back.saveConfigAsync(config, currentYaml.get(player));

        if (!clickInArrows.containsKey(player)) {
            currentNameBackpack.remove(player);
            currentUuidBackpack.remove(player);
            currentTypeBackpack.remove(player);
            currentOwnerUuid.remove(player);
            secondPageSize.remove(player);
            currentPage.remove(player);
            currentYaml.remove(player);
            currentSizeBackpack.remove(player);
        }
    }

    private static void setItems(Player player, Inventory inventory, Integer uuidback) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(currentYaml.get(player));

        ItemStack[] items = new ItemStack[9];

        if(config.isSet("config." + uuidback + ".upgrades")){
            Set<String> lista = config.getConfigurationSection("config." + uuidback + ".upgrades").getKeys(false);

            for(String index : lista){
                NBTItem item = new NBTItem(config.getItemStack("config." + uuidback + ".upgrades." + index));

                if(item.getCompound("backpack_tags").hasTag("stackupgrader"))   items[Integer.parseInt(index)] = item.getItem();

            }
        }

        currentMultiplier.put(player, getStackMultiplier(items));

        if(config.isSet("backpacks." + uuidback + "." + currentTypeBackpack.get(player) + "." + currentPage.get(player))) {
            Set<String> lista = config.getConfigurationSection("backpacks." + uuidback + "." + currentTypeBackpack.get(player) + "." + currentPage.get(player)).getKeys(false);

            for (String index : lista) {
                   inventory.setItem(Integer.parseInt(index), config.getItemStack("backpacks." + uuidback + "." + currentTypeBackpack.get(player) + "." + currentPage.get(player) + "." + index));
              }
            }

        if(currentPage.get(player) == 0){
            if(getMaxSlotsPerType(currentTypeBackpack.get(player)) > 54){
                ItemStack right = new ItemStack(Material.ARROW);
                ItemMeta rightMeta = right.getItemMeta();
                rightMeta.setDisplayName("Next Page");
                right.setItemMeta(rightMeta);
                NBTItem nbtItem = new NBTItem(right);
                nbtItem.addCompound("backpack_tags");
                inventory.setItem(inventory.getSize() - 2, right);
            }
        }   else{
            ItemStack left = new ItemStack(Material.ARROW);
            ItemMeta leftMeta = left.getItemMeta();
            leftMeta.setDisplayName("Previous Page");
            left.setItemMeta(leftMeta);
            NBTItem nbtItem = new NBTItem(left);
            nbtItem.addCompound("backpack_tags");
            inventory.setItem(inventory.getSize() - 2, left);
        }


    }

    public static Inventory secondPage(Player player) {
        Inventory inventory;

        currentPage.put(player, 1);
        currentSizeBackpack.put(player, secondPageSize.get(player));

       inventory = Bukkit.createInventory(player, secondPageSize.get(player), currentNameBackpack.get(player));

        setItems(player, inventory, currentUuidBackpack.get(player));

        ItemStack configOptions = new ItemStack(Material.TARGET);
        ItemMeta configOptionsMeta = configOptions.getItemMeta();
        configOptionsMeta.setDisplayName("Backpack Config");
        configOptions.setItemMeta(configOptionsMeta);

        NBTItem nbtItem = new NBTItem(configOptions);
        nbtItem.addCompound("backpack_tags");

        inventory.setItem(inventory.getSize() - 1, configOptions);

        return inventory;
    }

    public static Inventory createBackpack(Player player, String name, Integer uuidback, String type, UUID uuid) {
        File playerfile = new File(Main.back.getDataFolder() + "/" + uuid + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerfile);

        currentYaml.put(player, playerfile);
        Inventory inventory;

        currentPage.put(player, 0);

        currentTypeBackpack.put(player, type);
        currentUuidBackpack.put(player, uuidback);
        currentOwnerUuid.put(player, uuid);

        int size;

        if(getMaxSlotsPerType(type) > 54){
            size = 54;
            secondPageSize.put(player, getMaxSlotsPerType(type) - 54);
        }   else{
            size = getMaxSlotsPerType(type);
        }


        currentSizeBackpack.put(player, size);

        if(currentNameBackpack.containsKey(player)){
            inventory = Bukkit.createInventory(player, size, currentNameBackpack.get(player));
        }   else{
            if(config.isSet("config." + uuidback + ".name")){
                inventory = Bukkit.createInventory(player, size, config.getString("config." + uuidback + ".name"));
                name = config.getString("config." + uuidback + ".name");
                currentNameBackpack.put(player, name);
            }   else{
                inventory = Bukkit.createInventory(player, size, name + " of " + Bukkit.getPlayer(uuid).getName());
                name = name + " of " + Bukkit.getPlayer(uuid).getName();
                currentNameBackpack.put(player, name);
            }
        }

        setItems(player, inventory, uuidback);

        ItemStack configOptions = new ItemStack(Material.TARGET);
        ItemMeta configOptionsMeta = configOptions.getItemMeta();
        configOptionsMeta.setDisplayName("Backpack Config");
        configOptions.setItemMeta(configOptionsMeta);

        NBTItem nbtItem = new NBTItem(configOptions);
        nbtItem.addCompound("backpack_tags");

        inventory.setItem(inventory.getSize() - 1, configOptions);


        return inventory;

    }

    private static int getMaxSlotsPerType(String type) {
        return switch (type) {
            case "is_leatherbackpack" -> 18;
            case "is_goldbackpack" -> 27;
            case "is_ironbackpack" -> 45;
            case "is_lapisbackpack" -> 54;
            case "is_amethystbackpack" -> 72;
            case "is_diamondbackpack" -> 81;
            case "is_netheritebackpack" -> 108;
            default -> 0;
        };
    }

    public static String backpackName(NBTCompound compound) {
        if(compound.getBoolean("is_leatherbackpack")) return "Leather Backpack";
        if(compound.getBoolean("is_goldbackpack")) return "Gold Backpack";
        if(compound.getBoolean("is_ironbackpack")) return "Iron Backpack";
        if(compound.getBoolean("is_lapisbackpack")) return "Lapis Backpack";
        if(compound.getBoolean("is_amethystbackpack")) return "Amethyst Backpack";
        if(compound.getBoolean("is_diamondbackpack")) return "Diamond Backpack";
        if(compound.getBoolean("is_netheritebackpack")) return "Netherite Backpack";

        return "Backpack";
    }

    public static String backpackType(NBTCompound compound) {
        if(compound.getBoolean("is_leatherbackpack")) return "is_leatherbackpack";
        if(compound.getBoolean("is_goldbackpack")) return "is_goldbackpack";
        if(compound.getBoolean("is_ironbackpack")) return "is_ironbackpack";
        if(compound.getBoolean("is_lapisbackpack")) return "is_lapisbackpack";
        if(compound.getBoolean("is_amethystbackpack")) return "is_amethystbackpack";
        if(compound.getBoolean("is_diamondbackpack")) return "is_diamondbackpack";
        if(compound.getBoolean("is_netheritebackpack")) return "is_netheritebackpack";

        return "";
    }

    private static int getStackMultiplier(ItemStack[] items) {
        int totalMultiplier = 1;

        for (ItemStack item : items) {
            if(item == null) continue;
            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.getCompound("backpack_tags").hasTag("ironstackupgrader")) totalMultiplier *= 2;
            if (nbtItem.getCompound("backpack_tags").hasTag("goldstackupgrader")) totalMultiplier *= 4;
            if (nbtItem.getCompound("backpack_tags").hasTag("diamondstackupgrader")) totalMultiplier *= 8;
        }

        return totalMultiplier;
    }

    private static int useless_Slots(Player player) {
        if(currentSizeBackpack.get(player) == 54 || secondPageSize.containsKey(player)) return 2;
        else return 1;
    }

    public static HashMap<Player, Integer> currentPage = new HashMap<>();
    public static HashMap<Player, Integer> secondPageSize = new HashMap<>();

}
