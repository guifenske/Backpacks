package br.com.backpacks.backup;

import br.com.backpacks.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ScheduledBackupService {
    private BukkitTask task;
    private int interval = -1;
    private TimeUnit type;

    public ScheduledBackupService(){
    }
    public TimeUnit getType() {
        return type;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    public void setType(TimeUnit type) {
        this.type = type;
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
                Main.getMain().getBackupHandler().backup();
            } catch (IOException | InvalidConfigurationException e) {
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
