package br.com.backpacks.scheduler;

public class TickComponent {
    private static int componentCount = 0;

    private final int id;
    private final Runnable runnable;
    private int maxTickCount;
    private int currentTick = 0;

    public TickComponent(int interval, Runnable runnable) {
        this.maxTickCount = interval;
        this.id = componentCount;
        this.runnable = runnable;
        componentCount++;
    }

    public TickComponent(Runnable runnable){
        this.id = componentCount;
        this.runnable = runnable;
        componentCount++;
    }

    public int getMaxTickCount() {
        return maxTickCount;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public int getId() {
        return id;
    }

    public void tick(){
        runnable.run();
    }
}
