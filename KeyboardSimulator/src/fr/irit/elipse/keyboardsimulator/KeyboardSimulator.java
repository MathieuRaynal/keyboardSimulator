package fr.irit.elipse.keyboardsimulator;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

public class KeyboardSimulator implements Observer{
	Corpus corpus;
	Keyboard keyboard;
	String mot, saisie;
	int nbActivationBlock, nbValidationBlock, nbActivationKey, nbValidationKey;
	Logger logger;

	public KeyboardSimulator(Keyboard kb){
		corpus = new Corpus();
		corpus.load("resources/corpus.txt");
		logger = new Logger("logs/test.xml");
		logger.debutSimulation();
		mot = corpus.getNextWord();
		logger.debutDeMot(mot);
		saisie = "";
		keyboard = kb;
		initNb();
		keyboard.getKeyboardLayout().addObserver(this);
		keyboard.validate();
	}
	
	public void initNb(){
		nbActivationBlock = 0;
		nbValidationBlock = 0;
		nbActivationKey = 0;
		nbValidationKey = 0;
	}
	
	public void simuleMot(String mot) {
		
	}

	@Override
	public void update(Observable o, Object arg) {
		String s = (String)arg;
		String prefixe = s.substring(0,6);
		String res = s.substring(6).toLowerCase();
		if(!res.equals("layout")){
//			System.out.println(s+" = "+prefixe+"+"+res);
			switch(prefixe) {
				case "[A](B)":
					nbActivationBlock++;
					if(res.contains(mot.subSequence(0, 1))) {
						keyboard.validate();
					}
				break;
				case "[A](K)":
					nbActivationKey++;
					if(res.equals(mot.subSequence(0, 1))) {
						keyboard.validate();
					}
				break;
				case "[V](B)":
					nbValidationBlock++;
				break;
				case "[V](K)":
					saisie = saisie + res;
					mot = mot.substring(res.length());
					nbValidationKey++;
					logger.addCharacter(res, nbActivationBlock, nbValidationBlock, nbActivationKey, nbValidationKey);
					initNb();
					System.out.println("*************************** Saisie : "+saisie);
					System.out.println("Validation Key : "+nbValidationKey);
					
					if(mot.length()==0){
						System.out.println("Mot termine");
						logger.finDeMot();
						if(corpus.isEmpty()){
							System.out.println("Saisie terminee");
							logger.finSimulation();
						}else{
							System.out.print("Mot suivant : ");
							mot = corpus.getNextWord();
							System.out.println(mot);
							logger.debutDeMot(mot);
							saisie = "";
							keyboard.getKeyboardLayout().init();
							keyboard.getKeyboardLayout().activate();
							keyboard.getKeyboardLayout().validate();
						}
					}
					else{
						keyboard.getKeyboardLayout().init();
						keyboard.getKeyboardLayout().activate();
						keyboard.getKeyboardLayout().validate();
					}
				break;
				default:
				break;
			}
		}
	}
	
	public static void main(String[] args){
		new KeyboardSimulator(new Keyboard());
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
