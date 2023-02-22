import java.io.File;
import java.io.IOException;

import org.predict4all.nlp.language.LanguageModel;
import org.predict4all.nlp.language.french.FrenchLanguageModel;
import org.predict4all.nlp.ngram.dictionary.StaticNGramTrieDictionary;
import org.predict4all.nlp.prediction.PredictionParameter;
import org.predict4all.nlp.prediction.WordPrediction;
import org.predict4all.nlp.prediction.WordPredictionResult;
import org.predict4all.nlp.prediction.WordPredictor;
import org.predict4all.nlp.words.WordDictionary;

public class Test {

	public static void main(String[] args) {
		final File FILE_NGRAMS = new File("resources/fr_ngrams.bin");
		final File FILE_WORDS = new File("resources/fr_words.bin");

		LanguageModel languageModel = new FrenchLanguageModel();
		PredictionParameter predictionParameter = new PredictionParameter(languageModel);
		try {
			WordDictionary dictionary = WordDictionary.loadDictionary(languageModel, FILE_WORDS);
			StaticNGramTrieDictionary ngramDictionary = StaticNGramTrieDictionary.open(FILE_NGRAMS);
		    WordPredictor wordPredictor = new WordPredictor(predictionParameter, dictionary, ngramDictionary);
		    WordPredictionResult predictionResult = wordPredictor.predict("j'aime manger des ");
		    for (WordPrediction prediction : predictionResult.getPredictions()) {
		    	if(prediction.getPredictionToDisplay().equals("gâteaux"))
		    		System.out.println("GATEAUX !");
		    }
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

}
