package fr.irit.elipse.keyboardsimulator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Key extends Area{
	private String initialString, string;
	private int indexLocalCharPred, indexCharPred, indexWordPred;
	
	public Key(int activationTime, String s, double x, double y, double w, double h){
		super(activationTime);
		this.initialString = s;
		this.string = s;
		indexCharPred = -1;
		indexWordPred = -1;
		init();
		area = new Rectangle2D.Double(x, y, w, h);
	}
	
	public Key(int activationTime, String s, int indexLocalCharPred, int indexCharPred, int indexWordPred, double x, double y, double w, double h){
		super(activationTime);
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

	@Override
	public void activate(){
		super.activate();
		sendInfo("[A](K)"+getString());
	}
	
	@Override
	public void validate(){
		desactivate();
		sendInfo("[V](K)"+getString());
	}

	@Override
	public void sendInfo(String s) {
		Area b = getParent();
		while(b.getParent()!=null)
			b=b.getParent();
		b.sendInfo(s);
	}
	
	public void paint(Graphics2D g2){
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		if(isActive()){
			g2.setColor(Color.BLUE);
			g2.setStroke(new BasicStroke(2.0f));
		}else{
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(1.0f));
		}
		g2.draw(area);
		if(string!=null)
			UtilsPainting.paintText(g2, f, area, string, HAlign.CENTER, VAlign.CENTER);
	}
}
