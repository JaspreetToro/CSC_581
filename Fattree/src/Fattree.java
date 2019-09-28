
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Fattree {

	static HashMap<Integer, String> PodMap = new HashMap<Integer, String>();
	static HashMap<Integer, String> TopoMap = new HashMap<Integer, String>();


	public static void main(String[] args) {

		int[] nums = GetInput();		

		NumberOfHops(nums[0],nums[1],nums[2]);
	}

	private static HashMap<Integer, String> CreatePodMap(int k) {

		HashMap<Integer, String> podMap = new HashMap<Integer, String>();

		//assign map
		//assign Pod 
		for(int i = 0; i < k; i++){
			podMap.put(i, "Pod");
		}

		return podMap;
	}

	private static int[] GetInput() {
		boolean procceed = false;
		String temp;
		int input = -1;
		Scanner in = new Scanner(System.in);
		int[] inputs = new int[3];

		while(!procceed){
			System.out.println("Hello enter k please.");

			temp = in.nextLine();

			procceed = tryParseInt(temp);

			if(procceed) {
				input = Integer.parseInt(temp);
				if(input % 2 == 0 && input > 0)
					procceed = true;
				else {
					System.out.println("Error: Must be an even number greater than 0.");			
					procceed = false;
				}
			}
			else
			{
				System.out.println("Error: Must be an even number greater than 0.");	
			}
		}		
		inputs[0] = input;

		PodMap = CreatePodMap(inputs[0]);
		TopoMap = CreateTopology(inputs[0]);

		PrintMap(PodMap, true);
		PrintMap(TopoMap, false);	

		int total = (int) (((5 * Math.pow(inputs[0], 2))+(Math.pow(inputs[0], 3)))/4);

		System.out.println("Enter the first id of Pm or switch. Must be between 0 and " + total);

		inputs[1] = in.nextInt();	

		System.out.println(inputs[1] + " " + typeOf(inputs[0],inputs[1]));

		System.out.println("Enter the second id of Pm or switch. Must be between 0 and " + total);

		inputs[2] = in.nextInt();	

		System.out.println(inputs[2] + " " + typeOf(inputs[0],inputs[2]));

		in.close();
		return inputs;
	}

	public static boolean tryParseInt(String value) {  
		try {  
			Integer.parseInt(value);  
			return true;  
		} catch (NumberFormatException e) {  
			return false;  
		}  
	}

	@SuppressWarnings("unused")
	private static void NumberOfHops( int k,int i,int j) 
	{
		int hops = 0;//for now so we wont get a error later we wont initialize
		int start = 0;
		int tempI = i;
		int tempJ = j;


		//number of each nodes
		int numPms = (int)(Math.pow(k, 3)/4);
		int numEdges = (int)(Math.pow(k, 2)/2);
		int numAggs = (int)(Math.pow(k, 2)/2);
		int numCores = (int) ( Math.pow(k, 2)/4 );

		//start and end points of each type of node   						//if k = 2		k=4
		int pmStart = 0;													// 0			0
		int pmEnd = numPms - 1;												// 1			15
		int edgeStart = numPms;												// 2			16
		int edgeEnd = numPms + numEdges -1;									// 3			23
		int aggStart = edgeEnd +1;											// 4			24
		int aggEnd = aggStart + numAggs -1;									// 5			31
		int coreStart = aggEnd +1;											// 6			32
		int coreEnd = (int)(((5*Math.pow(k, 2)+ Math.pow(k, 3))/4) - 1);	// 7			35

		//determine the smaller and bigger one of the two ids
		i = Integer.min(tempI, tempJ);
		j = Integer.max(tempI, tempJ);

		if(i == j)//same node
		{
			hops = 0;
		}
		else if(typeOf(k,i) == "Physical Machine") { /////////// Physical Machine ///////////////////
			if(typeOf(k,j) == "Physical Machine") { // i and j are pm
				if(Math.floor((2*i)/k) == Math.floor((2*j)/k))// within same edge switch
				{
					hops = 2;
				}
				else if (Math.floor((4*i)/Math.pow(k, 2)) == Math.floor((4*j)/Math.pow(k, 2)))// within same pod
				{
					hops = 4;
				}
				else //any other
				{
					hops = 6;
				}

			}
			else if(typeOf(k,j) == "Edge Switch") {
				System.out.println("PM -> Edge Switch");
				int numPm = (int) (Math.pow(k, 3)/4);
				j = j - numPm;	
				if (Math.floor(i / 2) == j)
				{
					System.out.println("Under Same Edge Switch");
					System.out.println("Number of Hops: "+1);
				}
						/*else if(/4 == 0)
						{
							
						}*/
						
			}
			else if(typeOf(k,j) == "Aggregation Switch") {

			}
			else {//Pm and Core switch
				hops = 3;
			}
		}
		else if (typeOf(k,i) == "Edge Switch") { /////////////////Edge Switch ///////////////////
			if(typeOf(k,j) == "Edge Switch") { // j == edge
				int numEdge = (int) (Math.pow(k, 2)/2);
				int numPm = (int) (Math.pow(k, 3)/4);
				int edge_per_pod = numEdge/k;
				if(i >= numPm && j < 16 + edge_per_pod)
				{
					hops = 2;
				}
				else 
				{
					hops = 4;
				}
			}
			else if(typeOf(k,j) == "Aggregation Switch") { //j == agg
				if(Math.floor((i - edgeStart)/(k/2)) == Math.floor((j - aggStart)/(k/2))) {
					hops = 1;
				}
				else {
					hops = 3;
				}
			}
			else {//edge and Core switch j == core
				hops = 2;
			}
		}
		else if(typeOf(k,i) == "Aggregation Switch") { //////////////// Aggregation Switch ///////////////////
			if(typeOf(k,j) == "Aggregation Switch") {
				if(Math.floor((i - aggStart)/(k/2)) == Math.floor((j - aggStart)/(k/2)) || ((i - aggStart)%(k/2)) == ((j - aggStart)%(k/2))) {
					hops = 2;
				}
				else {
					hops = 4;
				}
			}
			else {//agg and Core switch
				if((i - aggStart)%(k/2) == Math.floor((j - coreStart)/(k/2))) {
					hops = 1;
				}
				else {
					hops = 3;
				}
			}
		}
		else { ///////////////// Core Switch //////////////////////
			if(Math.floor((i - coreStart)/(k/2)) == Math.floor((j - coreStart)/(k/2))) {
				hops = 2;
			}
			else {
				hops = 4;
			}
		}
		//		else if (i >= Math.pow(k, 3)/4 + Math.pow(k, 2)/2 && j <= Math.pow(k,3)/4 + Math.pow(k, 2)/2 + Math.pow(k, 2)/2 - 1 )
		//		{
		//			if((j-i) % k/2 == 0 )
		//			{
		//				hops = 2;
		//			}
		//			else 
		//			{
		//				hops = 4;
		//			}
		//		}


		System.out.println("Number of Hops: "+ hops);
	}

	private static HashMap<Integer, String> CreateTopology(int k) {
		//initialize map and variables needed
		HashMap<Integer, String> map = new HashMap<Integer, String>();

		int numCores = (int) ( Math.pow(k, 2)/4 );
		int numAgg = (int) (Math.pow(k, 2)/2);
		int numEdge = (int) (Math.pow(k, 2)/2);
		int numPm = (int) (Math.pow(k, 3)/4);		
		int idNum = 0;
		int localTotal = numPm;


		//assign map
		//assign PMs 
		while(idNum < localTotal){
			map.put(idNum, "Pm");
			idNum++;
		}

		//assign Edge
		localTotal += numEdge;
		while(idNum < localTotal) {
			map.put(idNum, "Edge");
			idNum++;
		}

		//assign Aggregation
		localTotal += numAgg;
		while(idNum < localTotal) {
			map.put(idNum, "Aggregation");
			idNum++;
		}

		//assign Cores
		localTotal += numCores;
		while(idNum < localTotal) {
			map.put(idNum, "Core");
			idNum++;
		}		

		return map;		
	}

	private static void PrintMap(Map<Integer, String> topoMap, boolean isPodMap) {

		String title = "";
		if(isPodMap) {
			title = "\n--------------------------------Pod------------------------------------";
		}
		else {      
			title = "\n------------------------Pm and all Switches----------------------------";
		}

		System.out.println(title);

		for(Map.Entry<Integer, String> entry : topoMap.entrySet()) {
			System.out.println(entry.getValue() + "Id: " + entry.getKey());
			//System.out.println(entry.getKey() + ":" + entry.getValue());
		}

		System.out.println("-----------------------------------------------------------------------");
	}

	public static String typeOf(int k, int id) {
		String type;

		if (id >= 0 && id <= (Math.pow(k, 3)/4) - 1)
		{
			type ="Physical Machine";

		}
		else if(id >= Math.pow(k, 3)/4 & id <= Math.pow(k, 3)/4 + Math.pow(k, 2)/2 - 1)
		{
			type ="Edge Switch";
		}
		else if (id >= Math.pow(k, 3)/4 + Math.pow(k, 2)/2 & id <= Math.pow(k, 3)/4 + Math.pow(k, 2)/2 + Math.pow(k, 2)/2 - 1 )
		{
			type ="Aggregation Switch";
		}
		else //if (id >= Math.pow(k, 3)/4 + Math.pow(k, 2) & id <= Math.pow(k, 3)/4 + Math.pow(k, 2) + Math.pow(k, 2)/4 - 1)
		{
			type ="Core Switch";
		}
		return type;
	}
}
