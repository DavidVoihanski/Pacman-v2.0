package Utils;

import Coords.LatLonAlt;

public class Fruit {
	
	private LatLonAlt location;
	private int id;
	public Fruit(String line) {
		extractGpsAndId(line);
	}
	
	public LatLonAlt getLocation() {
		return location;
	}

	public void setLocation(LatLonAlt location) {
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private void extractGpsAndId(String line) {
		String arr[] = line.split(",");
		double lat = Double.parseDouble(arr[0]);
		double lon = Double.parseDouble(arr[1]);
		double alt = 0;
		this.location = new LatLonAlt(lat, lon, alt);
		this.id = Integer.parseInt(arr[2]);
	}
	
}
