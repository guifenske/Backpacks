package br.com.backpacks.upgrades;

import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;

public class VillagersFollowUpgrade extends Upgrade {

    private boolean enabled;

    public VillagersFollowUpgrade(int id){
        super(UpgradeType.VILLAGERSFOLLOW, id);
        this.enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
