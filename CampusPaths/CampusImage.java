import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import Point;

/** A component that displays a map of UW campus. */
public class CampusImage extends JPanel {

    private final BufferedImage campusImage;
    private List<Point<Double>> points;
    
    public CampusImage() throws IOException {
        
        this.campusImage = ImageIO.read(new File("src/hw8/data/campus_map.jpg"));
        points = new ArrayList<Point<Double>>();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // paint background
        
        // Draw the image to fill up the whole available space.
        g.drawImage(campusImage, 0, 0, getParent().getWidth(), getParent().getHeight(),
                0, 0, campusImage.getWidth(null), campusImage.getHeight(null), null);
        
        
        if(!points.isEmpty()) {
            Point<Double> point;
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(5));
            g2d.setColor(Color.red);
            
            // Loop through all points drawing line between them
            int i = 0;
            while(i < points.size()-1) {
                point = points.get(i);
                Integer x1 = scaleX(point.first()).intValue();
                Integer y1 = scaleY(point.second()).intValue();
                
                point = points.get(i+1);
                Integer x2 = scaleX(point.first()).intValue();
                Integer y2 = scaleY(point.second()).intValue();
                
                Line2D line = new Line2D.Float(x1,y1,x2,y2);
                g2d.draw(line);
                
                i = i + 1;
            }
            
            // Draw circles on first and last points
            g2d.setColor(Color.green);
            point = points.get(0);
            Integer startx = scaleX(point.first()).intValue();
            Integer starty = scaleY(point.second()).intValue();
            g2d.fillOval(startx - 5, starty -5, 12, 12);
            
            g2d.setColor(Color.red);
            point = points.get(points.size()-1);
            Integer destx = scaleX(point.first()).intValue();
            Integer desty = scaleY(point.second()).intValue();
            g2d.fillOval(destx - 5, desty -5, 12, 12);
            
        }
 
    }
    
    /**
     * @param point Point to be drawn on the map
     * @modifies this
     * @requires point to be a valid Point<Double> starting at 0,0
     */
    public void addPoint(Point<Double> point) {
        
        points.add(point);
        
    }
    
    /**
     * Clears all points from the map
     * @modifies this
     */
    public void clearPoints() {
        
        points.clear();
        
    }
    
    /**
     * 
     * @param original original X coordinate to be scaled
     * @return scaled coordinate of X
     */
    private Double scaleX(Double original) {

        return original * this.getParent().getWidth() / campusImage.getWidth(null);
        
    }

    /**
     * 
     * @param original original Y coordinate to be scaled
     * @return scaled coordinate of Y
     */
    private Double scaleY(Double original) {

        return original * this.getParent().getHeight() / campusImage.getHeight(null);

    }
}
