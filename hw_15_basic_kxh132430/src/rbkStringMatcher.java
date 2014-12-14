import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * The rbkStringMatcher implements the following algorithms for string matching::
 * 1. Naive 
 * 2. Rabin-Karp
 * 3. Knuth-Morris-Pratt
 * 4. Boyer-Moore
 * 
 * The program takes its input from the command line. The first parameter
 * is the pattern string (composed of 0/1), and the second parameter is
 * the name of a file which contains the text (0/1).
 * 
 * In the first line of the output, print the number of valid shifts and
 * the running time of the algorithm in msec for each of the 4 algorithms.
 * In the second line of the output, print all the valid shifts, separated
 * by spaces.
 * 
 * @version 1.0 	Dec 13th, 2014
 * @author 	KAI
 * 
 */
public class rbkStringMatcher {

	public static void main(String[] args) {

		// The program takes its input from the command line,
		// if invalid number of arguments are provided, print 
		// error information and exit the program.
		if(args.length < 2){
			System.out.println("Invalid number of arguments!");
			System.out.println("Example: java rbkStringMatcher 010100 in.txt");
			System.exit(-1);
		}

		// Take the first parameter as the pattern string.
		String P = args[0];

		// Take the second parameter as the the name of a file which contains the text.
		String inputFile = args[1];

		// Read the text string from the input file, discard any newlines in the text file.
		String T = "";
		In in = new In(new File(inputFile));
		while(in.hasNextLine()){
			T += in.readLine();
		}
		in.close();

		// If the pattern string exceeds the text string, print error message
		if(T.length() < P.length()){
			System.err.println("The pattern string cannot exceeds the text string!");
			System.exit(-1);
		}

		// Store the shift index in an array list named result.
		ArrayList<Integer> result = new ArrayList<Integer>();

		// print the running time of the naive algorithm in msec.
		rbkTimer();
		result = NaiveMatch(T, P); 
		System.out.print(result.size() + " "); // print the he number of valid shifts
		rbkTimer(); 

		// print the running time of the Rabin-Karp algorithm in msec.
		rbkTimer();
		RKMatch(T, P);
		rbkTimer();

		// print the running time of the Knuth-Morris-Pratt algorithm in msec.
		rbkTimer();
		KMPMatch(T, P);
		rbkTimer();

		// print the running time of the Boyer-Moore algorithm in msec.
		rbkTimer();
		BMMatch(T, P);
		rbkTimer();
		System.out.println();

		// print all the valid shifts. If there are more than 20 valid shifts, 
		// print only the first 20.
		int count = 0;
		for(int shift: result){
			if(++count > 20) 
				break;
			System.out.print(shift + " ");
		}
		System.out.println();

	}

    /*
     * ********************************************************************
     *   Helper function to calculate the running time
     * ********************************************************************
     */
	static long startTime, endTime, elapsedTime;
	static int phase = 0;

	static void rbkTimer() {
		if(phase == 0) { startTime = System.currentTimeMillis();  phase = 1; }
		else {
			endTime = System.currentTimeMillis();  
			elapsedTime = endTime-startTime;
			System.out.print(elapsedTime + " ");
			phase = 0;
		}
	}
	
	
	/*
	 *************************************************
	 *    	 Section 1: naive algorithm
	 *************************************************
	 */
	/**
	 * NaiveMatch	Given a text and a pattern, find all the valid shift that 
	 * 				matches the pattern with the text using naive algorithm.
	 * @param T     text string
	 * @param P		pattern string
	 * @return		a list of all the valid shift
	 */
	static ArrayList<Integer> NaiveMatch(String T, String P){
		int n = T.length(); // The length of text
		int m = P.length(); // The length of pattern
		// Let result be a list of all the valid shifts
		ArrayList<Integer> result = new ArrayList<Integer>();

		// Shift from left to right one character a time, compare pattern with
		// all the m-grams in the text
		for(int i = 0; i <= n - m; i++){
			int j = 0;
			// If T[i] matches P[j], move to the next character and compare again
			while(j < m && T.charAt(i + j) ==P.charAt(j)){
				j = j + 1;
			}
			if(j == m){ // If all character matches, add this m-gram into result list
				result.add(i);
			}
		}
		return result;
	}


	/*
	 *************************************************
	 *    	 Section 2: Rabin-Karp algorithm
	 *************************************************
	 */
	// Generate a random 63-bit prime for the hash function
	static long q = BigInteger.probablePrime(63, new Random()).longValue();

