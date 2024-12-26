package br.com.backpacks.scheduler;

public abstract class TickComponent {
    private static int componentCount = 0;

    private final int id;
    private int maxTickCount;
    private int currentTick = 0;

    protected TickComponent(int tickDelay){
        this.maxTickCount = tickDelay;
        this.id = componentCount;
        componentCount++;
    }

    public TickComponent(){
        this.id = componentCount;
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

    public abstract void tick();
}
