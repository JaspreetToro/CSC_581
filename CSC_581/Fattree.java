
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Fattree {

	public static void main(String[] args) {

		System.out.println("Hello enter k please.");
		Scanner in = new Scanner(System.in);

		int k = in.nextInt();

		Map<Integer, String> topoMap = CreateTopology(k);

		System.out.println("print the id for first Pm");
		int i = in.nextInt();
		System.out.println("print the id for first Pm");
		int j = in.nextInt();

		System.out.println(topoMap);
		NumberOfHops(topoMap, k,i,j);

		in.close();
	}

	private static void NumberOfHops(Map<Integer, String> topoMap, int k,int i,int j) 
	{
		int hops;
		if ( i >= 0 && j <= Math.pow(k, 3)/4 -1) 
		{
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
			else if(Math.floor((4*i)/Math.pow(k, 2)) != Math.floor((4*j)/Math.pow(k, 2)))
			{
				hops = 6;
			}
			
			else
			{
				System.out.println("error");
				return;
			}
	
			System.out.println("Number of Hops: "+ hops);
		}
		else if (i >= Math.pow(k, 3)/4  && j <= Math.pow(k, 3)/4 + Math.pow(k, 2)/2 - 1)
		{
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
			System.out.println("Number of Hops: "+ hops);
		}
		else if (i >= Math.pow(k, 3)/4 + Math.pow(k, 2)/2 && j <= Math.pow(k,3)/4 + Math.pow(k, 2)/2 + Math.pow(k, 2)/2 - 1 )
		{
			if((j-i) % k/2 == 0 )
			{
				hops = 2;
			}
			else 
			{
				hops = 4;
			}
			System.out.println("Number of Hops: "+ hops);
		}
		else if(i >= Math.pow(k, 3)/4 + Math.pow(k, 2)/2 + Math.pow(k, 2)/2 && j <= Math.pow(k, 3)/4 + Math.pow(k, 2)/2 + Math.pow(k, 2)/2 + Math.pow(k, 2)/4 - 1)
		{
			System.out.println("Core Switch: ");
		}
	}

	@SuppressWarnings("unused")
	private static Map<Integer, String> CreateTopology(int k) {
		//initialize map and variables needed
		Map<Integer, String> map = new HashMap<Integer, String>();

		int total = (int) (((5 * Math.pow(k, 2))+(Math.pow(k, 3)))/4);
		int numCores = (int) ( Math.pow(k, 2)/4 );
		int numAgg = (int) (Math.pow(k, 2)/2);
		int numEdge = (int) (Math.pow(k, 2)/2);
		int numPm = (int) (Math.pow(k, 3)/4);		
		int idNum = 0;
		int localTotal = numPm;


		//assign map
		//assign PMs 
		while(idNum < localTotal){
			map.put(idNum, "PM");
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

}
