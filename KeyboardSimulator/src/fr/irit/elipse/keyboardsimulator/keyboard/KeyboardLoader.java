package fr.irit.elipse.keyboardsimulator.keyboard;

import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class KeyboardLoader implements ContentHandler{
	public static final String PATH_TO_KEYBOARDS = "resources/keyboards/";

	Pile pile;
	Keyboard keyboard;
	Block currentBlock;
	int nbWords;

	public static void loadXMLFile(Keyboard kb, String kbLayoutId) {
		String fileName = PATH_TO_KEYBOARDS + kbLayoutId + ".xml";
		XMLReader saxReader;

		kb.setLayout(new Block(Block.RACINE));
		try{
			saxReader = XMLReaderFactory.createXMLReader();
			saxReader.setContentHandler(new KeyboardLoader(kb));

			InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
			InputSource is = new InputSource();
			is.setCharacterStream(isr);
			saxReader.parse(is);
		} catch (IOException | SAXException e) {e.printStackTrace();}
	}

	private KeyboardLoader(Keyboard kb){
		pile = new Pile();
		keyboard = kb;
		currentBlock = keyboard.getKeyboardLayout();
		nbWords = 0;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		String element = localName.toLowerCase();
		switch(element){
			case "block":
				pile.empiler(currentBlock);
				currentBlock = new Block();
				break;
			case "keyboard":
				if(atts.getValue("localCharPrediction")!=null)
					keyboard.setLocalCharPrediction(Boolean.parseBoolean(atts.getValue("localCharPrediction")));
				if(atts.getValue("charPrediction")!=null)
					keyboard.setCharPrediction(Boolean.parseBoolean(atts.getValue("charPrediction")));
				if(atts.getValue("wordPrediction")!=null)
					keyboard.setWordPrediction(Boolean.parseBoolean(atts.getValue("wordPrediction")));
				break;
			case "key":
				String string = atts.getValue("string");
				int indexLocalCharPred = -1;
				int indexCharPred = -1;
				int indexWordPred = -1;
				if(atts.getValue("indexLocalCharPred")!=null)
					indexLocalCharPred = Integer.parseInt(atts.getValue("indexLocalCharPred"));
				if(atts.getValue("indexCharPred")!=null)
					indexCharPred = Integer.parseInt(atts.getValue("indexCharPred"));
				if(atts.getValue("indexWordPred")!=null) {
					indexWordPred = Integer.parseInt(atts.getValue("indexWordPred"));
					nbWords++;
				}
				int x = Integer.parseInt(atts.getValue("x"));
				int y = Integer.parseInt(atts.getValue("y"));
				int width = Integer.parseInt(atts.getValue("width"));
				int height = Integer.parseInt(atts.getValue("height"));
				Key k = new Key(string, indexLocalCharPred, indexCharPred, indexWordPred, x, y, width, height);
				currentBlock.addChild(k);
				break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		String element = localName.toLowerCase();
		switch(element){
			case "block":
				System.out.println("Fin de bloc");
				currentBlock.createArea();
				System.out.println("Current : "+currentBlock.getName());
				Block parent = pile.depiler();
				parent.addChild(currentBlock);
				currentBlock = parent;
				System.out.println("Taille pile : "+pile.size());
				break;
			case "keyboard":
				keyboard.setNbWords(nbWords);
				break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		
	}
	
	@Override public void setDocumentLocator(Locator locator){}
	@Override public void startDocument() throws SAXException{}
	@Override public void endDocument() throws SAXException{}
	@Override public void startPrefixMapping(String prefix, String uri) throws SAXException{}
	@Override public void endPrefixMapping(String prefix) throws SAXException{}
	@Override public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException{}
	@Override public void processingInstruction(String target, String data) throws SAXException{}
	@Override public void skippedEntity(String name) throws SAXException{}
}