package fr.irit.elipse.keyboardsimulator.keyboard;

import fr.irit.elipse.keyboardsimulator.keyboard.Block;

import java.util.ArrayList;

public class Pile {
	ArrayList<Block> pile;
	
	public Pile() {
		pile = new ArrayList<Block>();
	}
	
	public void empiler(Block b){ pile.add(b);}
	public Block depiler() {
		if(!isEmpty())
			return pile.remove(pile.size()-1);
		return null;
	}
	public boolean isEmpty() {return pile.size()==0;}
	public int size() {return pile.size();}
}
