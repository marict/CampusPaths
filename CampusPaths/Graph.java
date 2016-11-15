import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph<N,E> {

    // Representation:
    // A |V| x |V| matrix M in which an element M[u,v] is 
    // true iff there is an edge from u to v. An edge and a node
    // may have a label L, no nodes may have the same L, and no edges
    // that point from the same node to the same node have the same L.
    
    // Abstraction function: 
    // Nodes maps the name of a node N to a set of all the nodes n1...ni where
    // there exists an edge E such that E goes from N to ni.
    // edgeMap maps pairs of nodes to strings. The keys are pairs of nodes
    // and the values are the edges between the pairs of nodes.
    // Name represents the name of the graph.
   
    private Map<N,HashSet<N>> nodes;
    private Map<Pair<N>,HashSet<E>> edgeMap;

    /** 
        @effects Constructs a new empty Graph. 
    */
    public Graph() {
        nodes = new HashMap<N,HashSet<N>>();
        edgeMap = new HashMap<Pair<N>,HashSet<E>>();
        checkRep();
        
    }
    
    
    /** 
        @param nodes Set of node labels
        @effects Constructs a new graph initially containing the nodes passed.
    */
    public Graph(Set<N> nodes) {
        this.nodes = new HashMap<N,HashSet<N>>();
        for(N node : nodes) {
            this.nodes.put(node, new HashSet<N>());
        }
        edgeMap = new HashMap<Pair<N>,HashSet<E>>();
        checkRep();
    }
    
    
    /** 
        @param node1 label of first node
        @param node2 label of second node
        @param edgeLabel label of edge to be placed into graph
        @modifies this
        @effects Adds the edge with edgeLabel into the graph pointing from node1 to node2
        @throws IllegalArgumentException if node1 or node2 are not in the graph
        @throws IllegalArgumentException if there is already an edge from
             node1 to node2 with edgeLabel
    */
    public void addEdge(N node1, N node2, E edgeLabel) {
        Pair<N> p = new Pair<N>(node1,node2);
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new IllegalArgumentException("Node passed does not exist in the graph.");
        }
        if(edgeMap.containsKey(p) && edgeMap.get(p).contains(edgeLabel)) {
            throw new IllegalArgumentException("There already exists an edge between passed nodes "
                    + "with label " + edgeLabel + ".");
        }
        
        // if there is no set mapped to p, map p to a set.
        if(!edgeMap.containsKey(p)) {
            edgeMap.put(p, new HashSet<E>());
        }
        edgeMap.get(p).add(edgeLabel);
        
        // Note this allows a node to be a child of itself
        nodes.get(node1).add(node2);
        //checkRep();
        
    }
    
    /** 
        @param node1 label of first node
        @param node2 label of second node
        @param edgelabel Label of edge to be removed from graph
        @modifies this
        @effects Removes the edge from node1 to node2 with edgeLabel from the graph
        @throws IllegalArgumentException if node1 or node2 are not in the graph
        @throws IllegalArgumentException if there is not an edge from
             node1 to node2 with edgeLabel
    */
    public void removeEdge(N node1, N node2, E edgeLabel) {
        Pair<N> p = new Pair<N>(node1,node2);
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new IllegalArgumentException("Node passed does not exist in the graph.");
        }
        if(!edgeMap.containsKey(p) || !edgeMap.get(p).contains(edgeLabel)) {
            throw new IllegalArgumentException("There does not exist an edge between passed nodes "
                    + "with label " + edgeLabel + ".");
        }
        
        edgeMap.get(p).remove(edgeLabel);
        nodes.get(node1).remove(node2);
        //checkRep();
    }
    
    /**
     * 
     * @return set containing names of all the nodes in the graph
     */
    public Set<N> getNodes() {
        return new HashSet<N>(nodes.keySet());
    }
    
    /** 
        @param node1 label of first node
        @param node2 label of second node
        @returns set of edge labels from node1 to node2 in the graph
    */
    public Set<E> getEdges(N node1, N node2) {
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new IllegalArgumentException("Node passed does not exist in the graph.");
        }
        Pair<N> p = new Pair<N>(node1,node2);
        
        // If there are no previous connections a set must be put
        // into the mapping for that pair
        if(!edgeMap.containsKey(p)) {
            edgeMap.put(p, new HashSet<E>());
        }
        //checkRep();
        return edgeMap.get(p);
    } 
    
    /** 
        @param node1 label of first node
        @param node2 label of second node
        @param edges set of edge labels for edges to be added to graph
        @modifies this
        @throws IllegalArgumentException if node1 or node2 are not in the graph
        @effects replaces the edges from node1 to node2 with the edges
            with edge labels from the the set edges
    */
    public void setEdges(N node1, N node2, Set<E> edges) {
        Pair<N> p = new Pair<N>(node1,node2);
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new IllegalArgumentException("Node passed does not exist in the graph.");
        }
        
        HashSet<E> newEdges = new HashSet<E>(edges);
        
        // if there is no set mapped to p, map p to a set.
        if(!edgeMap.containsKey(p)) {
            edgeMap.put(p, new HashSet<E>());
        }
        
        edgeMap.put(p, newEdges);
        // If new set is empty, we can no longer call node2
        // a child of node1, we can if new set is not empty.
        if(newEdges.isEmpty()) {
            nodes.get(node1).remove(node2);
        } else {
            nodes.get(node1).add(node2);
        }
        //checkRep();
    } 
    
    /** 
        @param nodeVal label of new node
        @modifies this
        @effects adds a node with label nodeVal to the graph
            starting with no edges connecting it to any other node.
        @throws IllegalArgumentException if there is already a node in 
            the graph with nodeVal
    */
    public void addNode(N nodeVal) {
       if(nodes.containsKey(nodeVal)) {
           throw new IllegalArgumentException("There already exists a node in the graph "
                   + "with label " + nodeVal + ".");
       }
       
       nodes.put(nodeVal,new HashSet<N>());
       //checkRep();

    }
    
    /** 
        @param nodeVal Label of node to be removed
        @modifies this
        @effects removes the node with label nodeVal from the graph,
            removing all edges too and from the node.
        @throws IllegalArgumentException if there is no node in 
            the graph with nodeVal
    */
    public void removeNode(String nodeVal) {
        if(!nodes.containsKey(nodeVal)) {
            throw new IllegalArgumentException("There does not exist a node in the graph "
                    + "with label " + nodeVal + ".");
        }
        
        // Copy keySet so as not to get a concurrent modification exception
        HashSet<Pair<N>> pairs = new HashSet<Pair<N>>(edgeMap.keySet());
        for(Pair<N> pair : pairs) {
            // Remove old connections to the node
            if(pair.first().equals(nodeVal) || pair.second().equals(nodeVal)) {
                edgeMap.remove(pair);
            }   
        }
        
        // remove nodeVal from keys of node set
        nodes.remove(nodeVal);
        
        // Copy keySet so as not to get a concurrent modification exception
        // Remove any references to nodeVal as a child
        HashSet<N> nodeNames = new HashSet<N>(nodes.keySet());
        for(N node : nodeNames) {
            nodes.get(node).remove(nodeVal);
        }
        
        
        //checkRep();
        
    }
    
    /** 
        @param oldLabel Label who's label is to be replaced
        @param newLabel New label for node with label oldLabel
        @modifies this
        @effects changes the label of the node with label oldLabel to newLabel
            preserves all connections the node had with any other nodes.
        @throws IllegalArgumentException if there is no node in 
            the graph with oldLabel
        @throws IllegalArgumentException if there already exists a node in 
            the graph with newLabel
    */
    public void setNode(N oldLabel, N newLabel) {
        if(!nodes.containsKey(oldLabel)) {
            throw new IllegalArgumentException("There does not exist a node in the graph "
                    + "with label " + oldLabel + ".");
        }
        if(nodes.containsKey(newLabel)) {
            throw new IllegalArgumentException("There already exists a node in the graph "
                    + "with label " + newLabel + ".");
        }
        
        addNode(newLabel);
        
        // Copy keySet so as not to get a concurrent modification exception
        HashSet<Pair<N>> keys = new HashSet<Pair<N>>(edgeMap.keySet());
        
        // Check if pair contains oldLabel, if it does then
        // take the edges between them and assign them to be between oldLabel
        // and the other node.
        for(Pair<N> pair : keys) {
            // Circular edge (from oldLabel to itself)
            if(pair.first().equals(oldLabel) && pair.second().equals(oldLabel)) {
                this.setEdges(newLabel, newLabel, this.getEdges(oldLabel, oldLabel));
                edgeMap.remove(pair);
                // Nodes is now a child of itself
                nodes.get(newLabel).add(newLabel);
            }
            // From oldLabel to other node
            else if(pair.first().equals(oldLabel)) {
                this.setEdges(newLabel, pair.second(), this.getEdges(oldLabel, pair.second()));
                edgeMap.remove(pair);
                // add to children of newLabel
                nodes.get(newLabel).add(pair.second());
            // From other node to oldLabel
            } else if(pair.second().equals(oldLabel)) {
                this.setEdges(pair.first(), newLabel, this.getEdges(pair.first(), oldLabel));
                edgeMap.remove(pair);
            }
        }
        
        nodes.remove(oldLabel);
        //checkRep();
        
    }
    
    /**
     * 
     * @param nodeVal Label of node to get children of
     * @return set of node names of children of nodeVal
     * @throws IllegalArgumentException if there is no node in 
            the graph with oldLabel
     */
    public Set<N> getChildren(N nodeVal) {
        
        if(!nodes.containsKey(nodeVal)) {
            throw new IllegalArgumentException("There does not exist a node in the graph "
                    + "with label " + nodeVal + ".");
        }
        
        return new HashSet<N>(nodes.get(nodeVal));
    }
    
    /**
        @param o g2 Graph to be compared with this
        @returns true iff g1 has the same nodes as g2,
             has the same edges pointing to the same nodes as g2
             and is also a graph.
    */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Graph)) {
            return false;
        }
        
        Graph<?,?> g = (Graph<?,?>)o;
        return this.nodes.equals(g.nodes) && this.edgeMap.equals(g.edgeMap);
    }
    
    /**
     * 
     * @return true iff this  has no nodes and edges
     */
    public boolean isEmpty() {
        return nodes.isEmpty();
    }
    
    /**
     * 
     * @return number of nodes in the graph
     */
    public int size() {
        return nodes.keySet().size();
    }
    
    /**
     * Confirms the representation is holding by making
     * sure none of the nodes or node pairs are null.
     */
    private void checkRep() {
    
        for(Pair<N> key : edgeMap.keySet()) {
            assert(key.first() != null);
            assert(key.second() != null);
        }
        
        for(N node : nodes.keySet()) {
            assert(node != null);
        }
    
    }
    
}
