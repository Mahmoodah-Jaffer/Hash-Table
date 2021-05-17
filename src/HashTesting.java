/**
*Store datetime, voltage and power in an object called PowerStation
*@author Mahmoodah Jaffer - JFFMAH001
*@since 28 March 2019
*/

/**
HashFunction class stores the powerstation object in a hash table so that  it can be easily found
Probing method chosen in quadratic probing as this method of probing generally produces less collisions
Since the dataset is of size 500 the size of the hash table will be 1000 as the hash table needs to be almost half full in order for quadratic probing to work effectively
*/	


import java.io.*;
import java.util.*;
import java.text.DecimalFormat;


public class HashTesting{

	public static int linepos = 0;
	public static int tableSize = 0;
	public static int linearInsert = 0;
	public static int linearSearch = 0;
	public static int sum = 0;
	public static double average = 0;
	public static int max = 0;
	public static PowerStation [] powerdata;
	public static int [] searchOp;
	public static PowerStation [] hashTable;
	public static Element [] hashTable2;
	public static ArrayList<String> re = new ArrayList<String>();
	public static ArrayList<Integer> keys = new ArrayList<Integer>();
	public static int quadInsert = 0;
	public static int quadSearch = 0;
	public static int chains = 0;
	public static int chainInsert = 0;
	public static int chainSearch = 0;
	public static int num_keys = 0;
	public static double load = 0;
	/**
	The main method takes in the argument which would be the parameters and then performs insertion and search
	on hash table based on selected collision scheme
	@param args String[]
	@exception IOException if stream to file cannot be written to 
	*/
	public static void main (String [] args) throws IOException
	{

		if (args.length==4){
			tableSize = Integer.parseInt(args[0]);
			num_keys = Integer.parseInt(args[3]);
			if (primeTest(tableSize)==0){
				System.out.println("The table size is not a prime number. Please re-enter the table size.");
			}
			else{
				ArrayList<String> item = shuffledArray(args[2]);
				if(args[1].equals("linear")){
					if(tableSize < 500){
						System.out.println("Hash Table is full. Please increase table size.");
					}
					else{
						linearSearch(args[2],num_keys ,item, tableSize);
						load = (double) 500/tableSize;
						System.out.println("Load factor after table construction: " + load);
						System.out.println("Number of insertion probes: " + linearInsert);
						System.out.println("Total number of search probes: " + sum);
						average = (double) sum/num_keys;
						System.out.println("Average number of search probes: " + average);
						System.out.println("Maximum number of search probes: " + max);
					}
				}
				else if(args[1].equals("quadratic")){
						if(quadSearch(args[2],num_keys ,item, tableSize)==0){
							load = (double) 500/tableSize;
							System.out.println("Load factor after table construction: " + load);
							System.out.println("Number of insertion probes: " + quadInsert);
							System.out.println("Total number of search probes: " + sum);
							average = (double) sum/num_keys;
							System.out.println("Average number of search probes: " + average);
							System.out.println("Maximum number of search probes: " + max);
						}
						else{
							System.out.println("Probes exceeds table size. Please increase table size");
						}
				}
				else if(args[1].equals("chaining")){
						chainSearch(args[2],num_keys ,item, tableSize);
						load = (double) 500/tableSize;
						System.out.println("Load factor after table construction: " + load);
						System.out.println("Number of insertion probes: " + chainInsert);
						System.out.println("Total number of search probes: " + sum);
						average = (double) sum/num_keys;
						System.out.println("Average number of search probes: " + average);
						System.out.println("Maximum number of search probes: " + max);

				}

			}
		}
		else{
			System.out.println("Please re-enter the parameters. All four parameters must be specified");
		}

	}

