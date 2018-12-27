package Utils;

import Coords.LatLonAlt;
import Geom.Point3D;
import Utils.MyCoords;

public abstract class Range {
	// David->>> do you really need this variable ? you dont use it
	// private final double ratio = 0.6596583225494952;
	private final static double mapH = 422.6079031174025;
	private final static double mapW = 945.2295422334731;
	private final static Point3D topLeftP = new Point3D(32.10574, 35.20228, 0.0);
	private static MyCoords converter = new MyCoords();

	public static Point3D gps2Pixel(LatLonAlt gps, double dynHeight, double dynWidth) {
		Point3D vector = converter.vector3D(topLeftP, gps); // creating a meter vector between the top left Gps coord to
															// the
		// chosen point)
		double hRatio = calcDymHRatio(dynHeight);// calculating height ratio of the resolution as for right now
		double wRatio = calcDymWRatio(dynWidth);// calculating width ratio of the resolution as for right now
		// taking the integer part of the division as we talk about pixels
		double y = Math.ceil(vector.y() / wRatio);
		double x = Math.ceil(-vector.x() / hRatio);
		Point3D output = new Point3D(y, x, 0);// creating a new point and returning it - these are the pixels
		return output;
	}

	private static double calcDymHRatio(double imageHeight) {
		double dynHeight = imageHeight;
		return mapH / dynHeight;// the division between maps height and the current guis height
	}

	// calculating changing - dynamic width ratio
	private static double calcDymWRatio(double imageWidth) {
		double dynWidth = imageWidth;
		return mapW / dynWidth;// the division between maps width and the current guis width
	}
}
