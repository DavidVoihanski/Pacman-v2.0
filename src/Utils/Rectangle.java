package Utils;

import Coords.LatLonAlt;

public class Rectangle {
	private LatLonAlt topRight;
	private LatLonAlt bottomLeft;
	public Rectangle(String line) {
		String arr[]=line.split(",");
		double lat=Double.parseDouble(arr[0]);
		double lon=Double.parseDouble(arr[1]);
		double alt=0;
		bottomLeft=new LatLonAlt(lat,lon,alt);
		lat=Double.parseDouble(arr[2]);
		lon=Double.parseDouble(arr[3]);
		topRight=new LatLonAlt(lat,lon,alt);
	}
	public LatLonAlt getTopRight() {
		return topRight;
	}
	public void setTopRight(LatLonAlt topRight) {
		this.topRight = topRight;
	}
	public LatLonAlt getBottomLeft() {
		return bottomLeft;
	}
	public void setBottomLeft(LatLonAlt bottomLeft) {
		this.bottomLeft = bottomLeft;
	}
	
	
}
