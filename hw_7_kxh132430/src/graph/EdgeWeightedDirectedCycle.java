package graph;
import java.util.HashSet;
import java.util.Stack;

/*************************************************************************
 *  Compilation:  javac EdgeWeightedDirectedCycle.java
 *  Execution:    java EdgeWeightedDirectedCycle V E F
 *  Dependencies: EdgeWeightedDigraph.java DirectedEdge Stack.java
 *
 *  Finds a directed cycle in an edge-weighted digraph.
 *  Runs in O(E + V) time.
 *
 *
 *************************************************************************/

/**
 *  The <tt>EdgeWeightedDirectedCycle</tt> class represents a data type for 
 *  determining whether an edge-weighted digraph has a directed cycle.
 *  The <em>hasCycle</em> operation determines whether the edge-weighted
 *  digraph has a directed cycle and, if so, the <em>cycle</em> operation
 *  returns one.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the <em>hasCycle</em> operation takes constant time;
 *  the <em>cycle</em> operation takes time proportional
 *  to the length of the cycle.
 *  <p>
 *  See {@link Topological} to compute a topological order if the edge-weighted
 *  digraph is acyclic.
 *  <p>
 *  For additional documentation, see <a href="/algs4/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class EdgeWeightedDirectedCycle {
    private boolean[] marked;             // marked[v] = has vertex v been marked?
    private DirectedEdge[] edgeTo;        // edgeTo[v] = previous edge on path to v
    private boolean[] onStack;            // onStack[v] = is vertex on the stack?
    private Stack<DirectedEdge> cycle;    // directed cycle (or null if no such cycle)
    private HashSet<Integer> cycleSet;
    /**
     * Determines whether the edge-weighted digraph <tt>G</tt> has a directed cycle and,
     * if so, finds such a cycle.
     * @param G the edge-weighted digraph
     */
    public EdgeWeightedDirectedCycle(EdgeWeightedDigraph G) {
        marked  = new boolean[G.V() + 1];
        onStack = new boolean[G.V() + 1];
        edgeTo  = new DirectedEdge[G.V() + 1];
        cycleSet = new HashSet<Integer>();
        
        for (int v = 1; v <= G.V(); v++)
            if (!marked[v]) dfs(G, v);

        // check that digraph has a cycle
        assert check(G);
    }
    
    // check that algorithm computes either the topological order or finds a directed cycle
    private void dfs(EdgeWeightedDigraph G, int v) {
    	onStack[v] = true;
    	marked[v] = true;

    	for (DirectedEdge e : G.outgoing(v)) {
    		if(e != null && !(e.isForbidden())){
    			if(e.weight() == 0){ // Visit the 0-weight edge

    				int w = e.to();

    				// short circuit if directed cycle found
    				if (cycle != null) return;

    				//found new vertex, so recur
    				else if (!marked[w]) {
    					edgeTo[w] = e;
    					dfs(G, w);
    				}

    				// trace back directed cycle
    				else if (onStack[w]) {
    					cycle = new Stack<DirectedEdge>();
    					while (e.from() != w) {
    						//Add the vertex in the cycle to a set
    						cycleSet.add(e.from());
    						e.setForbidden(true);
    						cycle.push(e);

    						e = edgeTo[e.from()];
    					}
    					//Add the vertex in the cycle to a set
    					cycleSet.add(e.from());

    					// Mark the edge to be contracted as in cycle
    					e.setForbidden(true);
    					cycle.push(e);
    				}
    			}
    		}
    	}

        onStack[v] = false;
    }

    /**
     * Does the edge-weighted digraph have a directed cycle?
     * @return <tt>true</tt> if the edge-weighted digraph has a directed cycle,
     * <tt>false</tt> otherwise
     */
    public boolean hasCycle() {
        return cycle != null;
    }

    /**
     * Returns a directed cycle if the edge-weighted digraph has a directed cycle,
     * and <tt>null</tt> otherwise.
     * @return a directed cycle (as an iterable) if the edge-weighted digraph
     *    has a directed cycle, and <tt>null</tt> otherwise
     */
    public Iterable<DirectedEdge> cycle() {
        return cycle;
    }
    
    
    public Stack<DirectedEdge> getCycle() {
		return cycle;
	}

	public void setCycle(Stack<DirectedEdge> cycle) {
		this.cycle = cycle;
	}

	public HashSet<Integer> cycleSet() {
        return cycleSet;
    }
    
    // certify that digraph is either acyclic or has a directed cycle
    private boolean check(EdgeWeightedDigraph G) {

        // edge-weighted digraph is cyclic
        if (hasCycle()) {
            // verify cycle
            DirectedEdge first = null, last = null;
            for (DirectedEdge e : cycle()) {
                if (first == null) first = e;
                if (last != null) {
                    if (last.to() != e.from()) {
                        System.err.printf("cycle edges %s and %s not incident\n", last, e);
                        return false;
                    }
                }
                last = e;
            }

            if (last.to() != first.from()) {
                System.err.printf("cycle edges %s and %s not incident\n", last, first);
                return false;
            }
        }
        return true;
    }

}