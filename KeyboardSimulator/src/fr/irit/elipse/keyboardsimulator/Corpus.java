package fr.irit.elipse.keyboardsimulator;

import java.io.*;
import java.util.ArrayList;

public class Corpus{
	ArrayList<Mot> wordsList;
	
	public Corpus(){
		wordsList = new ArrayList<Mot>();
	}
	
	public void load(String fileName){
		try{
		   BufferedReader buf = new BufferedReader(new FileReader(fileName));
		   String line = buf.readLine();
		   while(line != null){
			   String[] tab = line.trim().split(",");
			   wordsList.add(new Mot(tab[0],Integer.parseInt(tab[1])));
		       line = buf.readLine();
		   }
		   buf.close();
		} 
		catch (IOException e){
		   e.printStackTrace();
		}
		System.out.println("Taille corpus : "+wordsList.size());
	}
	
	public Mot getNextWord(){
		if(!isEmpty())
			return wordsList.remove(0);
		return null;
	}
	
	public boolean isEmpty() {
		return wordsList.size() == 0;
	}
}
