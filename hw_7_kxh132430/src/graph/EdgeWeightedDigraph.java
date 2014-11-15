package graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;

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
    private int V;
    private int E;
    private int S;
	private ArrayList<DirectedEdge>[] outgoing;
    private ArrayList<DirectedEdge>[] incoming;
    private int[] outCyc;
    private int[] inCyc;
    
    public int getE() {
		return E;
	}

	public void setE(int e) {
		E = e;
	}

	public int getV() {
		return V;
	}

	public void setV(int v) {
		V = v;
	}

    
    public int[] getOutCyc() {
		return outCyc;
	}

	public void setOutCyc(int[] outCyc) {
		this.outCyc = outCyc;
	}

	public int[] getInCyc() {
		return inCyc;
	}

	public void setInCyc(int[] inCyc) {
		this.inCyc = inCyc;
	}

	public int getOldVertex(int x){
		return outCyc[incoming[x].get(0).from()];
	}
	
	public int getParent(int x){
		return incoming[x].get(0).from();
	}
	
	public int getChild(int x){
		if(outgoing[x] == null)
			return -1;
		else 
			return outgoing[x].get(0).to();
	}
	
	public ArrayList<DirectedEdge>[] getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(ArrayList<DirectedEdge>[] outgoing) {
		this.outgoing = outgoing;
	}

	public ArrayList<DirectedEdge>[] getIncoming() {
		return incoming;
	}

	public void setIncoming(ArrayList<DirectedEdge>[] incoming) {
		this.incoming = incoming;
	}

	
	public void deleteEdgesFrom(int v){
		
		//E -= (outgoing[v].size() + incoming[v].size());
		
		for(DirectedEdge e : outgoing[v]){
			
			e.setForbidden(true);
			
		}
		
		for(DirectedEdge e : incoming[v]){
			
			e.setForbidden(true);
			
		}
		
		//outgoing[v].clear();
		//incoming[v].clear();
	}
	
	public void updateCycle(int v, DirectedEdge minIn, DirectedEdge minOut){
		
		deleteEdgesFrom(v);
		
		if(minIn != null){
			outCyc[minIn.from()] = v;

			inCyc[v] = minIn.from();//
		}
		
		if(minOut != null){
			outCyc[v] = minOut.to();//

			inCyc[minOut.to()] = v;
		}
	}
	
	public void restoreEdge(int u, int v){
		
		for(DirectedEdge e : incoming[v]){
			if(e.from() == u)
				e.setForbidden(false);
				e.setWeight(0);
		}
		for(DirectedEdge e : outgoing[u]){
			if(e.to() == v)
				e.setForbidden(false);
				e.setWeight(0);
		}
		
	}
	
	/**
     * Initializes an empty edge-weighted digraph with <tt>V</tt> vertices and 0 edges.
     * param V the number of vertices
     * @throws java.lang.IllegalArgumentException if <tt>V</tt> < 0
     */
    @SuppressWarnings("unchecked")
	public EdgeWeightedDigraph(int V) {
        if (V <= 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.V = V;
        this.E = 0;
        
        outgoing = (ArrayList<DirectedEdge>[]) new ArrayList[V * 2];
        incoming = (ArrayList<DirectedEdge>[]) new ArrayList[V * 2]; // Incoming edge
        outCyc = new int[V * 2];
        inCyc = new int[V * 2]; 
        
        for (int v = 1; v < V * 2; v++){
            outgoing[v] = new ArrayList<DirectedEdge>();
            incoming[v] = new ArrayList<DirectedEdge>(); // incoming edge
            outCyc[v] = -1;
            inCyc[v] = -1;
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
        // Get the source for the MDST
        S = in.readInt();
        
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            if (v <= 0 || v > V) throw new IndexOutOfBoundsException("vertex " + v + " is not between 1 and " + V);
            if (w <= 0 || w > V) throw new IndexOutOfBoundsException("vertex " + w + " is not between 1 and " + V);
            // weight are integers
            int weight = in.readInt();
            if(w != S){
            	addEdge(new DirectedEdge(v, w, weight));
            }
            else 
            	this.E += 1;
        }
        
    }

    /**
     * Initializes a new edge-weighted digraph that is a deep copy of <tt>G</tt>.
     * @param G the edge-weighted graph to copy
     */
    public EdgeWeightedDigraph(EdgeWeightedDigraph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 1; v <= G.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<DirectedEdge> reverse = new Stack<DirectedEdge>();
            for (DirectedEdge e : G.outgoing[v]) {
                reverse.push(e);
            }
            for (DirectedEdge e : reverse) {
                outgoing[v].add(e);
            }
        }
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
    
    public ArrayList<DirectedEdge>[] incoming() {
        return incoming;
    }
    
    public ArrayList<DirectedEdge>[] outgoing(){
        return outgoing;
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
        int v = e.from();
        int w = e.to();
        //validateVertex(v);
        //validateVertex(w);
        outgoing[v].add(e);
        incoming[w].add(e);
        
        E++;
        
    }

    

    /**
     * Returns the directed edges incident from vertex <tt>v</tt>.
     * @return the directed edges incident from vertex <tt>v</tt> as an Iterable
     * @param v the vertex
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<DirectedEdge> outgoing(int v) {
        validateVertex(v);
        return outgoing[v];
    }
    
    /**
     * Returns the incoming directed edges to vertex <tt>v</tt>.
     * @return the incoming directed edges to vertex <tt>v</tt> as an Iterable
     * @param v the vertex
     * @throws java.lang.IndexOutOfBoundsException unless 1 <= v <= V
     */
    public Iterable<DirectedEdge> incoming(int v) {
        validateVertex(v);
        return incoming[v];
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
            for (DirectedEdge e : outgoing(v)) {
                list.add(e);
            }
        }
        return list;
    } 

    /**
     * Returns the number of directed edges incident from vertex <tt>v</tt>.
     * This is known as the <em>outdegree</em> of vertex <tt>v</tt>.
     * @return the number of directed edges incident from vertex <tt>v</tt>
     * @param v the vertex
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
     */
    public int outdegree(int v) {
        validateVertex(v);
        return outgoing[v].size();
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
        
        s.append(V + " " + E + " " + S + NEWLINE);
        for (int v = 1; v <= V; v++) {
            s.append(v + ": ");
            for (DirectedEdge e : outgoing[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        
        s.append("\nIncoming: \n");
        
        for (int v = 1; v <= V; v++) {
            s.append(v + ": ");
            for (DirectedEdge e : incoming[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        
        return s.toString();
    }
    
}
