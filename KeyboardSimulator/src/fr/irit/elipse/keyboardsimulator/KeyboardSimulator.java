package fr.irit.elipse.keyboardsimulator;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

public class KeyboardSimulator extends Observable implements Observer{
	public static final int DEFAULT_ACTIVATION_TIME = 80;
	Corpus corpus;
	Keyboard keyboard;
	Mot mot;
	String motEnCours, saisie;
	int nbActivationBlock, nbValidationBlock, nbActivationKey, nbValidationKey;
	Logger logger;

	public KeyboardSimulator(Keyboard kb){
		corpus = new Corpus();
		corpus.load("resources/corpus_2000_Cherifa.txt");
		logger = new Logger("logs/test.csv");
		logger.debutSimulation();
		keyboard = kb;
		keyboard.getKeyboardLayout().addObserver(this);
		keyboard.getKeyboardLayout().addObserver(keyboard);
		getMot();
		keyboard.validate();
	}
	// deuxième constructeur pour test
	public KeyboardSimulator(Keyboard kb,String log){
		corpus = new Corpus();
		corpus.load("resources/corpus_2000_Cherifa.txt");
		logger = new Logger(log);
		logger.debutSimulation();
		keyboard = kb;
		keyboard.getKeyboardLayout().addObserver(this);
		keyboard.getKeyboardLayout().addObserver(keyboard);
		getMot();
		keyboard.validate();
	}
	// fin deuxième constructeur
	
	public boolean getMot() {
		if(corpus.isEmpty())
			return false;
		mot = corpus.getNextWord();
		logger.debutDeMot(mot);
		motEnCours = mot.getMot();
		saisie = "";
		initNb();
		System.out.println("---------------- Mot à saisir : "+motEnCours);
		return true;
	}
	
	public void initNb(){
		nbActivationBlock = 0;
		nbValidationBlock = 0;
		nbActivationKey = 0;
		nbValidationKey = 0;
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
					if(keyboard.containsWord(saisie+motEnCours)) {
						if(containsWord(res, saisie+motEnCours))
							keyboard.validate();
					}else if(res.contains(motEnCours.subSequence(0, 1))) {
						keyboard.validate();
					}
				break;
				case "[A](K)":
					nbActivationKey++;
//					System.out.println("Activation : "+res+" / "+saisie+" / "+motEnCours);
					if(keyboard.containsWord(saisie+motEnCours)) {
						if(res.equals(saisie+motEnCours))
							keyboard.validate();
					}else if(res.equals(motEnCours.subSequence(0, 1))) {
						keyboard.validate();
					}
				break;
				case "[V](B)":
					nbValidationBlock++;
				break;
				case "[V](K)":
					if(res.equals(saisie+motEnCours)) {
						motEnCours = "";
					}else {
						saisie = saisie + res;	
						motEnCours = motEnCours.substring(res.length());
					}
					
					nbValidationKey++;
//					System.out.println("*** Saisie : "+saisie);
					
					if(motEnCours.length()==0){
						System.out.println("Mot termine");
						logger.finDeMot(nbActivationBlock+nbActivationKey,nbValidationBlock+nbValidationKey);
						if(corpus.isEmpty()){
							System.out.println("Saisie terminee");
							logger.finSimulation();
							setChanged(); 
							notifyObservers(); 
						}else{
							getMot();
							keyboard.initLayout();
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
	
	public boolean containsWord(String res, String word) {
		String[] words = res.split("/");
		for(String w:words)
			if(w.trim().equals(word))
				return true;
		return false;
	}
	
	public static void main(String[] args){
		ArrayList<String> claviers = new ArrayList<String>();
		claviers.add("PS_RC_C1");
		
		
		
		
		
		for (int i = 0 ; i < claviers.size(); i++){
			System.out.println(claviers.get(i)+ "§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§");
			
			
			String clavier = "resources/"+claviers.get(i)+".xml";
			String log = "logs/clavier"+claviers.get(i) +".csv";
			new KeyboardSimulator(new Keyboard(clavier,DEFAULT_ACTIVATION_TIME),log);
			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setVisible(true);}/*
		
		
		new KeyboardSimulator(new Keyboard(DEFAULT_ACTIVATION_TIME));
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);//*/
	}
}
