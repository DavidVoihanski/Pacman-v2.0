package Algo;

import java.util.ArrayList;
import java.util.Iterator;

import Coords.LatLonAlt;
import Robot.Fruit;
import Utils.MyPlayer;
import Utils.Positionts;
import Utils.Range;
import Utils.Rectangle;

public abstract class GameAlgo {

	public static double pathToColsestFruit(MyPlayer player,Fruit closest) {
		double angle=Range.GetAzi(player.getPosition(), closest.getLocation());
		return angle;
	}

	public static Fruit findClosestFruit(ArrayList<Fruit>fruits,MyPlayer player1) {
		Iterator<Fruit>it=fruits.iterator();
		if(!it.hasNext())return null;
		Fruit closest=it.next();
		double closestDistance=closest.getLocation().distance3D(player1.getPosition());
		while(it.hasNext()) {
			Fruit tempFruit=it.next();
			double tempDistance=tempFruit.getLocation().distance3D(player1.getPosition());
			if(tempDistance<closestDistance) {
				closestDistance=tempDistance;
				closest=tempFruit;
			}
		}
		return closest;
	}
	public static boolean isValidPointOnMap(Positionts pos,LatLonAlt gps) {
		ArrayList<Rectangle>rects=pos.getRactCollection();
		Iterator<Rectangle>it=rects.iterator();
		while(it.hasNext()) {
			Rectangle currRect=it.next();
			double minLat=currRect.getBottomLeft().x();
			double maxLat=currRect.getTopLeft().x();
			double minLon=currRect.getBottomLeft().y();
			double maxLon=currRect.getBottomRight().y();
			double lat=gps.x();
			double lon=gps.y();
			if(lat>=minLat&&lat<=maxLat&&lon>=minLon&&lon<=maxLon)
				return false;
		}
		return true;
	}
	public static void removeInvalidPointsFromRects(Positionts pos) {
		ArrayList<Rectangle>rects=pos.getRactCollection();
		Iterator<Rectangle>rectIt=rects.iterator();
		while(rectIt.hasNext()) {
			Rectangle currRect=rectIt.next();
			ArrayList<LatLonAlt>points=currRect.getAcceciblePoints();
			Iterator<LatLonAlt>pointsIt=points.iterator();
			while(pointsIt.hasNext()) {
				LatLonAlt currPoint=pointsIt.next();
				if(!isValidPointOnMap(pos, currPoint))
					points.remove(currPoint);
			}
		}
	}
}
