package br.com.backpacks.backupHandler;

import br.com.backpacks.Main;

import java.util.concurrent.TimeUnit;

public class ScheduledBackup {
    public int getInterval() {
        return interval;
    }
    public IntervalType getType() {
        return type;
    }
    private final int interval;
    private final IntervalType type;
    private final String path;
    public ScheduledBackup(IntervalType type, int interval, String path) {
        this.interval = interval;
        this.type = type;
        this.path = path;
        Main.getMain().debugMessage("Backup path specified, using: " + path);
    }

    public ScheduledBackup(IntervalType type, int interval) {
        this.interval = interval;
        this.type = type;
        this.path = Main.getMain().getDataFolder().getAbsolutePath() + "/Backups";
        Main.getMain().debugMessage("Backup path not specified, using default: " + path);
    }

    public void startWithDelay(){
        switch (type){
            case HOURS -> Main.getMain().getThreadBackpacks().getExecutor().scheduleAtFixedRate(() -> {
                try {
                    Main.getMain().getBackupHandler().backup(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, interval, interval, TimeUnit.HOURS);

            case MINUTES -> Main.getMain().getThreadBackpacks().getExecutor().scheduleAtFixedRate(() -> {
                try {
                    Main.getMain().getBackupHandler().backup(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, interval, interval, TimeUnit.MINUTES);

            case SECONDS -> Main.getMain().getThreadBackpacks().getExecutor().scheduleAtFixedRate(() -> {
                try {
                    Main.getMain().getBackupHandler().backup(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, interval, interval, TimeUnit.SECONDS);
        }
    }

    public void start(){
        switch (type){
            case HOURS -> Main.getMain().getThreadBackpacks().getExecutor().scheduleAtFixedRate(() -> {
                try {
                    Main.getMain().getBackupHandler().backup(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 0, interval, TimeUnit.HOURS);

            case MINUTES -> Main.getMain().getThreadBackpacks().getExecutor().scheduleAtFixedRate(() -> {
                try {
                    Main.getMain().getBackupHandler().backup(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 0 ,interval, TimeUnit.MINUTES);

            case SECONDS -> Main.getMain().getThreadBackpacks().getExecutor().scheduleAtFixedRate(() -> {
                try {
                    Main.getMain().getBackupHandler().backup(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 0 ,interval, TimeUnit.SECONDS);
        }
    }

    public enum IntervalType {
        MINUTES,
        SECONDS,
        HOURS
    }
}
