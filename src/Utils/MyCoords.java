package Utils;

import Coords.coords_converter;
import Geom.Point3D;

/**
 * this class is the implementation of coords_converter interface
 * 
 * @author Evgeny&David
 *
 */
public class MyCoords implements coords_converter {
	// earths radius based on given excel
	private final static int earthRadius = 6371000;
	// max distance were capable of calculating, 100KM
	private final static int diastanceLimit = 100 * 1000;

	/**
	 * adding a meter vector to the GPS coordinate and returning the new GPS
	 * coordinate after the sum <br>
	 * NOTE: if the point the method got as input doesn't represent a valid GPS
	 * coordinate the method will return its value as is without any changes, if the
	 * addition of the vector made the point invalid GPS coordinate, the method will
	 * return points first value as we got as input
	 * 
	 * @param gps                   input point which represents a gps coordinate
	 *                              which the meter vector should be added to
	 * @param local_vector_in_meter input point which represents the vector should
	 *                              be added to the gps coordinate
	 * @return a point which represents the new gps coordinate after the addition of
	 *         the vector
	 */
	@Override
	public Point3D add(Point3D gps0, Point3D local_vector_in_meter) {
		if (isValid_GPS_Point(gps0)
				&& (local_vector_in_meter.x() < diastanceLimit && local_vector_in_meter.y() < diastanceLimit)) {
			double lat0 = gps0.x();
			double lon0 = gps0.y();
			double deltaLat = local_vector_in_meter.x();
			double deltaLon = local_vector_in_meter.y();
			deltaLat = deltaLat / earthRadius;
			deltaLon = deltaLon / (earthRadius * Math.cos(Math.PI * lat0 / 180));

			// OffsetPosition, decimal degrees
			double lat1 = lat0 + deltaLat * 180 / Math.PI;
			double lon1 = lon0 + deltaLon * 180 / Math.PI;
			double alt1 = gps0.z() + local_vector_in_meter.z();
			Point3D gpsOutput = new Point3D(lat1, lon1, alt1);
			if (isValid_GPS_Point(gpsOutput)) {
				return gpsOutput;
			}
		}
		// checking whether the distance added is more than the maximum distance
		// limit
		else if (local_vector_in_meter.x() > diastanceLimit || local_vector_in_meter.y() > diastanceLimit) {
			System.out.println("ERR: you are trying to add more than the distance limit 100KM");
			return gps0;
		}
		// checking whether after the vector addition the point is still a valid GPS
		// coord
		else {
			System.out.println(
					"ERR: the sum of this vector with this given GPS coord is not a valid GPScoord, returning the input GPS coord");
			return gps0;
		}
		return gps0;
	}

	/**
	 * calculating the distance between two GPS point using the formula from given
	 * excel file which is calculated in vector3D method <br>
	 * NOTICE: the max distance this method can calculate is 200 km, any distance
	 * bigger than this will return NaN to indicate that the method didn't calculate
	 * a thing, in case one or more of the input points dosen't represent a valid
	 * GPS point the method will return NaN as well
	 * 
	 * @param gps0 first input point which represents a GPS coordinate
	 * @param gps1 second input point which represents a GPS coordinate
	 * @return double type value which represents the 2D distance between two GPS
	 *         coordinates
	 */
	@Override
	public double distance3d(Point3D gps0, Point3D gps1) {
		// checking whether these two points is valid GPS coordinates, if they are- do
		// the calculation
		if (isValid_GPS_Point(gps0) && isValid_GPS_Point(gps1)) {
			Point3D meterDiffVector = new Point3D(vector3D(gps0, gps1));
			double outPutMeterDistance = Math.sqrt(Math.pow(meterDiffVector.x(), 2) + Math.pow(meterDiffVector.y(), 2)
					+ Math.pow(meterDiffVector.z(), 2));
			if (outPutMeterDistance >= diastanceLimit) {
				System.out.println("the diastnce is too big, cannot calculate it, distance has to be less than 100KM");
				return Double.NaN;
			} else
				return outPutMeterDistance;
		}
		// in case one of them dosen't represent a valid GPS coordinate we'll print it
		// and return NaN double value
		if (!isValid_GPS_Point(gps0)) {
			System.out.println("first arg isnt a valid gps coord");
			return Double.NaN;
		} else if (!isValid_GPS_Point(gps1)) {
			System.out.println("second arg isnt a valid gps coord");
			return Double.NaN;
		} else {
			System.out.println("both arg aren't a valid gps coord");
			return Double.NaN;
		}
	}

