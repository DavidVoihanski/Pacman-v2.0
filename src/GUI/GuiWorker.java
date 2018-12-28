package GUI;

import Algo.StringToGame;
import Utils.Positionts;

public class GuiWorker implements Runnable {
	private Positionts game;
	private MyFrame_2 frame;
	ImagePanel imagePanel;

	/**
	 * basic constructor
	 * 
	 * @param thisGuisGame the game instance which we are running
	 * @param frame        the GUI on which the game is shown
	 * @param imagePanel   the image from the map
	 * @param gameMap      the map of the game
	 */
	public GuiWorker(Positionts game, MyFrame_2 frame, ImagePanel imagePanel) {
		this.game = game;
		this.frame = frame;
		this.imagePanel = imagePanel;
	}

	@Override
	public void run() {

		StringToGame.drawGame(game, frame);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.repaint();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringToGame.drawGame(game, frame);
	}

}
