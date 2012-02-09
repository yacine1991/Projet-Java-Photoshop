/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compressionMatrix;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author suzun2
 */
public class FenetreBruit extends JDialog{
    private JSlider j1; 

    public FenetreBruit(){
	
	
	setModal(true);	
	setTitle("Suppression Bruit");
	setSize(200, 200);
	JPanel pano = new JPanel();
	j1 = new JSlider(JSlider.HORIZONTAL, 0, 255, 70);
	JLabel jl1 = new JLabel("Seuil du Bruit");
	JButton btnOk = new JButton("Valider");
	btnOk.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		setVisible(false);
		
		
		
	    }
	});
	
	
	pano.add(jl1);
	pano.add(j1);
	pano.add(btnOk);
	this.add(pano);
	
	setVisible(true);
    } 
    public int getSeuil(){
	return j1.getValue();
    }
    
}