	/**
	 * RKMatch		Given a text and a pattern, find all the valid shift that 
	 * 				matches the pattern with the text using Rabin-Karp algorithm.
	 * @param T     text string
	 * @param P		pattern string
	 * @return		a list of all the valid shift
	 */
	static ArrayList<Integer> RKMatch(String T, String P){
		int n = T.length(); // The length of text
		int m = P.length(); // The length of pattern
		// Let result be a list of all the valid shifts
		ArrayList<Integer> result = new ArrayList<Integer>(); 

		// Let multiplier be the maginitude of the MSD in the m-gram in text (mod q)
		long multiplier = (1 << m - 1 ) % q;

		long p = 0;  // let p be the value of h[P[0...m-1]]
		long t = 0; // let t be the value of h[T[0...m-1]];

		for(int i = 0; i < m; i++){
			// Convert the pattern string P[0...m-1] into a decimal number (mod q)
			p = ((p * 2) + (P.charAt(i) -'0')) % q;   
			// Convert the m-gram T[0...m-1] into a decimal number (mod q)
			t = ((t * 2) + (T.charAt(i) - '0')) % q;
		}

		// Shift from left to right one character a time, compare pattern with
		// all the m-grams in the text
		for(int s = 0; s <= n-m; s++){
			if(p == t){ // If pattern and a m-gram in text hashes to the same slot in the hash table
				if(isMatch(T, P, s)){ // Add this m-gram to result list if all characters of it match with the pattern
					result.add(s);
				}
			}
			if(s <= n - m - 1){ // If the text has a next m-gram
				// compute the next m-gram T[s+1...s+m] from the current m-gram T[s...s+m-1]
				t = ((t - (T.charAt(s) - '0')* multiplier) * 2 + (T.charAt(s+m)-'0')) % q;
				if(t < 0){
					t = t + q;  // Normalize the remainder into positive value
				}
			}
		}
		return result;

	}

	// Helper function for Rabin-Karp function
	/**
	 * isMatch		Given a text T and a pattern P, 
	 * 				check if T[s...s+m-1] matches the pattern
	 * @param T     text string
	 * @param P		pattern string
	 * @param s		the 
	 * @return		return true if T[s...s+m-1] matches the pattern
	 * 				otherwise return false
	 */
	static boolean isMatch(String T, String P, int s){
		// test if the m-gram T[s...s+m-1] in the text matches the pattern
		int m = P.length();
		return T.substring(s, s + m -1).equals(P); 
	}

	/**
	 * @Deprecated  Validate if q is a prime number
	 * @param q		number for the prime test
	 * @return		return true is q is a prime number
	 */
	static boolean isPrime(long q){
		boolean prime = true;
		
		// 0 and 1 are not prime number
		if(q == 0 || q == 1) { 
			return false;
		}
		// If q has no divisor between 2 and sqrt(q), then q is a prime number
		long r = (long)Math.sqrt(q);
		for(long i = 2; i <= r; i++){
			if(q % i == 0){
				prime = false;
				break;
			}
		}
		return prime;
	}

	/*
	 *************************************************
	 *    Section 3: Knuth-Morris-Pratt algorithm
	 *************************************************
	 */
	/**
	 * KMPMatch		Given a text and a pattern, find all the valid shift that matches 
	 * 				the pattern with the text using Knuth-Morris-Pratt algorithm.
	 * @param T     text string
	 * @param P		pattern string
	 * @return		a list of all the valid shifts
	 */
	static ArrayList<Integer> KMPMatch(String T, String P){

		int n = T.length(); // The length of text
		int m = P.length(); // The length of pattern

		// Let pre be the prefix function
		int[] pre = compute_prefix_function(P); 

		// Let result be a list of all the valid shifts
		ArrayList<Integer> result = new ArrayList<Integer>();

		int q = 0;  // length of the matched characters in an m-gram and pattern 

		// Scan the text from left to right
		for(int i = 0; i < n; i++){
			// If pattern and text does not match at the current character,
			// but matches at some index before the current character
			while(q > 0 && P.charAt(q) != T.charAt(i)){
				q = pre[q];  // shift pattern by a amount such that its prefix
							 // matches the suffix that matched the text so far
			}
			// If the pattern and text match at the current index, 
			// increment the length of matched characters by one.
			if(P.charAt(q) == T.charAt(i)){
				q = q + 1;
			}
			// If the all character matches in the m-gram and pattern
			if(q == m){
				result.add( i - m + 1); // add the shift index to the result list
				q = pre[q]; // shift pattern by a amount such that its prefix
				 			// matches the suffix that matches the text so far
			}
		}
		return result;
	}

