package br.com.Backpacks.yaml;


import br.com.Backpacks.Main;
import br.com.Backpacks.backpackUtils.BackPack;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YamlUtils {

    public static void save_backpacks_yaml(Player player) throws IOException {
        File file = new File(Main.back.getDataFolder().getCanonicalFile().getAbsolutePath() + "/" + player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for(BackPack backPack : Main.back.backPackManager.getPlayerBackPacks(player)){
            config.set(backPack.getBackpack_id() + ".i", backPack.serialize());
            config.set(backPack.getBackpack_id() + ".1", backPack.getFirst_page().getStorageContents());

            if(backPack.get_Second_page_size() > 0){
                config.set(backPack.getBackpack_id() + ".2", backPack.getSecond_page().getStorageContents());
            }

        }
        config.save(file);
    }

    public static List<BackPack> load_backpacks_yaml(Player player) throws IOException {
        File file = new File(Main.back.getDataFolder().getCanonicalFile().getAbsolutePath() + "/" + player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        List<BackPack> backPacks = new ArrayList<>();
        for(int i = 0; i < config.getKeys(false).size(); i++){
            backPacks.add(new BackPack().deserialize(config, player));
        }

        file.delete();

        return backPacks;
    }
}