import java.util.*;

/**
 * The kxh132430_P4 class implements arithmetic with large integers, of arbitrary size. 
 * The number is represented by linked list of integers, where the digits are in base B. 
 * Each node of the list stores exactly one integer. 
 * The program take an optional command line parameter as base, ranging from 2 to 2^32-1.
 * By default the base for the program is 10.
 * 
 * @version 	1.0 October 6th, 2014
 * @author 		KAI HUANG              section 013
 * 				VILAS REDDY PODDUTURI  section 016
 *
 */
public class kxh132430_P4 {

	// BASE can be an arbitrary value ranging from 2 to 2^32-1
	static int BASE; 

	// Create a map to store the value of all the variables
	static HashMap<Character, LinkedList<Integer>> variables = new HashMap<Character, LinkedList<Integer>>();

	// Create an array list to store the commands
	static ArrayList<String> cmdList = new ArrayList<String>();
	
	public static void main(String[] args){

		//The program take an optional command line parameter as base.
		if(args.length == 0){ // If the parameter is not specified, then use 10 as default value for base B.
			BASE = 10;
		} else { // If specified, use the parameter as the value for base B.
			BASE = Integer.parseInt(args[0]);
		}

		//Print which base the program use to do arithmetic operation.
		System.out.println("Now you are in base " + BASE);
		System.out.println("Please type in '-1' to end your input");
		
		int linenum; // Line number for command
		String cmd;	 // Command from console

		//Create a scanner object to take input from console
		Scanner in = new Scanner(System.in);

		while(in.hasNext()) {
			linenum = in.nextInt();  // Get the line number
			if(linenum == -1) break; // If input is -1, stop taking input 
			cmd = in.next();	// Get the next command
			cmdList.add(cmd);	// Add the command to command list
		}

		try {
			in.close();	// Close scanner after taking the input from console
		} catch (Exception e) {
			e.printStackTrace();
		}

		int line = 1; int repeat = 0; // Let line be the line number, repeat be the line number where a loop begins

		// Parse the commands from the first line to the last line
		while(line <= cmdList.size()){ 

			repeat = parse(cmdList.get(line-1)); // Parse one line of command

			if(repeat != 0) { // If there is a loop in the command, go to the line where the loop begins
				line = repeat;
			} else line++;   // Otherwise go to the next line
		}
	}

	/**
	 * parse			Given a string of command, parse it and perform the corresponding operations.
	 * @param cmd		The command to be parsed
	 * @return			If there is a loop in the command, return the line where a loop begins,
	 * 					Otherwise return 0
	 */
	static int parse(String cmd){

		// For command like: var, print the value of the variable to stdout
		if(cmd.matches("[a-zA-Z]{1}")){
			LinkedList<Integer> output = variables.get(cmd.charAt(0)); // Get the value of var

			// Print var to stdout
			System.out.println(NumToStr(output)); 
		}

		// For command like: var?LineNumber, if var value is not 0, then go to Line number
		if(cmd.matches("[a-zA-Z][?][0-9]+")){
			LinkedList<Integer> loopVaraint = variables.get(cmd.charAt(0)); // Get the value of var

			// If loopVariant is not zero, return the line where the loop begins
			int repeat = Integer.parseInt(cmd.substring(2));
			if((loopVaraint.size() > 1)||(loopVaraint.get(0) != 0)){
				return repeat;
			}
		}

		// For command like: var=NumberInDecimal, sets var to be that number
		if(cmd.matches("[a-zA-Z][=][0-9]+")){
			variables.put(cmd.charAt(0),StrToNum(cmd.substring(2)));
		}

		// For command like: var=var op var, perform the arithmetic operation, where op is in {-+*/^}
		if(cmd.matches("[a-zA-Z][=][a-zA-Z][-+*/^][a-zA-Z]")){

			// Let var1 and var2 be the two operands, op be the operator type, result be the result of var1 op var2.
			LinkedList<Integer> var1 = variables.get(cmd.charAt(2));
			LinkedList<Integer> var2= variables.get(cmd.charAt(4));
			LinkedList<Integer> result = new LinkedList<Integer>();
			char op = cmd.charAt(3);

			// Perform specific arithmetic operation according to the operator type
			switch(op){
			case '+':	
				result = add(var1, var2); break;  // add two numbers and assign to var on left

			case '-':	
				result = subtract(var1, var2); break; // difference of two numbers

			case '*':	
				result = multiply(var1, var2); break; // product of two numbers

			case '^':	
				result = power(var1, var2); break; // power of two numbers
			default:break;
			}
			
			// set var to be the result of the arithmetic operation on var1 and var2
			variables.put(cmd.charAt(0), result);

		}
		
		// If no loop found, return 0
		return 0;
	}



