package Utils;

import Coords.LatLonAlt;

public class Ghost {
	private LatLonAlt position;
	public Ghost(LatLonAlt pos) {
		this.position=pos;
	}
	public LatLonAlt getPosition() {
		return position;
	}
	public void setPosition(LatLonAlt position) {
		this.position = position;
	}
}
