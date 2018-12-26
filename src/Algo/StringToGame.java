package Algo;

import java.util.ArrayList;
import java.util.Iterator;

import Coords.LatLonAlt;
import Robot.Fruit;
import Robot.Packman;
import Server.Robot_user;
import Server.Server_Robot;
import Utils.Ghost;
import Utils.Positionts;
import Utils.Rectangle;

public abstract class StringToGame {
	public static Positionts toGame(ArrayList<String>data) {
		Iterator<String>it=data.iterator();
		Positionts pos=new Positionts();
		
		while(it.hasNext()) {
			String currLine=it.next();
			String []lineToArr=currLine.split(",");
			if(lineToArr[0].equals("F")) {
				double lat=Double.parseDouble(lineToArr[2]);
				double lon=Double.parseDouble(lineToArr[3]);
				LatLonAlt location=new LatLonAlt(lat,lon,0);
				Fruit newFruit=new Fruit(location);
				pos.addFruit(newFruit);
			}
			else if(lineToArr[0].equals("P")) {
				double lat=Double.parseDouble(lineToArr[2]);
				double lon=Double.parseDouble(lineToArr[3]);
				LatLonAlt location=new LatLonAlt(lat,lon,0);
				double speed=Double.parseDouble(lineToArr[5]);
				Packman newPack=new Packman(location, speed);
				pos.addPackman(newPack);
			}
			else if(lineToArr[0].equals("G")) {
				double lat=Double.parseDouble(lineToArr[2]);
				double lon=Double.parseDouble(lineToArr[3]);
				LatLonAlt location=new LatLonAlt(lat,lon,0);
				 Ghost newGhost=new Ghost(location);
				 pos.addGhost(newGhost);
			}
			else if(lineToArr[0].equals("B")) {
				Rectangle newRec=new Rectangle(lineToArr[2]+","+lineToArr[3]+","+lineToArr[5]+", "+lineToArr[6]);
				pos.addRectangle(newRec);
			}
		}
		return pos;
	}
}
