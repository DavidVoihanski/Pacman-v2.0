package GUI;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.File;


import javax.swing.JFileChooser;

import Algo.StringToGame;
import Robot.Play;
import Utils.Positionts;

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
	private boolean isGameLoaded = false;
	private Play play1;

	/**
	 * basic constructor - only gets the GUI instance it was called from
	 * 
	 * @param instance
	 */
	public MenuAction(MyFrame_2 instance) {
		this.guiInstance = instance;
		this.fc = new JFileChooser("data");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.isGameLoaded = false;
		if (e.getActionCommand().equals("load game from CSV")) {
			this.isGameLoaded = true;
			int returnValue = fc.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fc.getSelectedFile();
				this.guiInstance.clear();
				play1 = new Play(selectedFile.toString());
			}
			Positionts pos = StringToGame.toGame(play1.getBoard());
			guiInstance.setGame(pos);
			StringToGame.drawGame(pos, guiInstance);
			// ******************************************//
		} else if (e.getActionCommand().equals("start playing")) {
			this.isGameLoaded = false;

			// ******************************************//
		}
	}

	public boolean getIsLoaded() {
		return this.isGameLoaded;
	}

	public void setIsLoaded(boolean toSet) {
		this.isGameLoaded = toSet;
	}
}
