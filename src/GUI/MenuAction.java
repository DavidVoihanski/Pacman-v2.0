package GUI;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFileChooser;
import Algo.GameAlgo;
import Algo.StringToGame;
import Coords.LatLonAlt;
import Geom.Point3D;
import MySql.Statistics;
import Robot.Play;
import Utils.Fruit;
import Utils.MyCoords;
import Utils.MyPlayer;
import Utils.Positionts;
import Utils.Range;
import Utils.Solution;

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
	private int mapId;

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
				mapId = play1.getHash1();
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
	/**
	 * The main function to play the game by yourself, each click will move the player toward the point clicked
	 * @param lastPixelClicked
	 */
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
	//calls the algorithm from GameAlgo to play the game automatically
	private void autoRun() {
		while(this.play1.isRuning()) {
			double angToMove;
			boolean loopFlag=true;
			MyCoords converter=new MyCoords();
			Positionts currentPos = StringToGame.toGame(this.play1.getBoard());
			MyPlayer player = currentPos.getPlayer();
			GameAlgo.removeInvalidPointsFromRects(currentPos);
			Solution sol=GameAlgo.runGame(currentPos);
			ArrayList<LatLonAlt>path=sol.getPath();
			int idOfFruit=sol.getIdOfTargetFruit();
			Iterator<LatLonAlt>it=path.iterator();
			while(it.hasNext()&&loopFlag) {
				LatLonAlt currTarger=it.next();
				while(converter.distance2d(player.getPosition(), currTarger)>1&&play1.isRuning()&&loopFlag) {
					loopFlag=isFruitAlive(idOfFruit, currentPos);
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
		String currStats=play1.getStatistics();

		double myScore = parseScore(currStats);
		double myPlace = Statistics.compareScore(mapId, myScore);
		System.out.println(myPlace + "% of top");
		
	}
	public boolean isGameRunning() {
		return this.play1.isRuning();
	}
	
	//returns angle to move the player to
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
	//checks if current fruit target is alive
	private boolean isFruitAlive(int id,Positionts pos) {
		ArrayList<Fruit>fruits=pos.getFruitCollection();
		Iterator<Fruit>it=fruits.iterator();
		while(it.hasNext()) {
			Fruit currFruit = it.next();
			if(id==currFruit.getId())return true;
		}
		return false;
	}
	//parses score from getStats line
	private double parseScore(String stats) {
		String statsToArr[] = stats.split(",");
		String scoreArr[] = statsToArr[2].split(":");
		double score = Double.parseDouble(scoreArr[1]);
		return score;
	}
}
