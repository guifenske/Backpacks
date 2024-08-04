package br.com.backpacks.commands;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.upgrades.*;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static br.com.backpacks.utils.UpgradeType.*;

public class BpUpGive implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length > 2 || args.length < 1){
            sender.sendMessage(Main.PREFIX + "Please use: /bpupgive <UpgradeType> <Player> or /bpupgive <UpgradeType>");
            return true;
        }

        UpgradeType type;
        try{
            type = UpgradeType.valueOf(args[0]);
        }   catch (IllegalArgumentException e){
            sender.sendMessage(Main.PREFIX + args[0] + " isn't a valid UpgradeType!");
            return true;
        }

        if(!sender.isOp()){
            sender.sendMessage(Main.PREFIX + "You aren't a operator!");
            return true;
        }

        Upgrade upgrade = null;
        switch (type){
            case JUKEBOX -> {
                upgrade = new JukeboxUpgrade(UpgradeManager.lastUpgradeID + 1);
            }

            case VILLAGERSFOLLOW -> {
                upgrade = new VillagersFollowUpgrade(UpgradeManager.lastUpgradeID + 1);
            }

            case AUTOFEED -> {
                upgrade = new AutoFeedUpgrade(UpgradeManager.lastUpgradeID + 1);
            }

            case FURNACE -> {
                upgrade = new FurnaceUpgrade(FURNACE, UpgradeManager.lastUpgradeID + 1);
            }

            case SMOKER -> {
                upgrade = new FurnaceUpgrade(SMOKER, UpgradeManager.lastUpgradeID + 1);
            }

            case BLAST_FURNACE -> {
                upgrade = new FurnaceUpgrade(BLAST_FURNACE, UpgradeManager.lastUpgradeID + 1);
            }

            case LIQUIDTANK -> {
                upgrade = new TanksUpgrade(UpgradeManager.lastUpgradeID + 1);
            }

            case COLLECTOR -> {
                upgrade = new CollectorUpgrade(UpgradeManager.lastUpgradeID + 1);
            }

            case AUTOFILL -> {
                sender.sendMessage(Main.PREFIX + args[0] + " isn't a valid UpgradeType!");
                return true;
            }

            default -> {
                upgrade = new Upgrade(type, UpgradeManager.lastUpgradeID + 1);
            }
        }

        ItemStack upgradeItem = RecipesUtils.getItemFromUpgrade(upgrade);

        if(args.length == 1){
            if(sender instanceof ConsoleCommandSender){
                sender.sendMessage(Main.PREFIX + "This command can only be executed as a player, please use: /bpupgive <UpgradeType> <Player>");
                return true;
            }

            Player player = (Player) sender;

            if(!player.getInventory().addItem(upgradeItem).isEmpty()){
                player.sendMessage(Main.PREFIX + "Your inventory is full!");
                return true;
            }
            UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
            UpgradeManager.lastUpgradeID++;
            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            return true;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if(player == null){
            sender.sendMessage(Main.PREFIX + "Invalid Player!");
            return true;
        }

        if(!player.getInventory().addItem(upgradeItem).isEmpty()){
            sender.sendMessage(Main.PREFIX + "The player inventory is full!");
            return true;
        }

        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
        UpgradeManager.lastUpgradeID++;
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> stringList = new ArrayList<>();
        List<String> playerList = new ArrayList<>();
        List<String> upgradeTypes = new ArrayList<>();
        for(UpgradeType type : UpgradeType.values()){
            upgradeTypes.add(type.toString());
        }
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            playerList.add(player.getName());
        }
        upgradeTypes.remove("AUTOFILL");

        if(args.length == 1){
            if(args[0].isEmpty()){
                return upgradeTypes;
            }   else{
                for(String s : upgradeTypes){
                    for(int i = 1; i <= s.length(); i++){
                        if(args[0].equalsIgnoreCase(s.substring(0, i))){
                            stringList.add(s);
                        }
                    }
                }
            }
            return stringList;
        }

        if(args.length == 2){
            if(args[1].isEmpty()){
                return playerList;
            }   else{
                for(String s : playerList){
                    for(int i = 1; i <= s.length(); i++){
                        if(args[0].equalsIgnoreCase(s.substring(0, i))){
                            stringList.add(s);
                        }
                    }
                }
            }
        }

        return null;
    }
}
