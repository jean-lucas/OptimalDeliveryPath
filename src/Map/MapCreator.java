package Map;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import misc.Location;



/**
 * This class is responsible for all the outputs to the client (HTML file and googleMaps)
 */
public class MapCreator {

	ArrayList<Location> list;
	int count;
	boolean getMap;
	
	public MapCreator(ArrayList<Location> Storelist,  int mapCount, boolean b) {
		list = Storelist;
		count = mapCount;
		getMap = b;
		setup();
	}
	
	public void setup() {
		generateOutputHTML();
		if (getMap) generateGoogleMap();
	}
	
	

	/**
	 * From template.html found in data folder, we can create an output.html with any information we wish.
	 * In this case, we will genereate the a list of directions in order from first to last store to visit.
	 * 
	 * Each driver will be given a different generated file
	 */
	private void generateOutputHTML() {

		
		try {
			Scanner in = new Scanner(new File("data/web/template.html"));
			PrintStream out = new PrintStream("data/web/output"+count+".html");
			System.setOut(out);
			
			while (in.hasNext()) {
				String line = in.nextLine();
				System.out.print(line);
				
				if (line.equals("<title>")) System.out.println("Path #" +count);
				
			
				if (line.equals("<body>")) { 				// this is where all the good stuff goes..
					
					System.out.println("<div class='bodyHeader'>");
					System.out.println("<p><strong>in order path for driver #" +count+ " " + "</strong></p>");
					System.out.println("</div>");
					
					System.out.println("<br><br><hr class='hrStyle'/>");
					
					// Creating info about the path in a table format
					System.out.println("<div class='pathInfo'>");
					System.out.println("<table class='infoTable' cellpadding='3' cellspacing='10'>");
					System.out.println("<tr><td>City: </td><td> " + list.get(0).getCity()+ " - " + list.get(0).getState() + "</td></tr>");
					System.out.println("<tr><td>Number of stops: </td><td>  " + (list.size()-1) + "</td></tr>");
					System.out.println("<tr><td>Store: </td><td> " + list.get(1).getName().toUpperCase()+ "</td></tr>");
					System.out.println("</table>");
					
//TODO: get starting address --- System.out.println("Starting address: " + list.get(0).getAddress());
					
					System.out.println("</div>");	
					
					
					list.remove(0);
					
					//create ordered table/list of path
					System.out.println("<div class='pathList'>");	
					System.out.println("<table class='pathTable' cellspacing='15'>");
					System.out.println("<tr><th>Order</th><th>Address</th><th></th><th>Latitude , Longitude</th></tr>");
					int i = 1;
					for (Location c: list) {
					// creating table row for each stop in route
						System.out.println("<tr>" + "<td>"+(i++)+"</td>" + c.toHTMLString() +"</tr>");			
					}
					
					System.out.println("</ol>");
					System.out.println("<br>");
					System.out.println("</div>");	
				}
				
				System.out.println();
			}
			
			
			in.close();
			out.close();
			
			//opening html file to the default browser in the system
			File htmlFile = new File("data/web/output"+count+".html");
			Desktop.getDesktop().browse(htmlFile.toURI());
		}
		
		catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		} 
	}
	
	
	/**
	 * A map representaion of the route will only be created if the client desires.
	 * If so, this function will generate a URL with the syntax of Google Maps URL.
	 * 
	 * IMPORTANT: number of stores generated on this map can be at MOST 25 (issue with google maps)
	 */
	private void generateGoogleMap() {
		
		
		if (list.size() > 25) {
			try {
				Desktop.getDesktop().browse(new File("data/web/maxSize.html").toURI());
				return;
			} 
			catch (IOException e) {
				System.out.println(e.getLocalizedMessage());
			}
		}
		String[] points = new String[list.size()];		
		
		// generate a list of latitude and longitude points
		for (int i = 0 ; i < list.size(); i++) {
			String lat = list.get(i).getLat() + "";
			String lon = ""+list.get(i).getLon() + "";
			points[i] = lat+","+lon;
		}
		
		String path = "http://maps.google.com/maps?saddr=";
		
		//initial point
		path += points[0];
		
		path += "&daddr=";
		
		// add all other stops in between the route
		for (int i = 1; i < list.size(); i++) {
			if (i == 1) path += points[i];
			else   path += "+to:" + points[i];
		}
		
		
		// wrap back around to final position
		path+= "+to:"+points[0];
		
		try {
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(path));
		} 
		catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
}

