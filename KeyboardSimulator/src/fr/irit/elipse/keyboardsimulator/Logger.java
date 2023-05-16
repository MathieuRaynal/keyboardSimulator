package fr.irit.elipse.keyboardsimulator;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Logger{
	BufferedWriter txtFile;
	Mot motEnCours;
	
	public Logger(String fileName) {
		try {
			txtFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName),StandardCharsets.UTF_8));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void debutSimulation(){
		try {
			txtFile.write("word;Nb_char (C_w);Freq (F_w);Nb_scan_block (S_w_b);Nb_scan_key (S_w_k);Nb_scan (S_w);Nb_act_block;Nb_act_key;Nb_act;S_w_b x F_w;S_w_k x F_w;S_w x F_w;C_w x F_w");
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

	public void finDeMot(int nbScanBlock, int nbActBlock, int nbScanKey, int nbActKey) {
		try {
			int nbScan = nbScanBlock+nbScanKey;
			int nbAct = nbActBlock + nbActKey;
			txtFile.write(";");
			txtFile.write(String.valueOf(nbScanBlock));
			txtFile.write(";");
			txtFile.write(String.valueOf(nbScanKey));
			txtFile.write(";");
			txtFile.write(String.valueOf(nbScan));
			txtFile.write(";");
			txtFile.write(String.valueOf(nbActBlock));
			txtFile.write(";");
			txtFile.write(String.valueOf(nbActKey));
			txtFile.write(";");
			txtFile.write(String.valueOf(nbAct));
			txtFile.write(";");
			txtFile.write(String.valueOf(nbScanBlock*motEnCours.getFreq()));
			txtFile.write(";");
			txtFile.write(String.valueOf(nbScanKey*motEnCours.getFreq()));
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