	//  Helper function for KMP algorithm
	/**
	 * compute_prefix_function	Given a pattern, compute the maximum length of prefix that
	 * 						    matches its own suffix
	 * @param P					The pattern
	 * @return					The prefix function for each index in the pattern
	 */
	static int[] compute_prefix_function(String P){

		int m = P.length(); // The length of pattern

		int[] pre = new int[m + 1];	// An array to store the prefix function

		// No prefix exists for empty string and string of length one
		pre[0] = pre[1] = 0;  

		int k = 0;

		for(int q = 1; q < m; q++){
			while(k > 0 && P.charAt(k) != P.charAt(q)){
				k = pre[k] ;
			}
			// If 
			if(P.charAt(k) == P.charAt(q)){
				k = k + 1;
			}
			pre[q + 1] = k;
		}

		return pre;
	}


	/*
	 *************************************************
     *	 Section 4: Boyer-Moore algorithm            *
	 *************************************************
	 */
	/**
	 * BMMatch		Given a text and a pattern, find all the valid shift that 
	 * 				matches the pattern with the text using Boyer-Moore algorithm.
	 * @param T     text string
	 * @param P		pattern string
	 * @return		a list of all the valid shift
	 */
	 static ArrayList<Integer> BMMatch(String T, String P){
		int n = T.length(); // The length of text
		int m = P.length(); // The length of pattern
		// Let result be a list of all the valid shifts
		ArrayList<Integer> result = new ArrayList<Integer>();

		// Let i be the last index of an m-gram in the text, 
		// Let j be the last index of the pattern
		int i = m - 1;

		if(i > n - 1){ // Return empty if pattern exceeds length of text
			return null;
		}
		int j = m - 1;

		// Scan the text from left to right, 
		// compare all the m-grams in text with the pattern from right to left
		do{ // If the m-gram and pattern match at the last index
			if(T.charAt(i) == P.charAt(j)){
				if(j == 0){  // If the left bound of pattern is reached
					result.add(i); // Add the shift index to the result list
					i = i + m; 
					j = m - 1;
				}else{
					j--; i--; // If the left bound of pattern is not reached yet, keep moving to the left
				}
			} else{ // If the m-gram and pattern does not match at the last index
				i = i + m - min(j, 1 + last(P)[T.charAt(i) - '0']); // shift to the last occurred location of the current character in the next m-gram 
				j = m - 1; // compare the new m-gram with pattern again
			}

		} while( i <= n - 1);

		return result;

	}

	// Helper function for Boyer-Moore algorithm
	/**
	 * last			Given a pattern, compute the last occurrence of each character in the pattern
	 * @param P		The pattern to search
	 * @return		Return an array that records the last occurrence of each character in the pattern
	 */
	static int[] last(String P){

		// Assume the alphabet for the pattern is {0,1}
		// Find the last occurrence of character '0' and '1' in the pattern
		int[] last = new int[2];

		last[0] = last[1] = -1;
		for(int i = 0; i <= P.length() - 1; i++){
			last[P.charAt(i) - '0'] = i;
		}

		return last;
	}

	/**
	 * min			Given two integers, return the smallest one
	 * @param i		First integer
	 * @param j		Second integer
	 * @return		The smallest integer among i and j
	 */
	static int min(int i, int j){
		return (i < j)? i:j;
	}

} // end of rbkStringMatcher class


// Helper class to calculate the running time of each algorithm
/**
 * The In class implements a driver to get input string from a text file.
 * In the readLine method, it discards any newlines in the string.
 * @author KAI
 * 
 */
class In{

	// read input file with a scanner
	private Scanner scanner;

	// assume Unicode UTF-8 encoding
	private static final String CHARSET_NAME = "UTF-8";

	// assume language = English, country = US for consistency with System.out.
	private static final Locale LOCALE = Locale.US;

	/**
	 * Create an input stream from a file.
	 */
	public In(File file) {
		try {
			scanner = new Scanner(file, CHARSET_NAME);
			scanner.useLocale(LOCALE);
		}
		catch (IOException ioe) {
			System.err.println("Could not open " + file);
		}
	}

	/**
	 * Does the input have a next line? Use this to know whether the
	 * next call to {@link #readLine()} will succeed. <p> Functionally
	 * equivalent to {@link #hasNextChar()}.
	 */
	public boolean hasNextLine() {
		return scanner.hasNextLine();
	}

	/**
	 * Read and return the next line.
	 */
	public String readLine() {
		String line;
		try                 { line = scanner.nextLine(); }
		catch (Exception e) { line = null;               }
		return line;
	}

	/**
	 * Close the input stream.
	 */
	public void close() {
		scanner.close();  
	}
	
} // end of In class