	/**
	 * method we created although it hasn't been found in the interface, this helps
	 * us to handle 2D distance in GPS points (in case the altitude difference
	 * dosen't matter)
	 * 
	 * @param gps0 first input point which represents a GPS coordinate
	 * @param gps1 second input point which represents a GPS coordinate
	 * @return double type value which represents the 2D distance between two GPS
	 *         coordinates
	 */
	public double distance2d(Point3D gps0, Point3D gps1) {
		if (isValid_GPS_Point(gps0) && isValid_GPS_Point(gps1)) {
			Point3D meterDiffVector = new Point3D(vector3D(gps0, gps1));
			double outPutMeterDistance = Math.sqrt(Math.pow(meterDiffVector.x(), 2) + Math.pow(meterDiffVector.y(), 2));
			if (outPutMeterDistance >= diastanceLimit) {
				System.out.println("the diastnce is too big, cannot calculate it, distance has to be less than 100KM");
				return Double.NaN;
			} else
				return outPutMeterDistance;
		}
		if (!isValid_GPS_Point(gps0)) {
			System.out.println("first arg isnt a valid gps coord");
			return Double.NaN;
		} else if (!isValid_GPS_Point(gps1)) {
			System.out.println("second arg isnt a valid gps coord");
			return Double.NaN;
		} else {
			System.out.println("both arg aren't a valid gps coord");
			return Double.NaN;
		}
	}

	/**
	 * calculating the meter vector between two GPS coordinates <br>
	 * NOTE: in case one of the input values is not a valid GPS the method will
	 * return its value, in case both point are invalid the method will return null
	 * 
	 * @param gps0 first input point which represents a GPS coordinate
	 * @param gps1 second input point which represents a GPS coordinate
	 * @return 3D point which represents the difference vector between both gps
	 *         coordinates
	 */
	@Override
	public Point3D vector3D(Point3D gps0, Point3D gps1) {
		if (isValid_GPS_Point(gps0) && isValid_GPS_Point(gps1)) {
			// turning the difference between two given GPS cords to radian
			double radianLatDiff = Point3D.d2r(gps1.x() - gps0.x());
			double radianLonDiff = Point3D.d2r(gps1.y() - gps0.y());
			// turning radian to actual meters (and calculating the alt diff)
			double meterLatDiff = r2mLat(radianLatDiff);
			double meterLonDiff = r2mLon(radianLonDiff, Math.cos(Point3D.d2r(gps0.x())));
			double meterAltDiff = gps1.z() - gps0.z();
			if (meterAltDiff > diastanceLimit || meterLatDiff > diastanceLimit || meterLonDiff > diastanceLimit) {
				System.out.println("the diastnce is too big, cannot calculate it");
				return null;
			}
			// returning a 3d point with these wanted values
			return new Point3D(meterLatDiff, meterLonDiff, meterAltDiff);
		}
		if (!isValid_GPS_Point(gps0)) {
			System.out.println("first arg isnt a valid gps coord");
			return gps0;
		} else if (!isValid_GPS_Point(gps1)) {
			System.out.println("second arg isnt a valid gps coord");
			return gps1;
		} else {
			System.out.println("both arg aren't a valid gps coord");
			return null;
		}
	}

