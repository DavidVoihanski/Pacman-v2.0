package Utils;

import Coords.LatLonAlt;
/**
 * This class represents a ghost on the map
 * @author David
 *
 */
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
