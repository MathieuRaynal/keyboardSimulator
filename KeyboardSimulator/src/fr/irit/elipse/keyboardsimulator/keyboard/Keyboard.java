package fr.irit.elipse.keyboardsimulator.keyboard;

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
import fr.irit.elipse.keyboardsimulator.TextUtils;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Keyboard implements Observer {
	final File FILE_CHARS = new File("resources/char-predictions.bin");
	final File FILE_NGRAMS = new File("resources/fr_ngrams.bin");
	final File FILE_WORDS = new File("resources/fr_words.bin");
	
	private Block layout;
	private boolean localCharPrediction, charPrediction, wordPrediction;
	private CharPredictor predictor;
	private String motEnCours;
	private WordPredictor wordPredictor;
	private ArrayList<String> wordList;
	private int nbWords;
	public Timer starter;

	private Area activeArea;

	private String layoutId;
	
	public Keyboard(String keyboardLayout) {
		super();
		layoutId = keyboardLayout;
		initKeyboard();

		activeArea = layout.getChildren().get(layout.getChildren().size()-1);

		// starter = new Timer(5000, e -> {
		// 	 layout.activate();
		//	 starter.stop();
		// });
		// starter.start();
	}

	// deuxième constructeur de test 
	
	public Keyboard(){
		this("CL4_DL_N");
	}

	public void initKeyboard() {
		localCharPrediction = false;
		charPrediction = false;
		wordPrediction = false;
		nbWords = 0;
		motEnCours = "";
		wordList = null;

		KeyboardLoader.loadXMLFile(this, layoutId);
	}
	// fin deuxième constructeur de test 

	public void setLayout(Block block) {
		this.layout = block;
	}

	public void setNbWords(int nb) {
		nbWords = nb;
	}

	public String getLayoutId() {
		return layoutId;
	}
	
	public void setCharPrediction(boolean charPrediction){
		this.charPrediction = charPrediction;
		createPredictor(charPrediction);
	}
	
	public void setLocalCharPrediction(boolean localCharPrediction){
		this.localCharPrediction = localCharPrediction;
		createPredictor(localCharPrediction);
	}

	private void createPredictor(boolean shouldCreate) {
		if (!shouldCreate || predictor != null) return;

		// Prediction de caractères
		CharPredictorData data = new CharPredictorData();
		try {
			data.loadFrom(FILE_CHARS);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		predictor = new CharPredictor(data);
		System.out.println("PING");
	}

	public void setWordPrediction(boolean wordPrediction){
		this.wordPrediction = wordPrediction;

		if (!wordPrediction) {
			wordPredictor = null;
			return;
		}

		// Prédiction de mots
		LanguageModel languageModel = new FrenchLanguageModel();
		PredictionParameter predictionParameter = new PredictionParameter(languageModel);
		try {
			WordDictionary dictionary = WordDictionary.loadDictionary(languageModel, FILE_WORDS);
			StaticNGramTrieDictionary ngramDictionary = StaticNGramTrieDictionary.open(FILE_NGRAMS);
			wordPredictor = new WordPredictor(predictionParameter, dictionary, ngramDictionary);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Block getKeyboardLayout(){ return layout; }

	// Input handling
	// =========================================================================

	public void activate() {
		System.out.println("Activate");
		activeArea.desactivate();

		activeArea = ((Block) activeArea.getParent()).getNextChild();
		activeArea.activate();
	}

	public void validate() {
		System.out.println("Validate");
		activeArea.validate();
		activeArea.desactivate();

		if(activeArea.getClass().equals(Block.class)) activeArea = ((Block) activeArea).getNextChild();
		else if(activeArea.getClass().equals(Key.class)) activeArea = layout.getChildren().get(0);

		activeArea.activate();
	}

	// =========================================================================

	public void initLayout() {
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
