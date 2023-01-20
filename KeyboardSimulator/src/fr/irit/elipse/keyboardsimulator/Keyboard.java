package fr.irit.elipse.keyboardsimulator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.nio.charset.StandardCharsets;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

public class Keyboard extends JComponent{
	private Block layout;
	
	public Keyboard(){
		super();
		initKeyboard();
		setPreferredSize(new Dimension(500, 500));
	}
	
	public void initKeyboard() {
		loadXMLFile("resources/azerty.xml");
	}
	
	public void init() {
		layout = new Block("Layout");
		layout.activate();
		
		Block line1 = new Block("B1");
		Key keyA = new Key("A",10,10,50,50);
		Key keyB = new Key("B",70,10,50,50);
		Key keyC = new Key("C",130,10,50,50);
		line1.addChild(keyA);
		line1.addChild(keyB);
		line1.addChild(keyC);
		line1.createArea();
		
		Block line2 = new Block("B2");
		Key keyD = new Key("D",10,70,50,50);
		Key keyE = new Key("E",70,70,50,50);
		Key keyF = new Key("F",130,70,50,50);
		line2.addChild(keyD);
		line2.addChild(keyE);
		line2.addChild(keyF);
		line2.createArea();

		layout.addChild(line1);
		layout.addChild(line2);
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
}
