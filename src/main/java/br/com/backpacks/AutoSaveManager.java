package br.com.backpacks;

import br.com.backpacks.storage.StorageManager;
import br.com.backpacks.scheduler.TickComponent;

import java.util.concurrent.TimeUnit;

public class AutoSaveManager {
    private int tickComponentId = 0;
    private TimeUnit type;
    private int interval;

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
            default -> time = interval * 20 * 60 * 60;
        }

        tickComponentId = Main.getMain().getTickManager().addAsyncComponent(new TickComponent(time) {
            @Override
            public void tick() {
                StorageManager.saveAll(false);
            }
        }, time).getId();
    }

    public void cancel(){
        if(tickComponentId != 0){
            Main.getMain().getTickManager().removeComponentFromQueue(tickComponentId);
        }
    }
}
