package br.com.backpacks;

import br.com.backpacks.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AutoSaveManager {
    private BukkitTask task;
    private TimeUnit type;
    private int interval;

    public AutoSaveManager(){
    }

    public TimeUnit getType() {
        return type;
    }

    public void setType(TimeUnit type) {
        this.type = type;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void start(){
        if(type == null) return;
        if(interval == -1) return;
        int time = 0;

        //convert the interval to ticks.
        switch (type){
            case SECONDS -> time = interval * 20;
            case MINUTES -> time = interval * 20 * 60;
            case HOURS -> time = interval * 20 * 60 * 60;
        }

        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getMain(), ()->{
            try {
                StorageManager.getProvider().saveUpgrades();
                StorageManager.getProvider().saveBackpacks();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, time, time);
    }

    public void cancel(){
        if(task != null){
            task.cancel();
        }
    }
}
