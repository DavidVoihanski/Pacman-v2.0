package GUI;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;

import javax.swing.JFileChooser;

import Robot.Play;
import algorithm.ShortestPathAlgo;
import gameUtils.Game;
import gameUtils.Paired;

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
		if (e.getActionCommand().equals("load game from CSV")) {
			int returnValue = fc.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fc.getSelectedFile();
				play1 = new Play(selectedFile.toString());
			}
			ArrayList<String> board_data = play1.getBoard();
			for(int i=0;i<board_data.size();i++) {
				System.out.println(board_data.get(i));
			}
			String map_data = play1.getBoundingBox();
			System.out.println("Bounding Box info: "+map_data);
			// ******************************************//
		} else if (e.getActionCommand().equals("start playing")) {

			// ******************************************//
		}
	}
}
