package fr.irit.elipse.keyboardsimulator;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

public class KeyboardSimulator implements Observer{
	Keyboard keyboard;
	String mot, saisie;

	public KeyboardSimulator(Keyboard kb){
		mot = "adbecf";
		saisie = "";
		keyboard = kb;
		keyboard.getKeyboardLayout().addObserver(this);
		keyboard.validate();
	}
	
	public static void main(String[] args){
		new KeyboardSimulator(new Keyboard());
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {
		String s = (String)arg;
		String prefixe = s.substring(0,6);
		String res = s.substring(6).toLowerCase();
		if(!res.equals("layout")){
			System.out.println(s+" = "+prefixe+"+"+res);
			switch(prefixe) {
				case "[A](B)":
					if(res.contains(mot.subSequence(0, 1)))
						keyboard.validate();
				break;
				case "[A](K)":
					if(res.equals(mot.subSequence(0, 1)))
						keyboard.validate();
				break;
				case "[V](B)":
				break;
				case "[V](K)":
					saisie = saisie + res;
					mot = mot.substring(res.length());
					System.out.println("*************************** Saisie : "+saisie);
					if(mot.length()==0) {
						System.out.println("Saisie terminee");
						System.exit(0);
					}
					keyboard.getKeyboardLayout().init();
					keyboard.getKeyboardLayout().activate();
					keyboard.getKeyboardLayout().validate();
				break;
				default:
				break;
			}
		}
	}
}
