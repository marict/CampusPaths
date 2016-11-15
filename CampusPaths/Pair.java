public class Pair<N> {

    private final N first, second;
    
    // Representation: A Pair contains two string of characters (n1,n2) such that n1 != null and n2 != null;
    // Note I am referring to strings in the mathematical sense
    // ex: http://web.cse.ohio-state.edu/software/2221/web-sw1/extras/slides/20.String-Theory.pdf
    
    // Abstraction function: 
    // First and second represent the first and second strings of characters
    // in the pair respectively.
    
    /** 
    @param first First string of characters
    @param second Second string of characters
    @effects Constructs a new Pair containing first and second
     */
    public Pair(N first, N second) {
        
        
        this.first = first;
        this.second = second;
        
    }
    
    /**
     * @return true iff first.equals(second)
     */
    @Override
    public boolean equals(Object o) {
        if( !(o instanceof Pair<?>)) {
            return false;
        }
        Pair<?> p = (Pair<?>) o;
        return first.equals(p.first) && second.equals(p.second);
        
    }
    
    /**
     * @return a unique integer hashCode for each pair
     */
    @Override
    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }
    
    /**
     * @return first string of characters in the pair
     */
    public N first() {
        return first;
    }
    
    /**
     * @return second string of characters in the pair
     */
    public N second() {
        return second;
    }
    
  
}
