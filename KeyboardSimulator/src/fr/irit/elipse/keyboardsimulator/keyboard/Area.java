package fr.irit.elipse.keyboardsimulator.keyboard;

import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

import javax.swing.Timer;

@SuppressWarnings("deprecation")
public abstract class Area extends Observable {
	protected Rectangle2D.Double area;
	private Block parent;
	private boolean isSelected, isActive, isValidated;

	public Area(){
		parent = null;
	}
	
	public void init() {
		isSelected=false;
		isActive=false;
		isValidated=false;
	}

	public abstract void setLocalChar(List<Character> list);
	public abstract void setChar(List<Character> list);
	public abstract void setWord(List<String> list);
	public abstract void initLayout();
	
	public Rectangle2D.Double getArea(){return area;}
	
	public void setParent(Block p){parent = p;}
	public Area getParent() {return parent;}
	
	public void setSelection(boolean selected){this.isSelected = selected;}
	public boolean isSelected(){ return isSelected;}

	// Input handling
	// =========================================================================

	public void activate(){
		isActive = true;
	}
	public void desactivate(){
		isActive = false;
	}

	public abstract void validate();

	// =========================================================================

	public boolean isActive(){ return isActive;}
	
	public boolean isValidated(){ return isValidated;}
	
	public void sendInfo(String s) {
		setChanged(); 
		notifyObservers(s); 
	}
	
}