	/**
	Uses the linear collision scheme to inserted PowerStation values into the Hash Table
	@param filename String 
	@param tablesize int
	@return PowerStation[] that is the hash table that underwent linear probing to insert the required values
	@exception IOException if stream to file cannot be written to 
	@exception FileNotFoundException if file does not exist
	@see IOException 
	@see FileNotFoundException
	*/
	public static PowerStation[] linearInsert(String filename, int tablesize) throws IOException, FileNotFoundException{

		PowerStation[] hashTable = new PowerStation[tablesize];

		for (int i=0; i<tablesize;i++){
			hashTable[i]=null;
		}

		FileReader read = new FileReader(filename);
		BufferedReader bread = new BufferedReader(read);

		String firstline = bread.readLine();
		String line = bread.readLine();

		while(line!=null){
			
			String [] data = line.split(","); //the data from line is split by the commas and is being stored in an array 

			String datetime = data[0]; //sets variable datetime to the first element in data array - Date/Time
			String power = data[1];//sets variable power to the second element in data array - Global Active Power
			String voltage = data[3];//sets variable voltage to the fourth element in data array - Voltage
			boolean check = re.contains(datetime);
			re.add(datetime);

			int key = hash(datetime);
			int temp = key; 
			int j=1;

			while(hashTable[key]!=null){
				key = (temp + j)%tablesize;
				j++;
				linearInsert++;
			}


			if (check==false){
				hashTable[key] = new PowerStation(datetime, voltage, power);
			}
			
			line = bread.readLine();

		}

		return hashTable;
	}

	/**
	Uses the linear collision scheme to search for K number of keys
	@param filename String 
	@param num_keys int
	@param re ArrayList of type String
	@param tableSize int
	@exception IOException if stream to file cannot be written to 
	@exception FileNotFoundException if file does not exist
	@see IOException
	@see FileNotFoundException
	*/
	public static void linearSearch(String filename, int num_keys, ArrayList<String> re, int tableSize)throws IOException, FileNotFoundException{
			PowerStation[] table = linearInsert(filename, tableSize);
			int[] searchOp = new int[num_keys];
			int line1 =0;


			while(line1<num_keys){
				String dateTime = re.get(line1);
				int key = hash(dateTime);
				int temp = key; 
				int j=1;

				while(table[key]!=null){
					String dt = table[key].getDateTime();
					if (dt.compareToIgnoreCase(dateTime)==0){
						key = (temp + j)%tableSize;
						j++;
					}
					else{
						linearSearch++;
						key = (temp + j)%tableSize;
						j++;
					}
				}
				searchOp[line1] = linearSearch;
				linearSearch = 0;
				line1++;

			}
			max = max(searchOp);

	}

	/**
	Uses the quadratic collision scheme to inserted PowerStation values into the Hash Table
	@param filename String 
	@param tablesize int
	@return PowerStation[] that is the hash table that underwent quadratic probing to insert the required values
	@exception IOException if stream to file cannot be written to 
	@exception FileNotFoundException if file does not exist
	@see IOException
	@see FileNotFoundException
	*/
	public static PowerStation[] quadInsert(String filename, int tablesize) throws IOException, FileNotFoundException{

		PowerStation[] hashTable = new PowerStation[tablesize];

		FileReader read = new FileReader(filename);
		BufferedReader bread = new BufferedReader(read);

		String firstline = bread.readLine();
		String line = bread.readLine();

		while(line!=null){
			
			String [] data = line.split(","); //the data from line is split by the commas and is being stored in an array 

			String datetime = data[0]; //sets variable datetime to the first element in data array - Date/Time
			String power = data[1];//sets variable power to the second element in data array - Global Active Power
			String voltage = data[3];//sets variable voltage to the fourth element in data array - Voltage
			boolean check = re.contains(datetime);
			re.add(datetime);
			
			int key = hash(datetime);
			int temp = key; 
			int j=1;

			while(hashTable[key]!=null){
				key = (temp + ((j<<1)-1))%tablesize;
				j++;
				quadInsert++;

			}			

			if (check==false){
				hashTable[key] = new PowerStation(datetime, voltage, power);
			}

			line = bread.readLine();

		}
		return hashTable;

	}

