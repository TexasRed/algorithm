import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * The kxh132430_P6 class implements a search engine that supports multi-dimensional 
 * search and a series of operations on the items including: Insert, Find, Delete, 
 * FindMinPrice, FindMaxPrice, FindPriceRange, and PriceHike. 
 * 
 * The program take a command line parameter as the input file name, read data from
 * the input file, and return the sum of the return values of each operations.
 * 
 * @version 	1.0 October 27th, 2014
 * @author 		KAI HUANG              section 013
 * 				VILAS REDDY PODDUTURI  section 016
 *
 */
public class kxh132430_P6 {

	public static void main(String[] args) {

		//Ask the user to provide a filename if the filename is missing.
		if(args.length < 1){
			System.out.println("Filename is missing:");
			System.out.println("java kxh132430_P6 /path/to/input/file");
			System.exit(1);
		}
		
		// Get the input file name from the command line parameter
		File file = new File(args[0]);

		// Create a store object that supports multidimensional search on items 
		Store store = new Store();
		
		//OPTIONAL: Calculate the running time for all operations on the items in the store.
		
		// long startTime = 0, endTime = 0;
		
		// startTime = System.currentTimeMillis();
		
		// Read the item data from the input file, populate store with these items.
		// Perform a series of operations on the items and return the sum of return 
		// values for each operations.
		double sum = store.populateItem(file);
		
		//endTime = System.currentTimeMillis();
		
		// Print the output, which is the sum of the values obtained by the algorithm as it processes the input.
		System.out.printf("%.2f", sum);
		
		// Print the running time of the algorithm
		// System.out.print("\nrunning time: " + (endTime - startTime));
	}	
}

/**
 * The class Item implements the data structure of the Item objects, 
 * which consists of ID, price and name.
 * @author KAI
 *
 */
class Item{

	long id;   // The ID of the item represented as a long int
	long price;	// The price of the item represented as a long int
	ArrayList<Long> name = new ArrayList<Long>();	// The name of the item represented as a list of long ints

	/**
	 * Item 			A constructor creates an new item with a given ID, price and name
	 * @param id		The ID of the new item
	 * @param price		The price of the new item
	 * @param name		The name of the new item
	 */
	Item(long id, long price, ArrayList<Long> name){
		this.id = id;
		this.price = price;
		this.name = name;
	}

	// Get the ID of the item 
	public long getId(){
		return this.id;
	}

	// Get the price of the item 
	public long getPrice(){
		return this.price;
	}

	// Get the Name of the item 
	public ArrayList<Long> getName(){
		return this.name;
	}

	// Update the price of the item 
	public void setPrice(long price){
		this.price = price;
	}

	// Update the name of the item 
	public void setName(ArrayList<Long> name){
		this.name = name;
	}

	// Update both the price and name of the item 
	public void setPriceName(long price, ArrayList<Long> name){
		this.price = price;
		this.name = name;
	}

}

/**
 * The Store class implements a data structure that supports multi-dimensional 
 * search and a series of operations on the item including: Insert, Find, Delete, 
 * FindMinPrice, FindMaxPrice, FindPriceRange, and PriceHike. 
 * 
 * @author KAI
 *
 */
class Store{

	// Index on id: a HashMap that stores <ID, reference to Item>
	HashMap<Long, Item> itemList;	

	// Index on id: a TreeMap that stores <ID, reference to Item>
	TreeMap<Long, Item> idIndex;	

	// Index on keyword: a HashMap that stores <keyword, reference to Index on price>
	HashMap<Long, TreeMap<Long, Long>> keywordIndex; // Index on price: a secondary TreeMap that stores <price, count of Items>

	/**
	 * Store	A constructor that creates a new Store object
	 */
	Store(){
		
		this.itemList = new HashMap<Long, Item>();
		
		this.idIndex = new TreeMap<Long, Item>();
		
		this.keywordIndex = new HashMap<Long, TreeMap<Long, Long>>();
	}