	/**
	 * add				Takes two lists var1 and var2 as parameters and returns the list corresponding to the sum of the two numbers represented by those lists.
	 * @param var1		The first list
	 * @param var2		The second list
	 * @return result	The sum of var1 and var2
	 */
	static LinkedList<Integer> add(LinkedList<Integer>var1, LinkedList<Integer>var2){

		LinkedList<Integer> result = new LinkedList<Integer>(); //The sum of var1 and var2

		Iterator<Integer> itr1 = var1.iterator();

		Iterator<Integer> itr2 = var2.iterator();

		int carrier = 0; // The carrier for a given digit in add operation

		// Add var1 and var2 digit by digit, from LSD to MSD
		while((itr1.hasNext())&&(itr2.hasNext())){
			int x = itr1.next().intValue();	// The integer value of a digit in var1
			int y = itr2.next().intValue(); // The integer value of a digit in var2
			int sum = x + y + carrier; // The sum of the two digits plus carrier
			if(sum >= BASE) { // If the sum is greater than BASE, set the carrier for the next digit as 1
				sum = sum - BASE; 
				carrier = 1;   
			} else { // Otherwise, set the carrier for the next digit as 0
				carrier = 0; 
			}
			result.add(sum); // append one digit to the result
		}

		// If var1 has more digits than var2, append the overflowing digits to result
		while(itr1.hasNext()){
			int x = itr1.next().intValue(); // The integer value of a digit in var1
			int sum = x + carrier; // The sum of the digit of var1 and carrier
			if(sum >= BASE){ // If the sum is greater than BASE, set the carrier for the next digit as 1
				sum = sum -BASE;
				carrier = 1;  
			} else{ // Otherwise, set the carrier for the next digit as 0
				carrier = 0; 
			}
			result.add(sum); // append one digit to the result
		}
		
		// If var2 has more digits than var1, append the overflowing digits to result
		while(itr2.hasNext()){
			int y = itr2.next().intValue(); // The integer value of a digit in var2
			int sum = y + carrier; // The sum of the digit of var2 and carrier
			if(sum >= BASE){ // If the sum is greater than BASE, set the carrier for the next digit as 1
				sum = sum -BASE;
				carrier = 1; 
			} else{ // Otherwise, set the carrier for the next digit as 0
				carrier = 0; 
			}
			result.add(sum);// append one digit to the result
		}

		// If there is an overflowing digit, append it to the result
		if(carrier > 0) result.add(carrier); 

		return result;
	}


	/**
	 * subtract			given two lists var1 and var2 as parameter, representing the numbers n1 and n2 respectively, returns the list corresponding to n1-n2. If the result is negative, store 0, instead.
	 * @param var1		The first list
	 * @param var2		The second list
	 * @return result	The difference of var1 and var2
	 */
	static LinkedList<Integer> subtract(LinkedList<Integer>var1, LinkedList<Integer>var2){

		LinkedList<Integer> result = new LinkedList<Integer>();

		// If var1 has fewer digits than var2, var1 must be smaller than var2, so return 0
		if(var1.size() < var2.size()){
			result.add(0);
			return result;
		}

		Iterator<Integer> itr1 = var1.iterator();
		Iterator<Integer> itr2 = var2.iterator();

		int carrier = 0; // The carrier for a given digit in subtract operation

		// while var1 has the same number of digits as var2, subtract var2 from var1 digit by digit, from LSD to MSD
		while((itr1.hasNext())&&(itr2.hasNext())){
			int x = itr1.next().intValue(); // The integer value of a digit in var1
			int y = itr2.next().intValue(); // The integer value of a digit in var2
			int diff = x - y + carrier; // The difference of the digit of var1 and var2, plus carrier
			if(diff < 0) { // If the difference is less than zero, borrow "1" from the next digit
				diff = diff + BASE; 
				carrier = -1;
			} else { // Otherwise, set the carrier for the next digit as 0
				carrier = 0;
			}
			result.add(diff);
		}

		// If var1 has more digits than var2, subtract var2 from var1 digit by digit, from LSD to MSD
		while(itr1.hasNext()){
			int x = itr1.next().intValue();
			int diff = x + carrier;
			if(diff < 0){ // If the difference is less than zero, borrow "1" from the next digit
				diff = diff + BASE;
				carrier = -1;
			} else{ // Otherwise, set the carrier for the next digit as 0
				carrier = 0;
			}
			result.add(diff);
		}
		
		// If var1 is smaller than var2, return 0
		if(carrier < 0) {
			result.clear();
			result.add(0);
		}
		
		// Remove the leading zeros from the result
		while((result.getLast() == 0)&&(result.size() > 1)){
			result.removeLast();
		}

		return result;
	}