	/**
	Uses the quadratic collision scheme to search for K number of keys
	@param filename String 
	@param num_keys int
	@param shuf ArrayList of type String
	@param tableSize int
	@return PowerStation[] that is the hash table that underwent quadratic probing to insert the required values
	@exception IOException if stream to file cannot be written to 
	@exception FileNotFoundException if file does not exist
	@see IOException
	@see FileNotFoundException
	*/
	public static int quadSearch(String filename, int num_keys, ArrayList<String> shuf, int tableSize)throws IOException, FileNotFoundException{
		PowerStation[] table = quadInsert(filename, tableSize);
		int[] searchOp = new int[num_keys];
		int line2 =0;
		int flag=0;

		if (quadInsert > tableSize){
			flag=1;
			return flag;
		}

			while(line2<num_keys){
				String dateTime = shuf.get(line2);

				int key = hash(dateTime);
				int temp = key; 
				int j=1;


				while(table[key]!=null){
					String dt = table[key].getDateTime();
					if (dt.compareToIgnoreCase(dateTime)==0){
						key = (temp + ((j<<1)-1))%tableSize;
						j++;
					}
					else{
						quadSearch++;
						key = (temp + ((j<<1)-1))%tableSize;
						j++;
					}
				}
				searchOp[line2] = quadSearch;
				quadSearch = 0;
				line2++;

			}
			max = max(searchOp);
		return flag;	
	}

	/**
	Uses the chaining collision scheme to insert required values into array	
	@param filename String 
	@param tablesize int
	@return Element[] that is the hash table that underwent chaining to insert the required values
	@exception IOException if stream to file cannot be written to 
	@exception FileNotFoundException if file does not exist
	@see IOException
	@see FileNotFoundException
	*/
	public static Element[] chainInsert(String filename, int tablesize) throws IOException, FileNotFoundException{

		Element[] hashTable2 = new Element[tablesize];

		FileReader read = new FileReader(filename);
		BufferedReader bread = new BufferedReader(read);

		String firstline = bread.readLine();
		String line = bread.readLine();

		while(line!=null){
			
			String [] data = line.split(","); //the data from line is split by the commas and is being stored in an array 

			String datetime = data[0]; //sets variable datetime to the first element in data array - Date/Time
			String power = data[1];//sets variable power to the second element in data array - Global Active Power
			String voltage = data[3];//sets variable voltage to the fourth element in data array - Voltage
			boolean check = re.contains(datetime);
			re.add(datetime);
			
			int key = hash(datetime);
			boolean check2 = keys.contains(key);
			keys.add(key);

			if (hashTable2[key]==null && check2 == false){
				hashTable2[key] = new Element(new PowerStation(datetime, voltage, power));
			}

			else{
				Element  temp = hashTable2[key];
				chainInsert++;
				while(temp.getNext()!=null){
					chainInsert++;
					temp = temp.getNext();
				}
				chains++;
				temp.setNext(new Element(new PowerStation(datetime, voltage, power)));
			}			

			line = bread.readLine();

		}
		return hashTable2;
	}
	
