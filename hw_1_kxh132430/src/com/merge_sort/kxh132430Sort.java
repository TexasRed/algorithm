/**
 * @(#)kxh132430Sort.java        2.0 	09/05/2014
 * Course 				CS6301 	IMPL ADV DATA STRC & ALGORITHM
 * Section				013 
 * Project 				1
 * Name					Kai Huang
 * NetID				kxh132430
 */

package com.merge_sort;

import java.io.IOException;

/**
 * The kxh132430Sort class implements 3 versions of merge sort discussed in class
 * 
 * @version 	2.0 September 5th, 2014
 * @author 		KAI HUANG
 */
public class kxh132430Sort {
	
	/**
	 * MergeSort	Merge Sort Algorithm 1
	 */
	static void MergeSort(int[] A, int p, int r) {
		
		if (p < r) {
			if((r-p)>11){
				int q = p + ((r-p)>>1);	// Get the medium element by shifting
				MergeSort(A, p, q);
				MergeSort(A, q+1, r);
				Merge(A, p, q, r);
			} else {  // Insertion sort
				for(int i=p, j=i; i<r; j=++i) {
				    int ai = A[i+1];
				    while(ai < A[j]) {
				    	A[j+1] = A[j];
				    	if (j-- == p) {
				    		break;
				    	}
				    }
				    A[j+1] = ai;
				}
			 }
        }
    }

	/**
	 * Merge			Merge A[p...q] and A[q+1...r]
	 */
    static void Merge(int[] A, int p, int q, int r) {
    	
    	//Allocate dynamic memory for L and R within merge.
    	int ls = q-p+1;
    	int rs = r-q;
        int[] L = new int[ls];
        int[] R = new int[rs];
        
        //Copy A[p...q] into L[0...ls-1]; A[q+1,r] into R[0...rs-1]        
        for(int i=p; i<=q; i++){ 
        	L[i-p] = A[i];
        }
        
        for(int i=q+1; i<=r; i++){ 
        	R[i-(q+1)] = A[i];
        }
        
        //Merge the data in L and R back to A
        int i = 0; int j = 0;
        
        for(int k=p; k<=r; k++) {
        	if ((j>=rs) || ((i<ls) && (L[i] <= R[j])))
        		A[k] = L[i++];
        	else
        		A[k] = R[j++];
        }
        return;	
    }
    
    
   /**
    * AuxillaryMergeSort	Merge Sort Algorithm 2
    */
   static void AuxillaryMergeSort(int[] A, int[]B, int p, int r) {
       
	   if (p < r) {
		   
		   if((r-p)>11){ 
			   
			   int q = p + ((r-p)>>1);// Get the medium element by shifting
			   AuxillaryMergeSort(A, B, p, q);
			   AuxillaryMergeSort(A, B, q+1, r);
			   AuxillaryMerge(A, B, p, q, r);
			   
		   } else {  // Insertion sort
				for(int i=p, j=i; i<r; j=++i) {
				    int ai = A[i+1];
				    while(ai < A[j]) {
				    	A[j+1] = A[j];
				    	if (j-- == p) {
				    		break;
				    	}
				    }
				    A[j+1] = ai;
				}
		   	}
        }
    }

   /**
    * AuxillaryMerge	Merge A[p...q] and A[q+1...r] using a global auxiliary array.
    */
   static void AuxillaryMerge(int[] A, int[]B, int p, int q, int r) {
	   
	    //Data is first copied from A to global auxiliary array B
        for(int i=p; i<=r; i++){ 
        	B[i] = A[i];
        }
        
        //Merge the data in B back to A
        int i = p; int j = q+1;
        
        for(int k=p; k<=r; k++) {
        	if ((j>r) || ((i<=q) && (B[i] <= B[j])))
        		A[k] = B[i++];
        	else
        		A[k] = B[j++];
        }
        return;	
    }
  
