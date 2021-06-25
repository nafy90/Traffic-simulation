
import java.awt.*;

import javax.swing.JFrame;

/**
 * The abstract class that is the parent of all entities in the simulation (Car and TrafficLight)
 */
public abstract class IEntity extends java.awt.Rectangle implements Runnable, Comparable<IEntity> {
    
    private static final long serialVersionUID = 1L;
    protected double locationX;
    protected int locationY;
    public Thread t;
    protected String name;
    private TrafficTimer timer;
    private long currentTime;
    private Drawing drawing;

    public IEntity(int locationX, int locationY, String name, TrafficTimer timer, Drawing drawing) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.name = name;
        this.timer = timer;
        this.drawing = drawing;
        drawing.addEntity(this);
        this.currentTime = timer.getTime();
        t = new Thread(this, name);
        System.out.println("New thread: " + t);
    }

    @Override
	public int compareTo(IEntity o) {
        if(this.locationX <  o.locationX) {
            return -1;
        } 

        if(this.locationX == o.locationX) {
            return 0;
        }

        return 1;
    }
    
    // Return true if the entity is enable (for using in the UI drawing)
    abstract boolean isEnabled();

    // gets the rounded position X for this entity
    public int getPositionX() {
        return (int) Math.floor(locationX);
    }

    abstract void wake(long timeLaps);

    // draws this entity in the Drawing object
    public abstract void draw(Graphics g, JFrame parent);

    @Override
    public boolean equals(Object obj) {
        return this.name.equals(((IEntity)obj).name);
    }

    @Override
    public void run() {
        boolean stop = false;
        while (!stop && !Thread.interrupted()) {
            long oldTime = currentTime;
            currentTime = timer.getTime();
            wake(currentTime - oldTime);
            drawing.drawShape();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                stop = true;
            }
        }
        System.out.println(name + " exiting.");
        t = new Thread(this, name);
    }

}
