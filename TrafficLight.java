import java.util.Random;

import javax.swing.JFrame;
import java.awt.*;

/**
 * Traffic light class
 */
public class TrafficLight extends IEntity {

    private static final long serialVersionUID = 1L;

    // distance of the traffic light in meter
    private final String distance;

    // constant fot the size of the circle
    private int RADIUS = 20;

    public enum EStatus {RED, GREEN, YELLOW}

    private EStatus status;
    private static final int RED_WAIT = 10;
    private static final int YELLOW_WAIT = 5;
    private static final int GREEN_WAIT = 10;

    private int redCounter = 0;
    private int yellowCounter = 0;
    private int greenCounter = 0;


    public TrafficLight(int locationX, int locationY, int id, TrafficTimer timer, Drawing drawing) {
        super(locationX, locationY, "trafficLight" + id, timer, drawing);
        distance = id * 1000 + " Meters";
        Random r = new Random(); 
        int randomState = r.nextInt(2);
        switch(randomState) {
            case 0:
                status = EStatus.RED;
                redCounter = RED_WAIT;
                break;
            case 1: 
                status = EStatus.GREEN;
                greenCounter = GREEN_WAIT;
                break;
            case 2: 
                status = EStatus.YELLOW;
                yellowCounter = YELLOW_WAIT;
                break;
        }
    }

    public boolean isRed() {
        return status == EStatus.RED;
    }

    @Override
    public void wake(long timePassed) {
        if(status == EStatus.RED) {
            redCounter-=timePassed;
            if(redCounter <= 0) {
                status = EStatus.GREEN;
                greenCounter = GREEN_WAIT;
            }
        } else if(status == EStatus.YELLOW) {
            yellowCounter-=timePassed;
            if(yellowCounter <= 0) {
                status = EStatus.RED;
                redCounter = RED_WAIT;
            }
        } else if(status == EStatus.GREEN) {
            greenCounter-=timePassed;
            if(greenCounter <= 0) {
                status = EStatus.YELLOW;
                yellowCounter = YELLOW_WAIT;
            }
        }
    }

    @Override
    public void draw(Graphics g, JFrame parent) {

        g.setColor(Color.black);
        g.drawString(distance, (int)locationX, (int)locationY - 20);

        switch(status) {
            case RED:
                g.setColor(Color.RED);
                break;
            case GREEN:
                g.setColor(Color.GREEN);
                break;
            case YELLOW:
                g.setColor(Color.YELLOW);
                break;
        }
        
        g.fillOval((int)locationX, (int)locationY, RADIUS, RADIUS);
        g.drawLine((int)locationX + (RADIUS/2), (int)locationY + RADIUS, (int)locationX + (RADIUS/2), (int)locationY+ 300);
    }

    // Always enabled, a traffic light never dies
    @Override
    boolean isEnabled() {
        return true;
    }
    
}
