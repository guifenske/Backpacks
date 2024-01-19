package br.com.backpacks.backpackUtils;

public class Upgrade {

    private final UpgradeType type;
    private final int id;
    public Upgrade(UpgradeType type, int id) {
        this.type = type;
        this.id = id;
    }

    public UpgradeType getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
