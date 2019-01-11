package Utils;

import java.util.ArrayList;

import Coords.LatLonAlt;
import Geom.Point3D;

/**
 * This class represents a black box on the map
 * @author David&evegny
 *
 */
public class Rectangle {
	private LatLonAlt topRight;
	private LatLonAlt bottomLeft;
	private LatLonAlt bottomRight;
	private LatLonAlt topLeft;
	private ArrayList<LatLonAlt> acceciblePoints; //one meter to the side from every corner
	public Rectangle(String line) {
		extractGps(line);
		bottomRight=new LatLonAlt(bottomLeft.x(),topRight.y(),0);
		topLeft=new LatLonAlt(topRight.x(),bottomLeft.y(),0);
		calcAcceciblePoints();
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
	
	public LatLonAlt getBottomRight() {
		return bottomRight;
	}

	public void setBottomRight(LatLonAlt bottomRight) {
		this.bottomRight = bottomRight;
	}

	public LatLonAlt getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(LatLonAlt topLeft) {
		this.topLeft = topLeft;
	}

	public ArrayList<LatLonAlt> getAcceciblePoints() {
		return acceciblePoints;
	}

	public void setAcceciblePoints(ArrayList<LatLonAlt> acceciblePoints) {
		this.acceciblePoints = acceciblePoints;
	}

	public Point3D getWidthAndHeight(double guiH, double guiW) {
		Point3D topRightPixel = Range.gps2Pixel(this.topRight, guiH, guiW);
		Point3D bottomleftPixel = Range.gps2Pixel(this.bottomLeft, guiH, guiW);
		Point3D output = new Point3D((topRightPixel.x()-bottomleftPixel.x()),(bottomleftPixel.y()-topRightPixel.y()));
		return output;
	}
	private void extractGps(String line) {
		String arr[] = line.split(",");
		double lat = Double.parseDouble(arr[0]);
		double lon = Double.parseDouble(arr[1]);
		double alt = 0;
		bottomLeft = new LatLonAlt(lat, lon, alt);
		lat = Double.parseDouble(arr[2]);
		lon = Double.parseDouble(arr[3]);
		topRight = new LatLonAlt(lat, lon, alt);
	}
	//takes every corner of this rect and add a point one meter away in the corner's position
	private void calcAcceciblePoints() {
		acceciblePoints=new ArrayList<>();
		MyCoords converter=new MyCoords();
		Point3D vector1=converter.vector3D(bottomRight, topLeft);
		Point3D vector2=converter.vector3D(bottomLeft, topRight);
		vector1=Range.normVec(vector1);
		vector2=Range.normVec(vector2);
		LatLonAlt toAdd=new LatLonAlt(topRight);
		Point3D temp=converter.add(toAdd, vector2);
		toAdd=new LatLonAlt(temp.x(),temp.y(),0);
		acceciblePoints.add(toAdd);
		toAdd=new LatLonAlt(topLeft);
		temp=converter.add(toAdd, vector1);
		toAdd=new LatLonAlt(temp.x(),temp.y(),0);
		acceciblePoints.add(toAdd);
		vector1=Range.multVec(-1, vector1);
		vector2=Range.multVec(-1, vector2);
		toAdd=new LatLonAlt(bottomLeft);
		temp=converter.add(toAdd, vector2);
		toAdd=new LatLonAlt(temp.x(),temp.y(),0);
		acceciblePoints.add(toAdd);
		toAdd=new LatLonAlt(bottomRight);
		temp=converter.add(toAdd, vector1);
		toAdd=new LatLonAlt(temp.x(),temp.y(),0);
		acceciblePoints.add(toAdd);
	}
	
}
