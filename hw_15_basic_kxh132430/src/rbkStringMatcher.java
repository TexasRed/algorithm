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

	// Section 1: Naive algorithm
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

		ArrayList<Integer> result = new ArrayList<Integer>();

		for(int i = 0; i <= n - m; i++){
			int j = 0;
			while(j < m && T.charAt(i + j) ==P.charAt(j)){
				j = j + 1;
			}
			if(j == m){
				result.add(i);
			}
		}
		return result;
	}

	
	// Section 2: Rabin-Karp algorithm
	
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

		long h = (1 << m - 1 ) % q;

		//long[] t = new long[n - m + 1];

		long p = 0;
		//t[0] = 0;
		long t = 0;

		for(int i = 0; i < m; i++){
			p = ((p * 2) + (P.charAt(i) -'0')) % q;
			//t[0] = ((t[0] * 2) + (T.charAt(i) - '0')) % q;
			t = ((t * 2) + (T.charAt(i) - '0')) % q;
			//System.out.println(h + " " + p + " " + t[0] +" "+ q);
		}


		for(int s = 0; s <= n-m; s++){

			//if(p == t[s]){
			if(p == t){
				if(isMatch(T, P, s)){
					result.add(s);
				}
			}
			if(s <= n - m - 1){
				/*
				t[s+1] = ((t[s] - (T.charAt(s) - '0')* h) * 2 + (T.charAt(s+m)-'0')) % q;
				if(t[s+1] < 0){
					t[s+1] = t[s+1] + q;
				}
				//System.out.println("T["+(s+1)+"]="+t[s+1]);
				/*/
				t = ((t - (T.charAt(s) - '0')* h) * 2 + (T.charAt(s+m)-'0')) % q;
				if(t < 0){
					t = t + q;
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

		int m = P.length();
		
		return T.substring(s, s + m -1).equals(P);
		
	}

	/**
	 * isPrime 		Test if q is a prime number
	 * @param q		number for the prime test
	 * @return		return true is q is a prime number
	 */
	static boolean isPrime(int q){

		boolean prime = true;

		// 0 and 1 are not prime number
		if(q == 0 || q == 1) { 
			return false;
		}

		long r = (long)Math.sqrt(q);
		
		// If q has no divisor between 2 and sqrt(q), then q is a prime number
		for(long i = 2; i <= r; i++){
			if(q % i == 0){
				prime = false;
				break;
			}
		}
		return prime;
	}

	// Section 3: KMP Matcher

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

		int q = 0;  // length of the matched characters in the text 

		// Scan the text from left to right
		for(int i = 0; i < n; i++){
			// If pattern and text does not match at the current character,
			// but matches at certain index before the current character
			// 
			while(q > 0 && P.charAt(q) != T.charAt(i)){
				q = pre[q];
			}
			// If the pattern and text match at the current index, 
			// increment the length of matched characters by one.
			if(P.charAt(q) == T.charAt(i)){
				q = q + 1;
			}
			// If the substring in
			if(q == m){
				result.add( i - m + 1);
				q = pre[q];
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


	// Section 4: Boyer-Moore algorithm
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

		int i = m - 1;

		if(i > n - 1){
			return null;
		}
		int j = m - 1;

		do{
			if(T.charAt(i) == P.charAt(j)){
				if(j == 0){ 
					result.add(i);
					i = i + m;
					j = m - 1;
				}else{
					j--; i--;
				}
			} else{
				i = i + m - min(j, 1 + last(P)[T.charAt(i) - '0']);
				j = m - 1;
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

		int j = P.length() - 1;

		int[] last = new int[2];

		last[0] = last[1] = -1;

		for(int i = 0; i <= j; i++){
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


	// Helper function to calculate the running time of each algorithm above
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

}


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
}
