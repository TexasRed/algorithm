import java.util.Queue;
import java.util.Stack;

import graph.DirectedEdge;
import graph.EdgeWeightedDigraph;
import graph.In;
import graph.IndexMinPQ;
import graph.StdOut;


public class kxh132430_P8 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		In in = new In("G:\\test\\test.txt");
		
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
		
		StdOut.println(G);
		
		if(G.negative() == false){
			
			Dijkstra dsp = new Dijkstra(G);
			
			dsp.printPath(G);
			
		} else {
			
			BellmanFord bmf = new BellmanFord(G);
			
			bmf.printPath(G);
			
		}
		
	}
	
	
}

class Dijkstra{
	
	private int[] dist;        
	private int[] pred;
	private int[] count;
	private IndexMinPQ<Integer> pq;
	
	Dijkstra(EdgeWeightedDigraph G){
		int s = G.S();
		dist = new int[G.V() + 1];
		pred = new int[G.V() + 1];
		count = new int[G.V() + 1];
		
		// Initialization
		for (int u = 1; u <= G.V(); u++){
			dist[u] = Integer.MAX_VALUE;;
			count[u] = 0;
		}
		dist[s] = 0;
		count[s] = 1;

		// relax vertices in order of distance from s
		pq = new IndexMinPQ<Integer>(G.V());
		pq.insert(s, dist[s]);
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			for (DirectedEdge e : G.adj(v))
				relax(e);
		}
	}

    // relax edge e and update pq if changed
    private void relax(DirectedEdge e) {
        int u = e.from(), v = e.to();
        if (dist[v] > dist[u] + e.weight()) {
            dist[v] = dist[u] + e.weight();
            pred[v] = u;
            count[v] = count[u];
            if (pq.contains(v)) 
            	pq.decreaseKey(v, dist[v]);
            else                
            	pq.insert(v, dist[v]);
        } else {
        	count[v] += count[u];
        }
    }

    /**
     * Is there a path from the source vertex <tt>s</tt> to vertex <tt>v</tt>?
     * @param u the destination vertex
     * @return <tt>true</tt> if there is a path from the source vertex
     *    <tt>s</tt> to vertex <tt>v</tt>, and <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int u) {
        return dist[u] < Integer.MAX_VALUE;
    }
    
    public boolean hasPred(int u) {
        return pred[u] != 0;
    }
    
    public void printPath(EdgeWeightedDigraph G){
    // print shortest path
    	for(int u = 1; u <= G.V(); u++){
    		String distance = hasPathTo(u)? String.valueOf(dist[u]) : "INF";
    		String predecessor = hasPred(u)? String.valueOf(pred[u]) : "-";
    		System.out.println(u + " " + distance + " " + predecessor + " " + count[u]);
    	}
    }    

}

class BellmanFord{
	private int[] dist;              
    private int[] pred;
    private int[] count;
    private boolean[] onQueue;             // onQueue[v] = is v currently on the queue?
    private Queue<Integer> queue;          // queue of vertices to relax
    private int cost;                      // number of calls to relax()
    private Iterable<DirectedEdge> cycle;  // negative cycle (or null if no such cycle)

    BellmanFord(EdgeWeightedDigraph G){
    	int s = G.S();
		dist = new int[G.V() + 1];
		pred = new int[G.V() + 1];
		count = new int[G.V() + 1];
    	onQueue = new boolean[G.V() + 1];
    	for (int u = 1; u <= G.V(); u++){
    		dist[u] = Integer.MAX_VALUE;
    		count[u] = 0;
    	}
    	dist[s] = 0;
    	count[s] = 1;

    	// Bellman-Ford algorithm
    	queue = new Queue<Integer>();
    	queue.enqueue(s);
    	onQueue[s] = true;
    	while (!queue.isEmpty() && !hasNegativeCycle()) {
    		int v = queue.dequeue();
    		onQueue[v] = false;
    		relax(G, v);
    	}
    }

    // relax vertex v and put other endpoints on queue if changed
    private void relax(EdgeWeightedDigraph G) {
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if (!onQueue[w]) {
                    queue.enqueue(w);
                    onQueue[w] = true;
                }
            }
            if (cost++ % G.V() == 0)
                findNegativeCycle();
        }
    }

    /**
     * Is there a negative cycle reachable from the source vertex <tt>s</tt>?
     * @return <tt>true</tt> if there is a negative cycle reachable from the
     *    source vertex <tt>s</tt>, and <tt>false</tt> otherwise
     */
    public boolean hasNegativeCycle() {
        return cycle != null;
    }
	
}