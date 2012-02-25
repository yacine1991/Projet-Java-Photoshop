import java.awt.Rectangle;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


public class CompressionMatrix {

    private HashMap<Integer, HashMap<Integer, Integer>> matrix;
    private int height = -1;
    private int width = -1;
    //savoir si matrice a été modifiée
    private boolean updated = true;
    private double zoomFactor;
    //private int oldpix;
    //racine sauvegarde
    static Element racine = new Element("image");
    //On crée un nouveau Document JDOM basé sur la racine que l'on vient de créer
    static Document document = new Document(racine);

    public CompressionMatrix(int h, int w) {
        this();
        width = w;
        height = h;
    }

    public CompressionMatrix() {
        matrix = new HashMap<Integer, HashMap<Integer, Integer>>();
        updated = true;
    }

    public void colorPixel(int x, int y, int color) {
        if (!matrix.containsKey(x)) {
            HashMap<Integer, Integer> temp;
            temp = new HashMap<Integer, Integer>();
            matrix.put(x, temp);
        }
        matrix.get(x).put(y, color);
    }

    public int getPixelColor(int x, int y) {
        if (matrix.containsKey(x) && matrix.get(x).containsKey(y)) {
            return matrix.get(x).get(y);
        } else {
            return 0;
        }
    }

    //Méthode pour récupérer le niveau de gris du pixel
    public int getPixelLevel(int x, int y) {
        //A compléter
        return 0;
    }

    //Permet de récupérer, l'ensemble des absices 
    public Set<Integer> getAbscissaSet() {
        return matrix.keySet();
    }

    /*
     * Permet de récupérer les colonnes où il y a un pixel allumé sur une
     * certaine ligne
     */
    public Set<Integer> getOrdinateForAbscissa(int ab) {
        if (matrix.containsKey(ab)) {
            return matrix.get(ab).keySet();
        } else {
            return new HashSet<Integer>();
        }
    }

    //Permet de savoir si la matrice a été modifiée dernièrement
    public boolean getUpdateStatus() {
        return updated;
    }

    /*
     * Permet de changer le booléen qui permet de signaler que la matrice a été
     * modifiée
     */
    public void setUpdated(boolean up) {
        updated = up;
    }

