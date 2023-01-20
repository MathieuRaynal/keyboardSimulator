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
}
