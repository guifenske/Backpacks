package br.com.backpacks.events;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.events.upgrades.Furnace;
import br.com.backpacks.events.upgrades.VillagersFollow;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ServerLoadEvent implements Listener {

    @EventHandler
    private void onServerLoad(org.bukkit.event.server.ServerLoadEvent event){
        VillagersFollow.tick();
        for(Upgrade upgrade : Main.backPackManager.getUpgradeHashMap().values()){
            if(upgrade.getType().equals(UpgradeType.FURNACE) || upgrade.getType().equals(UpgradeType.BLAST_FURNACE) || upgrade.getType().equals(UpgradeType.SMOKER)){
                if(!((FurnaceUpgrade) upgrade).canTick()) continue;
                Furnace.shouldTick.add(upgrade.getId());
                Furnace.tick((FurnaceUpgrade) upgrade);
            }
        }
    }
}