	/*
	***************************************************************
	*															  *
	*  The implementation of the 7 major operations on the items  *										  
	*															  *
	***************************************************************
	*/
	
	/**
	 * Insert 			Insert a new item. If an entry with the same id 
	 * 					already exists, its name and price are replaced 
	 * 					by the new values.  If name is empty, then just 
	 * 					the price is updated.
	 * 
	 * @param id		The ID of the new item
	 * @param price		The price of the new item
	 * @param name		The name of the new item
	 * @return			1 if the item is new, and 0 otherwise
	 */
	public long Insert(long id, long price, ArrayList<Long> name){

		long retval = 0;

		// Search for Item with the given id
		Item oldItem = this.itemList.get(id);

		// If the item does not exist, create a new item and insert into store
		if(oldItem == null){  

			retval = 1; // If the item is new, return 1

			Item item = new Item(id, price, name); // Create a new item

			addToPriceIndex(id, price, name); // Insert the item into the Index on price

			this.itemList.put(id, item); // Insert item into the first index on ID

			this.idIndex.put(id, item); // Insert item into the second index on ID

		} else{ 	// If the item already exists, update the index and item.

			retval = 0; //If the item exists, return 0

			if(name.isEmpty()){	// If no name detected, update only the price of the item
				
				updatePriceIndex(id, price, oldItem.getName()); // Update the Index on price
				oldItem.setPrice(price); // Update the price
				
			} else{		// If a different name is provided, update both the price and name of the item
				
				updatePriceIndex(id, price, name);	// Update the Index on price
				oldItem.setPriceName(price, name); // Update the price and name
			}
		}

		return retval;
	}
	
	
	/**
	 * Delete			Delete an item from storage and all index data structures
	 * 
	 * @param id		The id of the item to be deleted
	 * @return			The sum of the long ints that are in the name of the item deleted
	 * 					return 0, if such an id did not exist.
	 */
	public long Delete(long id){

		long retval = 0;

		// Search for the item with the given id
		Item item = this.itemList.get(id);

		if(item != null){ // If the id already exists in the store

			long price = item.getPrice(); // Get the item price

			ArrayList<Long> name = item.getName(); // Get the item name
			
			// Remove the item from the Price Index
			retval = removeFromPriceIndex(id, price, name);

			// Remove the item from the first ID Index
			itemList.remove(id);

			// Remove the item from the second ID Index
			idIndex.remove(id);

		} else{	//return 0, if such an id did not exist.
			retval = 0;
		}

		// Return the sum of the long ints that are in the name of the item deleted
		return retval;
	}

	/**
	 * Find				Find the price of item with given id
	 * 
	 * @param id		The id for the item
	 * @return			The price of item with given id (or 0, if not found)
	 */
	public long Find(long id){
		
		// Search for the item with the given id
		Item item = this.itemList.get(id);

		if(item != null){ // Return the price of item with the given id
			return item.price;
		} else 
			return 0; // return 0 if item not found
	}
	
	/**
	 * FindMinPrice		Given a keyword, find items whose name contains this keyword,
	 * 					and return lowest price of those items.
	 * 
	 * @param n			The keyword in a given name
	 * @return			The lowest price of all the matched items.
	 */
	public long FindMinPrice(long n){

		// Search for the price Index corresponding to the given keyword
		TreeMap<Long, Long> priceIndex = this.keywordIndex.get(n);

		if(priceIndex != null && !(priceIndex.isEmpty())){
			return priceIndex.firstKey();  // Return the lowest price of all the matched items
		} else { 
			return 0; // Return 0 if price Index not found 
		}
	}
	
	/**
	 * FindMaxPrice			Given a keyword, find items whose name contains this keyword, 
	 * 						and return highest price of those items.
	 * 
	 * @param n				The keyword in a given name
	 * @return				The lowest price of all the matched items.
	 */
	public long FindMaxPrice(long n){

		// Search for the price Index corresponding to the given keyword
		TreeMap<Long, Long> priceIndex = this.keywordIndex.get(n);

		if(priceIndex != null && !(priceIndex.isEmpty())){
			return priceIndex.lastKey();  // Return the highest price of all the matched items
		} else {
			return 0;  // Return 0 if price Index not found 
		}
	}
	
