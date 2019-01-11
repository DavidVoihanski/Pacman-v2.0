package Utils;

import Coords.LatLonAlt;

/**
 * This class represents the player of a game 
 * @author David
 *
 */
public class MyPlayer {
	private LatLonAlt startPosition;
	public MyPlayer(LatLonAlt pos) {
		this.startPosition=pos;
	}
	public LatLonAlt getPosition() {
		return startPosition;
	}
	public void setPosition(LatLonAlt position) {
		//this.position = position;
	}
}
