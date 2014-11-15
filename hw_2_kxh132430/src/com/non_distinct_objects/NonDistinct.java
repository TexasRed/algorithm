/**
 * @(#)NonDistinct.java        1.0 	09/14/2014
 */
package com.non_distinct_objects;

import java.util.Arrays;
import java.util.Scanner;

/**
 * The NonDistinct class visits all permutations of n (not necessarily
 * distinct) objects. The program reads from its standard input (stdin)
 * and writes the output to stdout.
 * 
 * @version 	1.0 September 14th, 2014
 * @author 		KAI HUANG
 */
public class NonDistinct {

	/**.
	 * @param args[0]		n is the total number of objects
	 * @param args[1]		v is the verbose
	 * @return
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 int n = Integer.parseInt(args[0]);	//n is between 3 and 1000; 
	     int v = Integer.parseInt(args[1]);	//v is in {0,1}.
	     
	     int [] A = new int [n+1];	// 	The array that holds the data
	     A[0] = -1;	// Introduce a sentinel A[0], which is strictly less than the largest element in A[1...n].
	     
	     System.out.println("Please enter " + n +" integers, separated by space or newline:");
	     
	     // Get the next n integers from the input (separated by spaces and/or newlines)
	     
	     Scanner in = new Scanner(System.in);	//Create a Scanner to get input from keyboard
	     
	     for(int i = 1; i <= n; i++){
	   	    	 A[i] = in.nextInt();
	   	 }
	     
	     try {
			in.close();		// Close the Scanner
	     } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	     }
	    
	    // Generate the permutation with the n numbers from stdin 
	    System.out.println("\nThe permutations of " + n +" nondistinct integers are:");
	    
	    long startTime = 0, endTime = 0;	// The start and end time of running the algorithm
	    
	    long sum = 0;  // The total counts of all possible permutations
	    
	    //visit P(n, n) and get the number of all possible permutations
	    startTime = System.currentTimeMillis();
	    sum = permutation(A, n, v);
	    endTime = System.currentTimeMillis();
	   
	    // Print the total counts of all possible permutations and the time taken by the program in milliseconds.
		System.out.println(sum + " " +(endTime - startTime));
		
	}
	
		/**
		 * visit  					visit the elements after a new permutation is generated
		 * @param A					A is the array that contains the data
		 * @param n					n is the number of elements to be visited
		 * @param verbose			verbose is the output mode
		 * @return count			count is the accumulative number of visits
		 */
		static long visit(int A[], int n, int verbose, long count){
			
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
		 * permutation		visit all permutations of a set of n non-distinct objects.
		 * @param A			A is the array that holds the data
		 * @param n			n is the total number of objects
		 * @param v			v is the verbose
		 * @return count	count is the number of all possible permutations in P(n,n)
		 */
		static long permutation(int [] A, int n, int v){
			
			long count = 0; // Let count be the number of all possible permutations in P(n,k)
			
			Arrays.sort(A); // Sort the array in lexical order
			
			do{
				count = visit(A, n, v, count); // visit A[1...n]
				
				// Find the max j with A[j] > A[j+1], scanning from right to left
				int j = n-1; 
				while(A[j] >= A[j+1]){
					if(j == 1) return count;	// If j=1 then break
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
				
			} while(true);	// forever looping until j=1
				
		}
		
	}	// end of class