	/**
	 * FindPriceRange		Given a keyword, find the number of items whose name contains this keyword, 
	 * 						and their prices fall within the given range, [low, high].
	 * 
	 * @param n				The keyword in a given name
	 * @param low			The lower bound of the price range
	 * @param high			The upper bound of the price range
	 * @return				The number of matched items whose price fall within the range
	 */
	public long FindPriceRange(long n, long low, long high){

		long count = 0;
		
		// Search for the price Index corresponding to the given keyword
		TreeMap<Long, Long> priceIndex = this.keywordIndex.get(n);

		//If the price falls within the given range, sum up the count for that price
		if(priceIndex != null){
			for(Entry<Long, Long> entry : priceIndex.entrySet()) {
				long price = entry.getKey();
				if(price >= low && price <= high){
					count += entry.getValue();
				}
			}
		} 

		return count;
	}

	/**
	 * PriceHike		Increase the price of every product, whose id is in the range [l,h], by r%.
	 *   				Discard any fractional pennies in the new prices of items.
	 *   
	 * @param l			The lower bound of the id range
	 * @param h			The upper bound of the id range
	 * @param r			The increment ratio of the price hike
	 * @return			The sum of the net increases of the prices.
	 */
	public long PriceHike(long l, long h, int r){

		long retval = 0;

		//If the id falls within the given range, sum up the net increases of the prices
		for(Entry<Long, Item> entry : this.idIndex.entrySet()) {

			Long id = entry.getKey();

			if(id >= l && id <= h){ //If the id falls within the given range

				Item item =  entry.getValue();

				ArrayList<Long> name = item.getName(); // Get the name of the item

				long oldPrice = item.getPrice(); // Get the old price of the item

				long newPrice = getNewPrice(oldPrice, r); // Get the new price by increasing the old price by r%

				updatePriceIndex(id, newPrice, name); // Update the price Index with the new price

				item.setPrice(newPrice); // Update the price for the item whose id is within the range

				retval += newPrice - oldPrice; // Sum up the net increases of the prices

			}
		}

		return retval;
	}

	/*
	***************************************************************
	*															  *
	*      Auxiliary methods for updating the Price Index         *										  
	*															  *
	***************************************************************
	*/
	
	/**
	 * updatePriceIndex		Given an id of an existing item, a new price and a new name, 
	 * 						update the price Index.
	 * 
	 * @param id			The ID of an existing item
	 * @param newPrice		The new price for an existing item
	 * @param newName		The new name for an existing item
	 */
	public void updatePriceIndex(long id, long newPrice, ArrayList<Long> newName){

		// For index on price, increment the count for Item <id, newPrice, newName> by one
		addToPriceIndex(id, newPrice, newName);

		// Get the name and price of the existing item
		Item oldItem = this.itemList.get(id);

		long oldPrice = oldItem.getPrice();  // Get the price of an existing item

		ArrayList<Long> oldName = oldItem.getName(); // Get the name of an existing item

		// For index on price, decrement the count for Item <id, oldPrice, oldName> by one
		removeFromPriceIndex(id, oldPrice, oldName);

	}
	
