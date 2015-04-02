package misc;
/**
 * @author Jean Lucas
 *
 * Represent a unique store on a map, with attributes such as:
 * chain name, address, and latitude and longitude points.
 * 
 * TODO: implement an alternative of toString() method
 */

public class Location {
	private final double R = 6378100;
	private double latidude;		// latitude point with format XX.xxxx
	private double longitude;		// longitude point with formath YY.yyyy
	private String storeName; 		// Name of store (Starbucks, WalMart, etc..)
	private String address;			// Includes street name, street number and zip
	private String city;
	private String state;
	private int ID; 			// each location has an unique ID, helps when using graph algorithms
	public boolean isMarked = false;
	
	// standard constructor for a Location object
	public Location(double lat, double lon, String name, String address, String city, String state, int id) {
		this.latidude = lat;
		this.longitude = lon;
		this.storeName = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.ID = id;  
	}
	

	// secondary constructor for city Locations where the ID, name, and address are irrelevant
	public Location(double lat, double lon, String city, String state) {
		this(lat, lon, "", "", city, state, 0);
	}
	
	
	
	public double getLat() { 
		return this.latidude; 
	}
	public double getLon() {
		return this.longitude; 
	}
	
	public String getName() {
		return this.storeName.trim(); 
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public String getCity() {
		return this.city.trim();
	}
	
	public String getState() {
		return this.state.trim();
	}
	
	public int getID() { 
		return this.ID;
	}
	
	public void setID(int id) {
		this.ID = id;
	}
	
	
//calculates the distance using harversine function from this object, to Location object b
		public int getDistance(Location b) {
			double dLat = Math.toRadians(b.getLat() - this.getLat());
			double dLon = Math.toRadians(b.getLon() - this.getLon());
			double lat1 = Math.toRadians(this.getLat());
			double lat2 = Math.toRadians(b.getLat());
			
			double val1 = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
			double c = 2*Math.asin(Math.sqrt(val1));
			return (int) Math.floor(R*c);
		}

	@Override
	public String toString() {
		return String.format("%s: %s %25s %s %s %s (%4f %4f) ",
				this.getName(),"&nbsp&nbsp&nbsp&nbsp&nbsp",this.getAddress(), this.getCity(), this.getState(),
				"&nbsp&nbsp&nbsp&nbsp&nbsp",this.getLat(),this.getLon() );
		
//		
//		return String.format("%4f  %4f \t  %-20s  \t %-20s  %-5s  %-20s %n",
//												 this.getLat(),this.getLon(), this.getName(), this.getCity(), this.getState(), this.getAddress() );
	}
	
	

	@Override 
	public boolean equals(Object obj) {
		if (obj == null) return false;																		
		if (!(obj instanceof Location)) return false;										
	
		Location objTest = (Location) obj;	//temporary Locatiob object for testing purposes
		
		if (objTest.getID() == this.getID()) 
			return true;
		
		return false;
	}
	
}


