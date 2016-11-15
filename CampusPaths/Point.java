public class Point<N> {

    private final N first, second;
    
    // Representation: A Pair contains two objects (n1,n2)
    
    // Abstraction function: 
    // First and second represent the first and second objects
    // in the pair respectively.
    
    
    /**
     * @effects Constructs a new empty Pair
     */
    public Point() {
        this.first = null;
        this.second = null;
        
    }
    
    /** 
    @param first First object
    @param second Second object
    @effects Constructs a new Pair containing first and second
     */
    public Point(N first, N second) {
        
        
        this.first = first;
        this.second = second;
        checkRep();
        
    }
    
    /**
     * @returns true iff first.equals(second)
     */
    @Override
    public boolean equals(Object o) {
        if( !(o instanceof Point<?>)) {
            return false;
        }
        Point<?> p = (Point<?>) o;
        return first.equals(p.first) && second.equals(p.second);
        
    }
    
    /**
     * @returns a unique integer hashCode for each pair
     */
    @Override
    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }
    
    /**
     * @returns first object in the pair
     */
    public N first() {
        return first;
    }
    
    /**
     * @returns second object in the pair
     */
    public N second() {
        return second;
    }
    
    /**
     * @returns String representation of the Point
     * if an element is null then that element will be represented as the string "null"
     */
    @Override
    public String toString() {    
        if(first == null) {
            if(second == null) {
                return "( null null )";
            }
            return "( null " + second.toString() + ") ";
        }
        if(second == null) {
            return "( " + first.toString() + " null )";
        }
        return "( " + first.toString() + " " + second.toString() + ") ";
    }
    
    /**
     * Checks the representation invariant
     */
    public void checkRep() {
        assert(this.first == null || this.first instanceof Object);
        assert(this.second == null || this.second instanceof Object);
    }
    
}
