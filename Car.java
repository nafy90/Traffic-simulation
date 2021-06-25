import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import java.awt.*;

/**
 * Car class
 */
public class Car extends IEntity {

    private static final long serialVersionUID = 1L;
    private final int speed;
    private List<TrafficLight> lights;
    private Color color;
    private int maxX;

    public Car(int speed, int locationX, int locationY, int id, TrafficTimer timer, Drawing drawing, List<TrafficLight> lights) {
        super(locationX, locationY, "car" + id, timer, drawing);
        this.speed = speed;
        this.lights = lights.stream().sorted().collect(Collectors.toList());
        Random random = new Random();
        color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));

        Optional<TrafficLight> maxLight = lights.stream().max(IEntity::compareTo);
        maxX = maxLight.isPresent() ? maxLight.get().getPositionX() + 50 : 500;
    }

    // adds a light to the lights for this car
    public void addLight(TrafficLight light) {
        if(lights.indexOf(light) == -1) {
            lights.add(light);
            maxX = light.getPositionX() + 50;
        }
    }

    @Override
    public void wake(long timePassesd) {
        double nextPosition = locationX + timePassesd * ((double)speed / 36);
        for(TrafficLight light : lights) {
            int lightPosition = light.getPositionX();
            if(nextPosition >= lightPosition && locationX <= lightPosition && light.isRed()) {
                locationX = light.getPositionX();
                return;
            }
        }

        locationX =  nextPosition;

        if(locationX > maxX) {
            t.interrupt();
        }
    }

    @Override
    public void draw(Graphics g, JFrame parent) {
        g.setColor(color);
        g.drawString(String.valueOf(speed) + " km", (int)locationX - 5, (int)locationY);
        g.fillRect((int)locationX, (int)locationY, 10, 5);
    }

    // if we are at the end of the line for the car, it should be disabled
    @Override
    boolean isEnabled() {
        return locationX <= maxX;
    }
}