	/**
	 * returning a double type array where [0]=> is the azimuth between given gps0
	 * and gps1 <br>
	 * [1]=> is the elevation between gps0 and gps1 <br>
	 * [2]=> is the distance between gps0 and gps1 <br>
	 * NOTICE: this method won't work for two coordinates which are too distant, the
	 * MAX distance is defined to be 200km, if one or more of the input GPS coords
	 * is invalid the method will return null
	 * 
	 * @param gps0 first input point which represents a GPS coordinate
	 * @param gps1 second input point which represents a GPS coordinate
	 * @return array which elements represent azimuth, elevation & 3d distance
	 */

	@Override
	public double[] azimuth_elevation_dist(Point3D gps0, Point3D gps1) {
		if (isValid_GPS_Point(gps0) && isValid_GPS_Point(gps1)) {
			double[] outPut = new double[3];
			// ***calculating the distance, we're starting from the distance because if the
			// GPS coordinates too distant we can't calculate nothing based on them:
			double distBetweenPoints = distance2d(gps0, gps1);
			if (distBetweenPoints == Double.NaN) {
				System.out.println("the diastnce is too big, cannot calculate it");
				return null;
			}
			outPut[2] = distBetweenPoints;
			// ***calculating the azimuth:
			// converting all GPS decimal degree values to radian:
			double radianLatGps0 = Point3D.d2r(gps0.x());
			double radianLonGps0 = Point3D.d2r(gps0.y());
			double radianLatGps1 = Point3D.d2r(gps1.x());
			double radianLonGps1 = Point3D.d2r(gps1.y());
			// calculating x & y according to the formula for forward azimuth [according
			// to:https://www.movable-type.co.uk/scripts/latlong.html]
			double y = Math.sin(radianLonGps1 - radianLonGps0) * Math.cos(radianLatGps1);
			double x = Math.cos(radianLatGps0) * Math.sin(radianLatGps1)
					- Math.sin(radianLatGps0) * Math.cos(radianLatGps1) * Math.cos(radianLonGps1 - radianLonGps0);
			// using arctan2 function
			double azimuth = Math.atan2(y, x);
			// converting back to decimal degrees
			azimuth = Point3D.r2d(azimuth);
			if (azimuth < 0) {
				azimuth = 360 + azimuth;
			}
			outPut[0] = azimuth;
			// ***calculating the elevation based on the trigonometric functions tangens in
			// a right-angled triangle:
			double elevation = Math.atan((gps1.z() - gps0.z()) / distBetweenPoints);
			outPut[1] = Point3D.r2d(elevation);
			return outPut;
		}
		if (!isValid_GPS_Point(gps0)) {
			System.out.println("first arg isnt a valid gps coord, returning first arg");
			return null;
		} else {
			System.out.println("second arg isnt a valid gps coord. returning second arg");
			return null;
		}
	}

	/**
	 * checking whether given GPS point is valid by values, NOTICE : altitude is
	 * check based on dead sea lowest altitude which is (-430) [according
	 * to:https://en.wikipedia.org/wiki/Dead_Sea] and mount everest highest altitude
	 * which is 8,848 [according to:https://en.wikipedia.org/wiki/Mount_Everest]
	 * 
	 * @param p input point which should be checked
	 * @return boolean variable which indicates whether the point is a valid GPS
	 *         coordinate
	 */
	@Override
	public boolean isValid_GPS_Point(Point3D p) {
		return ((p.x() >= -90 && p.x() <= 90) && (p.y() >= -180 && p.y() <= 180) && (p.z() >= -430 && p.z() <= 8848));
	}

	// private methods to convert radian to meter :
	private double r2mLat(double radianLatInput) {
		return (Math.sin(radianLatInput) * earthRadius);
	}

	private double r2mLon(double radianLonInput, double lonNorm) {
		return (Math.sin(radianLonInput) * (earthRadius * lonNorm));
	}
}
