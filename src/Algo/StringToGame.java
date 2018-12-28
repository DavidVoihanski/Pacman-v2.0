package Algo;

import java.util.ArrayList;
import java.util.Iterator;

import Coords.LatLonAlt;
import GUI.MyFrame_2;
import Geom.Point3D;
import Robot.Fruit;
import Robot.Packman;
import Utils.Ghost;
import Utils.MyPlayer;
import Utils.Positionts;
import Utils.Range;
import Utils.Rectangle;

public abstract class StringToGame {
	public static Positionts toGame(ArrayList<String> data) {
		Iterator<String> it = data.iterator();
		Positionts pos = new Positionts();
		while (it.hasNext()) {
			String currLine = it.next();
			String[] lineToArr = currLine.split(",");
			if(lineToArr[0].equals("M")) {
				double lat = Double.parseDouble(lineToArr[2]);
				double lon = Double.parseDouble(lineToArr[3]);
				LatLonAlt location = new LatLonAlt(lat, lon, 0);
				MyPlayer newPlayer = new MyPlayer(location);
				pos.setPlayer(newPlayer);
			}
			else if (lineToArr[0].equals("F")) {
				double lat = Double.parseDouble(lineToArr[2]);
				double lon = Double.parseDouble(lineToArr[3]);
				LatLonAlt location = new LatLonAlt(lat, lon, 0);
				Fruit newFruit = new Fruit(location);
				pos.addFruit(newFruit);
			} else if (lineToArr[0].equals("P")) {
				double lat = Double.parseDouble(lineToArr[2]);
				double lon = Double.parseDouble(lineToArr[3]);
				LatLonAlt location = new LatLonAlt(lat, lon, 0);
				double speed = Double.parseDouble(lineToArr[5]);
				Packman newPack = new Packman(location, speed);
				pos.addPackman(newPack);
			} else if (lineToArr[0].equals("G")) {
				double lat = Double.parseDouble(lineToArr[2]);
				double lon = Double.parseDouble(lineToArr[3]);
				LatLonAlt location = new LatLonAlt(lat, lon, 0);
				Ghost newGhost = new Ghost(location);
				pos.addGhost(newGhost);
			} else if (lineToArr[0].equals("B")) {
				Rectangle newRec = new Rectangle(
						lineToArr[2] + "," + lineToArr[3] + "," + lineToArr[5] + ", " + lineToArr[6]);
				pos.addRectangle(newRec);
			}
		}
		return pos;
	}

	public static void drawGame(Positionts givenGame, MyFrame_2 givenGuiWidow) {
		drawAllBlocks(givenGame, givenGuiWidow);
		drawAllPackman(givenGame, givenGuiWidow);
		drawAllFruit(givenGame, givenGuiWidow);
		drawAllGhosts(givenGame, givenGuiWidow);
		drawMyPlayerMovement(givenGame, givenGuiWidow);
	}

/////******************PRIVATE********************************
	private static void drawMyPlayerMovement(Positionts givenGame, MyFrame_2 givenGuiWidow) {
		MyPlayer p = givenGame.getPlayer();
		if (p != null) {
			Point3D pixelCoords = Range.gps2Pixel(p.getPosition(), givenGuiWidow.getWindowHeight(),
					givenGuiWidow.getWindowWidth());
			givenGuiWidow.getImagePanel().drawMPlayer(pixelCoords.ix() + 10, pixelCoords.iy() + 57,
					givenGuiWidow.getGraphics());
		}
	}

	private static void drawAllBlocks(Positionts givenGame, MyFrame_2 givenGuiWidow) {
		Iterator<Rectangle> itRec = givenGame.getRactCollection().iterator();
		while (itRec.hasNext()) {
			// next block in the "game"
			Rectangle current = itRec.next();
			// getting the "starting point" of every block
			int xStartPixel = Range
					.gps2Pixel(current.getBottomLeft(), givenGuiWidow.getWindowHeight(), givenGuiWidow.getWindowWidth())
					.ix() + 10;
			int yStartPixel = Range
					.gps2Pixel(current.getTopRight(), givenGuiWidow.getWindowHeight(), givenGuiWidow.getWindowWidth())
					.iy() + 57;
			// getting blocks width and height -> checked
			Point3D currentblockPixelWidthHeight = current.getWidthAndHeight(givenGuiWidow.getWindowHeight(),
					givenGuiWidow.getWindowWidth());
			// drawing the block
			givenGuiWidow.getImagePanel().drawBlock(xStartPixel, yStartPixel, currentblockPixelWidthHeight.ix(),
					currentblockPixelWidthHeight.iy(), givenGuiWidow.getGraphics());
		}
	}

	private static void drawAllPackman(Positionts givenGame, MyFrame_2 givenGuiWidow) {
		Iterator<Packman> itPac = givenGame.getPackCollection().iterator();
		while (itPac.hasNext()) {
			Packman current = itPac.next();
			Point3D pixelCoords = Range.gps2Pixel(current.getLocation(), givenGuiWidow.getWindowHeight(),
					givenGuiWidow.getWindowWidth());
			givenGuiWidow.getImagePanel().drawPackman(pixelCoords.ix() + 10, pixelCoords.iy() + 57,
					givenGuiWidow.getGraphics());
		}
	}

	private static void drawAllFruit(Positionts givenGame, MyFrame_2 givenGuiWidow) {
		Iterator<Fruit> itFruit = givenGame.getFruitCollection().iterator();
		while (itFruit.hasNext()) {
			Fruit current = itFruit.next();
			Point3D pixelCoords = Range.gps2Pixel(current.getLocation(), givenGuiWidow.getWindowHeight(),
					givenGuiWidow.getWindowWidth());
			givenGuiWidow.getImagePanel().drawFruit(pixelCoords.ix() + 10, pixelCoords.iy() + 57,
					givenGuiWidow.getGraphics());
		}
	}

	private static void drawAllGhosts(Positionts givenGame, MyFrame_2 givenGuiWidow) {
		Iterator<Ghost> itGhost = givenGame.getGhostCollection().iterator();
		while (itGhost.hasNext()) {
			Ghost current = itGhost.next();
			Point3D pixelCoords = Range.gps2Pixel(current.getPosition(), givenGuiWidow.getWindowHeight(),
					givenGuiWidow.getWindowWidth());
			givenGuiWidow.getImagePanel().drawGhost(pixelCoords.ix() + 10, pixelCoords.iy() + 57,
					givenGuiWidow.getGraphics());
		}
	}

}
