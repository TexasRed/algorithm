import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import graph.DirectedEdge;
import graph.EdgeWeightedDigraph;
import graph.EdgeWeightedDirectedCycle;
import graph.In;
import graph.StdOut;

public class kxh132430_P7 {

	public static void main(String[] args) {

		// Test input files
		In in = new In("G:\\test\\p7-in0.txt");
		
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
		
		StdOut.println(G);
		
		long mst = findMST(G, null);
		
		StdOut.println("Result:\n" + G);
		
		StdOut.println(mst);
		
		
	}

	static long findMST(EdgeWeightedDigraph G, HashSet<Integer>cycleSet){

		int total_cost = 0;
		
		total_cost += transformWeight(G, cycleSet);
		
		StdOut.println(G);
		
		EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);

		if (finder.hasCycle()) {
			
			HashSet<Integer> newCycleSet = finder.cycleSet();
			
			int x = shrinkCycle(G, newCycleSet);
			
			//System.out.println("cycle shrinked:\n" + G);

			total_cost += findMST(G, newCycleSet);
			
			expandCycle(G, x, finder.getCycle());
			
		}
		
		//StdOut.println(G);
		 
		return total_cost;
	}
	
    static long transformWeight(EdgeWeightedDigraph G, HashSet<Integer> cycleSet){
    	
    	int cost = 0;
    	
    	for (int v = 1; v <= G.V(); v++) {
    		
    		if(v != G.S()){
    			
    			int minEdgeWeight = Integer.MAX_VALUE;

    			for (DirectedEdge e : G.incoming(v)) {
    				if(e != null && !(e.isForbidden())){
    					if(e.weight() < minEdgeWeight)
    						minEdgeWeight = e.weight();
    				}
    			}

    			if(minEdgeWeight != Integer.MAX_VALUE){
    				for (DirectedEdge e : G.incoming(v)) {
    					if(e != null &&!(e.isForbidden())){
    						e.setWeight(e.weight() - minEdgeWeight);					
    					}
    				}
    				cost += minEdgeWeight;
    			}
    		}
        }
    	
    	return cost;
    }
    
    //TODO
    static int shrinkCycle(EdgeWeightedDigraph G, HashSet<Integer> cycleSet){

    	int x = G.V() + 1;
    	
    	G.setV(x);
    	
    	/*
    	int minInWeight = Integer.MAX_VALUE;	
    	DirectedEdge In= null;
    	
    	for(Integer v : cycleSet){
    		
    		for(DirectedEdge e: G.incoming(v)) {
    			
    			if(cycleSet.contains(e.from()) == false) {
    				//e.setForbidden(true); // Delete the edges (a,u)
    				if(minInWeight > e.weight()) {
    					minInWeight = e.weight();
    					In = e;
    				}
    			}
    		}
    	}	
    		
    		
    		DirectedEdge newMinIn = new DirectedEdge(In);
			
			newMinIn.setW(G.V());
			
			System.out.println("newMinIn->->->: " + newMinIn);
			
			G.addEdge(newMinIn);
    	
	    	
	    	for(int v = 1; v <= G.V(); v++){
	    		
	    		int minOutWeight = Integer.MAX_VALUE;	
		    	DirectedEdge Out= null;
	    		for(DirectedEdge e: G.incoming(v)) {
	    			
	    			if(cycleSet.contains(e.from())) {
	    				//e.setForbidden(true); // Delete the edges (a,u)
	    				if(minOutWeight > e.weight()) {
	    					minOutWeight = e.weight();
	    					Out = e;
	    				}
	    			}
	    		}
	    		DirectedEdge newMinOut = new DirectedEdge(Out);
				
				newMinOut.setW(G.V());
				
				System.out.println("newMinIn->->->: " + newMinOut);
				
				G.addEdge(newMinOut);
				
				for(Integer v1 : cycleSet){
		    		G.updateCycle(v1, newMinIn, newMinOut);
		    	}
	    		
	    	}	
	    	
	    	*/
			
    	for(Integer v : cycleSet){
    		
    		DirectedEdge minIn = findMinIn(G.incoming()[v], cycleSet);

    		if(minIn != null){
    			
    			DirectedEdge newMinIn = new DirectedEdge(minIn);
    			
    			newMinIn.setW(G.V());
    			
    			G.addEdge(newMinIn);
    			
    		}

    		DirectedEdge minOut = findMinOut(G.outgoing()[v], cycleSet);

    		if(minOut != null){
    			
    			DirectedEdge newMinOut = new DirectedEdge(minOut);
    			
    			newMinOut.setV(G.V());

    			G.addEdge(newMinOut);
    			
    		}

    		G.updateCycle(v, minIn, minOut);
    	}
    	
    	return x;
    }

    //TODO
    static void expandCycle(EdgeWeightedDigraph G, int x, Stack<DirectedEdge> cycle){

    	int u = G.getParent(x);
    	
    	int a = G.getOutCyc()[u];
    	
    	System.out.print("u->a:" + u + "->" + a + "\n");
    	
    	G.restoreEdge(u, a);
    	
    	// expand a,u
    	
    	for(DirectedEdge e: G.outgoing(x)){
    		if(e.weight() == 0){
    			int b = G.getInCyc()[e.to()];
    			System.out.println("restore:" + b + "->" +e.to());
    			G.restoreEdge(b, e.to());
    		}
    	}
    	
        while(!(cycle.isEmpty())){
    		DirectedEdge e = cycle.pop();
    		if(e.to() != a){
    			e.setForbidden(false);
    			//G.setE(G.getE() + 1);
    		}
    	}
        
        G.deleteEdgesFrom(x);
    }
    
    static DirectedEdge findMinIn(ArrayList<DirectedEdge> incoming, HashSet<Integer> cycleSet){
    	
    	int minInWeight = Integer.MAX_VALUE;	
    	DirectedEdge minInEdge = null;
    	
    	for(DirectedEdge e: incoming) {
    			e.setForbidden(true); // Delete the edges (a,u)
    			if(cycleSet.contains(e.from()) == false) {
    				if(minInWeight > e.weight()) {
    					minInWeight = e.weight();
    					minInEdge = e;
    				}
    			}                        
    	}
		return minInEdge;
    }
    
    static DirectedEdge findMinOut(ArrayList<DirectedEdge> outgoing, HashSet<Integer> cycleSet){

    	int minOutWeight = Integer.MAX_VALUE;	
    	DirectedEdge minOutEdge = null;

    	for(DirectedEdge e: outgoing) {
    		e.setForbidden(true); // Delete the edges (a,u)
    		if(cycleSet.contains(e.to()) == false) {
    			if(minOutWeight > e.weight()) {
    				minOutWeight = e.weight();
    				minOutEdge = e;
    			}
    		}                        
    	}
    	return minOutEdge;
    }

    
}
