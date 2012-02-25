import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;


public class ImageView extends JLabel implements Scrollable {

    //Représentation graphique de la matrice
    private BufferedImage display;
    //La matrice associée à l'image
    private CompressionMatrix matrix;
    //Le facteur de zoom
    private double zoomFactor = 1;
    private ArrayList<Rectangle> rect;

    public ImageView(CompressionMatrix m) {
	super();
	matrix = m;
	display = new BufferedImage((int) (matrix.getWidth() * zoomFactor),
		(int) (matrix.getHeight() * zoomFactor),
		BufferedImage.TYPE_3BYTE_BGR);
	setIcon(new ImageIcon(display));
	rect = null;
	setOpaque(true);
	setAutoscrolls(true);
	addMouseMotionListener(new MouseScrolling());
    }

    //Peinture de l'image
    @Override
    public void paint(Graphics g) {
	/*
	 * Nécessaire pour ne pas reconstruire l'image à chaque changement
	 * graphique : il y a un énorme lag sinon ...
	 */
	if (matrix.getUpdateStatus()) {
	    //Reconstruction de l'image
	    reconstructImage();

	    matrix.setUpdated(false);
	}
	if (rect != null) {
	    for (Rectangle r : rect) {

		display.getGraphics().drawRect(r.x, r.y, r.width, r.height);
	    }
	}
	super.paint(g);
	//g.drawImage(display, 0, 0, this);
    }

    public void setZoomFactor(double factor) {
	zoomFactor = factor;
	matrix.setUpdated(true);
	repaint();
    }

    /*public BufferedImage scaleImage() {

	int scaledWidth = (int) (display.getWidth() * zoomFactor);
	int scaledHeight = (int) (display.getHeight() * zoomFactor);

	BufferedImage display2 = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_3BYTE_BGR);

	for (int x = 0; x < display.getWidth(); x++) {
	    for (int y = 0; y < display.getHeight(); y++) {

		int newX = (int) Math.round(x * zoomFactor);
		int newY = (int) Math.round(y * zoomFactor);

		for (int z = newX; z < newX + zoomFactor; z++) {
		    for (int w = newY; w < newY + zoomFactor; w++) {
			display2.setRGB(z, w, display.getRGB(x, y));
		    }
		}
	    }
	}
	//repaint();
	return display2;

    }*/

    public void reconstructImage() {


	int scaledWidth = (int) (matrix.getWidth() * zoomFactor);
	int scaledHeight = (int) (matrix.getHeight() * zoomFactor);

	display = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_3BYTE_BGR);

	for (int x = 0; x < matrix.getWidth(); x++) {
	    for (int y = 0; y < matrix.getHeight(); y++) {

		int color = matrix.getPixelColor(x, y);

		int newX = (int) Math.round(x * zoomFactor);
		int newY = (int) Math.round(y * zoomFactor);

		for (int z = newX; z < newX + zoomFactor; z++) {
		    for (int w = newY; w < newY + zoomFactor; w++) {
			display.setRGB(z, w, ((color << 16) + (color << 8) + color));
		    }
		}
	    }
	}
	setIcon(new ImageIcon(display));
	//repaint();
    }

    public CompressionMatrix getMatrix() {
	return matrix;
    }

    public BufferedImage getImage() {
	return display;
    }

    public void showRects() {
	rect = matrix.detectionEtoile();
	repaint();
    }
    /*
     * Scroll Bar
     */
    private int maxUnitIncrement = 10;

    private class MouseScrolling implements MouseMotionListener {

	@Override
	public void mouseDragged(MouseEvent e) {
	    Rectangle r = new Rectangle(e.getX(), e.getY(), 0, 0);
	    scrollRectToVisible(r);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
	return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
	int currentPos = orientation == SwingConstants.HORIZONTAL ? visibleRect.x : visibleRect.y;
	int newPos;
	if (direction < 0) {
	    newPos = currentPos - (currentPos / maxUnitIncrement) * maxUnitIncrement;
	    return newPos == 0 ? maxUnitIncrement : newPos;
	} else {
	    return ((currentPos / maxUnitIncrement) + 1) * maxUnitIncrement - currentPos;
	}
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
	return orientation == SwingConstants.HORIZONTAL ? visibleRect.width - maxUnitIncrement : visibleRect.height - maxUnitIncrement;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
	return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
	return false;
    }
}
