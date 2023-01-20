package fr.irit.elipse.keyboardsimulator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Key extends Area{
	private String string;
	
	public Key(String s, double x, double y, double w, double h){
		this.string = s;
		init();
		area = new Rectangle2D.Double(x, y, w, h);
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
		UtilsPainting.paintText(g2, f, area, string, HAlign.CENTER, VAlign.CENTER);
	}
}
