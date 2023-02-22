package fr.irit.elipse.keyboardsimulator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JFrame;

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
	final File FILE_NGRAMS = new File("resources/fr_ngrams.bin");
	final File FILE_WORDS = new File("resources/fr_words.bin");
	
	private Block layout;
	private CharPredictor predictor;
	private String motEnCours;
	private WordPredictor wordPredictor;
	
	public Keyboard(){
		super();
		initKeyboard();
		motEnCours = "";
		
		// Prediction de caractères
		CharPredictorData data = new CharPredictorData();
		data.executeTraining("bonjour bonsoir tout le monde au revoir je teste avec des phrases enfin des mots juste pour faire du volume et de la longueur dans mon corpus");
		predictor = new CharPredictor(data);
		
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
		
		layout.addObserver(this);
		setPreferredSize(new Dimension(600, 500));
	}
	
	public void initKeyboard() {
		loadXMLFile("resources/alpha.xml");
	}
	
	public void loadXMLFile(String fileName) {
		layout = new Block("Layout");
		XMLReader saxReader;
		try{
			saxReader = XMLReaderFactory.createXMLReader();
			saxReader.setContentHandler(new XMLParser(layout));
			try{
				InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName),StandardCharsets.UTF_8);
				InputSource is = new InputSource();
				is.setCharacterStream(isr);
				saxReader.parse(is);
			}catch (IOException e){e.printStackTrace();}
		}catch (SAXException e) {e.printStackTrace();}

		layout.activate();
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

	@Override
	public void update(Observable o, Object arg) {
		String s = (String)arg;
//		System.out.println(s);
		if(s.startsWith("[V](K)")){
			motEnCours += s.substring(6);
			List<Character> listChar = predictor.predict(motEnCours, 100);
			System.out.println("--- Saisie : "+motEnCours);
			System.out.println("--- Prediction char : "+listChar);
			layout.setChar(listChar);
			
			WordPredictionResult predictionResult;
			try {
				predictionResult = wordPredictor.predict(motEnCours);
				List<String> wordList = new ArrayList<String>();
				for (WordPrediction prediction : predictionResult.getPredictions())
					wordList.add(prediction.getPredictionToDisplay());
				System.out.println("--- Prediction word : "+wordList);
				layout.setWord(wordList);
				
		    } catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
