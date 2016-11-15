import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Graph;

/**
 * 
 * Contains the data for a campus map and methods
 * to access such data.
 * 
 * @author Paul Curry
 *
 */
public class CampusData {

    /**
     * CampusData contains the data of distances to places on UW campus. 
     * It also contains methods for querying the data and
     * returning shortest paths to specified campus buildings.
     */
    
    // Map of abbreviated names of buildings to their full name
    private Map<String,String> shortToLong;
    
    // Map of abbreviated names of buildings to their coordinates
    private Map<String,Point<Double>> bToCoords;
    
    // Map where nodes are coordinates and edges
    // are distances between them.
    private DistGraph distGraph;
    
    // Text input file containing coordinate data
    private final String CAMPUSPATHS = "src/hw8/data/campus_paths.dat";
    // Text input file containing coordinates of buildings and abbreviated building names.
    private final String CAMPUSBUILDINGS = "src/hw8/data/campus_buildings.dat";
    
    /**
     * 
     * @effects constructs a CampusData object
     */
    public CampusData() {
        
        // Load graph with campus paths
        distGraph = new DistGraph(CAMPUSPATHS);
        
        shortToLong = new HashMap<String,String>();
        bToCoords = new HashMap<String,Point<Double>>();
        
        
        // Parse data for campus path  
        List<String> lines = TxtInputParser.parse(CAMPUSBUILDINGS);
        
        for(String inputLine : lines) {
            
            String[] tokens = inputLine.split("\t");
            
            // Map short names to long names
            String shortName = tokens[0];
            String longName = tokens[1];
            shortToLong.put(shortName, longName);
            
            // Map short names to coordinates
            Double x = Double.parseDouble(tokens[2]);
            Double y = Double.parseDouble(tokens[3]);
            Point<Double> coords = new Point<Double>(x,y);
            bToCoords.put(shortName, coords);
            
            
        }

    }
    
    /**
     * Calculates edge distance, total cost of route.
     * 
     * 
     * @param shortName1 short name of building to start at
     * @param shortNamen short name of building to end route at
     * @modifies path
     * @requires two points in distGraph to only have one edge between them
     * @return List of Points of the form
     * [(xi,yi),(distance (xi,yi)->(xi+1,yi+1),direction), ... (Total cost,null)] 
     * Where every other point contains information about the distance and direction
     * required to travel to the next coordinate. The last point will contain the total cost
     * as the first element of the Point and no direction information.
     * 
     * If either coords1 or coordsn are not buildings in the data set, returns 
     * a point where is first is null coords1 was not recognized and if second is 
     * null then coords2 was not recognized.
     * Ex. if shortName1 was not recognized but shortNamen was, would return a list 
     * of one point [ (null, 0.0) ]
     * 
     */
    public List<Point<Double>> getRoute(String shortName1, String shortNamen) {
        
        // Check for non-existent buildings 
        List<Point<Double>> pathFull = new ArrayList<Point<Double>>();
        if(!bToCoords.containsKey(shortName1)) {
            if(!bToCoords.containsKey(shortNamen)) {
                pathFull.add(new Point<Double>());
                return pathFull;
            }
            pathFull.add(new Point<Double>(null,0.0));
            return pathFull;
        }
        if(!bToCoords.containsKey(shortNamen)) {
            pathFull.add(new Point<Double>(0.0,null));
            return pathFull;
        }
        
        Point<Double> coords1 = this.bToCoords.get(shortName1);
        Point<Double> coordsn = this.bToCoords.get(shortNamen);
       
        // Get node path from distGraph
        List<Point<Double>> pathNodes = distGraph.findPath(coords1, coordsn);
        
        int i = 0;
        Double totalCost = 0.0;
        while(i < pathNodes.size() - 1) {
            
            coords1 = pathNodes.get(i);
            Point<Double> coords2 = pathNodes.get(i+1);
            
            // Get distance
            Double edgeVal = distGraph.getEdges(coords1, coords2).iterator().next();
            totalCost += edgeVal;
            
            // Get angle
            Double x = coords2.first() - coords1.first();
            Double y = coords2.second() - coords1.second();
            Double angle = Math.atan2(y, x);
            
            // Shift angle down by pi/2
            //angle -= Math.PI/2;
            
            // Normalize angle
            //while(angle <= -Math.PI/2) {
            //    angle += 2*Math.PI;
            //}
            //while(angle > Math.PI/2) {
            //    angle -= 2*Math.PI;
            //}
           
            
            //angle = Math.atan2(Math.sin(angle), Math.cos(angle));
           
            
            
            // Add in original coords and new data.
            pathFull.add(coords1);
            pathFull.add(new Point<Double>(edgeVal,angle));
            
            i = i + 1;
        }
        // Add total cost point 
        pathFull.add(pathNodes.get(i));
        pathFull.add(new Point<Double>(totalCost,null));
        return pathFull;
        
    }
    
    /**
     * 
     * @return a list of Strings where each String is like so:
     * shortName: longName;
     * where shortName and longName are the short and long name 
     * of all the campus buildings.
     */
    public List<String> getBuildings() {
        List<String> bList = new ArrayList<String>();
        for(String shortName : shortToLong.keySet()) {
            bList.add(shortName + ": " + shortToLong.get(shortName));
        }
        return bList;
    }
    
    /**
     * 
     * @return a list of Strings where each String is the shortName
     * of all the campus buildings.
     */
    public List<String> getShortBuildings() {
      
        return new ArrayList<String>(shortToLong.keySet());
    
    }
    
    /**
     * @return the long name of the building
     * @param shortName abbreviated name of building
     * @requires shortName to be within the list of buildings
     */
    public String getLong(String shortName) {
        return shortToLong.get(shortName);
    }
}
