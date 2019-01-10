package Utils;

import java.util.ArrayList;

import Coords.LatLonAlt;
/**
 * This class holds path to closest fruit and its id
 * @author David&evegny
 *
 */
public class Solution {
	
	private ArrayList<LatLonAlt>path;
	private int idOfTargetFruit;
	
	public Solution(ArrayList<LatLonAlt>path,int id) {
		this.path=path;
		this.idOfTargetFruit=id;
	}

	public ArrayList<LatLonAlt> getPath() {
		return path;
	}

	public void setPath(ArrayList<LatLonAlt> path) {
		this.path = path;
	}

	public int getIdOfTargetFruit() {
		return idOfTargetFruit;
	}

	public void setIdOfTargetFruit(int idOfTargetFruit) {
		this.idOfTargetFruit = idOfTargetFruit;
	}
	
}
