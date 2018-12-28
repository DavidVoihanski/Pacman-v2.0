package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import Algo.StringToGame;
import Coords.LatLonAlt;
import Geom.Point3D;
import Utils.GpsCoord;
import Utils.MyPlayer;
import Utils.Positionts;
import Utils.Range;

public class MyFrame_2 extends JFrame implements MouseListener, ComponentListener, MenuListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private JMenu mainMenu;
	private JMenuItem loadGame;
	private JMenuItem playGame;
	private ImagePanel images;
	private Positionts game;
	private MenuAction menu;
	private MyPlayer p1;

	public MyFrame_2() {
		initComp();
		this.addMouseListener(this);
		this.addComponentListener(this);
	}

	public double getWindowWidth() {
		return (this.getWidth() - 20);
	}

	public double getWindowHeight() {
		return (this.getHeight() - 70);
	}

	public ImagePanel getImagePanel() {
		return this.images;
	}

	public void setGame(Positionts p) {
		this.game = p;
	}

	public void clear() {
		this.game = null;
		this.paint(this.getGraphics());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void menuSelected(MenuEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void menuDeselected(MenuEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void menuCanceled(MenuEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent e) {
		// resize the actual image
		this.images.resizeImage(this.getWidth() - 15, this.getHeight() - 60);
		// make the thread "go to sleep" to avoid smearing the screen
		try {
			Thread.sleep(20);
		} catch (InterruptedException e0) {
			// TODO Auto-generated catch block
			e0.printStackTrace();
		}
		drawAll();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
//		Point3D pixelClicked = new Point3D (e.getX(),e.getY());
//		System.out.println(pixelClicked);
//		try {
//			System.out.println(Range.pixel2Gps(pixelClicked, this.getWindowHeight(), this.getWindowWidth()));
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		if (this.menu.IsAllowedToPutMyP()) {
			Point3D pixelClicked = new Point3D (e.getX(),e.getY());
			LatLonAlt startingLocation = null;
			try {
				startingLocation = Range.pixel2Gps(pixelClicked, this.getWindowHeight(), this.getWindowWidth());
			} catch (IOException e1) {
				System.out.println("ERR==>> placing MyPlayer for the first time");
				e1.printStackTrace();
			}
			this.p1 = new MyPlayer(startingLocation);
			this.images.drawMPlayer(e.getX(), e.getY(), this.getGraphics());
			this.menu.setIsLoaded(false);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	// *****************private:

	private void initComp() {
		menuCreator();
		windowSetter();

	}

	private void windowSetter() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.images = new ImagePanel();
		this.add(this.images);
		this.pack();
		this.setVisible(true);
	}

	private void menuCreator() {
		JMenuBar menuBar = new JMenuBar();
		// menu first - external "button:
		this.mainMenu = new JMenu("Menu");
		this.menu = new MenuAction(this);
		mainMenu.setMnemonic(KeyEvent.VK_R);
		mainMenu.addMenuListener(this);
		this.loadGame = new JMenuItem("load game from CSV");
		loadGame.setMnemonic(KeyEvent.VK_R);
		loadGame.addActionListener(this.menu);
		this.playGame = new JMenuItem("start playing");
		this.playGame.setMnemonic(KeyEvent.VK_R);
		playGame.addActionListener(this.menu);
		this.mainMenu.add(loadGame);
		this.mainMenu.add(playGame);
		menuBar.add(mainMenu);
		this.setJMenuBar(menuBar);
	}

	private void drawAll() {
		if (this.game != null) {
			StringToGame.drawGame(this.game, this);
		}
	}
}
