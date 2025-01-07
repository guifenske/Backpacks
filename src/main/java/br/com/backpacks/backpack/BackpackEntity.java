package br.com.backpacks.backpack;

import br.com.backpacks.Main;
import br.com.backpacks.scheduler.TickComponent;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemCustomModelData;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import me.tofaa.entitylib.meta.EntityMeta;
import me.tofaa.entitylib.meta.other.ArmorStandMeta;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class BackpackEntity {
    private final @NotNull Player player;
    private final @NotNull WrapperEntity backpackEntity;
    private TickComponent tickComponent;

    public BackpackEntity(@NotNull Player player){
        this.player = player;
        this.backpackEntity = new WrapperEntity(EntityTypes.ARMOR_STAND);
        backpackEntity.setTicking(false);

        ArmorStandMeta meta = (ArmorStandMeta) EntityMeta.getMeta(backpackEntity.getEntityId());
        meta.setHasNoBasePlate(true);
        meta.setInvisible(true);
        meta.setMarker(true);

        Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), this::init);
    }

    private void init(){
        Location location = updateLocation();

        this.backpackEntity.spawn(location);

        this.tickComponent = Main.getMain().getTickManager().addAsyncComponent(new TickComponent(1, ()->{
            this.backpackEntity.rotateHead(player.getBodyYaw(), 0.0f);
        }), 0);

        backpackEntity.addViewer(player.getUniqueId());
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, getPassengersPacket());
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, getEquipmentPacket());
    }

    private PacketWrapper<?> getPassengersPacket() {
        int[] bukkitPassengers = player.getPassengers().stream().mapToInt(Entity::getEntityId).toArray();
        int[] passengers = Arrays.copyOf(bukkitPassengers, bukkitPassengers.length + 1);

        passengers[passengers.length - 1] = backpackEntity.getEntityId();
        return new WrapperPlayServerSetPassengers(player.getEntityId(), passengers);
    }

    private PacketWrapper<?> getEquipmentPacket(){
        ItemCustomModelData itemCustomModelData = new ItemCustomModelData(101921);

        ItemStack itemStack = ItemStack.builder().type(ItemTypes.CARVED_PUMPKIN)
                .component(ComponentTypes.CUSTOM_MODEL_DATA_LISTS, itemCustomModelData).amount(1).build();

        return new WrapperPlayServerEntityEquipment(backpackEntity.getEntityId(), List.of(new Equipment(EquipmentSlot.HELMET, itemStack)));
    }

    private @NotNull Location updateLocation() {
        Location location = SpigotConversionUtil.fromBukkitLocation(
                player.getLocation().clone()
        );

        location.setYaw(player.getBodyYaw());
        location.setPitch(0f);

        this.backpackEntity.teleport(location);

        return location;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull WrapperEntity getBackpackEntity() {
        return backpackEntity;
    }

    public void clear(){
        Main.getMain().getTickManager().removeComponentFromQueue(this.tickComponent.getId());
        this.backpackEntity.despawn();
    }
}