	/**
	Uses the chaining collision scheme to search for K number of keys
	@param filename String 
	@param num_keys int
	@param shuf ArrayList of type String
	@param tableSize int
	@exception IOException if stream to file cannot be written to 
	@exception FileNotFoundException if file does not exist
	@see IOException
	@see FileNotFoundException
	*/
	public static void chainSearch(String filename, int num_keys, ArrayList<String> shuf, int tableSize)throws IOException, FileNotFoundException{
		Element[] table = chainInsert(filename, tableSize);
		int[] searchOp = new int[num_keys];
		int line2 =0;

			while(line2<num_keys){
				String dateTime = shuf.get(line2);

				int key = hash(dateTime);
				int temp = key; 
				int j=1;

				String dt = table[key].key;
				if (dt.compareToIgnoreCase(dateTime)==0){
					chainSearch = chainSearch;
				}
				else{						
					Element  temp2 = table[key];
					chainSearch++;
					while(temp2.getNext()!=null && (temp2.key)!=dateTime){
						chainSearch++;
		 				temp2 = temp2.getNext();
		 			}
				}
				
				searchOp[line2] = chainSearch;
				chainSearch = 0;
				line2++;

			}
			max = max(searchOp);	
	}
	/**
	Method primeTest determines whether the size of the hash table is a prime number or not 
	@param tableSize int
	@return integer 0 if tableSize is not a prime number OR 1 if it is a prime number
	@exception IOException if stream to file cannot be written to 
	@see IOException
	*/
	public static int primeTest(int tableSize) throws IOException{

		int i,m=0;
		int flag=0;

		m = tableSize/2;

		if (tableSize==0||tableSize==1){
			return 0;//returns 0 if tableSize is not a prime number
		}
		else{ 
			for (i=2; i<=m;i++){
				if(tableSize%i==0){
					flag = 1;//flag set to 1 to indicate that the table size is not a prime number
					break;
				}
			}
			if (flag==1){
				return 0;//returns 1 if the table size is a prime number
			}
			return 1;//returns 0 if tableSize is not a prime number
		}
	}

	/**
	Method returns the maximum element in the array
	@param searchOp int []
	@return integer that is maxmimum in array
	@exception IOException if stream to file cannot be written to 
	@see IOException
	*/
	public static int max(int[] searchOp) throws IOException{
		for(int j = 0; j<searchOp.length;j++){
				sum += searchOp[j];
				if (searchOp[j]>=max){
					max = searchOp[j];
				}
		}	
		return max;	
	}
	/**
	Method hash takes in the datetime value of the PowerStation objects and gets the unicode value of each character and multiplies it by 31
	to get a random index for the hashtable 
	@param filename String
	@return ArrayList of type String that is an array of shuffled datetime values
	@exception IOException if stream to file cannot be written to 
	@exception FileNotFoundException if file does not exist
	@see IOException
	@see FileNotFoundException
	*/
	public static ArrayList<String> shuffledArray(String filename)throws IOException, FileNotFoundException{
		ArrayList<String> arr = new ArrayList<String>();
		FileReader read = new FileReader(filename);
		BufferedReader bread = new BufferedReader(read);

		String firstline = bread.readLine();
		String line = bread.readLine();

		while(line!=null){
			
			String [] data = line.split(","); //the data from line is split by the commas and is being stored in an array 

			String datetime = data[0]; //sets variable datetime to the first element in data array - Date/Time
			String power = data[1];//sets variable power to the second element in data array - Global Active Power
			String voltage = data[3];//sets variable voltage to the fourth element in data array - Voltage
			boolean check = arr.contains(datetime);
			arr.add(datetime);
			line = bread.readLine();
		}
		long seed = 44000000;
		Collections.shuffle(arr, new Random(seed));
		return arr;
	}
	/**
	Method hash takes in the datetime value of the PowerStation objects and gets the unicode value of each character and multiplies it by 31
	to get a random index for the hashtable 
	@param key String
	@return integer that will be the index value in the hashtable
	@exception IOException if stream to file cannot be written to 
	@see IOException
	*/
	public static int hash(String key) throws IOException{
		int hashVal = 0;

		for (int i=0; i<key.length(); i++){
			hashVal = (32*hashVal) + key.charAt(i);
		}
		return Math.abs(hashVal%tableSize);
	}
	/**
	Element is an innerclass of the class HashFunction that creates a node of type Element in each element of the hash table
	*/
	public static class Element{
		PowerStation data;
		Element next;
		String key;

		/**
		Element constructor takes in an object of type PowerStation and will store it as the value of a node in the HashTable
		@param item PowerStation
		*/
		public Element(PowerStation item){
			data = item;
			key = item.getDateTime();
		}

		/**
		Method getNext returns the address of the next node in the linked list
		@return Element datetime
		*/
		public Element getNext(){
			return next;
		}

		/**
		Method setNext sets the address of the next node in the linked list
		@param next Element
		*/
		public void setNext(Element next){
			this.next = next;
		}
	}	

}