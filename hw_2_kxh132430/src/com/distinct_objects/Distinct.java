/**
 * @(#)Distinct.java        1.0 	09/14/2014
 */

package com.distinct_objects;

/**
 * The Distinct class visits all permutations/combinations of k objects
   from a set of n distinct objects, numbered 1..n. 
 * 
 * @version 	1.0 September 14th, 2014
 * @author 		KAI HUANG
 */

public class Distinct {

	static int N=0, K=0;	// Global variables to hold the value of n and k
	
	/**.
	 * @param args[0]		n is the total number of objects
	 * @param args[1]		k is the number of objects chosen from n
	 * @param args[2]		v is the verbose
	 * @return
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	     int n = Integer.parseInt(args[0]);		//n is between 3 and 1000; 
	     int k = Integer.parseInt(args[1]);		//k is an integer between 0 and n;
	     int v = Integer.parseInt(args[2]);		//v is in {0,1,2,3}.
	     
	     N = n; K = k;		// Copy n, k to N, K for the convenience of subsequent retrieval
	     
	     int [] A = new int [n+1];	// 	The array that holds the data
	     
	     long sum = 0;  // The total counts of all possible permutations/combinations
	     
	     long startTime=0, endTime=0;	// The start and end time of running the algorithm
	     
	     switch(v){	// Test verbose to determine the output mode
	     
	     	/* If v = 0, the program outputs one line with 2 integers:
	     	 * 		the number of permutations of k objects out of n distinct objects, 
	     	 * 		the time taken by the program in milliseconds. 
	     	 * */
	     	case 0: {
	     		
	     		// Initialize array A with 1...n 
	     	    for(int i = 1; i <= n; i++){
	   	    	 A[i] = i;
	   	        }
	     	    
	     	    A[0] = -1;	// Introduce a sentinel A[0], which is strictly less than the largest element in A[1...n].
	     	    
	     	    //visit P(n, k) and get the number of all possible permutations
	     	    startTime = System.currentTimeMillis();
	     	    sum = permutation(A, n, k, v); 	
	            endTime = System.currentTimeMillis();
	            
	     		break;
	     	}
	     	
	        /* If v = 1, the program again outputs one line with 2 integers:
	     	 * 		the number of permutations of k objects out of n distinct objects, 
	     	 *  	the time taken by the program in milliseconds. 
	     	 * */
	     	case 1: {
	     		
	     		// Initialize array A with {0}. When an object is selected, A[i] = 1; otherwise A[i] = 0
	   	        for(int i = 1; i <= n; i++){
	   	    	   A[i] = 0;
	   	        }
	   	        
	   	        // visit C(n, k) and get the number of all possible combinations
	   	        startTime = System.currentTimeMillis();
	   	        sum = combination(A, n, k, v);	
	            endTime = System.currentTimeMillis();
	            
	     		break;
	     	}
	     	
	     	/* If v = 2, the program should output:
	     	 * 		all the actual permutations, one line at a time, 
	     	 * 		and then the output of v=0.
	     	 * */
	     	case 2: {
	     		
	     		// Initialize array A with 1...n 
	     	    for(int i = 1; i <= n; i++){
	   	    	A[i] = i;
	     	    }
	     	    
	     	    A[0] = -1;	// Introduce a sentinel A[0], which is strictly less than the largest element A[n].
	     	   
	     	    //visit P(n, k) and get the number of all possible permutations
	     	    startTime = System.currentTimeMillis();
	     	    sum = permutation(A, n, k, v); 	
	            endTime = System.currentTimeMillis();
	            
	     	    break;
	     		
	     	}
	     	
	     	/* If v = 3, the program should output:
	     	 * 		all the actual combinations, one line at a time, 
	     	 * 		and then the output of v=1.
	     	 * */
	     	case 3: {
	     		
	     		// Initialize array A with {0}. When an object is selected, A[i] = 1; otherwise A[i] = 0
	     		for(int i = 1; i <= n; i++){
		   	    	   A[i] = 0;
		   	     }
	     		
	     		// visit C(n, k) and get the number of all possible combinations
	     		startTime = System.currentTimeMillis();
	     		sum = combination(A, n, k, v);    
		        endTime = System.currentTimeMillis();
		        
	     		break;
	     	}
	     	