    //Inversion des couleurs
    public CompressionMatrix negative() {
        updated = true;
        CompressionMatrix mat = new CompressionMatrix(getHeight(), getWidth());
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                mat.colorPixel(x, y, 255 - getPixelColor(x, y));
            }
        }
        return mat;
    }

    /*
     * Creation du flou de l'image. On utilise calculMoyen pour connaitre la
     * valeur moyenne des pixels
     */
    public CompressionMatrix flou() {
        updated = true;
        CompressionMatrix cm = new CompressionMatrix(getHeight(), getWidth());
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                int x1 = calculMoyen(x, y);
                if (x1 != 0) {
                    cm.colorPixel(x, y, x1);
                }
            }
        }
        return cm;
    }
    
    private int calculMoyen(int x, int y) {
        int somme = 0;
        int nbPixel = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < getWidth() && j >= 0 && j < getHeight()) {
                    somme += this.getPixelColor(i, j);
                    nbPixel++;
                }
            }
        }
        return (somme / nbPixel);
    }
    
    /*
     * Creation du flou bilinéaire de l'image. Même procédé que pour le flou
     * simple sauf qu'on attribut un poids différent pour chaque pixel 
     * alentour (40% milieu - 10% droit, gauche, haut, bas - 0.05 diagonales)
     */
    public CompressionMatrix flouBili() {
        updated = true;
        CompressionMatrix cm = new CompressionMatrix(getHeight(), getWidth());
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                int x1 = calculMoyenBili(x, y);
                if (x1 != 0) {
                    cm.colorPixel(x, y, x1);
                }
            }
        }
        return cm;
    }
    
    private int calculMoyenBili(int x, int y) {
        int somme = 0;
        int nbPixel = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < getWidth() && j >= 0 && j < getHeight()) {
		    //milieu
		    if (i == x && j == y) {
			somme += (this.getPixelColor(i, j)*(1-0.40));
			nbPixel++;
		    }
		    //coté droit, gauche, haut, bas
		    if ((i == x-1 && j == y) || (i == x+1 && j == y) || 
			    (i == x && j == y+1) || (i == x && j == y-1)) {
			
			somme += (this.getPixelColor(i, j)*(1-0.10));
			nbPixel++;
		    }
		    //diagonales
		    if ((i == x-1 && j == y-1) || (i == x+1 && j == y+1) || 
			    (i == x-1 && j == y+1) || (i == x+1 && j == y-1)) {
			
			somme += (this.getPixelColor(i, j)*(1-0.05));
			nbPixel++;
		    }	
		}
	    }
	}
	return(somme/nbPixel);
    }
     
    
    /* Même procédé que bili - edit : pour le calcul de la moyenne, on aurait pu
     * définir une matrix sur laquelle on pioche la valeur au lieu de faire
     * cas par cas comme il est fait ici (technique de petit joueur !) 
     */
    public CompressionMatrix flouBicu() {
        updated = true;
        CompressionMatrix cm = new CompressionMatrix(getHeight(), getWidth());
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                int x1 = calculMoyenBicu(x, y);
                if (x1 != 0) {
                    cm.colorPixel(x, y, x1);
                }
            }
        }
        return cm;
    }
    /* 1-2-3- 2-1
     * 2-3-5- 3-2
     * 3-5-39-5-3
     * 2-3-5- 3-2
     * 1-2-3- 2-1
     */
    private int calculMoyenBicu(int x, int y) {
        int somme = 0;
        int nbPixel = 0;
        for (int i = x - 2; i <= x + 2; i++) {
            for (int j = y - 2; j <= y + 2; j++) {
                if (i >= 0 && i < getWidth() && j >= 0 && j < getHeight()) {
		    //milieu
		    if (i == x && j == y) {
			somme += (this.getPixelColor(i, j)*(1-0.39));
			nbPixel++;
		    }
		    
		    /* PREMIERE COURONNE */
		    
		    //coté droit, gauche, haut, bas
		    if ((i == x-1 && j == y) || (i == x+1 && j == y) || 
			    (i == x && j == y+1) || (i == x && j == y-1)) {
			
			somme += (this.getPixelColor(i, j)*(1-0.05));
			nbPixel++;
		    }
		    //diagonales
		    if ((i == x-1 && j == y-1) || (i == x+1 && j == y+1) || 
			    (i == x-1 && j == y+1) || (i == x+1 && j == y-1)) {
			
			somme += (this.getPixelColor(i, j)*(1-0.03));
			nbPixel++;
		    }	
		    
		    /* DEUXIEME COURONNE */
		    
		    //coté droit, gauche, haut, bas
		    if ((i == x-2 && j == y) || (i == x+2 && j == y) || 
			    (i == x && j == y+2) || (i == x && j == y-2)) {
			
			somme += (this.getPixelColor(i, j)*(1-0.03));
			nbPixel++;
		    }
		    
		    //diagonales
		    if ((i == x-2 && j == y-2) || (i == x+2 && j == y+ 2) || 
			    (i == x-2 && j == y+2) || (i == x+2 && j == y-2)) {
			
			somme += (this.getPixelColor(i, j)*(1-0.01));
			nbPixel++;
		    }
		    //diagonales 2% (intermédiaire)
		    if ((i == x-2 && j == y+1) || (i == x + 2 && j == y+1) || 
			    (i == x+1 && j == y+2) || (i == x-1 && j == y+2)
			    || (i == x+2 && j == y-1) || (i == x+1 && j == y-2) 
			    || (i==x-1 && j == y-2) || (i==x-2 && j == y-1)) {
			
			somme += (this.getPixelColor(i, j)*(1-0.02));
			nbPixel++;
		    }
		}
	    }
	}
	return(somme/nbPixel);
    }

    public int getWidth() {
        if (width == -1) {
            width = Collections.max(matrix.keySet()) + 1;
        }
        return width;
    }

    public int getHeight() {
        if (height == -1) {
            int max=0;
            for (Integer i : matrix.keySet()) {
                max = Collections.max(matrix.get(i).keySet());
                height = max > height ? max : height;
            }
            height++;
        }
        return height;
    }

    public CompressionMatrix rotation90() {
        updated = true;
        CompressionMatrix cm = new CompressionMatrix();
        for (Integer x : matrix.keySet()) {
            for (Integer y : matrix.get(x).keySet()) {
                cm.colorPixel(getHeight() - 1 - y, x, getPixelColor(x, y));
            }
        }
        return cm;
    }

    public CompressionMatrix rotation180() {
        updated = true;
        CompressionMatrix cm = new CompressionMatrix();
        for (Integer x : matrix.keySet()) {
            for (Integer y : matrix.get(x).keySet()) {
                cm.colorPixel(getWidth() - x - 1, getHeight() - y - 1, getPixelColor(x, y));
            }
        }
        return cm;
    }

    public CompressionMatrix rotation270() {
        updated = true;
        CompressionMatrix cm = new CompressionMatrix();
        for (Integer x : matrix.keySet()) {
            for (Integer y : matrix.get(x).keySet()) {
                cm.colorPixel(y, getWidth() - x - 1, getPixelColor(x, y));
            }
        }
        return cm;
    }

    //Miroir de l'image par rapport à l'axe vertical
    public CompressionMatrix miroirVertical() {
        CompressionMatrix cm = new CompressionMatrix();
        for (Integer x : matrix.keySet()) {
            for (Integer y : matrix.get(x).keySet()) {
                cm.colorPixel(x, y, getPixelColor(getWidth() - x, y));
            }
        }
        return cm;
    }

    //Miroir de l'image par rapport à l'axe horizontal
    public CompressionMatrix miroirHorizontal() {
        updated = true;
        CompressionMatrix cm = new CompressionMatrix();
        for (Integer x : matrix.keySet()) {
            for (Integer y : matrix.get(x).keySet()) {
                cm.colorPixel(x, y, getPixelColor(x, getHeight() - y));
            }
        }
        return cm;
    }

    //Agrandissement de l'image (ouverture dans un nouvel onglet)
    public CompressionMatrix agrandissement(int zoomFactor) {
        updated = true;
        int scaledWidth = (int)(getWidth() * zoomFactor);
        int scaledHeight = (int)(getHeight() * zoomFactor);

        CompressionMatrix cm = new CompressionMatrix(scaledHeight, scaledWidth);

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                int color = getPixelColor(x, y);
                cm.colorPixelBloc((int) Math.round(x * zoomFactor), (int) Math.round(y * zoomFactor), zoomFactor, color);
            }
        }
        return cm;
    }

    //coloration bloc pixel (pour agrandissement)
    public void colorPixelBloc(int abs, int ord, double zoomFactor, int color) {
        for (int z = 0; z <= zoomFactor; z++) {
            for (int w = 0; w <= zoomFactor; w++) {
                colorPixel(abs + z, ord + w, color);
            }
        }
    }

    //suppression des pixels "parasites" (recolorier en noir)
    public CompressionMatrix suppressionBruit(int seuil) {
        updated = true;
        CompressionMatrix cm = new CompressionMatrix();

        for (Integer x : matrix.keySet()) {
            for (Integer y : matrix.get(x).keySet()) {
                int colorpix = getPixelColor(x, y);
                if (colorpix <= seuil) {
                    cm.colorPixel(x, y, 0);
                } else {
                    cm.colorPixel(x, y, colorpix);
                }
            }
        }
        return cm;
    }

    public ArrayList<Rectangle> detectionEtoile() {
        updated = true;
        ArrayList<Rectangle> rectList = new ArrayList<Rectangle>();

        for (Integer x : matrix.keySet()) {
            for (Integer y : matrix.get(x).keySet()) {
                int colorpix = getPixelColor(x, y);
                if (colorpix == 255) {
                    Rectangle rect = new Rectangle(x-20, y+20, 15, 15);
                    rectList.add(rect);
                }

            }
        }
        return rectList;
    }

   /* public void detectionEtendue(int x, int y){
        
        for(int i = x-1; i<=2; i++ ){
            for(int j = y-1; j<=2; j++ ){
                int colorpix = getPixelColor(i, j);
                if(colorpix!=255)
            }

        }
    }
*/
    /* ---- SAUVEGARDE ---- */
    
    public void saveXml(String fileName) {

        int pc;
        
        for (int x = 0; x < height; x++) {

            Element row = new Element("row") {};
            racine.addContent(row);
            Attribute index = new Attribute("Index", "" + x);
            row.setAttribute(index);

            for (int y = 0; y < width; y++) {
                pc = getPixelColor(x, y);
		
                if (pc != 0) {
                    Element pix = new Element("pix");
                    Attribute column = new Attribute("column", "" + y);
                    Attribute color = new Attribute("color", "" + pc);
                    pix.setAttribute(column);
                    pix.setAttribute(color);
                    row.addContent(pix);
                }
            }
        }
        enregistre(fileName);  
    }
    

    static void affiche() {
        try { 
            //On utilise ici un affichage classique avec getPrettyFormat() 
	    XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(document,System.out);
        } catch (java.io.IOException e) {}
    }

    static void enregistre(String fichier) {
        try {
	    XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(document, new FileOutputStream(fichier));
        } catch (java.io.IOException e) {}
    }
}