package fr.irit.elipse.keyboardsimulator.keyboard;

import java.awt.geom.Rectangle2D;
import java.util.List;

public class Key extends Area {
	private final String initialString;
	private String string;
	private int indexLocalCharPred;
	private final int indexCharPred, indexWordPred;
	
	public Key(String s, double x, double y, double w, double h){
		super();
		this.initialString = s;
		this.string = s;
		indexCharPred = -1;
		indexWordPred = -1;
		init();
		area = new Rectangle2D.Double(x, y, w, h);
	}
	
	public Key(String s, int indexLocalCharPred, int indexCharPred, int indexWordPred, double x, double y, double w, double h){
		super();
		this.initialString = s;
		this.string = s;
		this.indexLocalCharPred = indexLocalCharPred;
		this.indexCharPred = indexCharPred;
		this.indexWordPred = indexWordPred;
		init();
		area = new Rectangle2D.Double(x, y, w, h);
	}
	
	public void setLocalChar(List<Character> list){
		if(indexLocalCharPred!=-1) {
			if(list.size()>indexLocalCharPred)
				string = (String.valueOf(list.get(indexLocalCharPred))).toUpperCase();
			else
				string = "";
		}
	}
	
	public void setChar(List<Character> list){
		if(indexCharPred!=-1) {
			if(list.size()>indexCharPred)
				string = (String.valueOf(list.get(indexCharPred))).toUpperCase();
			else
				string = "";
		}
	}

	@Override
	public void setWord(List<String> list) {
		if(indexWordPred!=-1) {
			if(list.size()>indexWordPred)
				string = (String.valueOf(list.get(indexWordPred))).toUpperCase();
			else
				string = "";
		}
	}
	
	public void initLayout() {
		string = initialString;
	}
	
	public String getString(){return string;}

	// Input handling
	// =========================================================================

	@Override
	public void activate(){
		super.activate();
		sendInfo("[A](K)"+getString());
	}
	
	@Override
	public void validate(){
		sendInfo("[V](K)"+getString());
	}

	// =========================================================================

	@Override
	public void sendInfo(String s) {
		Area b = getParent();
		while(b.getParent()!=null)
			b=b.getParent();
		b.sendInfo(s);
	}
}
