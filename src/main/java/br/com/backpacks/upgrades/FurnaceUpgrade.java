package br.com.backpacks.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.scheduler.TickComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FurnaceUpgrade extends Upgrade {
    private org.bukkit.block.Furnace furnace;
    private int tickComponentId = -1;

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
        Main.getMain().getTickManager().removeComponentFromQueue(tickComponentId);
        tickComponentId = -1;
    }

    public void setTickComponentId(int tickComponentId) {
        this.tickComponentId = tickComponentId;
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

        while(true) {
            for (int chX = -chunkRadius; chX <= chunkRadius; chX++) {
                for (int chZ = -chunkRadius; chZ <= chunkRadius; chZ++) {
                    Chunk chunk = new Location(location.getWorld(), location.x() + (chX * 16), 0, location.z() + (chZ * 16)).getChunk();

                    randomX = ThreadLocalRandom.current().nextInt(0, 15);
                    randomZ = ThreadLocalRandom.current().nextInt(0, 15);

                    block = chunk.getBlock(randomX, 0, randomZ);

                    int maxY = world.getHighestBlockYAt(block.getX(), block.getZ());

                    if (!chunk.getBlock(randomX, maxY, randomZ).getType().isOccluding()) {
                        continue;
                    }

                    randomY = ThreadLocalRandom.current().nextInt(minY, maxY);

                    block = chunk.getBlock(randomX, randomY, randomZ);

                    if (!block.isEmpty()) {
                        continue;
                    }

                    block.setType(Material.FURNACE);

                    final Location location1 = block.getLocation();
                    this.tickComponentId = Main.getMain().getTickManager().addAsyncComponent(new TickComponent(10) {
                        @Override
                        public void tick() {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendBlockChange(location1, Material.AIR.createBlockData());
                            }
                        }
                    }, 0).getId();

                    return (org.bukkit.block.Furnace) block.getState();
                }
            }
        }
    }

    public Inventory getInventory(){
        if(this.furnace == null){
            Instant start = Instant.now();
            this.furnace = createFurnace();

            Instant finish = Instant.now();
            Main.debugMessage("Virtual furnace created at " + furnace.getLocation() + " in " + Duration.between(start, finish).toMillis() + "ms");
        }

        return furnace.getInventory();
    }

    public void setFurnace(org.bukkit.block.Furnace furnace){
        this.furnace = furnace;
    }

    public boolean canBeRemoved(){
        if(this.furnace == null){
            return true;
        }

        return this.furnace.getInventory().getFuel() == null && this.furnace.getInventory().getSmelting() == null && this.furnace.getInventory().getResult() == null;
    }

    public org.bukkit.block.Furnace getFurnace() {
        return furnace;
    }
}
