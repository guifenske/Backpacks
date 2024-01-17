package br.com.backpacks.upgrades;

import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;

public class CollectorUpgrade extends Upgrade {
    //mode 0 = only backpack items, mode 1 = every item
    private int mode;

    private boolean enabled;

    public CollectorUpgrade(int id){
        super(UpgradeType.COLLECTOR, id);
        this.enabled = false;
        this.mode = 0;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
