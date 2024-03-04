package br.com.backpacks.storage;

import java.io.IOException;

public class StorageProvider {
    private final StorageManager.StorageProviderType type;

    public StorageProvider(StorageManager.StorageProviderType type) {
        this.type = type;
    }

    public StorageManager.StorageProviderType getType() {
        return type;
    }

    public void loadBackpacks(){

    }

    public void loadUpgrades(){

    }

    public void saveBackpacks() throws IOException {

    }

    public void saveUpgrades() throws IOException {

    }
}
