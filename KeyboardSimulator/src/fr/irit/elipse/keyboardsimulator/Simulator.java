package fr.irit.elipse.keyboardsimulator;

import fr.irit.elipse.keyboardsimulator.keyboard.Keyboard;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

public class Simulator implements Observer{
	ArrayList<String> claviers;
	
	public Simulator() {
		claviers = new ArrayList<String>();
		//claviers.add("1b_SMK_anglais");
		claviers.add("1a_SMK_L_DG");
		claviers.add("1a_SMK_L");
		
		// à tester 
		/*claviers.add("1a_SMK_RC");
		claviers.add("1a_SMK_L");
		claviers.add("1a_SMK_HH");
		claviers.add("1a_SMK_nary");
		*/
		
	}
	
	public void startNextKeyboard() {
		if(claviers.size()>0) {
			String name = claviers.remove(0);
			String clavier = "resources/"+name+".xml";
			String log = "logs/correction/clavier_fr2k_"+name+".csv";
			KeyboardSimulator ks = new KeyboardSimulator(new Keyboard(clavier,KeyboardSimulator.DEFAULT_ACTIVATION_TIME, null),log);
			ks.addObserver(this);
			System.out.println("--------------------------------------------------------------------------");
			System.out.println("| START "+name);
			System.out.println("--------------------------------------------------------------------------");
		}else
			System.out.println("**** FIN DE LA SIMULATION ****");
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		Simulator simul = new Simulator();
		simul.startNextKeyboard();
	}

	@Override
	public void update(Observable o, Object arg) {
		startNextKeyboard();
	}
}
