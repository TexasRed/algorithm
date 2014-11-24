import graph.DirectedEdge;
import graph.EdgeWeightedDigraph;
import graph.EdgeWeightedDirectedCycle;
import graph.In;
import graph.IndexMinPQ;
import graph.Queue;


public class kxh132430_P8_a {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		In in = new In("G:\\test\\p8-data\\channel-50-2.txt");
		
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);

		EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);

		if(finder.hasCycle() == true){
			System.err.println("Non-positive cycle in graph. DAC is not applicable.");
			System.exit(-1);
		}

		long startTime = 0, endTime = 0;

		if(G.hasNegativeEdge()){

			startTime = System.currentTimeMillis();
			BellmanFord bmf = new BellmanFord(G);
			endTime = System.currentTimeMillis();

			bmf.printPath(G, (endTime - startTime));
			
		} else {
			
			startTime = System.currentTimeMillis();
			Dijkstra dsp = new Dijkstra(G);
			endTime = System.currentTimeMillis();

			dsp.printPath(G, (endTime - startTime));
			
		}
	}
}

class Dijkstra{

	private long[] dist;        
	int[] pred;
	private long[] count;
	private IndexMinPQ<Long> pq;

	Dijkstra(EdgeWeightedDigraph G){
		int s = G.S();
		dist = new long[G.V() + 1];
		pred = new int[G.V() + 1];
		count = new long[G.V() + 1];

		// Initialization
		for (int u = 1; u <= G.V(); u++){
			dist[u] = Long.MAX_VALUE;
			count[u] = 0;
		}
		dist[s] = 0;
		count[s] = 1;
		// relax vertices in order of distance from s
		pq = new IndexMinPQ<Long>(G.V() + 1);
		pq.insert(s, dist[s]);
		while (!pq.isEmpty()) {
			int u = pq.delMin();
			for (DirectedEdge e : G.adj(u))
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
		} else if(dist[v] == dist[u] + e.weight()){
			count[v] += count[u];
		}
	}

	public boolean hasPathTo(int u) {
		return dist[u] < Long.MAX_VALUE;
	}

	public boolean hasPred(int u) {
		return pred[u] != 0;
	}

	public void printPath(EdgeWeightedDigraph G, long time){
		// print shortest path
		int t = G.T();
		System.out.println(dist[t] + " " + count[t] + " " + time);

		if(G.V() <= 100){
			for(int u = 1; u <= G.V(); u++){
				String distance = hasPathTo(u)? String.valueOf(dist[u]) : "INF";
				String predecessor = hasPred(u)? String.valueOf(pred[u]) : "-";
				System.out.println(u + " " + distance + " " + predecessor + " " + count[u]);
			}
		}
	}   
	
}

class BellmanFord{
	private long[] dist;              
	private int[] pred;
	private long[] count;
	private boolean[] onQueue;             // onQueue[v] = is v currently on the queue?
	private Queue<Integer> queue;          // queue of vertices to relax
	private int[] num; // number of calls to relax()

	BellmanFord(EdgeWeightedDigraph G){

		//	Intialization
		int s = G.S();
		dist = new long[G.V() + 1];
		pred = new int[G.V() + 1];
		count = new long[G.V() + 1];
		num = new int[G.V() + 1];
		onQueue = new boolean[G.V() + 1];
		for (int u = 1; u <= G.V(); u++){
			dist[u] = Long.MAX_VALUE;
			count[u] = 0;
		}
		dist[s] = 0;
		count[s] = 1;

		// Bellman-Ford algorithm
		queue = new Queue<Integer>();
		queue.enqueue(s);
		onQueue[s] = true;
		while (!queue.isEmpty()) {
			int u = queue.dequeue();
			if((num[u]++) >= G.V()){
				throw new RuntimeException("Non-positive cycle in graph. DAC is not applicable.");
			}
			onQueue[u] = false;
			relax(G, u);
		}
	}

	// relax vertex v and put other endpoints on queue if changed
	private void relax(EdgeWeightedDigraph G, int u) {
		for (DirectedEdge e : G.adj(u)) {
			int v = e.to();

			if (dist[v] > dist[u] + e.weight()) {
				dist[v] = dist[u] + e.weight();
				pred[v] = u;
				count[v] = count[u];
				if (!onQueue[v]) {
					queue.enqueue(v);
					onQueue[v] = true;
				} 
			} else if(dist[v] == dist[u] + e.weight()){
				count[v] += count[u];
			}

		}
	}

	public boolean hasPathTo(int u) {
		return dist[u] < Long.MAX_VALUE;
	}

	public boolean hasPred(int u) {
		return pred[u] != 0;
	}

	public void printPath(EdgeWeightedDigraph G, long time){
		// print shortest path
		int t = G.T();
		System.out.println(dist[t] + " " + count[t] + " " + time);
		if(G.V() <= 100){ // Print only if V >= 100
			for(int u = 1; u <= G.V(); u++){
				String distance = hasPathTo(u)? String.valueOf(dist[u]) : "INF";
				String predecessor = hasPred(u)? String.valueOf(pred[u]) : "-";
				System.out.println(u + " " + distance + " " + predecessor + " " + count[u]);
			}
		}
	} 
}
