import java.util.TimerTask;

import javax.swing.JLabel;

/**
 * The timer task class to support counting the seconds for the simulator
 */
public class TrafficTimer extends TimerTask {

    // the time
    private Long time = Long.valueOf(0);

    // label to be updated with each update of the time
    private JLabel timeLabel;

    // preserved time when we pause the timer
    private Long preservedTime = Long.valueOf(0);

    public TrafficTimer(JLabel timeLabel) {
        this.timeLabel = timeLabel;
    }

    // starts the counting
    public void start() {
        synchronized (time) {
            time = 0L;
        }
    }

    // stop the counting
    public void stop() {
        synchronized (time) {
            time = -1L;
            timeLabel.setText("0");
        }
    }

    // pause the counting
    public void pause() {
        synchronized (time) {
            preservedTime = time;
            time = -1L;
        }
    }

    // continue counting
    public void cont() {
        synchronized (time) {
            time = preservedTime;
        }
    }

    @Override
    public void run() {
        synchronized (time) {
            if(time >= 0) {
                time++;
                timeLabel.setText(time.toString());
            }
        }
    }

    // get the current time
    public long getTime() {
        synchronized (time) {
            return time;
        }
    }

    
    
}
