package Utils;

import java.util.InvalidPropertiesFormatException;

import Geom.Geom_element;
import Geom.Point3D;

/**
 * this class represents a GPS coordinate
 * @author Evgeny&David
 */
public class GpsCoord implements Geom_element {
	// MyCoords instance used to "get" the methods from the MyCoords class
	private MyCoords convertMethods;
	// Point3D instance that represents the internal coordinates of the GPS
	// coordinate
	private Point3D internalPoint;

	/**
	 * GPS coordinate constructor
	 * 
	 * @param lat latitude of this certain GPS point in a decimal degree
	 *            representation
	 * @param lon longitude of this certain GPS point in a decimal degree
	 *            representation
	 * @param alt altitude of this certain GPS point in a decimal meters
	 *            representation
	 * @throws InvalidPropertiesFormatException in case the Point3D created doesn't
	 *                                          represent a valid GPS coordinate
	 */
	public GpsCoord(double lat, double lon, double alt) throws InvalidPropertiesFormatException {
		this.convertMethods = new MyCoords();
		this.internalPoint = new Point3D(lat, lon, alt);
		if (!convertMethods.isValid_GPS_Point(internalPoint)) {
			throw new InvalidPropertiesFormatException(
					"the point: " + internalPoint + " dose not represent a valid GPS coord");
		}
	}

	/**
	 * copy constructor which input is a Point3D instance
	 * 
	 * @param inputToCopy instance we would like to copy
	 * @throws InvalidPropertiesFormatException in case input point doesn't
	 *                                          represent a valid gps coord
	 */
	public GpsCoord(Point3D inputToCopy) throws InvalidPropertiesFormatException {
		this.convertMethods = new MyCoords();
		if (convertMethods.isValid_GPS_Point(inputToCopy)) {// validating that the point is valid gps coord
			this.internalPoint = new Point3D(inputToCopy);
		} else
			throw new InvalidPropertiesFormatException(
					"the point: " + inputToCopy + " dose not represent a valid GPS coord");
	}

	/**
	 * 
	 * @param inputToCopy
	 * @throws InvalidPropertiesFormatException
	 */
	public GpsCoord(Geom_element inputToCopy) throws InvalidPropertiesFormatException {
		this.convertMethods = new MyCoords();
		this.internalPoint = (Point3D) ((GpsCoord) inputToCopy).getInternalPoint();
	}

	/**
	 * 
	 * @param inputCoord
	 * @param meterVector
	 * @return
	 */
	public Point3D add(Point3D meterVector) {
		return (convertMethods.add(this.getInternalPoint(), meterVector));
	}

	/**
	 * basic toString method
	 */
	@Override
	public String toString() {
		return ("" + this.internalPoint.x() + ", " + this.internalPoint.y() + ", " + this.internalPoint.z());
	}

	/**
	 * calculates the 3D distance between two GPS coordinates
	 * 
	 * @param inPutCoord input GPS coordinate
	 * @return 3D distance in meters
	 */
	public double distance3D(GpsCoord inPutCoord) {
		return (convertMethods.distance3d(this.internalPoint, inPutCoord.internalPoint));
	}

	/**
	 * calculates the 2D distance between two GPS coords
	 * 
	 * @param inPutCoord input GPS coordinate
	 * @return 2D distance in meters
	 */
	public double distance2D(GpsCoord inPutCoord) {
		return convertMethods.distance2d(this.internalPoint, inPutCoord.internalPoint);
	}

	/**
	 * calculates the difference 3D vector between two points
	 * 
	 * @param inPutCoord input GPS coordinate
	 * @return 3D vector which values are the difference between both GPS
	 *         coordinates in meters
	 */
	public Point3D vector3D(Point3D inputGpsInternalPoint) {
		return convertMethods.vector3D(this.internalPoint, inputGpsInternalPoint);
	}

	/**
	 * calculates the azimuth, elevation and distance between two GPS coordinates
	 * 
	 * @param inPutCoord input GPS coordinate
	 * @return a double array which has 3 elements as [0] is azimuth in decimal
	 *         degrees, [1] is elevation in decimal degrees and [2] is 3D distance
	 *         in meters
	 */
	public double[] azimuth_elevation_dist(GpsCoord inPutCoord) {
		return convertMethods.azimuth_elevation_dist(this.internalPoint, inPutCoord.internalPoint);
	}

	@Override
	public double distance3D(Point3D p) {
		return distance3D(p);
	}

	@Override
	public double distance2D(Point3D p) {
		return distance2D(p);
	}

	// getters:
/**
 * 
 * @return the point3D inside the GpsCoord
 */
	public Point3D getInternalPoint() {
		return internalPoint;
	}
/**
 * 
 * @return lat value of the Gps coord
 */
	public double getLat() {
		return this.internalPoint.x();
	}
/**
 * 
 * @return lon value of the Gps coord
 */
	public double getLon() {
		return this.internalPoint.y();
	}
/**
 * 
 * @return alt value of the Gps coord
 */
	public double getAlt() {
		return this.internalPoint.z();
	}

}