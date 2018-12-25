package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class MyFrame_2 extends JFrame implements MouseListener, ComponentListener, MenuListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private JMenu mainMenu;
	private JMenuItem loadGame;
	private JMenuItem playGame;
	private ImagePanel images;

	public MyFrame_2() {
		initComp();
		this.addMouseListener(this);
		this.addComponentListener(this);
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
		this.images.resizeImage(this.getWidth()- 22, this.getHeight()-79);
		// make the thread "go to sleep" to avoid smearing the screen
		try {
			Thread.sleep(20);
		} catch (InterruptedException e0) {
			// TODO Auto-generated catch block
			e0.printStackTrace();
		}
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
		// TODO Auto-generated method stub

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
		this.images = new ImagePanel();
		this.getContentPane().add(images);
		this.setSize(this.images.getPreferredSize());;
		this.setVisible(true);
	}

	private void menuCreator() {
		JMenuBar menuBar = new JMenuBar();
		// menu first - external "button:
		this.mainMenu = new JMenu("Menu");
		mainMenu.setMnemonic(KeyEvent.VK_R);
		mainMenu.addMenuListener(this);
		this.loadGame = new JMenuItem("load game from CSV");
		loadGame.setMnemonic(KeyEvent.VK_R);
		 loadGame.addActionListener(new MenuAction(this));
		this.playGame = new JMenuItem("start playing");
		this.playGame.setMnemonic(KeyEvent.VK_R);
		playGame.addActionListener(new MenuAction(this));
		this.mainMenu.add(loadGame);
		this.mainMenu.add(playGame);
		menuBar.add(mainMenu);
		this.setJMenuBar(menuBar);
	}

}
