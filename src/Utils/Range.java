package Utils;

import java.io.IOException;

import Coords.LatLonAlt;
import Geom.Point3D;
import Utils.MyCoords;

public abstract class Range {

	private static final double ratio = 0.6596583225494952;
	private final static double mapH = 422.6079031174025;
	private final static double mapW = 945.2295422334731;
	private final static Point3D topLeftP = new Point3D(32.10574, 35.20228, 0.0);
	private final static int originalPixelHeight=642;
	private final static int originalPixelWidth=1433;
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

	public static LatLonAlt pixel2Gps(Point3D pixelClicked, double dynHeight, double dynWidth) throws IOException {
		Point3D vectorMeter = calcDiffMeterVector(pixelClicked, dynHeight, dynWidth);// calculating the difference meter
																						// vector and adding it to the
																						// to
		// left pixel after we normalize it by the ratio
		GpsCoord afterAdded = new GpsCoord(converter.add(topLeftP, vectorMeter));
		LatLonAlt output = new LatLonAlt(afterAdded.getLat(), afterAdded.getLon(), afterAdded.getAlt());
		return output;
	}

/////******************PRIVATE********************************

	private static double calcDymHRatio(double windowsHeight) {
		double dynHeight = windowsHeight;
		return mapH / dynHeight;// the division between maps height and the current guis height
	}

	// calculating changing - dynamic width ratio
	private static double calcDymWRatio(double windowsWidth) {
		double dynWidth = windowsWidth;
		return mapW / dynWidth;// the division between maps width and the current guis width
	}

	private static Point3D calcDiffMeterVector(Point3D pixelClicked, double dynHeight, double dynWidth)
			throws IOException {
		double hRatio = dynHeight / originalPixelHeight;// calculating height ratio of the resolution as for right now
		double wRatio = dynWidth / originalPixelWidth;// calculating width ratio of the resolution as for right now
		Point3D vector = new Point3D((((-1) * (pixelClicked.y() - 57)) * (ratio / hRatio)),
				((pixelClicked.x()) * (ratio / wRatio)), 0);// calculating the actual vector and returning it
		return vector;
	}

}