    /**
     * AlternateMergeSort 	Merge Sort Algorithm 3
     */
   	static int AlternateMergeSort(int[] A, int[]B, int p, int r) {
      
   		if(p<r){
   			
			if((r-p)>11){
				
				int q = p + ((r-p)>>1); // Get the medium element by shifting
          
				int h1, h2;// Let h1 and h2 be the depths of the two sub-arrays in the recursion tree
          
				h1 = AlternateMergeSort(A, B, p, q);
				h2 = AlternateMergeSort(A, B, q+1, r);
      	  
				//If h1 is odd, copy data from B to A, otherwise from A to B
				if((h1&1)==1){
        	  
					if(h1!=h2){	// Copy A[q+1...r] to B[q+1...r], then merge with B[p...q]
						for(int i = q+1;i<=r;i++){
							B[i]=A[i];
						}
					}
					AlternateMerge(B, A, p, q, r);
				} else {
        	
					if(h1!=h2){	// Copy B[q+1...r] to A[q+1...r], then merge with A[p...q]
						for(int i = q+1;i<=r;i++){
							A[i]=B[i];
						} 
					}
					AlternateMerge(A, B, p, q, r);
				}
				
				return ++h1;	// return the depth of the merged array
				
			} else {  // Insertion sort
				for(int i=p, j=i; i<r; j=++i) {
					int ai = A[i+1];
					while(ai < A[j]) {
						A[j+1] = A[j];
						if (j-- == p) {
							break;
						}
					}
					A[j+1] = ai;
				}
				return 0;
			}
   		}
   		// If there is only one element, do nothing and return 0
   		return 0;
   	}

   	/**
   	 * AlternateMerge 	Merge the data from source array to destination array
   	 */
   	static void AlternateMerge(int[] src, int[]dest, int p, int q, int r) {
   		
   	  // Copy data from src to dest
      int i = p; int j = q+1;
      
      for(int k=p; k<=r; k++) {
    	  if ((j>r) || ((i<=q) && (src[i] <= src[j])))
    		  dest[k] = src[i++];
      	  else
      		  dest[k] = src[j++];
      }
      
      return;
   	}
 
   	public static void main(String[] args) throws IOException{
        
   		// Take integer n from command line as input
    	int n = Integer.parseInt(args[0]);
    	
    	// Creates an array A of size n and populates it with numbers in reverse sorted order.
    	int[] A = new int[n];
    	
        for (int i = 0; i < n; i++) {
            A[i] = n-i;
        }
        
        // Calculate the running time of the old MergeSort
        long startTime = System.currentTimeMillis();
        MergeSort(A,  0, n-1);
        long endTime = System.currentTimeMillis();
        
        // Test whether the algorithm 1 succeeded in sorting the array.
        for (int j = 0; j < A.length-1; j++) {
            if(A[j] > A[j+1]) {
            	System.out.println("Sorting failed :-(");
            	return;
            }
        }
        System.out.println("Success!");
        System.out.println("Algorithm 1 took " + (endTime - startTime) + " milliseconds");
        
        
        
        // Re-populates A with numbers in reverse sorted order.
        for (int i = 0; i < n; i++) {
            A[i] = n-i;
        }
        
        // Create a global auxiliary array B for AuxiliaryMergeSort
        int[] B = new int[n];
        
        // Calculate the running time of the AuxiliaryMergeSort
        startTime = System.currentTimeMillis();
        AuxillaryMergeSort(A, B, 0, n-1);
        endTime = System.currentTimeMillis();
       
        // Test whether the algorithm 2 succeeded in sorting the array.
        for (int j = 0; j < A.length-1; j++) {
            if(A[j] > A[j+1]) {
            	System.out.println("Sorting failed :-(");
            	return;
            }
        }
        System.out.println("Success!");
        System.out.println("Algorithm 2 took " + (endTime - startTime) + " milliseconds");
    	
        
        
        // Re-populates A with numbers in reverse sorted order.
        for (int i = 0; i < n; i++) {
            A[i] = n-i;
        }
        
        // Create a global auxiliary array B for AuxiliaryMergeSort
        B = new int[n];
        
        // Calculate the running time of the AlternateMergeSort
        startTime = System.currentTimeMillis();
        int h = AlternateMergeSort(A, B, 0, n-1); // Get the depth of the recursion tree 
        endTime = System.currentTimeMillis();
       
        // If depth is odd, the sorted data is in B. Let A reference to B
		if((h&1)==1){ 
			A = B;
		} 
		 
		// Test whether the algorithm 3 succeeded in sorting the array.
        for (int j = 0; j < A.length-1; j++) {
            if(A[j] > A[j+1]) {
            	System.out.println("Sorting failed :-(");
            	return;
            }
        }
        System.out.println("Success!");
        System.out.println("Algorithm 3 took " + (endTime - startTime) + " milliseconds");
		
    }	
    
}	
