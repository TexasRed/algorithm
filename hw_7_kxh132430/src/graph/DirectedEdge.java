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

public class DirectedEdge { 
    private int v;
    private int w;
    private int weight;
	private boolean forbidden;
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
    public DirectedEdge(int v, int w, int weight) {
        if (v <= 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (w <= 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        //if (Integer.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.weight = weight;
        this.forbidden = false;
    }
    
    public DirectedEdge(DirectedEdge e){
        this.v = e.v;
        this.w = e.w;
        this.weight = e.weight;
        this.forbidden = false;
    }
    
    public void setV(int v){
    	this.v = v;
    }
    
    public void setW(int w){
    	this.w = w;
    }
    
    /**
     * Returns the tail vertex of the directed edge.
     * @return the tail vertex of the directed edge
     */
    public int from() {
        return v;
    }

    /**
     * Returns the head vertex of the directed edge.
     * @return the head vertex of the directed edge
     */
    public int to() {
        return w;
    }

    /**
     * Returns the weight of the directed edge.
     * @return the weight of the directed edge
     */
    public int weight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    public boolean isForbidden() {
 		return forbidden;
 	}

 	public void setForbidden(boolean forbidden) {
 		this.forbidden = forbidden;
 	}
 	
    /**
     * Returns a string representation of the directed edge.
     * @return a string representation of the directed edge
     */
    public String toString() {
        return v + "->" + w + " " + weight + " " + forbidden;
    }

    /**
     * Unit tests the <tt>DirectedEdge</tt> data type.
     *
    public static void main(String[] args) {
        DirectedEdge e = new DirectedEdge(12, 23, 3);
        StdOut.println(e);
    }*/
}
