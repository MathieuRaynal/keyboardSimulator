package fr.irit.elipse.keyboardsimulator;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Logger{
	BufferedWriter txtFile;
	int nbTab;
	
	public Logger(String fileName) {
		try {
			txtFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName),StandardCharsets.UTF_8));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		nbTab = 0;
	}
	
	public void debutSimulation(){
		addLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		addLine("<KeyboardLog xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"tba.xsd\">");
		nbTab++;
	}
	
	public void finSimulation(){
		nbTab--;
		addLine("</KeyboardLog>");
		try {
			txtFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void debutDeMot(String mot){
		addLine("<Word word=\""+mot+"\">");
		nbTab++;
	}

	public void finDeMot() {
		nbTab--;
		addLine("</Word>");
	}
	
	public void addCharacter(String s, int nbActivationBlock, int nbValidationBlock, int nbActivationKey, int nbValidationKey) {
		addLine("</Key char=\""+s+"\" nbActivationBlock=\""+nbActivationBlock+"\" nbValidationBlock=\""+nbValidationBlock+"\" nbActivationKey=\""+nbActivationKey+"\" nbValidationKey=\""+nbValidationKey+"\">");
	}
	
	public void addLine(String texte){
		try {
			for(int i=0;i<nbTab;i++)
				txtFile.write("\t");
			txtFile.write(texte);
			txtFile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}