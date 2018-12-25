package GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * this class represents the JPanel component we use to hold our image with
 * 
 * @author Evgeny & David
 *
 */
public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private BufferedImage changingImage;
	private BufferedImage originalImage;
	private BufferedImage packman;
	private BufferedImage fruit;
	private BufferedImage ghost;
	private final static String PathToMapImage = "images/Ariel1.png";
	private final static String PathToPackImage = "images/pacman.png";
	private final static String PathToFruitImage = "images/fruit.png";
	private final static String PathToGhostImage = "images/ghost.png";

	/**
	 * basic constructor
	 * 
	 * @param image games image
	 */
	ImagePanel() {
		try {
			this.originalImage = ImageIO.read(new File(PathToMapImage));
		} catch (IOException e1) {
			System.out.println("ERR=>reading map image");
			e1.printStackTrace();
		}
		try {
			this.changingImage = ImageIO.read(new File(PathToMapImage));
		} catch (IOException e1) {
			System.out.println("ERR=>reading map image");
			e1.printStackTrace();
		}
		try {
			this.packman = ImageIO.read(new File(PathToPackImage));
		} catch (IOException e) {
			System.out.println("CANT READ THE PACMAN ICON");
			e.printStackTrace();
		}
		packman = this.resizeIcon(30, 30, packman);
		try {
			this.fruit = ImageIO.read(new File(PathToFruitImage));
		} catch (IOException e) {
			System.out.println("CANT READ THE FRUIT ICON");
			e.printStackTrace();
		}
		fruit = this.resizeIcon(30, 30, fruit);
		try {
			this.ghost = ImageIO.read(new File(PathToGhostImage));
		} catch (IOException e) {
			System.out.println("CANT READ THE GHOST ICON");
			e.printStackTrace();
		}
		ghost = this.resizeIcon(30, 30, ghost);

	}

	/****** public methods *********/

//these two are for internal uses
	@Override
	public Dimension getPreferredSize() {
		if (super.isPreferredSizeSet()) {
			return super.getPreferredSize();
		}
		return new Dimension(changingImage.getWidth(), changingImage.getHeight());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(changingImage, 0, 0, null);
	}

	/**
	 * used to draw the pacman icon on the screen
	 * 
	 * @param x x pixel location value
	 * @param y y pixel location value
	 * @param g graphics instance from GUI
	 */
	public void drawingPackman(int x, int y, Graphics g) {
		g.drawImage(this.packman, x, y, null);
	}

	public void drawGhost(int x, int y, Graphics g) {
		g.drawImage(this.ghost, x, y, null);
	}
	/**
	 * draw the fruit icon on the screen
	 * 
	 * @param x x pixel location value
	 * @param y y pixel location value
	 * @param g graphics instance from GUI
	 */
	public void drawingFruit(int x, int y, Graphics g) {
		g.drawImage(this.fruit, x, y, null);
	}

	/**
	 * resizing the image by given width & height values
	 * 
	 * @param width  wanted images width
	 * @param height wanted images height
	 */
	public void resizeImage(int width, int height) {
		int imageWidth = this.originalImage.getWidth();
		int imageHeight = this.originalImage.getHeight();
		double scaleX = (double) width / imageWidth;// calculating the X ratio
		double scaleY = (double) height / imageHeight;// calculating the Y ratio
		// using "Affine Transform" to scale the image based on the ratio
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
		this.changingImage = bilinearScaleOp.filter(this.originalImage,
				new BufferedImage(width, height, this.originalImage.getType()));
	}

	/****** private methods *********/

	// resizing an icon, used to change icons size in GUI
	private BufferedImage resizeIcon(int width, int height, BufferedImage icon) {
		int imageWidth = icon.getWidth();
		int imageHeight = icon.getHeight();
		double scaleX = (double) width / imageWidth;
		double scaleY = (double) height / imageHeight;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
		return bilinearScaleOp.filter(icon, new BufferedImage(width, height, icon.getType()));
	}

}
