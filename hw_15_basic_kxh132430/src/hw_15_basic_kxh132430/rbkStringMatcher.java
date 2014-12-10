package hw_15_basic_kxh132430;

import java.util.ArrayList;
import java.util.Random;

public class rbkStringMatcher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String T = "1010101010";
		String P = "101";
		
		System.out.println("naive: " + naiveMatch(T, P));
		
		System.out.println("BM: " + BMMatch(T, P));
		
		System.out.println("KMP: " + KMPMatch(T, P));
		
		System.out.println("RM: " + RKMatch(T, P));
		
		
	}
	
	//Naive Matcher
	
	static ArrayList<Integer> naiveMatch(String T, String P){
		int n = T.length();
		int m = P.length();
		
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
	
	//RK Matcher
	
	static ArrayList<Integer> RKMatch(String T, String P){
		int n = T.length();
		int m = P.length();
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		int q = getPrime(m);
	
		int h = (2 << m - 1) % q;
		
		int[] t = new int[n - m + 1];
		int p = 0;
		t[0] = 0;
		
		for(int i = 0; i < m; i++){
			p = ((p * 2) + (P.charAt(i) -'0')) % q;
			t[0] = ((t[0] * 2) + (T.charAt(i) - '0')) % q;
			System.out.println(h + " " + p + " " + t[0] +" "+ q);
		}
		
		
		for(int s = 0; s <= n-m; s++){
			
			if(p == t[s]){
				if(isMatch(T, P, s, m)){
					//System.out.println("match:" + s);
					result.add(s);
				}
			}
			if(s < n - m - 1){
				t[s+1] = Math.abs(((t[s] - (T.charAt(s) - '0')* h) * 2 + (T.charAt(s+m)-'0')) % q);
				//System.out.println("T["+(s+1)+"]="+t[s+1]);
			}
		}
		
		return result;
		
	}
	
	static boolean isMatch(String T, String P, int s, int m){
		
		boolean matched = true;
		
		for(int i = s; i < s + m; i++){
			if(T.charAt(i) != P.charAt(i - s)){
				matched = false; 
				break;
			}
		}
		
		return matched;
	}
	
	static int getPrime(int m){
		
		int q = 0;
		
		while(!isPrime(q) || q < m){
			
			Random rand = new Random();
		
			q = rand.nextInt(m * m * (int)(Math.log((double)m))-1) + 2;
		}
		
		return q;
		
	}
	
	static boolean isPrime(int q){
		
		if(q == 0 || q == 1) 
			return false;
		
		boolean prime = true;
		
		int r = (int)Math.sqrt(q);
		
		for(int i = 2; i <= r; i++){
			if(q % i == 0){
				prime = false;
				break;
			}
		}
		
		return prime;
	}
	
	
	//BM Matcher
	
	static ArrayList<Integer> BMMatch(String T, String P){
		int n = T.length();
		int m = P.length();
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
	
	static int[] last(String P){
		
		int j = P.length() - 1;
		
		int[] last = new int[2];

		last[0] = last[1] = -1;

		for(int i = 0; i <= j; i++){
			last[P.charAt(i) - '0'] = i;
		}
		
		return last;
	}
	
	static int min(int i, int j){
		return (i < j)? i:j;
	}
	
	//KMP Matcher
	
	static int[] compute_prefix_function(String P){
		int m = P.length();
		
		int[] pre = new int[m + 1];
		pre[0] = pre[1] = 0;
		int k = 0;
		
		for(int q = 1; q < m; q++){
			while(k > 0 && P.charAt(k) != P.charAt(q)){
				k = pre[k] ;
			}
			if(P.charAt(k) == P.charAt(q)){
				k = k + 1;
			}
			pre[q + 1] = k;
		}
		
		return pre;
		
	}
	
	static ArrayList<Integer> KMPMatch(String T, String P){
		int n = T.length();
		int m = P.length();
		int[] pre = compute_prefix_function(P); 
		
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		int q = 0;  // length of match
		
		for(int i = 0; i < n; i++){
			while(q > 0 && P.charAt(q) != T.charAt(i)){
				q = pre[q];
			}
			if(P.charAt(q) == T.charAt(i)){
				q = q + 1;
			}
			if(q == m){
				result.add( i - m + 1);
				q = pre[q];
			}
		}
		
		return result;
	}
	
}
