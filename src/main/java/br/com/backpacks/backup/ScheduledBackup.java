package br.com.backpacks.backup;

import br.com.backpacks.Main;

import java.util.concurrent.TimeUnit;

public class ScheduledBackup {
    public int getInterval() {
        return interval;
    }
    public IntervalType getType() {
        return type;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setType(IntervalType type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private int interval = -1;
    private IntervalType type;
    private String path;
    public ScheduledBackup(){
    }

    public void startWithDelay(){
        if(type == null) return;
        if(path == null) return;
        if(interval == -1) return;

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
        if(type == null) return;
        if(path == null) return;
        if(interval == -1) return;

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
