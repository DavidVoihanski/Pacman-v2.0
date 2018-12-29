package Algo;

import java.util.ArrayList;
import java.util.Iterator;

import Robot.Fruit;
import Utils.MyPlayer;
import Utils.Positionts;
import Utils.Range;

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
}
