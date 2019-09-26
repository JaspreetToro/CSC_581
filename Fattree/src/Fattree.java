
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Fattree {

	public static void main(String[] args) {

		int[] nums = GetInput();
		
		HashMap<Integer, String> podMap = CreatePodMap(nums[0]);
		HashMap<Integer, String> topoMap = CreateTopology(nums[0]);
		
		PrintMap(podMap, true);
		PrintMap(topoMap, false);			

		NumberOfHops(topoMap, nums[0],nums[1],nums[2]);
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
		
		int total = (int) (((5 * Math.pow(inputs[0], 2))+(Math.pow(inputs[0], 3)))/4);

		System.out.println("Enter the first id of Pm or switch. Must be between 0 and " + total);

		inputs[1] = in.nextInt();	
		
		System.out.println("Enter the second id of Pm or switch. Must be between 0 and " + total);

		inputs[2] = in.nextInt();	
		
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


	private static void NumberOfHops(Map<Integer, String> topoMap, int k,int i,int j) 
	{
		int hops;

		if(i == j)
		{
			hops = 0;
		}
		else if(Math.floor((2*i)/k) == Math.floor((2*j)/k))
		{
			hops = 2;
		}
		else if (Math.floor((2*i)/k) != Math.floor((2*j)/k) && Math.floor((4*i)/Math.pow(k, 2)) == Math.floor((4*j)/Math.pow(k, 2)))
		{
			hops = 4;
		}
		else if(Math.floor((4*i)/Math.pow(k, 2)) == Math.floor((4*j)/Math.pow(k, 2)))
		{
			hops = 6;
		}
		else
		{
			return;
		}

		System.out.println(hops);
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
}
