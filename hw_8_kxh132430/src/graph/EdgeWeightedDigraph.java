package graph;
import java.util.ArrayList;

/*************************************************************************
 *  Compilation:  javac EdgeWeightedDigraph.java
 *  Execution:    java EdgeWeightedDigraph V E
 *  Dependencies: Bag.java DirectedEdge.java
 *
 *  An edge-weighted digraph, implemented using adjacency lists.
 *
 *************************************************************************/

/**
 *  The <tt>EdgeWeightedDigraph</tt> class represents a edge-weighted
 *  digraph of vertices named 0 through <em>V</em> - 1, where each
 *  directed edge is of type {@link DirectedEdge} and has a real-valued weight.
 *  It supports the following two primary operations: add a directed edge
 *  to the digraph and iterate over all of edges incident from a given vertex.
 *  It also provides
 *  methods for returning the number of vertices <em>V</em> and the number
 *  of edges <em>E</em>. Parallel edges and self-loops are permitted.
 *  <p>
 *  This implementation uses an adjacency-lists representation, which 
 *  is a vertex-indexed array of @link{Bag} objects.
 *  All operations take constant time (in the worst case) except
 *  iterating over the edges incident from a given vertex, which takes
 *  time proportional to the number of such edges.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class EdgeWeightedDigraph {
    protected int V;
    protected int E;
    protected int S;
    protected int T;
	protected ArrayList<DirectedEdge>[] adj;
	protected boolean negativeEdge;
    
	/**
     * Initializes an empty edge-weighted digraph with <tt>V</tt> vertices and 0 edges.
     * param V the number of vertices
     * @throws java.lang.IllegalArgumentException if <tt>V</tt> < 0
     */
    @SuppressWarnings("unchecked")
	public EdgeWeightedDigraph(int V) {
        if (V < 3 || V > 1000) throw new IllegalArgumentException("Number of vertices in a Digraph must be within 3 to 1000");
        this.V = V;
        this.E = 0;
        
        adj = (ArrayList<DirectedEdge>[]) new ArrayList[V + 1];
        for (int v = 1; v <= V; v++){
            adj[v] = new ArrayList<DirectedEdge>();
        }
    }
    
    /**  
     * Initializes an edge-weighted digraph from an input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     * @param StdIn the input stream
     * @throws java.lang.IndexOutOfBoundsException if the endpoints of any edge are not in prescribed range
     * @throws java.lang.IllegalArgumentException if the number of vertices or edges is negative
     */
    public EdgeWeightedDigraph(In in) {
    	this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        this.S = in.readInt();
        this.T = in.readInt();
        if(E > 100000) 
        	E = 100000;
        for (int i = 1; i <= E; i++) {
            
        	int u = in.readInt();
            
            int v = in.readInt();
            
            if (u <= 0 || u > V) throw new IndexOutOfBoundsException("vertex " + u + " is not between 1 and " + V);
            
            if (v <= 0 || v > V) throw new IndexOutOfBoundsException("vertex " + v + " is not between 1 and " + V);

            int w = in.readInt();
            
            if(w < 0) {
            	this.negativeEdge = true;
            }
            
            addEdge(new DirectedEdge(u, v, w));
            
        }
        
    }

    public boolean hasNegativeEdge() {
		return negativeEdge;
	}
    
    /**
     * Returns the number of vertices in the edge-weighted digraph.
     * @return the number of vertices in the edge-weighted digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in the edge-weighted digraph.
     * @return the number of edges in the edge-weighted digraph
     */
    public int E() {
        return E;
    }
    
   
    /**
     * Returns the source in the edge-weighted digraph.
     * @return the source in the edge-weighted digraph
     */
    public int S() {
        return S;
    }
    
    /**
     * Returns the target in the edge-weighted digraph.
     * @return the target in the edge-weighted digraph
     */
    public int T() {
        return T;
    }
    
    public ArrayList<DirectedEdge>[] adj(){
        return adj;
    }

    /**
     * Returns the directed edges incident from vertex <tt>v</tt>.
     * @return the directed edges incident from vertex <tt>v</tt> as an Iterable
     * @param v the vertex
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<DirectedEdge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }
    
    
    /**
     * Returns all directed edges in the edge-weighted digraph.
     * To iterate over the edges in the edge-weighted graph, use foreach notation:
     * <tt>for (DirectedEdge e : G.edges())</tt>.
     * @return all edges in the edge-weighted graph as an Iterable.
     */
    public Iterable<DirectedEdge> edges() {
    	ArrayList<DirectedEdge> list = new ArrayList<DirectedEdge>();
        for (int v = 1; v <= V; v++) {
            for (DirectedEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    } 

    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validateVertex(int v) {
        if (v <= 0 || v > V)
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Adds the directed edge <tt>e</tt> to the edge-weighted digraph.
     * @param e the edge
     * @throws java.lang.IndexOutOfBoundsException unless endpoints of edge are between 1 and V
     */
    public void addEdge(DirectedEdge e) {
        int u = e.from();
        int v = e.to();
        validateVertex(u);
        validateVertex(v);
        adj[u].add(e);
        this.E += 1;
    }
    
    
    /**
     * Returns a string representation of the edge-weighted digraph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *   followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        
        s.append("\nGraph: \n");
        
        s.append(V + " " + E + " " + S + " " + T + " " + negativeEdge + NEWLINE);
        for (int v = 1; v <= V; v++) {
            s.append(v + ": ");
            for (DirectedEdge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        
        return s.toString();
    }
    
}
