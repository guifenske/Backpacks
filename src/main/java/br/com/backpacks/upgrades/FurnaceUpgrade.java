package br.com.backpacks.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.events.upgrades.Furnace;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeType;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.view.FurnaceView;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FurnaceUpgrade extends Upgrade {
    private org.bukkit.block.Furnace furnace;

    public FurnaceUpgrade(int id){
        super(UpgradeType.FURNACE, id);
    }

    @Override
    public boolean isAdvanced() {
        return true;
    }

    @Override
    public List<Integer> inputSlots() {
        return List.of(0, 1);
    }

    @Override
    public List<Integer> outputSlots() {
        return List.of(2);
    }

    @Override
    public void stopTicking() {
        if(furnace == null) return;
        furnace.getLocation().getBlock().setType(Material.AIR);
        furnace = null;
    }

    public org.bukkit.block.Furnace createFurnace(){
        Block block;

        int randomX;
        int randomY;
        int randomZ;

        int minY = -64;
        int chunkRadius = 1;

        Location location = new Location(Bukkit.getWorld(NamespacedKey.fromString("minecraft:overworld")), 0, 0, 0);
        World world = location.getWorld();

        while(true){
            for (int chX = -chunkRadius; chX <= chunkRadius; chX++) {
                for (int chZ = -chunkRadius; chZ <= chunkRadius; chZ++) {
                    Chunk chunk = new Location(location.getWorld(), location.x() + (chX * 16), 0, location.z() + (chZ * 16)).getChunk();

                    randomX = ThreadLocalRandom.current().nextInt(0, 15);
                    randomZ = ThreadLocalRandom.current().nextInt(0, 15);

                    block = chunk.getBlock(randomX, 0, randomZ);

                    int maxY = world.getHighestBlockYAt(block.getX(), block.getZ());
                    randomY = ThreadLocalRandom.current().nextInt(minY, maxY);

                    block = chunk.getBlock(randomX, randomY, randomZ);

                    if(block.getType().isSolid()) {
                        block.setType(Material.FURNACE);
                        return (org.bukkit.block.Furnace) block.getState();
                    }
                }
            }
        }
    }

    public Inventory getInventory(){
        if(this.furnace == null){
            this.furnace = createFurnace();
            Main.getMain().getLogger().info("Furnace created at " + furnace.getLocation());
        }

        return furnace.getInventory();
    }

    public void setFurnace(org.bukkit.block.Furnace furnace){
        this.furnace = furnace;
    }

    public org.bukkit.block.Furnace getFurnace() {
        return furnace;
    }
}
