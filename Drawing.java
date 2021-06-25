
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.*;
import java.util.ArrayList;

/**
 * Drawing class to be used for paiting objects in the simulation
 */
public class Drawing extends JPanel{
    
    private static final long serialVersionUID = 1L;
    private java.util.List<IEntity> currentShapes;

    public Drawing() {
        this.currentShapes = new ArrayList<IEntity>();
    }

    public void addEntity(IEntity e) {
        this.currentShapes.add(e);
    }

    public void clearAll() {
        this.currentShapes.clear();
        drawShape();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(currentShapes != null){
            for(IEntity currentShape : currentShapes) {
                if(currentShape.isEnabled()) {
                    currentShape.draw(g, (JFrame) SwingUtilities.getWindowAncestor(this));
                }
            }
        }
    }
     
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }

    public void drawShape() {
        repaint();
    }
}