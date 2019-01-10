package GUI;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JMenuBar;

import Algo.GameAlgo;
import Algo.StringToGame;
import Coords.LatLonAlt;
import Geom.Point3D;
import Robot.Fruit;
import Robot.Play;
import Utils.Ghost;
import Utils.GpsCoord;
import Utils.MyCoords;
import Utils.MyPlayer;
import Utils.Positionts;
import Utils.Range;

/**
 * this class was created to deal with "menu action" , i.e pressing the menu
 * buttons in the GUI window
 * 
 * @author Evgeny & David
 *
 */
class MenuAction implements ActionListener {
	private MyFrame_2 guiInstance;
	private JFileChooser fc;
	private boolean isTryingToPutMyP = false;
	private Play play1;
	private long firstID = 313962193L;
	private long secID = 315873455L;

	/**
	 * basic constructor - only gets the GUI instance it was called from
	 * 
	 * @param instance
	 */
	public MenuAction(MyFrame_2 instance) {
		this.guiInstance = instance;
		this.fc = new JFileChooser("data");
		this.isTryingToPutMyP = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("load game from CSV")) {
			this.isTryingToPutMyP = true;
			int returnValue = fc.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fc.getSelectedFile();
				this.guiInstance.clear();
				this.play1 = new Play(selectedFile.toString());
			}
			this.play1.setIDs(firstID, secID);
			Positionts pos = StringToGame.toGame(play1.getBoard());
			guiInstance.setGame(pos);
			StringToGame.drawGame(pos, guiInstance);
			// ******************************************//
		} else if (e.getActionCommand().equals("start playing")) {
			if (this.guiInstance.isMyPlayerSet()) {
				this.play1.start();
				this.guiInstance.setIsPlaying(true);
			}
			// ******************************************//
		}
		else if(e.getActionCommand().equals("Run Algorithm")) {
			this.play1.start();
			this.autoRun();
		}
	}

	public void setMyPlayerLoc(LatLonAlt arg) {
		this.play1.setInitLocation(arg.lat(), arg.lon());
		this.guiInstance.setIsSetToPlay(true);
	}

	public boolean IsAllowedToPutMyP() {
		return this.isTryingToPutMyP;
	}

	public void setIsLoaded(boolean arg) {
		this.isTryingToPutMyP = arg;
	}

	public void moveMyPlayer(Point3D lastPixelClicked) {
		System.out.println(play1.isRuning());
		if (play1.isRuning()) {
			double angToMove;
			Positionts currentPos = StringToGame.toGame(this.play1.getBoard());
			GameAlgo.removeInvalidPointsFromRects(currentPos);
			Fruit closest=GameAlgo.findClosestFruit(currentPos.getFruitCollection(), currentPos.getPlayer());
			double distance=closest.getLocation().distance2D(currentPos.getPlayer().getPosition())*100000;
			System.out.println(distance);
			if(distance>8) {
				angToMove = getAngForMovement(lastPixelClicked, currentPos.getPlayer().getPosition());
				this.play1.rotate(angToMove);
			} else {
				angToMove = GameAlgo.pathToColsestFruit(currentPos.getPlayer(), closest);
				this.play1.rotate(angToMove);
			}
			this.guiInstance.getMapImage().paintComponent((this.guiInstance.getGraphics()));
			StringToGame.drawGame(currentPos, this.guiInstance);
			this.guiInstance.setGame(currentPos);
			System.out.println(play1.getStatistics());
		}
	}
	private void autoRun() {
		while(this.play1.isRuning()) {
			double angToMove;
			MyCoords converter=new MyCoords();
			Positionts currentPos = StringToGame.toGame(this.play1.getBoard());
			MyPlayer player = currentPos.getPlayer();
			GameAlgo.removeInvalidPointsFromRects(currentPos);
			ArrayList<LatLonAlt>path=GameAlgo.runGame(currentPos);
			Iterator<LatLonAlt>it=path.iterator();
			while(it.hasNext()) {
				LatLonAlt currTarger=it.next();
				while(converter.distance2d(player.getPosition(), currTarger)>1&&play1.isRuning()) {
					angToMove=Range.GetAzi(player.getPosition(), currTarger);
					this.play1.rotate(angToMove);
					currentPos = StringToGame.toGame(this.play1.getBoard());
					player=currentPos.getPlayer();
					this.guiInstance.getMapImage().paintComponent((this.guiInstance.getGraphics()));
					StringToGame.drawGame(currentPos, this.guiInstance);
					this.guiInstance.setGame(currentPos);
					System.out.println(play1.getStatistics());
				}
			}
		}
	}
	public boolean isGameRunning() {
		return this.play1.isRuning();
	}

	private double getAngForMovement(Point3D lastPixelClicked, LatLonAlt currentLoc) {
		LatLonAlt convertedPixel = null;
		try {
			convertedPixel = Range.pixel2Gps(lastPixelClicked, this.guiInstance.getWindowHeight(),
					this.guiInstance.getWindowWidth());
		} catch (IOException e) {
			System.out.println("ERR=>> in getting GPS location of pixel clicked");
			e.printStackTrace();
		}
		return (Range.GetAzi(currentLoc, convertedPixel));
	}
}