	/**
	 * multiply	   			Given two number var1 and var2 as lists, return the product	of n1 * n2	
	 * @param var1			The first list
	 * @param var2			The second list
	 * @return result 		The product of var1 and var2
	 */
	static LinkedList<Integer> multiply(LinkedList<Integer>var1, LinkedList<Integer>var2){

		// Create an array list to store all the intermediate products of digit-wise multiplication
		ArrayList<LinkedList<Integer>> productList = new ArrayList<LinkedList<Integer>>();

		int round = 0; // Let round be a counter of the digit-wise multiplication

		Iterator<Integer> itr1 = var1.iterator();

		// Take digit from var1 from LSD TO MSD, one at a time, multiply this digit with each digit in var2
		while(itr1.hasNext()){

			int carrier = 0;  // Carrier for a given digit in digit-wise multiplication

			int x = itr1.next().intValue();  // A digit x from var1

			// Create an array list to store an intermediate result of digit-wise multiplication
			LinkedList<Integer> product = new LinkedList<Integer>();

			Iterator<Integer> itr2 = var2.iterator();

			// Multiply digit x with each digit in var2
			while(itr2.hasNext()){
				int y = itr2.next().intValue();
				int digit = (x * y + carrier) % BASE ; // Update the digit after getting the product
				product.add(digit);
				carrier = (x * y + carrier) / BASE; // Update carrier after getting the product
			}

			// If there is an overflowing digit, append it to the result
			if(carrier != 0) product.add(carrier);

			// Add trailing zeros for the convenience of subsequent addition
			int append = round;
			while(append != 0){
				product.addFirst(0);
				append--;
			}

			// Add leading zeros for the convenience of subsequent addition
			int lead = var1.size() + var2.size() - product.size();
			while(lead > 0){
				product.add(0);
				lead--;
			}
			
			// Add this intermediate result of digit-wise multiplication to tempList
			productList.add(product);

			round++; // Increment the counter by one
		}

		// Create a list to store result
		LinkedList<Integer> result = new LinkedList<Integer>();
		
		// Align the intermediate products by digit from LSD to MSD, add all the products to get the final result
		int carrier = 0; // Carrier for a given digit in addition operation
		for(int i = 0; i < var1.size() + var2.size(); i++){
			int sum = 0;
			Iterator<LinkedList<Integer>> itr = productList.iterator();
			// Perform a digit-wise addition by taking the i-th digit from each product and add them up
			while(itr.hasNext()){ 
				LinkedList<Integer> item = itr.next(); 
				sum += item.get(i); // Add to sum the i-th digit from an intermediate product
			}
			int digit = (sum + carrier) % BASE;		// Update the digit after digit-wise addition
			carrier = (sum + carrier) / BASE;	// Update the digit after digit-wise addition
			result.add(digit); // Add the digit to the final result
		}
		
		// If there is an overflowing digit, append it to the result
		if(carrier != 0) result.add(carrier);

		// Remove the leading zeros from the result
		while((result.getLast() == 0)&&(result.size() > 1)){
			result.removeLast();
		}

		return result;
	}

	/**
	 * power			Given two lists var1 and var2 as parameter, representing the numbers n1 and n2 respectively, returns the list corresponding to n1^n2 (n1 to the power n2).
	 * @param var1		The first list represents base number n1
	 * @param var2		The second list represents exponent number n2
	 * @return			The list corresponding to n1^n2
	 */
	static LinkedList<Integer> power(LinkedList<Integer> var1, LinkedList<Integer> var2){

		// If exponent is 0, return 1
		if((var2.size() == 1)&&(var2.get(0) == 0)){
			return StrToNum("1");
		}
		// If exponent is 1, return var1
		if((var2.size() == 1)&&(var2.get(0) == 1)){
			return var1;
		}
		else{ // Otherwise, divide the exponent var2 by 2, and call power method recursively
			Tuple tuple = divide(var2, 2, BASE); 
			var2 = tuple.quot;
			// Get var1*var1 by multiplication
			LinkedList<Integer> squareOfVar1 = multiply(var1, var1);
			if(((tuple.rem) & 1) == 0){ // If exponent is even, return power(var1*var1, var2)
				return power(squareOfVar1, var2);
			} else{ // If exponent is odd, return var1 * power(var1*var1, var2)
				return multiply(var1, power(squareOfVar1, var2));
			}
		}
	}

