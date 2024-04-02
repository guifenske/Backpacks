package br.com.backpacks.storage;

public class StorageManager {
    private static StorageProvider provider;

    public static StorageProvider getProvider() {
        return provider;
    }

    public static void setProvider(StorageProvider provider) {
        StorageManager.provider = provider;
    }

    public enum StorageProviderType {
        MYSQL,
        SQLITE,
        TOML,
        YAML,
        REDIS
    }
}
