package GUI;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import Algo.StringToGame;
import Coords.LatLonAlt;
import Geom.Point3D;
import Robot.Play;
import Utils.GpsCoord;
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
	private GuiWorker gw;

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
			this.gw = new GuiWorker(pos, this.guiInstance, this.guiInstance.getImagePanel());
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
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double angToMove = getAngForMovement(lastPixelClicked, this.guiInstance.getMyPlayerLoc());
		this.play1.rotate(angToMove);
		Positionts currentPos = StringToGame.toGame(this.play1.getBoard());
		this.guiInstance.paint(this.guiInstance.getGraphics());
		StringToGame.drawGame(currentPos, this.guiInstance);
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
