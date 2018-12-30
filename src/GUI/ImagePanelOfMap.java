package GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JMenuBar;

public class ImagePanelOfMap extends JLabel {
	private static final long serialVersionUID = 1L;
	private BufferedImage changingImage;
	private BufferedImage originalImage;
	private final static String PathToMapImage = "images/Ariel1.png";
	private JMenuBar menu;
	private MyFrame_2 frame;

	/**
	 * basic constructor
	 * 
	 * @param image games image
	 */
	public ImagePanelOfMap(JMenuBar menu, MyFrame_2 frame) {
		this.menu = menu;
		this.frame = frame;
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
		this.frame.setJMenuBar(this.menu);
		if(this.frame.isPlaying()) {
			
			resizeImage(this.frame.getWidth() - 10, this.frame.getHeight() - 10);
		}
		g.drawImage(changingImage, 0, 0, null);
	}
}
