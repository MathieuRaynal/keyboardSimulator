package fr.irit.elipse.keyboardsimulator;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

public class Simulator implements Observer{
	ArrayList<String> claviers;
	
	public Simulator() {
		claviers = new ArrayList<String>();

		claviers.add("1a_SMK_HH");
		claviers.add("1a_SMK_nary");
		claviers.add("1a_SMK_RC");
		claviers.add("1a_SMK_L");

		/*
		
		//claviers.add("CL4_DL_N");//idée gardée
		claviers.add("PS_RC_clavier");//idée gardée
		claviers.add("PS_RC_C1");
		claviers.add("PS_RC_Cmulti");
		
		/*claviers.add("PDG_L_N");//idée gardée
		claviers.add("ADG_L_N");//idée gardée
		claviers.add("AS_RC_N");
		claviers.add("PDG_RC_N");*/
	
		
		
		
		//claviers.add("idée1");
		
		//claviers.add("idée5_1");
		//claviers.add("idée5_2");
		//claviers.add("idée5_3");
		
		//claviers.add("SK_0");
		//claviers.add("SK_1");
		//claviers.add("SK_2");
		
		//claviers.add("SK3_1");
		
		//claviers.add("SK3_2");
		//claviers.add("SK3_3");
		
		//claviers.add("SK4_0");
		//claviers.add("SK4_1");
		//claviers.add("SK4_2");
		
		claviers.add("CL3_S_N");
		
		claviers.add("CL3_DL_d1");
		claviers.add("CL3_DL_dmulti");
		
		claviers.add("CP3_DL_d1");
		claviers.add("CP3_DL_dmulti");
		
		
		//claviers.add("CL2_DL_N");
		//claviers.add("CL2_DL_dmulti");
		//claviers.add("CL2_DL_d1");
		//claviers.add("CP2_DL_N");
		
		//claviers.add("CL2_S_d1");
		//claviers.add("CL2_S_dmulti");
		//claviers.add("CP2_S_N");
		//claviers.add("CL2_S_N");
		
		
		
		//claviers.add("");
		//claviers.add("1a_SMK");
		//claviers.add("1b_SMK");
		//claviers.add("1c_SMK");
		
		//claviers.add("1a_SMK_DL");
		//claviers.add("1b_SMK_DL");
		//claviers.add("1c_SMK_DL");
		
		//claviers.add("1a_SMK_espace");
		//claviers.add("1b_SMK_espace");
		
		//claviers.add("1a_SMK_anglais");
		//claviers.add("1b_SMK_anglais");
		//claviers.add("1c_SMK_anglais");*/
		
	}
	
	public void startNextKeyboard() {
		if(claviers.size()>0) {
			String name = claviers.remove(0);
			String clavier = "resources/"+name+".xml";
			String log = "logs/correction/clavier_fr2k_"+name+".csv";
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
