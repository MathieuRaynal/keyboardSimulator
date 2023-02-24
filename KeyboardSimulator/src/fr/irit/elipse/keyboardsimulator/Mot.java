package fr.irit.elipse.keyboardsimulator;

public class Mot {
	String mot;
	int freq;
	
	public Mot(String mot, int freq) {
		this.mot = mot;
		this.freq = freq;
	}
	
	public String getMot() {
		return mot;
	}
	
	public int getFreq() {
		return freq;
	}
	
	public int getNbChar() {
		return mot.length();
	}
}