	/**
	 * addToPriceIndex		Given an id of a new item, a new price and a new name, 
	 * 						add the item into the price Index by incrementing 
	 * 						the count by 1.
	 * 
	 * @param id			The ID of a new item
	 * @param newPrice		The price for a new item
	 * @param newName		The name for a new item
	 */
	public void addToPriceIndex(long id, long price, ArrayList<Long> name){

		// Iterate through all the long ints (keyword) that are in the name of the item
		Iterator<Long> itr = name.iterator();

		while(itr.hasNext()){

			Long keyword = itr.next(); // Get the keyword in the name
			
			TreeMap<Long, Long> priceIndex = this.keywordIndex.get(keyword);
			
			if(priceIndex != null){ // If a <keyword, priceIndex> pair exists in the Index on keyword

				Long count = priceIndex.get(price); // Get the number of items that have the same price

				if(count == null){ // If there is no item whose name contains the keyword, Initialize the count as 1.
					priceIndex.put(price, 1l); 
				}
				else{ // If there is an item whose name contains the keyword, Increment the count by 1
					priceIndex.put(price, count + 1);  
				}
				
			} else{ // If items of the same price does not exist

				priceIndex = new TreeMap<Long, Long>(); // Create a new index on price
				
				priceIndex.put(price, 1l);	// Initialize the count as 1
				
				this.keywordIndex.put(keyword, priceIndex); // Add the new <keyword, priceIndex> pair to Index on keyword
			}
			
		}
	}
	
	/**
	 * removeFromPriceIndex		Given an id of a new item, a new price and a new name, 
	 * 						    remove the item from the price Index by decrementing 
	 * 							the count by 1.
	 * 
	 * @param id				The ID of an existing item
	 * @param price				The price for an existing item
	 * @param name				The name for an existing item
	 * @return
	 */
	public long removeFromPriceIndex(long id, long price, ArrayList<Long> name){

		long retval = 0;

		// Iterate through all the long ints (keyword) that are in the name of the item
		Iterator<Long> itr = name.iterator();

		while(itr.hasNext()){

			Long keyword = itr.next(); // Get the keyword in the name

			retval += keyword;  // Get the sum of the keywords that are in the name of the item deleted

			TreeMap<Long, Long> priceIndex = this.keywordIndex.get(keyword);

			if(priceIndex != null){ // If the priceIndex for a given keyword exists

				Long count = priceIndex.get(price); // Get the number of items that have the same price

				if(count != null){ 
					if(count >= 2){ //If the count is more than one in the priceIndex

						priceIndex.put(price, count - 1);  // Decrement the count by 1

					} else { // If the count is one in the priceIndex
						priceIndex.remove(price); // Remove this node from the priceIndex
					}
				}
				
				// If there is no priceIndex for a given keyword, remove that keyword from the keywordIndex
				if(priceIndex.isEmpty()){
					keywordIndex.remove(keyword);
				}
			}
		}

		return retval;
	}
	
	
	/*
	***************************************************************
	*															  *
	* 		   Auxiliary methods for operations on price   		  *										  
	*															  *
	***************************************************************
	*/
	
	/**
	 * getNewPrice 	Given an old price and increment ratio of the price hike, 
	 * 				return the new price of the item. Discard any fractional 
	 * 				pennies in the new prices of items.
	 * 
	 * @param r		The increment ratio of item price
	 * @return		The new price of the item after price hike.
	 */
	public long getNewPrice(long price, int r){

		// Increase the price by r%, discard any fractional pennies in the new prices of items.
		price = (long) (price * (1 + r / 100.0)); 
		
		return price;
	}
	
	
	/**
	 * doubleToLong 	Given a double value, convert it into a long type.
	 * 					Multiply the double value by 100, assuming that
	 * 					all double values have exact two decimal places.					
	 * 
	 * @param val		The double value to be converted.
	 * @return			A long value converted from the double value.
	 */
	public long doubleToLong(double val){
		// Multiply a double by 100 and cast it into long
		long l = (long) (val * 100); 
		return l;
	}
	
	/**
	 * longToDouble 	Given a long value, convert it into a double value.
	 * 					Divide the long value by 100, assuming that
	 * 					all double values have exact two decimal places.
	 * 
	 * @param val		The double value to be converted.
	 * @return			A double value converted from the long value.
	 */
	public double longToDouble(long val){
		// Divide a long by 100 and cast it into double
		double d = (double) (val / 100.0); 
		return d;
	}
	
	
	/*
	***************************************************************
	*															  *
	* 	Populate the store with the items from a input file   	  *										  
	*															  *
	***************************************************************
	*/
	
