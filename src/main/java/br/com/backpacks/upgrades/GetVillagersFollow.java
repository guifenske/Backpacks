package br.com.backpacks.upgrades;

import org.bukkit.entity.Entity;

import java.util.List;

public interface GetVillagersFollow {
    boolean isVillagersEnabled();

    void setVillagersIsEnabled(boolean isEnabled);

    List<Entity> getVillagers();

}
