package br.com.backpacks.scheduler;

import br.com.backpacks.Main;
import org.bukkit.Bukkit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TickManager {
    private final ConcurrentMap<Integer, TickComponent> asyncComponents = new ConcurrentHashMap<>();

    public void startAsyncTicking(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getMain(), ()->{
            for(TickComponent component : asyncComponents.values()){
                if(component.getCurrentTick() == component.getMaxTickCount() - 1){
                    component.tick();
                    component.setCurrentTick(0);
                    continue;
                }

                component.setCurrentTick(component.getCurrentTick() + 1);
            }
        }, 0L, 1L);
    }

    public TickComponent addAsyncComponent(TickComponent component, int delay){
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getMain(), ()->{
            this.asyncComponents.put(component.getId(), component);
        }, delay);

        return component;
    }

    public void runComponentAsync(TickComponent component){
        Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), component::tick);
    }

    public void removeComponentFromQueue(int id){
        asyncComponents.remove(id);
    }
}
