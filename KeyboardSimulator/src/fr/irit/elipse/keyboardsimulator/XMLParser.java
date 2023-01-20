package fr.irit.elipse.keyboardsimulator;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class XMLParser implements ContentHandler{
	Pile pile;
	Block currentBlock;
	
	public XMLParser(Block block) {
		pile = new Pile();
		currentBlock = block;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		String element = localName.toLowerCase();
		switch(element){
			case "block":
				System.out.println("Nouveau bloc");
				pile.empiler(currentBlock);
				currentBlock = new Block();
				break;
			case "key":
				System.out.println("Nouvelle touche");
				String string = atts.getValue("string");
				int x = Integer.parseInt(atts.getValue("x"));
				int y = Integer.parseInt(atts.getValue("y"));
				int width = Integer.parseInt(atts.getValue("width"));
				int height = Integer.parseInt(atts.getValue("height"));
				Key k = new Key(string, x, y, width, height);
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
