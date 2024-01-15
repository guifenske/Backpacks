package br.com.backpacks.upgrades;

public interface GetCollector {
    boolean isCollectorEnabled();

    void setCollectorIsEnabled(boolean isEnabled);

    void setCollectorMode(int mode);

    int getCollectorMode();
}
