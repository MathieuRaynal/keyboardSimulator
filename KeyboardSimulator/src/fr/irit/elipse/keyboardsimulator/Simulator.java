package fr.irit.elipse.keyboardsimulator;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

public class Simulator implements Observer{
	ArrayList<String> claviers;
	
	public Simulator() {
		claviers = new ArrayList<String>();
		claviers.add("1a_SMK_RC_DG");
		claviers.add("1a_SMK_L_DG");
		
		
	}
	
	public void startNextKeyboard() {
		if(claviers.size()>0) {
			String name = claviers.remove(0);
			String clavier = "resources/"+name+".xml";
			String log = "logs/clavier"+name+".csv";
			KeyboardSimulator ks = new KeyboardSimulator(new Keyboard(clavier,KeyboardSimulator.DEFAULT_ACTIVATION_TIME),log);
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