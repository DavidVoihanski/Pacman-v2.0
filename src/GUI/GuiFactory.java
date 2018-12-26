package GUI;

import Coords.LatLonAlt;
import Geom.Point3D;
import Utils.Positionts;

public abstract class GuiFactory {
	private double ratio=0.6596583225494952;
	private double mapH=422.6079031174025;
	private double mapW=937.2295422334731;
	private Point3D topLeftP=new Point3D(32.10574,35.20228,0.0);
	public Point3D gps2Pixel(LatLonAlt gps) {
		Point3D temp = new Point3D(gps.getLat(), gps.getLon(), gps.getAlt());
		//topLeftP.
		Point3D vector =  topLeftP.vector3D(temp); // creating a meter vector between the top left Gps coord to the
													// chosen point)
		double hRatio = calcDymHRatio();// calculating height ratio of the resolution as for right now
		double wRatio = calcDymWRatio();// calculating width ratio of the resolution as for right now
		// taking the integer part of the division as we talk about pixels
		double y = Math.ceil(vector.y() / wRatio);
		double x = Math.ceil(-vector.x() / hRatio);
		Point3D output = new Point3D(x, y, 0);// creating a new point and returning it - these are the pixels
		return output;
	}
}
