package fr.irit.elipse.keyboardsimulator;

import java.io.*;
import java.util.ArrayList;

public class Corpus{
	ArrayList<String> list;
	
	public Corpus(){
		list = new ArrayList<String>();
	}
	
	public void load(String fileName){
		try{
		   BufferedReader buf = new BufferedReader(new FileReader(fileName));
		   String line = buf.readLine();
		   while(line != null){
			   String[] tab = line.split(" ");
			   for(String s:tab)
				   list.add(s);
		       line = buf.readLine();
		   }
		   buf.close();
		} 
		catch (IOException e){
		   e.printStackTrace();
		}
		System.out.println("Taille corpus : "+list.size());
	}
	
	public String getNextWord(){
		if(!isEmpty())
			return list.remove(0);
		return null;
	}
	
	public boolean isEmpty() {
		return list.size() == 0;
	}
}
