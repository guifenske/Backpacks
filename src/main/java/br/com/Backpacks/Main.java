package br.com.Backpacks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main back;


    @Override
    public void onEnable() {
        // Plugin startup logic
        back = this;

        if(Bukkit.getPluginManager().getPlugin("NBTAPI") == null){
            Bukkit.getConsoleSender().sendMessage(this.getName() + " >> NBTAPI is not installed! Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        }   else    Bukkit.getConsoleSender().sendMessage(this.getName() + " >> NBTAPI found! Starting up...");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Bye from " + this.getName() + "!");
    }
}
