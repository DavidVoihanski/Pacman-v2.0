package Utils;

import Coords.LatLonAlt;

public class MyPlayer {
	private LatLonAlt position;
	public MyPlayer(LatLonAlt pos) {
		this.position=pos;
	}
	public LatLonAlt getPosition() {
		return position;
	}
	public void setPosition(LatLonAlt position) {
		this.position = position;
	}
}