	/**
	 * divide				Given two numbers dividend and divisor, return the quotient and remainder			
	 * @param dividend		The dividend
	 * @param divisor		The divisor
	 * @param base			The base adopted by the dividend
	 * @return tuple		A tuple consisting of the quotient and remainder of the division
	 */
	static Tuple divide(LinkedList<Integer>dividend, int divisor, int base){

		// Create an array list to store the quotient of the division
		LinkedList<Integer> quotient = new LinkedList<Integer>();

		int rem = 0; int quo = 0; // The remainder and quotient of each digit-wise division

		// Divide each digit of the dividend by the divisor, from MSD to LSD
		int idx = dividend.size()-1;
		do{
			int val = dividend.get(idx);
			quo = (val + rem * base) / divisor; // Update the digit-wise quotient
			rem = (val + rem * base) % divisor; // Update the digit-wise remainder
			quotient.addFirst(quo);	// Add the digit-wise quotient to the final quotient
			idx--;
		} while(idx >= 0); // Division if performed from MSD to LSD	

		// Remove the leading zeros from the result
		while((quotient.getLast() == 0)&&(quotient.size() > 1)){
			quotient.removeLast();
		}

		// Return the quotient and remainder of the division as a tuple
		Tuple tuple = new Tuple(quotient, rem);
		return tuple;

	}


	/**
	 * StrToNum			Takes a string as parameter, that stores a number in decimal, and returns the list corresponding that number. 
	 * @param str		The string that represents a decimal number
	 * @return list		A linked list corresponding to that number in arbitrary base
	 */
	static LinkedList<Integer> StrToNum(String str){

		//Create a list to store the number represented by the string in arbitrary base
		LinkedList<Integer> list = new LinkedList<Integer>();

		 // For each character in the string, convert it into a digit in decimal
		for(int idx = 0; idx < str.length(); idx++){
			char ch = str.charAt(idx);
			int digit = ch - '0'; 
			list.addFirst(digit); // Store the digit in the list
		}

		// If the target base is not decimal, convert the list from decimal to the target base 
		if(BASE != 10) list = ConvertToBase(list, 10, BASE);

		return list;
	}


	/**
	 * NumToStr			Takes a linked list as parameter and returns the number that it represents as a string (in decimal). 
	 * @param list		A linked list that represents a number in arbitrary base
	 * @return			A string that represents that number in decimal
	 */
	static String NumToStr(LinkedList<Integer> list){

		//Create a string to display the number represented by the linked list
		StringBuffer sb = new StringBuffer();

		// If the base adopted by the list is not decimal, convert the list from to the original base to decimal 
		if(BASE != 10) list = ConvertToBase(list, BASE, 10);

		for(int i = list.size() - 1; i >= 0; i--){

			int digit = list.get(i); // Get each digit from the list

			sb.append(digit);	// Append the digit to the string

		}
		return sb.toString(); // Return the string

	}


	/**
	 * ConvertToBase		Given a linked list, convert the list from source base into destination base
	 * @param list			A linked list that represents a number in arbitrary base
	 * @param srcBase		The source base the linked list used
	 * @param destBase		The destination base the list will be converted into
	 * @return result		A linked list that represent the number in destination base
	 */
	static LinkedList<Integer> ConvertToBase(LinkedList<Integer> list, int srcBase, int destBase){

		// Create a list to store the converted list
		LinkedList<Integer> result = new LinkedList<Integer>();

		// Create a tuple for the convenience of division operation
		Tuple tuple = new Tuple(list, 0);

		// Divide the list by destination base until the quotient is zero
		do {
			tuple = divide(tuple.quot, destBase, srcBase); 
			result.add(tuple.rem);  // Add the remainder as a digit to the result list
			tuple.rem = 0;
		} while((tuple.quot.size() > 1) || (tuple.quot.get(0) != 0)); // If quotient is not zero, continue division

		return result;

	}

}

/**
 * The Tuple class is an auxiliary class that stores the quotient and remainder of a division operation
 * @author KAI
 *
 */
class Tuple{

	LinkedList<Integer> quot = new LinkedList<Integer>(); // The quotient
	int rem;	// The remainder
	/**
	 * Tuple			A constructor that creates a tuple with a pair of quotient and remainder
	 * @param quot		The quotient
	 * @param rem		The remainder
	 */
	Tuple(LinkedList<Integer> quot, int rem){
		this.quot = quot;
		this.rem = rem;
	}

}