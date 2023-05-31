package fr.irit.elipse.keyboardsimulator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.Timer;

import org.lifecompanion.model.impl.textprediction.charprediction.CharPredictor;
import org.lifecompanion.model.impl.textprediction.charprediction.CharPredictorData;
import org.predict4all.nlp.language.LanguageModel;
import org.predict4all.nlp.language.french.FrenchLanguageModel;
import org.predict4all.nlp.ngram.dictionary.StaticNGramTrieDictionary;
import org.predict4all.nlp.prediction.PredictionParameter;
import org.predict4all.nlp.prediction.WordPrediction;
import org.predict4all.nlp.prediction.WordPredictionResult;
import org.predict4all.nlp.prediction.WordPredictor;
import org.predict4all.nlp.words.WordDictionary;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

public class Keyboard extends JComponent implements Observer{
	final File FILE_CHARS = new File("resources/char-predictions.bin");
	final File FILE_NGRAMS = new File("resources/fr_ngrams.bin");
	final File FILE_WORDS = new File("resources/fr_words.bin");
	
	private Block layout;
	private int activationTime;
	private boolean localCharPrediction, charPrediction, wordPrediction;
	private CharPredictor predictor;
	private String motEnCours;
	private WordPredictor wordPredictor;
	private ArrayList<String> wordList;
	private int nbWords;
	public Timer starter;
	
	public Keyboard(String clavier, int activationTime){
		super();
		this.activationTime = activationTime;
		localCharPrediction = false;
		charPrediction = false;
		wordPrediction = false;
		nbWords = 0;
		initKeyboard(clavier);
		motEnCours = "";
		wordList = null;
		
		if(localCharPrediction || charPrediction) {
			// Prediction de caractères
			CharPredictorData data = new CharPredictorData();
			try {
				data.loadFrom(FILE_CHARS);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			predictor = new CharPredictor(data);
		}
		if(wordPrediction) {
			// Prédiction de mots
			LanguageModel languageModel = new FrenchLanguageModel();
			PredictionParameter predictionParameter = new PredictionParameter(languageModel);
			try {
				WordDictionary dictionary = WordDictionary.loadDictionary(languageModel, FILE_WORDS);
				StaticNGramTrieDictionary ngramDictionary = StaticNGramTrieDictionary.open(FILE_NGRAMS);
			    wordPredictor = new WordPredictor(predictionParameter, dictionary, ngramDictionary);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		setPreferredSize(new Dimension(600, 500));
		starter = new Timer(5000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				layout.activate();
				starter.stop();
			}
		});
		starter.start();
	}
	
	
	
	// deuxième constructeur de test 
	
	public Keyboard(int activationTime){
		super();
		this.activationTime = activationTime;
		localCharPrediction = false;
		charPrediction = false;
		wordPrediction = false;
		initKeyboard();
		motEnCours = "";
		wordList = null;
		
		if(localCharPrediction || charPrediction) {
			// Prediction de caractères
			CharPredictorData data = new CharPredictorData();
			try {
				data.loadFrom(FILE_CHARS);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			predictor = new CharPredictor(data);
		}
		if(wordPrediction) {
			// Prédiction de mots
			LanguageModel languageModel = new FrenchLanguageModel();
			PredictionParameter predictionParameter = new PredictionParameter(languageModel);
			try {
				WordDictionary dictionary = WordDictionary.loadDictionary(languageModel, FILE_WORDS);
				StaticNGramTrieDictionary ngramDictionary = StaticNGramTrieDictionary.open(FILE_NGRAMS);
			    wordPredictor = new WordPredictor(predictionParameter, dictionary, ngramDictionary);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		setPreferredSize(new Dimension(600, 500));
		layout.activate();
	}
	// fin deuxième constructeur de test 
	
	public void setNbWords(int nb) {
		nbWords = nb;
	}
	
	public int getActivationTime() {
		return activationTime;
	}
	
	public void setCharPrediction(boolean charPrediction){
		this.charPrediction = charPrediction;
	}
	
	public void setLocalCharPrediction(boolean localCharPrediction){
		this.localCharPrediction = localCharPrediction;
	}
	
	public void setWordPrediction(boolean wordPrediction){
		this.wordPrediction = wordPrediction;
	}
	
	public void initKeyboard(String keyboard) {
		loadXMLFile(keyboard);
	}
	
	// Méthode de test - pour le deuxième constructeur
	
	public void initKeyboard() {
		loadXMLFile("resources/keyboards/CL4_DL_N.xml");
	}
	//fin éthode de tets 

	public void loadXMLFile(String fileName) {
		layout = new Block(Block.RACINE, activationTime);
		XMLReader saxReader;
		try{
			saxReader = XMLReaderFactory.createXMLReader();
			saxReader.setContentHandler(new XMLParser(this));
			try{
				InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName),StandardCharsets.UTF_8);
				InputSource is = new InputSource();
				is.setCharacterStream(isr);
				saxReader.parse(is);
			}catch (IOException e){e.printStackTrace();}
		}catch (SAXException e) {e.printStackTrace();}
	}
	
	public Block getKeyboardLayout(){return layout;}

	public void validate() {
		layout.validate();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		layout.paint(g2);
	}

	public void initLayout(){
		if(charPrediction){
			motEnCours = "";
			List<Character> listChar = TextUtils.getCharList(predictor.predict(motEnCours, 100));
			layout.setChar(listChar);
		}
		if(localCharPrediction){
			motEnCours = "";
			List<Character> listChar = TextUtils.getCharList(predictor.predict(motEnCours, 100));
			layout.setLocalChar(listChar);
		}
	}
	
	public boolean containsWord(String word){
		if(wordList==null)
			return false;
		int i = 0;
		while(i<nbWords && i<wordList.size()) {
			String w = wordList.get(i);
//			System.out.println(w+"___"+word+".");
			if(w.equals(word.trim()))
				return true;
			i++;
		}
		return false;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		String s = (String)arg;
		if(s.startsWith("[V](K)")){
			if(s.substring(6).equals(" ")) {
				motEnCours = "";
				layout.initLayout();
			}
			else{
				String ajout = s.substring(6);
				if(ajout.length()>1) {
					motEnCours = "";
					layout.initLayout();
				}else{
					motEnCours += s.substring(6);
					
					if(localCharPrediction) {
						List<Character> listChar = TextUtils.getCharList(predictor.predict(motEnCours, 100));
						layout.setLocalChar(listChar);
					}
					if(charPrediction) {
						List<Character> listChar = TextUtils.getCharList(predictor.predict(motEnCours, 100));
						layout.setChar(listChar);
					}
				}
			}

			if(wordPrediction) {
				WordPredictionResult predictionResult;
				try {
					predictionResult = wordPredictor.predict(motEnCours);
					wordList = new ArrayList<String>();
					for (WordPrediction prediction : predictionResult.getPredictions())
						wordList.add(TextUtils.getWord(prediction.getPredictionToDisplay().toLowerCase()));
					layout.setWord(wordList);
					
			    } catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}