	/**
	 * populateItem		Given a file of items, populate the store with these items.
	 * 
	 * @param file		The input file that contains the item information and operations on the item
	 * @return			The sum of the return values for each operation.
	 */
	public double populateItem(File file){

		long sum = 0;  // The sum of all integer values
		
		long priceSum = 0;  // The sum of all the dollar and cents values

		// Create a scanner object to get input from the file InputStream
		Scanner in = null;

		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		while (in.hasNext()) {
			
			// Get the command mode from the input
			String cmd = in.next();

			// If the command is Insert
			if(cmd.equals("Insert")){

				long id = in.nextLong(); // Get ID from input

				double float_price = in.nextDouble(); // Get price from input

				long price = doubleToLong(float_price); // Convert price to long int

				String fullName = in.nextLine(); // Get full name from input

				fullName = fullName.substring(1, fullName.length() - 1); // Trim the trailing 0 in the line

				ArrayList<Long> name = parseName(fullName); // Parse the full name into a list of long ints (keywords)

				sum += Insert(id, price, name); // Add the return value (0/1) of insertion to the final result

			}

			// If the command is Find.
			if(cmd.equals("Find")){

				long id = in.nextLong(); // Get ID of the item from input

				priceSum += Find(id); // Add the price of the found item to the final result

			}

			// If the command is Delete.
			if(cmd.equals("Delete")){

				long id = in.nextLong(); // Get ID of the item from input

				sum += Delete(id); // Add the sum of keywords of the deleted item to the final result

			}

			// If the command is FindMaxPrice.
			if(cmd.equals("FindMaxPrice")){

				long n = in.nextLong(); // Get a keyword n from input

				priceSum += FindMaxPrice(n); // Add the Max Price to the final result

			}

			// If the command is FindMinPrice.
			if(cmd.equals("FindMinPrice")){

				long n = in.nextLong(); // Get a keyword n from input

				priceSum += FindMinPrice(n); // Add the Min Price to the final result

			}

			// If the command is FindPriceRange.
			if(cmd.equals("FindPriceRange")){

				long n = in.nextLong();  // Get a keyword n from input
				
				// Get the lower bound and upper bound of the price range,
				// and convert them into long type
				double float_low = in.nextDouble();
				double float_high = in.nextDouble();

				long low = doubleToLong(float_low);
				long high = doubleToLong(float_high);

				sum += FindPriceRange(n, low, high); // Add the number of items falls within the range to the final result

			}

			// If the command is PriceHike.
			if(cmd.equals("PriceHike")){

				// Get the lower bound and upper bound of the ID range,
				long l = in.nextLong();
				long h = in.nextLong();
				
				// Get the increase ratio of the price hike
				int r = in.nextInt();

				priceSum += PriceHike(l, h, r); // Add the net increases of prices falls within the range to the final result

			}

		}

		// Add the sum of integer-type return values and dollar and cents-type return values
		long retval = (sum * 100 + priceSum);

		//Convert the result from long to double type
		double result = longToDouble(retval);


		try {
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * parseName			An auxiliary method to parse the full name of an item into a list of long ints.
	 * 
	 * @param fullName		An string that represents the full name
	 * @return				An array list of the long ints in the name
	 */
	public ArrayList<Long> parseName(String fullName){

		// Create a new scanner object that takes a string as input
		Scanner keywordScanner = new Scanner(fullName);

		// Create a list to store the keywords in the full name
		ArrayList<Long> name = new ArrayList<Long>(); 

		while(keywordScanner.hasNext()){

			Long keyword = keywordScanner.nextLong(); // Get the next keyword from full name

			name.add(keyword); // Append the keyword to the name list
		}

		try {
			keywordScanner.close(); // Close the scanner
		} catch (Exception e) {
			e.printStackTrace();
		}

		return name;

	}
	
}
