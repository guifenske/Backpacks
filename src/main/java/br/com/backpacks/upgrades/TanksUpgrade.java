package br.com.backpacks.upgrades;

import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class TanksUpgrade extends Upgrade {
    private Inventory inventory;
    public static List<Integer> blankSlots = List.of(0, 9, 18, 8, 17, 26, 14, 12);
    public Inventory getInventory() {
        return inventory;
    }

    public TanksUpgrade(int id) {
        super(UpgradeType.LIQUIDTANK, id);
        this.inventory = Bukkit.createInventory(null, 27, "Tanks Upgrade");
        updateInventory();
    }

    public void updateInventory(){
        ItemStack loremIpsum = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE," ").build();
        for(int i = 0; i < inventory.getSize(); i++){
            if(blankSlots.contains(i)) {
                inventory.setItem(i, null);
                continue;
            }
            inventory.setItem(i, loremIpsum);
        }
    }

    public HashMap<Integer, ItemStack> getItemsPerTank(int tank){
        HashMap<Integer, ItemStack> hashMap = new HashMap<>();
        if(tank == 1){
            hashMap.put(0, inventory.getItem(0));
            hashMap.put(9, inventory.getItem(9));
            hashMap.put(18, inventory.getItem(18));
            return hashMap;
        }
        if(tank == 2){
            hashMap.put(8, inventory.getItem(8));
            hashMap.put(17, inventory.getItem(17));
            hashMap.put(26, inventory.getItem(26));
            return hashMap;
        }
        return hashMap;
    }
    
    public void removeFirstLiquidFromTank(int tank){
        if(tank == 1){
            if(inventory.getItem(0) != null){
                ItemStack itemStack = inventory.getItem(0);
                inventory.setItem(12, itemStack);
                inventory.setItem(0, null);
                return;
            }
            if(inventory.getItem(9) != null){
                ItemStack itemStack = inventory.getItem(9);
                inventory.setItem(12, itemStack);
                inventory.setItem(9, null);
                return;
            }
            if(inventory.getItem(18) != null){
                ItemStack itemStack = inventory.getItem(18);
                inventory.setItem(12, itemStack);
                inventory.setItem(18, null);
                return;
            }
        }
        if(tank == 2){
            if(inventory.getItem(8) != null){
                ItemStack itemStack = inventory.getItem(8);
                inventory.setItem(14, itemStack);
                inventory.setItem(8, null);
                return;
            }
            if(inventory.getItem(17) != null){
                ItemStack itemStack = inventory.getItem(17);
                inventory.setItem(14, itemStack);
                inventory.setItem(17, null);
                return;
            }
            if(inventory.getItem(26) != null){
                ItemStack itemStack = inventory.getItem(26);
                inventory.setItem(14, itemStack);
                inventory.setItem(26, null);
                return;
            }
        }
    }

    public boolean canFillTank(int tank){
        if(tank == 1){
            if(inventory.getItem(0) == null) return true;
            if(inventory.getItem(9) == null) return true;
            if(inventory.getItem(18) == null) return true;
        }
        if(tank == 2){
            if(inventory.getItem(8) == null) return true;
            if(inventory.getItem(17) == null) return true;
            if(inventory.getItem(26) == null) return true;
        }
        return false;
    }

    public void addLiquidToTank(ItemStack item, int tank){
        if(tank == 1){
            if(inventory.getItem(0) == null){
                inventory.setItem(0, item);
                return;
            }
            if(inventory.getItem(9) == null){
                inventory.setItem(9, item);
                return;
            }
            if(inventory.getItem(18) == null){
                inventory.setItem(18, item);
                return;
            }
        }
        if(tank == 2){
            if(inventory.getItem(8) == null){
                inventory.setItem(8,item);
                return;
            }
            if(inventory.getItem(17) == null){
                inventory.setItem(17, item);
                return;
            }
            if(inventory.getItem(26) == null){
                inventory.setItem(26, item);
                return;
            }
        }
    }

}
