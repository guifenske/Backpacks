package br.com.backpacks.events;

import br.com.backpacks.Main;
import br.com.backpacks.backpack.BackpackEntity;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class OutgoingPacketListener implements PacketListener {

    @Override
    public void onPacketSend(@NotNull PacketSendEvent event) {
        if (event.getPacketType().equals(PacketType.Play.Server.SET_PASSENGERS)) {
            final WrapperPlayServerSetPassengers packet = new WrapperPlayServerSetPassengers(event);

            final BackpackEntity backpackEntity = Main.backpackManager.getBackpackEntityByOwnerId(packet.getEntityId());

            if (backpackEntity == null) return;

            // If the packet doesn't already contain our entity
            if (Arrays.stream(packet.getPassengers()).noneMatch((i) -> backpackEntity.getBackpackEntity().getEntityId() == i)) {

                // Add our entity
                int[] passengers = Arrays.copyOf(packet.getPassengers(), packet.getPassengers().length + 1);
                passengers[passengers.length - 1] = backpackEntity.getBackpackEntity().getEntityId();

                packet.setPassengers(passengers);
                event.markForReEncode(true);
            }
        }

        else if(event.getPacketType().equals(PacketType.Play.Server.DESTROY_ENTITIES)){
            WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(event);

            for(int entityId : packet.getEntityIds()){
                BackpackEntity backpackEntity = Main.backpackManager.getBackpackEntityByEntityId(entityId);

                if(backpackEntity == null){
                    return;
                }

                backpackEntity.getBackpackEntity().removeViewer(event.getUser());
            }
        }
    }

}
