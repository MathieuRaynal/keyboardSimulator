package fr.irit.elipse.keyboardsimulator.logging;

import fr.irit.elipse.keyboardsimulator.Mot;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class LoggerCSV {
	BufferedWriter txtFile;
	Mot motEnCours;
	
	public LoggerCSV(String fileName) {
		try {
			txtFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName),StandardCharsets.UTF_8));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void debutSimulation(){
		try {
			txtFile.write("word;Nb_char (C_w);Freq (F_w);Nb_scan (S_w);Nb_act;S_w x F_w;C_w x F_w");
			txtFile.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void finSimulation(){
		try {
			txtFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void debutDeMot(Mot mot){
		motEnCours = mot;
		try {
			txtFile.write(mot.getMot());
			txtFile.write(";");
			txtFile.write(String.valueOf(mot.getNbChar()));
			txtFile.write(";");
			txtFile.write(String.valueOf(mot.getFreq()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void finDeMot(int nbScan, int nbAct) {
		try {
			txtFile.write(";");
			txtFile.write(String.valueOf(nbScan));
			txtFile.write(";");
			txtFile.write(String.valueOf(nbAct));
			txtFile.write(";");
			txtFile.write(String.valueOf(nbScan*motEnCours.getFreq()));
			txtFile.write(";");
			txtFile.write(String.valueOf(motEnCours.getNbChar()*motEnCours.getFreq()));
			txtFile.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}