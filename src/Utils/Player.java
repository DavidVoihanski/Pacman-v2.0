package Utils;

import Coords.LatLonAlt;

public class Player {
	private LatLonAlt position;
	public Player(String line) {
		String arr[]=line.split(",");
		double lat=Double.parseDouble(arr[0]);
		double lon=Double.parseDouble(arr[1]);
		double alt=0;
		position=new LatLonAlt(lat,lon,alt);
	}
	public LatLonAlt getPosition() {
		return position;
	}
	public void setPosition(LatLonAlt position) {
		this.position = position;
	}
	
}
