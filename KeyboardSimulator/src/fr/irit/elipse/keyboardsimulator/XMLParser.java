package fr.irit.elipse.keyboardsimulator;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class XMLParser implements ContentHandler{
	Pile pile;
	Keyboard keyboard;
	Block currentBlock;
	
	public XMLParser(Keyboard kb){
		pile = new Pile();
		keyboard = kb;
		currentBlock = keyboard.getKeyboardLayout();
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
				if(atts.getValue("charPrediction")!=null)
					keyboard.setCharPrediction(Boolean.parseBoolean(atts.getValue("charPrediction")));
				if(atts.getValue("wordPrediction")!=null)
					keyboard.setWordPrediction(Boolean.parseBoolean(atts.getValue("wordPrediction")));
				
				break;
			case "key":
				String string = atts.getValue("string");
				int indexCharPred = -1;
				int indexWordPred = -1;
				if(atts.getValue("indexCharPred")!=null)
					indexCharPred = Integer.parseInt(atts.getValue("indexCharPred"));
				if(atts.getValue("indexWordPred")!=null)
					indexWordPred = Integer.parseInt(atts.getValue("indexWordPred"));
				int x = Integer.parseInt(atts.getValue("x"));
				int y = Integer.parseInt(atts.getValue("y"));
				int width = Integer.parseInt(atts.getValue("width"));
				int height = Integer.parseInt(atts.getValue("height"));
				Key k = new Key(string, indexCharPred, indexWordPred, x, y, width, height);
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
