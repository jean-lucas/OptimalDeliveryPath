package PathFinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import misc.Location;
import FileOp.FileOperator;
import Map.MapCreator;


/**
 * Find an optimal path from a list of Location objects (stores) given
 * 
 * TODO:  Try to find ways to make it more efficient
 */
public class PathFinder {

	private Digraph G;
	
	
	// anything labeled with " //remove " is just there for testing purposes
	// and corresponding lines will be removed for final implementation
	public PathFinder(ArrayList<Location> storesInSection, Location nearestCenter, boolean getMap) {
		
		if (getMap)
			new MapCreator().generateMap(storesInSection);		//remove
		
		// give each location an id # from 0..size     this is bad and slows down program. Find better way
		for (int i = 0; i < storesInSection.size(); i++) 
			storesInSection.get(i).setID(i);
		
		for (Location s: storesInSection) 				//remove
			System.out.println(s.getAddress() + " ==== " + s.getID());		// remove
		
		
		G = new Digraph(storesInSection.size());
		nearestNeighbour(storesInSection, nearestCenter, G);
		
		
//		System.out.println(G.toString());		//remove
		
		
//		writeToOutput(G);
	}
	
		
	

	/**
	 * This function recursively calls itself with a shorter list of valid store locations,
	 * and a new center point within the store list.
	 * When two stores are found to be optimally close together, they are connected by an edge in G
	 * 
	 * @param storeList		valid Stores to create a path from
	 * @param center			position to calculate distance from
	 * @param G						Digraph
	 */
	public void nearestNeighbour(ArrayList<Location> storeList, Location center, Digraph G) {
		
		int size = storeList.size();
		if (size == 0) return;		// base case
		
		Location tempCenter = null;
		
		center.isMarked = true;			
		storeList.remove(center);		
		
		for (int radius = 0; radius < 30; radius++) {
			tempCenter =   storesInRadius(center,storeList,radius);	 // check if any stores are within current radius
			
			if (tempCenter != null) {                               // if true, a store has been found
				G.addEdge(center.getID(), tempCenter.getID());        // connect it to the current Center store
				nearestNeighbour(storeList, tempCenter, G);						// recusive call
			}
		}
	}
	
	
	
	/**
	 * Finds the FIRST Location/store that is within the radius
	 * @param center	Position to calculate distance from
	 * @param stores	List of valid stores to measure distances
	 * @param r		radius bound
	 * @return	returns the first location, else it returns null if nothing is found
	 */
	private Location storesInRadius(Location center, ArrayList<Location> stores,int r) {
		// if stores is empty, the loop will not run, and will return null
		
		for (Location store: stores) {
			if (center.getDistance(store) <= r*1000) // multiply by 1000 to get meters
				return store;
		}
		return null;
	}
	
	
//	private void writeToOutput(Digraph G) {
//		try {
//			Scanner in = new Scanner(new File("data/template.html"));
//			PrintStream out = new PrintStream("data/output.html");
//			System.setOut(out);
//			while (in.hasNext()) {
//				String line = in.nextLine();
//				System.out.println(line);
//				if (line.equals("<title>")) System.out.println("Map 1");
//				
//				if (line.equals("<body>")) System.out.println(G.toString());
//			}
//			
//			in.close();
//			out.close();
//		}
//		catch (FileNotFoundException e) {
//			System.out.println(e.getLocalizedMessage());
//		}
//	}
	
	public static void main(String[] args) {
		
		
		FileOperator fOp = new FileOperator("mcdonalds_locations.txt","Phoenix","AZ","mcdonalds");
		Location center = fOp.getCityLocation();
		AreaDivider ar = new AreaDivider(1, fOp.getStoreInRadius(center, 5), center);
		System.out.println(ar.getMinDist().toString());
		PathFinder a = new PathFinder(ar.getSections().get(0), ar.getMinDist(),true);
	}
}
