package fr.irit.elipse.keyboardsimulator;

import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

import javax.swing.Timer;

@SuppressWarnings("deprecation")
public abstract class Area extends Observable implements ActionListener{
	private static final int DEFAULT_ACTIVATION_TIME = 100;
	protected Rectangle2D.Double area;
	private int activationTime;
	private Block parent;
	private boolean isSelected, isActive, isValidated;
	protected Timer activeTimer;
	
	public Area(){
		activationTime = DEFAULT_ACTIVATION_TIME;
		activeTimer = new Timer(activationTime, this);
		parent = null;
	}
	
	public void init() {
		isSelected=false;
		isActive=false;
		isValidated=false;
	}
	
	public Rectangle2D.Double getArea(){return area;}
	
	public void setParent(Block p){parent = p;}
	public Area getParent() {return parent;}
	
	public void setSelection(boolean selected){this.isSelected = selected;}
	public boolean isSelected(){ return isSelected;}
	
	public void activate(){
		isActive = true;
		activeTimer.start();
	}
	public void desactivate(){
		isActive = false;
		activeTimer.stop();
	}
	public boolean isActive(){ return isActive;}
	
	public boolean isValidated(){ return isValidated;}
	public abstract void validate();
	
	public void actionPerformed(ActionEvent ae) {
		desactivate();
		((Block)getParent()).getNextChild().activate();
	}
	
	public void sendInfo(String s) {
		setChanged(); 
		notifyObservers(s); 
	}
	
	public abstract void paint(Graphics2D g2);
	
}
