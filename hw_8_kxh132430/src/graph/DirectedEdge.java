package graph;
/*************************************************************************
 *  Compilation:  javac DirectedEdge.java
 *  Execution:    java DirectedEdge
 *
 *  Immutable weighted directed edge.
 *
 *************************************************************************/
/**
 *  The <tt>DirectedEdge</tt> class represents a weighted edge in an 
 *  {@link EdgeWeightedDigraph}. Each edge consists of two integers
 *  (naming the two vertices) and a real-value weight. The data type
 *  provides methods for accessing the two endpoints of the directed edge and
 *  the weight.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
// Now everything looks fine.
public class DirectedEdge { 
    private int u;
    private int v;
    private int w;
    /**
     * Initializes a directed edge from vertex <tt>v</tt> to vertex <tt>w</tt> with
     * the given <tt>weight</tt>.
     * @param v the tail vertex
     * @param w the head vertex
     * @param weight the weight of the directed edge
     * @throws java.lang.IndexOutOfBoundsException if either <tt>v</tt> or <tt>w</tt>
     *    is a negative integer
     * @throws IllegalArgumentException if <tt>weight</tt> is <tt>NaN</tt>
     */
    public DirectedEdge(int u, int v, int w) {
        if (u <= 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (v <= 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        //if (Integer.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.u = u;
        this.v = v;
        this.w = w;
    }
    
    //testing.
    DirectedEdge(DirectedEdge e){
        this.u = e.u;
        this.v = e.v;
        this.w = e.w;
    }
    
    public void setFrom(int from){
    	this.u = from;
    }
    
    public void setTo(int to){
    	this.v = to;
    }
    
    /**
     * Returns the tail vertex of the directed edge.
     * @return the tail vertex of the directed edge
     */
    public int from() {
        return u;
    }

    /**
     * Returns the head vertex of the directed edge.
     * @return the head vertex of the directed edge
     */
    public int to() {
        return v;
    }

    /**
     * Returns the weight of the directed edge.
     * @return the weight of the directed edge
     */
    public int weight() {
        return w;
    }

    public void setWeight(int weight) {
        this.w = weight;
    }
    
    /**
     * Returns a string representation of the directed edge.
     * @return a string representation of the directed edge
     */
    public String toString() {
        return u + "->" + v + " " + w;
    }

}
