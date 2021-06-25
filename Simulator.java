import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Simulation main class
 */
public class Simulator extends JFrame {

    private static final long serialVersionUID = 1L;
    private Drawing jpDrawing;
    private JLabel timerLbl;
    private JButton startBtn;
    private JButton stopBtn;
    private JButton pauseBtn;
    private JButton continueBtn;
    private JButton addTrafficLightBtn;
    private JButton addCarBtn;
    private JTextField carSpeedTxt;

    private enum ExecutionStatus { STOPPED, RUNNING, PAUSED };

    // Status of the exectution
    private ExecutionStatus status;

    // Constant for the Y location of traffic lights
    final static int Y = 100;

    // Array of all traffic lights
    private List<TrafficLight> lights = new ArrayList<TrafficLight>();

    // Array of all cars
    private List<Car> cars = new ArrayList<Car>();

    // timers
    private TrafficTimer trafficTimer;

    // Contains last light x location
    private int maxLightX = 0;

    // Contains last car Y location
    private int maxCarY = Y + 50;

    public static void main(String[] arrgs) {

        Simulator simulator = new Simulator();
        System.out.println(simulator.getTitle());
    }

    // Creates the starting data
    private void initiateData() {
        lights.add(new TrafficLight(100, Y, 1, trafficTimer, jpDrawing));
        lights.add(new TrafficLight(200, Y, 2, trafficTimer, jpDrawing));
        lights.add(new TrafficLight(300, Y, 3, trafficTimer, jpDrawing));
        maxLightX = 300;

        maxCarY =  Y + 50;
        cars.add(new Car(500, 0, maxCarY, 1, trafficTimer, jpDrawing, lights));
        maxCarY += 20;
        cars.add(new Car(100, 0, maxCarY, 2, trafficTimer, jpDrawing, lights));
        maxCarY += 20;
        cars.add(new Car(360, 0, maxCarY, 3, trafficTimer, jpDrawing, lights));
    }

    // Starts all the threads for cars and traffic lights
    private void startThreads() {
        for (IEntity l : lights) {
            l.t.start();
        }

        for (IEntity c : cars) {
            c.t.start();
        }
    }

    // Event handler method for Continue
    private void cont(){
        if(status == ExecutionStatus.PAUSED) {
            startThreads();
            trafficTimer.cont();
            status = ExecutionStatus.RUNNING;
        }
    }

    // Event handler for Start
    private void start() {
        if(status != ExecutionStatus.STOPPED) {
            JOptionPane.showMessageDialog(this, "Simulation is in progress!");
            return;
        }
        initiateData();
        trafficTimer.start();
        startThreads();
        status = ExecutionStatus.RUNNING;
    }

    // Event handler for Stop
    private void stop() {
        if(status != ExecutionStatus.STOPPED) {

            for (IEntity l : lights) {
                l.t.interrupt();
            }
    
            for (IEntity c : cars) {
                c.t.interrupt();
            }

            trafficTimer.stop();
            jpDrawing.clearAll();
            lights.clear();
            cars.clear();

            status = ExecutionStatus.STOPPED;
        }
    }

    // Event handler for Pause
    private void pause() {

        if(status != ExecutionStatus.RUNNING){
            JOptionPane.showMessageDialog(this, "Simulation is not running!");
            return;
        }
        trafficTimer.pause();

        for (IEntity l : lights) {
            l.t.interrupt();
        }

        for (IEntity c : cars) {
            c.t.interrupt();
        }

        status = ExecutionStatus.PAUSED;
    }


    // adds a light to the simulation
    private void addLight() {
        maxLightX +=100;
        TrafficLight newLight = new TrafficLight(maxLightX, Y, lights.size() + 1, trafficTimer, jpDrawing);
        newLight.t.start();
        lights.add(newLight);

        for(Car c : cars) {
            c.addLight(newLight);
        }
    }

    // adds a car to the simulation
    private void addCar() {
        try {
            maxCarY += 20;
            int speed = Integer.parseInt(carSpeedTxt.getText());
            Car newCar = new Car(speed, 0, maxCarY, cars.size() + 1, trafficTimer, jpDrawing, lights);
            cars.add(newCar);
            newCar.t.start();
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid car speed value!");
        }
    }

    public Simulator() {
        status = ExecutionStatus.RUNNING;
        jpDrawing = new Drawing();
        timerLbl = new JLabel("", SwingConstants.CENTER);

        Timer timer = new Timer();
        trafficTimer = new TrafficTimer(timerLbl);
        timer.schedule(trafficTimer, 0, 1000);

        initiateData();

        createAndShowGUI();

        startThreads();

    }



    private void addComponentsToPane(Container pane) {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(timerLbl, BorderLayout.CENTER);

        jpDrawing.setBorder(BorderFactory.createTitledBorder(""));
        pane.add(topPanel, BorderLayout.PAGE_START);
        pane.add(jpDrawing, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(4, 4));
        
        startBtn = new JButton("Start");
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });

        stopBtn = new JButton("Stop");
        stopBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });

        pauseBtn = new JButton("Pause");
        pauseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pause();
            }
        });

        continueBtn = new JButton("Continue");
        continueBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cont();
            }
        });

        addCarBtn =  new JButton("Add Car");
        carSpeedTxt = new JTextField(5);
        addCarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCar();
            }
        });

        addTrafficLightBtn = new JButton("Add Light");
        addTrafficLightBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLight();
            }
        });


        bottomPanel.add(startBtn, 0, 0);
        bottomPanel.add(stopBtn, 0, 1);
        bottomPanel.add(pauseBtn, 0, 2);
        bottomPanel.add(continueBtn, 0, 3);
        bottomPanel.add(new JLabel("Speed:", SwingConstants.RIGHT), 1, 0);
        bottomPanel.add(carSpeedTxt, 1, 1);
        bottomPanel.add(addCarBtn, 1, 2);
        
        bottomPanel.add(addTrafficLightBtn, 2, 2);


        pane.add(bottomPanel, BorderLayout.PAGE_END);

    }

    // Create and setup Frame
    private void createAndShowGUI() {
        setTitle("Traffic Simulator");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(getContentPane());
        pack();
        setVisible(true);
    }
    
}
