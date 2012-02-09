
import compressionMatrix.FenetreBruit;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/*
 * Classe qui représente la fenêtre principale de l'application.
 */
class Fenetre extends JFrame {

    private JMenuBar menu; 
    private JMenuBar bm;
    //Panneau à onglet, on le met en global pour pouvoir y accéder partout, et
    //donc pour donner la possibilité à des méthode d'ajouter de nouveaux onglets
    private JTabbedPane tabs;

    //Constructeur qui génère tout 
    public Fenetre() {

        setTitle("Photoshop Star Watch edition");
        setSize(900, 600);

        //Définition des menu avec chacun leur listener
        menu = new JMenuBar();
        
        JMenu fileMenu = new JMenu("Fichier");
        
        /* Definition des raccourcis */
        
        //Ouvrir
        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O,
        InputEvent.CTRL_MASK);
        
        //Quitter
        KeyStroke altF4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4,
        InputEvent.ALT_MASK);

        //Enregistrer
        KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S,
        InputEvent.CTRL_MASK);
        
        //Enregistrer tout
        KeyStroke ctrlAltS = KeyStroke.getKeyStroke(KeyEvent.VK_S,
        InputEvent.CTRL_MASK + InputEvent.ALT_MASK);
        
        JMenuItem openMenu = new JMenuItem("Ouvrir...");
        openMenu.setAccelerator(ctrlO);
        openMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        JMenuItem saveCurrent = new JMenuItem("Enregistrer");
        saveCurrent.setAccelerator(ctrlS);
        saveCurrent.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveCurrentPanel();
            }
        });
        JMenuItem saveAllFiles = new JMenuItem("Enregistrer tout");
        saveAllFiles.setAccelerator(ctrlAltS);
        saveAllFiles.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveAllPanels();
            }
        });
        JMenuItem quitMenu = new JMenuItem("Quitter");
        quitMenu.setAccelerator(altF4);
        quitMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });

        fileMenu.add(openMenu);
        fileMenu.addSeparator();
        fileMenu.add(saveCurrent);
        fileMenu.add(saveAllFiles);
        fileMenu.addSeparator();
        fileMenu.add(quitMenu);

        JMenu editMenu = new JMenu("Edition");

        JMenuItem itEditAnnul = new JMenuItem("Annuler");
        itEditAnnul.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                annuler();
            }
        });
        JMenuItem itEditReta = new JMenuItem("Rétablir");
        itEditReta.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                retablir();
            }
        });
        JMenuItem itEditCopier = new JMenuItem("Copier");
        itEditCopier.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                copier();
            }
        });
        JMenuItem itEditColle = new JMenuItem("Coller");
        itEditColle.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                coller();
            }
        });

        editMenu.add(itEditAnnul);
        editMenu.add(itEditReta);
        editMenu.addSeparator();
        editMenu.add(itEditCopier);
        editMenu.add(itEditColle);


        JMenu transformationsMenu = new JMenu("Effet");
        
        /* ---- Flou ---- */
        
        JMenuItem flouMenu = new JMenuItem("Flou");
        flouMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                flou();
            }
        });
        
        /* ---- Negatif ---- */
        
        JMenuItem negativeMenu = new JMenuItem("Negatif");
        negativeMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                negateImage();
            }
        });
        
        /* ---- Supp bruit ---- */
        
	JMenuItem suppressionBruit = new JMenuItem("Suppression bruit");
	suppressionBruit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                suppBruit();
            }
        });
        
        /* ---- Rotations ---- */
	
	JMenu rotationMenu = new JMenu("Rotation");
	
	JMenuItem rotat90Menu = new JMenuItem("90°");
        rotat90Menu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                rotation90();
            }
        });
	
	JMenuItem rotat180Menu = new JMenuItem("180°");
        rotat180Menu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                rotation180();
            }
        });
	
	JMenuItem rotat270Menu = new JMenuItem("270°");
        rotat270Menu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                rotation270();
            }
        });
	
	rotationMenu.add(rotat90Menu);
        rotationMenu.add(rotat180Menu);
        rotationMenu.add(rotat270Menu);
        
        /* ---- Miroir ---- */
	
	JMenu miroirMenu = new JMenu("Miroir");
	
	JMenuItem miroirVMenu = new JMenuItem("Horizontal");
        miroirVMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                miroirV();
            }
        });
	
	JMenuItem miroirHMenu = new JMenuItem("Vertical");
        miroirHMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                miroirH();
            }
        });
	
	miroirMenu.add(miroirVMenu);
	miroirMenu.add(miroirHMenu);

			
        transformationsMenu.add(negativeMenu);
        transformationsMenu.add(flouMenu);
	transformationsMenu.add(suppressionBruit);
        transformationsMenu.addSeparator();
        transformationsMenu.add(rotationMenu);
	transformationsMenu.add(miroirMenu);
        
        JMenu affichageMenu = new JMenu("Affichage");
        
        /* ---- Zoom ---- */
        
        JMenu zoomMenu = new JMenu("Zoom");

        JMenuItem zoom1Menu = new JMenuItem("x1");
        zoom1Menu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                zoom(1);
            }
        });
        JMenuItem zoom2Menu = new JMenuItem("x2");
        zoom2Menu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                zoom(2);
            }
        });
        JMenuItem zoom3Menu = new JMenuItem("x3");
        zoom3Menu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                zoom(3);
            }
        });
	
        JMenuItem zoom4Menu = new JMenuItem("x4");
        zoom4Menu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                zoom(4);
            }
        });
        JMenuItem zoom5Menu = new JMenuItem("x5");
        zoom5Menu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                zoom(5);
            }
        });
        zoomMenu.add(zoom1Menu);
        zoomMenu.add(zoom2Menu);
        zoomMenu.add(zoom3Menu);
        zoomMenu.add(zoom4Menu);
        zoomMenu.add(zoom5Menu);
        
	/* ---- Agrandissement ---- */
        
	JMenu agrandMenu = new JMenu("Agrandissement");

        JMenuItem agrand1Menu = new JMenuItem("x1");
        agrand1Menu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                agrandissement(1);
            }
        });
        JMenuItem agrand2Menu = new JMenuItem("x2");
        agrand2Menu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                agrandissement(2);
            }
        });
        JMenuItem agrand3Menu = new JMenuItem("x3");
        agrand3Menu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                agrandissement(3);
            }
        });
	
        JMenuItem agrand4Menu = new JMenuItem("x4");
        agrand4Menu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                agrandissement(4);
            }
        });
        JMenuItem agrand5Menu = new JMenuItem("x5");
        agrand5Menu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                agrandissement(5);
            }
        });
        agrandMenu.add(agrand1Menu);
        agrandMenu.add(agrand2Menu);
        agrandMenu.add(agrand3Menu);
        agrandMenu.add(agrand4Menu);
        agrandMenu.add(agrand5Menu);
        affichageMenu.add(zoomMenu);
	affichageMenu.add(agrandMenu);
	

        menu.add(fileMenu);
        menu.add(editMenu);
        menu.add(transformationsMenu);
        menu.add(affichageMenu);

        
        this.setJMenuBar(menu);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //ar défaut
        setPreferredSize(new Dimension(500, 500));
        setMinimumSize(new Dimension(500, 500));
        setMaximumSize(new Dimension(500, 500));

        // Centrer fenetre à l'ouverture
        this.setLocationRelativeTo(this.getParent());
        this.show();

        tabs = new JTabbedPane();
        //Ajout du panneau à onglet
        getContentPane().add(tabs);

    }

    public static void main(String[] args) {
	try{
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch(Exception e) {}
	
	new Fenetre();
    }

    private void openFile() {
        //Nouveau selecteur de fichier
        JFileChooser chooser = new JFileChooser();
        //Permet de ne choisir qu'un seul fichier
        chooser.setMultiSelectionEnabled(false);
        //Ouverture du selecteur
        int res = chooser.showOpenDialog(this);
	String title = "";

        if (res == JFileChooser.APPROVE_OPTION) {
            //L'utilisateur a choisi d'ouvrir un fichier
            try {
                //On lit ce fichier comme une image
		title = chooser.getSelectedFile().getName();
                BufferedImage loadedImg = ImageIO.read(chooser.getSelectedFile());
                //On crée la matrice qui va représenter cette image
                CompressionMatrix tmpMat = new CompressionMatrix(loadedImg.getHeight(), loadedImg.getWidth());
                for (int x = 0; x < loadedImg.getWidth(); x++) {
                    for (int y = 0; y < loadedImg.getHeight(); y++) {
                        //Calcul du composant rouge et remis dans une échelle 0-255
                        int red = (loadedImg.getRGB(x, y) & 0xFF0000) >> 16;
                        //Calcul du composant vert et remis dans une échelle 0-255
                        int green = (loadedImg.getRGB(x, y) & 0x00FF00) >> 8;
                        //Calcul du composant bleu et remis dans une échelle 0-255
                        int blue = (loadedImg.getRGB(x, y) & 0x0000FF);
                        /*
                         * Pour avoir une image en noir et blanc issu de
                         * l'image, On calcule la luminance de ce pixel en
                         * proportion de rouge,vert et bleu respectivement 30%,
                         * 59% et 11% D'autres formules existent, mais nous
                         * utiliserons celle là
                         */
                        int lumi = (int) (red * 0.30 + green * 0.59 + blue * 0.11);
                        //On rempli la matrice
                        tmpMat.colorPixel(x, y, lumi);
                    }
                }         
                tabs.addTab(title, new ImageView(tmpMat));
                //Pour pouvoir fermer l'onglet
                tabs.setTabComponentAt(tabs.getTabCount()-1,new ButtonTabComponent(tabs));
                
            } catch (IOException ex) {
            }
        }
    }

    private void quit() {
        int rep =
                JOptionPane.showConfirmDialog(
                this,
                "Voulez vous vraiment quitter ?",
                "Fermeture de l'application",
                JOptionPane.YES_NO_OPTION);
        System.out.println("rep :" + rep);
        if (rep == 0) {
            System.exit(0);
        }
    }
    
    private CompressionMatrix getMatrix() {
	return ((ImageView)tabs.getSelectedComponent()).getMatrix();
    }
    
    private ImageView getImageView() {
	return ((ImageView)tabs.getSelectedComponent());
    }

    private void openNouv() {
        //tabs.setTabComponentAt(tabs.getTabCount()-1,new ButtonTabComponent(tabs));
    }

    private void zoom(double factor) {
        getImageView().setZoomFactor(factor);
    }
    
    private void agrandissement(int factor) {
	tabs.addTab("Agrandissement"+factor, new ImageView(getMatrix().agrandissement(factor)));
        tabs.setTabComponentAt(tabs.getTabCount()-1,new ButtonTabComponent(tabs));
    }
    
    private void flou() {
	tabs.addTab("Flou", new ImageView(getMatrix().flou()));
        tabs.setTabComponentAt(tabs.getTabCount()-1,new ButtonTabComponent(tabs));
    }
    
    private void negateImage() {
	tabs.addTab("Negatif", new ImageView(getMatrix().negative()));
        tabs.setTabComponentAt(tabs.getTabCount()-1,new ButtonTabComponent(tabs));
    }

    private void suppBruit() {
	
	FenetreBruit fb = new FenetreBruit();
	tabs.addTab("Suppression bruit", new ImageView(getMatrix().suppressionBruit(fb.getSeuil())));
	
    }
    
    private void rotation90() {
	tabs.addTab("Rotation90", new ImageView(getMatrix().rotation90()));
        tabs.setTabComponentAt(tabs.getTabCount()-1,new ButtonTabComponent(tabs));
    }
    
    private void rotation180() {
	tabs.addTab("Rotation180", new ImageView(getMatrix().rotation180()));
        tabs.setTabComponentAt(tabs.getTabCount()-1,new ButtonTabComponent(tabs));
    }
    
    private void rotation270() {
	tabs.addTab("Rotation270", new ImageView(getMatrix().rotation270()));
        tabs.setTabComponentAt(tabs.getTabCount()-1,new ButtonTabComponent(tabs));
    }
    
    private void miroirV() {
	tabs.addTab("Miroir vertical", new ImageView(getMatrix().miroirVertical()));
        tabs.setTabComponentAt(tabs.getTabCount()-1,new ButtonTabComponent(tabs));
    }
    
    private void miroirH() {
	tabs.addTab("Miroir horizontal", new ImageView(getMatrix().miroirHorizontal()));
        tabs.setTabComponentAt(tabs.getTabCount()-1,new ButtonTabComponent(tabs));
    }
    

    private void saveCurrentPanel() {
        //A compléter
    }

    private void saveAllPanels() {
        //A compléter
    }

    private void annuler() {
    }

    private void retablir() {
    }

    private void copier() {
    }

    private void coller() {
    }
}