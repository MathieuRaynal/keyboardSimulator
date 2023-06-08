package fr.irit.elipse.keyboardsimulator;

import fr.irit.elipse.keyboardsimulator.keyboard.Keyboard;

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
	LoggerCSV logger;

	public KeyboardSimulator(Keyboard kb){
		corpus = new Corpus();
		corpus.load("resources/corpus_2000_Cherifa.txt");
		logger = new LoggerCSV("logs/test.csv");
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
		logger = new LoggerCSV(log);
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
//		System.out.println("Deb : "+motEnCours+".");
		saisie = "";
		initNb();
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
//			System.out.println(s+" = "+prefixe+"+"+res+"_"+motEnCours+"_");
			switch(prefixe) {
				case "[A](B)":
//					System.out.println("---"+res+"_"+motEnCours+"_");
					nbActivationBlock++;
					if(!motEnCours.trim().equals("") && keyboard.containsWord(saisie+motEnCours)) {
//						System.out.println("passe ici");
						if(containsWord(res, saisie+motEnCours)) {
							keyboard.validate();
						}
					}else {
//						System.out.println(">>>"+res+"_"+motEnCours+"_");
						if(res.contains("/")) {
//							System.out.println("--->>>"+res+"_"+motEnCours+"_");
							String[] words = res.split("/");
//							System.out.println("Words : "+words);
							for(String w:words) {
//								System.out.println("___"+w+"_");
								if(w.equals(motEnCours.subSequence(0, 1)))
									keyboard.validate();
							}
						}else if(res.contains(motEnCours.subSequence(0, 1)))
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
//					System.out.println("-->"+res+"="+saisie+"+"+motEnCours);
					if(res.equals(saisie+motEnCours)) {
						saisie += motEnCours; 
						motEnCours = "";
					}else {
						if(res.equals(" ")) {
							saisie = saisie + res;	
							motEnCours = "";
						}else {
							if(res.length()>1) {
								saisie = res.trim();	
								motEnCours = " ";
							}
							else if(res.length()==1) {
								saisie = saisie + res.trim();	
								motEnCours = motEnCours.substring(1);
							}
						}
					}
					
					nbValidationKey++;
//					System.out.println("*** Saisie : "+saisie);
					
					if(motEnCours.length()==0){
						System.out.println("Mot termine : "+saisie+".");
						logger.finDeMot(nbActivationBlock, nbValidationBlock, nbActivationKey, nbValidationKey);
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
		for(String w:words) {
			if(w.equals(word.trim()))
				return true;
		}
		return false;
	}
	
	public static void main(String[] args){
		ArrayList<String> claviers = new ArrayList<String>();
		claviers.add("PS_RC_C1");
		
		
		for (int i = 0 ; i < claviers.size(); i++){
			System.out.println(claviers.get(i)+ "§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§");
			
			String clavier = "resources/"+claviers.get(i)+".xml";
			String log = "logs/clavier"+claviers.get(i) +".csv";
			new KeyboardSimulator(new Keyboard(clavier),log);
			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setVisible(true);}/*
		
			new KeyboardSimulator(new Keyboard(DEFAULT_ACTIVATION_TIME));
			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setVisible(true);//*/
	}
}