	       default: break;
	     }
	     
	     // Print the total counts of all possible permutations/combinations and the time taken by the program in milliseconds.
	     System.out.println(sum + " " +(endTime - startTime));
	}
	
	/**
	 * combination 		visit all combinations of k objects from a set of n distinct objects, numbered 1..n.
	 * @param A			A is the array that holds  for each objects
	 * @param n			n is the total number of objects
	 * @param k			k is the number of objects chosen from n
	 * @param v			v is the verbose
	 * @return count    count is the number of all possible combinations in C(n,k)
	 */
	static long combination(int[] A, int i, int k, int v){
		
		// Let count, count_1, count_2 be the number of all possible combinations in C(n,k), C(n-1,k-1), and C(n-1,k), respectively
		long count=0, count_1=0, count_2 = 0; 
		
		// If k = 0, visit C(n, 0) and return count
		if(k == 0){ 
			return visit(A, N, v, count);
		} 
		// If i< k, then do nothing but return count
		else if(i < k) return count;
			 else {
				   A[i] = 1;	// Choose object i
			   	   count_1 = combination(A, i - 1, k - 1, v);	// Choose k-1 objects from the remaining i-1 objects
			   	   
			   	   A[i] = 0;	// Not choose object i
			   	   count_2 = combination(A, i - 1, k, v);	// Choose k objects from the remaining i-1 objects
			   	   
			   	   count = count_1 + count_2;	// get the number of all possible combinations in C(n,k)
			  }
		return count;
	}
	
	/**
	 * visit  					visit the elements after a new permutation/combination is generated
	 * @param A					A is the array that contains the data
	 * @param n					n is the number of elements to be visited
	 * @param verbose			verbose is the output mode
	 * @return count			count is the accumulative number of visits
	 */
	static long visit(int A[], int n, int verbose, long count){
		
	// If verbose mode = 3, print all the possible combinations
	   if (verbose == 3) {
			 for(int i = 1; i <= n; i++) {
				 if (A[i] != 0) System.out.print(i + " "); // print the chosen elements from 1...n
			 }
			 System.out.println();
	   }
	   
	// If verbose mode = 2, print all the possible permutations
	   else if (verbose == 2) {
			 for(int i = 1; i <= n; i++) {
				 System.out.print(A[i] + " "); // print all elements
			 }
			 System.out.println();
	   }
	   
	 //Otherwise do nothing but increment the count by 1 
	   return ++count;
	}

	/**
	 * swap 		swap the value of two elements A[i] and A[j]
	 * @param A		A is the array that contains the data		
	 * @param i		i is the index of the first element
	 * @param j		j is the index of the second element
	 * @return
	 */
	static void swap(int A[], int i, int j){
		int temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}
	
	/**
	 * permutation		visit all permutations of k objects from a set of n distinct objects, numbered 1..n.
	 * @param A			A is the array that holds the data
	 * @param n			n is the total number of objects
	 * @param k			k is the number of objects chosen from n
	 * @param v			v is the verbose
	 * @return count	count is the number of all possible permutations in P(n,k)
	 */
	static long permutation(int [] A, int n, int k, int v){
		
		long count = 0; // Let count be the number of all possible permutations in P(n,k)
		
		int current = 0; // Let current be the index where a swapping has just occurred
		
		do{
			// visit A[1...k] ONLY IF a distinct permutation is generated (a swapping occurs within [1...k])
			if(current <= k){
				count = visit(A, k, v, count);
			}
			
			// Find the max j with A[j] > A[j+1], scanning from right to left
			int j = n-1; 
			while(A[j] >= A[j+1]){
				// If j=1 then break
				if(j == 1) return count;
				j--;
			}
			
			// Find the max l with A[j] >= A[l], scanning from right to left
			int l = n;
			while(A[j] >= A[l]){
				l--;
			}
			
			// Exchange A[j] and A[l]
			swap(A, j, l);
			
			// Reverse A[j+1...n]
			for(int left = j+1, right = n; left < right ; left++, right--){
				swap(A, left, right);
			}
			
			current = j; // set current index to j
			
		} while(true);	// forever looping until j=1
			
	}
	
}	// end of class
