import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import java.util.Collections;
import java.util.List;

import CampusData;
import Point;

public class CampusPathsGUI {
    
    /**
     * GUI for displaying the shortest routes to and from buildings on campus
     */
    public static void main(String[] args) throws IOException {
        
        // Define initial app dimensions
        Dimension appSize = new Dimension(1024,768); 
        
        // Load in data
        final CampusData data = new CampusData();

        // Create initial frame
        final JFrame frame = new JFrame("Campus Paths (TM)");
        frame.setSize(appSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
        frame.setMinimumSize(appSize);
        
        // Set up wrapper to hold map and top toolbar
        JPanel wrapper = new JPanel();
        wrapper.setBackground(Color.black);
        wrapper.setLayout(new BorderLayout());
        frame.add(wrapper);

        // Add top toolbar container into wrapper
        JPanel top = new JPanel();
        top.setBackground(Color.cyan);
        //top.setPreferredSize(new Dimension(200,100));
        top.setLayout(new BorderLayout());
        wrapper.add(top, BorderLayout.PAGE_START);
        
        // Add Find Path and Clear buttons into top toolbar
        final JButton pathButton = new JButton("Find Path");
        final JButton clearButton = new JButton("Clear Paths");
        // Requires extra wrapper
        JPanel searchWrapper = new JPanel();
        top.add(searchWrapper,BorderLayout.NORTH);
        searchWrapper.add(pathButton);
        searchWrapper.add(clearButton);
        
        // Add drop down menu for start building into top toolbar
        // Load in buildings
        List<String> buildings = data.getShortBuildings();
        // sort buildings
        Collections.sort(buildings);

        final JComboBox<Object> startCombo = new JComboBox<Object>(buildings.toArray());
        JLabel startText = new JLabel("From");
        //startCombo.setSelectedIndex(4);
        // Requires extra wrapper
        JPanel startWrapper = new JPanel();
        top.add(startWrapper, BorderLayout.CENTER);
        startWrapper.add(startText);
        startWrapper.add(startCombo);
        
        // Add drop down menu for dest building into top toolbar
        final JComboBox<Object> destCombo = new JComboBox<Object>(buildings.toArray());
        // Requires extra wrapper
        JPanel destWrapper = new JPanel();
        JLabel destText = new JLabel("To");
        top.add(destWrapper, BorderLayout.SOUTH);
        destWrapper.add(destText);
        destWrapper.add(destCombo);

        // Add map into wrapper
        final CampusImage map = new CampusImage();
        map.setBackground(Color.white); 
        wrapper.add(map);
        map.setBounds(0,0,appSize.width,appSize.height);

        
        // ADD EVENT LISTENERS --------------------------------------------------------

        // Define find path event handler

        /**
         * 
         * @author Paul Curry
         * Event listener for the find path button. Will disable the button and the combo
         * boxes in the GUI until it has computed a path. When it has, it will display the 
         * path on the map image and re-enable the button and combo boxes.
         *
         */
        class PathButtonListener implements ActionListener {
            
            
            public void actionPerformed(ActionEvent e) {
            
                // Disable GUI elements to prevent spam clicking/show loading
                pathButton.setEnabled(false);
                clearButton.setEnabled(false);
                startCombo.setEnabled(false);
                destCombo.setEnabled(false);
                
                String start = (String)startCombo.getSelectedItem();
                String dest = (String)destCombo.getSelectedItem();
                
                // Clear any previous points
                map.clearPoints();
                
                // Cannot find path from same buildings, ex. CSE to CSE.
                if(!start.equals(dest)) {
                    
                    List<Point<Double>> route = data.getRoute(start, dest);
                    
                    // Send list of points to markPath method to mark path on map
                    // Since every other point in the route list is information about 
                    // distance and direction, we only move through the list of points by twos.
                    int i = 0;
                    while(i < route.size()) {
                        map.addPoint(route.get(i));
                        i = i + 2;
                    }
 
                    
                } else {
                    
                    JOptionPane.showMessageDialog(frame, "Turn around. You've arrived.");
                
                }
                
                // Redraw map
                frame.repaint();
                
                // Re-enable GUI elements
                pathButton.setEnabled(true);
                clearButton.setEnabled(true);
                startCombo.setEnabled(true);
                destCombo.setEnabled(true);
                
            }
        
        }
        
        /**
         * 
         * @author Paul Curry
         * Event listener for the clear button.
         * Tells the map to clear itself and the frame
         * to repaint.
         *
         */
        class ClearButtonListener implements ActionListener {
                
            public void actionPerformed(ActionEvent e) {
            
                    map.clearPoints();
                    frame.repaint();
                    
            }
                
        }
        
        
        // Set actionListener of Find Path button to PathButtonListener (see above)
        pathButton.addActionListener(new PathButtonListener());
        
        // Set actionListener of Clear button to ClearButtonListener
        clearButton.addActionListener(new ClearButtonListener());
        
        // Set frame to be visible
        frame.setVisible(true);
    
    }
    
}
