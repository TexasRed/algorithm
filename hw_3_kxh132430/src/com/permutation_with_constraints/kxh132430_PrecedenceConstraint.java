package com.permutation_with_constraints;

import java.util.Scanner;

/**
 * The kxh132430_PrecedenceConstriant class visit all the enumeration of permutations satisfying precedence constraints). The program reads from its standard input (stdin)
 * and writes the output to stdout.
 * 
 * @version 	1.0 September 22nd, 2014
 * @author 		KAI HUANG              section 013
 * 				VILAS REDDY PODDUTURI  section 016
 */
public class kxh132430_PrecedenceConstraint {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Initialize an Scanner object to get input from stdin
		Scanner scan = new Scanner(System.in);
		System.out.println("Please enter your input as: n c v");
		int n = scan.nextInt();		// n is the number of objects
		int c = scan.nextInt();		// c is the number of constraint pairs
		int v = scan.nextInt();		// v is the verbose

		// Initialize an adjacency matrix to store the constraints
		int[][] AdjMat = new int[n+1][n+1];

		// Get the constraint pairs from stdin and store them in an adjacency matrix 
		for(int k = 1, i = 0, j = 0; k <= c; k++) {
			i = scan.nextInt();
			j = scan.nextInt();
			AdjMat[i][j] = 1; // If i precedes j, set AdjMat[i][j] to 1
		}

		scan.close();

		int[] A = new int[n+1]; // Initialize array A to store the data

		long sum = 0;  // The total counts of all possible permutations/combinations

		long startTime=0, endTime=0;	// The start and end time of running the algorithm

		/* If verbose = 0, the program outputs one line with 2 integers:
		 * 		the number of permutations of k objects out of n distinct objects, 
		 * 		the time taken by the program in milliseconds. 
		 * */
		if( v == 0){
			startTime = System.currentTimeMillis();
			sum = permutate(A, AdjMat, n, n, 0); 	
			endTime = System.currentTimeMillis();
		}

		/* If v = 1, the program should output:
		 * 		all the actual permutations, one line at a time, 
		 * 		and then the output of v=0.
		 * */
		else {
			startTime = System.currentTimeMillis();
			sum = permutate(A, AdjMat, n, n, 1); 	
			endTime = System.currentTimeMillis();
		}

		// Print the total counts of all possible permutations and the time taken by the program in milliseconds.
		System.out.println(sum + " " +(endTime - startTime));

	}

	/**
	 * visit  					visit the elements after a new permutation/combination is generated
	 * @param A					A is the array that contains the data
	 * @param n					n is the number of elements to be visited
	 * @param verbose			verbose is the output mode
	 * @return count			count is the accumulative number of visits
	 */
	static long visit(int[] A,  int n, int verbose, long count){

		// If verbose mode > 0, print all the possible permutations
		if (verbose > 0) {
			for(int i = 1; i <= n; i++) {
				System.out.print(A[i] + " "); // print all elements
			}
			System.out.println();
		}
		//Otherwise do nothing but increment the count by 1 
		return ++count;
	}

	static long permutate(int []A, int[][] AdjMat, int i, int n, int v){

		long count = 0;

		if(i == 0) count = visit(A, n ,v, count);

		else{
			for(int k = 1; k <= n; k++){
				if(A[k] == 0){
					if(!isOrdered(A, AdjMat, i, k, n)) continue; // If put i in A[k] violates any constraints, skip this step.
					A[k] = i;
					count += permutate(A, AdjMat, i-1, n, v);
					A[k] = 0;
				}
			}
		}
		return count;
	}

	static boolean isOrdered(int[] A, int[][] AdjMat, int i, int k, int n){

		boolean ordered = true;

		for(int j = 1; j <= n; j++){
			int u = A[j];
			if(j < k){
				if(AdjMat[i][u] == 1){
					ordered = false;
					break;
				}
			}
			else{
				if(AdjMat[u][i] == 1){
					ordered = false;
					break;
				}
			}
		}
		return ordered;
	}

}
