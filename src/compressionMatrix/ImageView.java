import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;


public class ImageView extends JLabel implements Scrollable{
    
    //Représentation graphique de la matrice
    private BufferedImage display;
    //La matrice associée à l'image
    private CompressionMatrix matrix;
    //Le facteur de zoom
    private double zoomFactor = 1;

    //Construction de l'image à partir d'une matrice donnée
    /*public ImageView(CompressionMatrix m) {
	matrix = m;
    }*/
    
    public ImageView(CompressionMatrix m){
        super();
        matrix = m;
        display = new BufferedImage((int)(matrix.getWidth()*zoomFactor),
		(int)(matrix.getHeight()*zoomFactor),
		BufferedImage.TYPE_3BYTE_BGR);
        setIcon(new ImageIcon(display));
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
	//Redimensionne l'image
	g.drawImage(scaleImage(), 0, 0, this);
    }


    public void setZoomFactor(double factor){
        zoomFactor = factor;
	//display = new BufferedImage((int)(matrix.getWidth()*zoomFactor), (int)(matrix.getHeight()*zoomFactor), BufferedImage.TYPE_3BYTE_BGR);
        repaint();
    }
    
    public BufferedImage scaleImage() {
	int scaledWidth = (int)(display.getWidth() * zoomFactor);
	int scaledHeight = (int)(display.getHeight() * zoomFactor);
	BufferedImage display2;
	
	display2 = new BufferedImage(scaledWidth,scaledHeight, BufferedImage.TYPE_3BYTE_BGR);
	Graphics2D g2 = display2.createGraphics();
        g2.drawImage(display,0,0,scaledWidth,scaledHeight,null);
        
	/*Methode avec fonction pre-def
	Graphics2D graphics2D = display2.createGraphics();
	
	graphics2D.setRenderingHint(
		RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	
	graphics2D.drawImage(display, 0, 0, scaledWidth, scaledHeight, null);
	graphics2D.dispose();*/
	
	return display2;
    }

    public void reconstructImage() {
	display = new BufferedImage(matrix.getWidth(), matrix.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

	for (int x = 0; x < matrix.getWidth(); x++) {
	    for (int y = 0; y < matrix.getHeight(); y++) {
		int color = matrix.getPixelColor(x, y);
		display.setRGB(x, y, ((color << 16) + (color << 8) + color));
	    }
	}
	repaint();
    }

    public CompressionMatrix getMatrix() {
	return matrix;
    }

    public BufferedImage getImage() {
	return display;
    }
    
    /*public BufferedImage drawRect(Graphics g, ArrayList<Rectangle>){
    }*/
    
    
    /* Scroll Bar */
    private int maxUnitIncrement = 10;
    private class MouseScrolling implements MouseMotionListener{
		
        public void mouseDragged(MouseEvent e) {
            Rectangle r = new Rectangle(e.getX(), e.getY(),0,0);
            scrollRectToVisible(r);
        }
		
        public void mouseMoved(MouseEvent e) {}
        
    }
    
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }
	
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        int currentPos = orientation==SwingConstants.HORIZONTAL?visibleRect.x:visibleRect.y;
        int newPos;
        if(direction<0){
            newPos = currentPos-(currentPos/maxUnitIncrement)*maxUnitIncrement;
            return newPos==0?maxUnitIncrement:newPos;
        }else{
            return ((currentPos/maxUnitIncrement)+1)*maxUnitIncrement-currentPos;
        }
    }
	
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return orientation==SwingConstants.HORIZONTAL?visibleRect.width-maxUnitIncrement:visibleRect.height-maxUnitIncrement;
    }
	
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }
	
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
