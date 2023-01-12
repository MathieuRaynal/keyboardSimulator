package fr.irit.elipse.keyboardsimulator;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

public class UtilsPainting {
	private static int paddingLeft = 1;
	private static int paddingRight = 1;

	public static void setPaddingLeft(int pl){paddingLeft = pl;}
	public static void setPaddingRight(int pr){paddingRight = pr;}
	
	public static void paintText(Graphics2D g2, Font f, Rectangle2D box, String text, HAlign hAlign, VAlign vAlign){
		FontRenderContext frc = g2.getFontRenderContext();
		Rectangle2D bounds = f.getStringBounds(text,frc);
		LineMetrics metrics = f.getLineMetrics(text,frc);
		float width = (float)bounds.getWidth(); // Largeur du texte
		float lineheight = metrics.getHeight(); // Hauteur total de la ligne
		float ascent = metrics.getAscent();     // Du haut à la ligne de base
		
		int n=1;
		Font fBis = f;
		while(width>(box.getWidth()-paddingLeft-paddingRight) || lineheight>box.getHeight()){
			fBis = new Font(f.getName(),f.getStyle(),f.getSize()-n);
			bounds = fBis.getStringBounds(text,frc);
			metrics = fBis.getLineMetrics(text,frc);
			width = (float)bounds.getWidth();
			lineheight = metrics.getHeight();
			ascent = metrics.getAscent();
			n++;
		}
		
		float x0 = 0;
		switch (hAlign) {
			case LEFT:
				x0 = (float)(box.getX())+paddingLeft;
			break;
			case CENTER:
				x0 = (float)(box.getX() + (box.getWidth() - width)/2);
			break;
			case RIGHT:
				x0 = (float)(box.getX() + box.getWidth() - width)-paddingRight;
			break;
		}
		float y0 = 0;
		switch (vAlign) {
			case TOP:
				y0 = (float)(box.getY() + lineheight);
			break;
			case CENTER:
				y0 = (float)(box.getY() + (box.getHeight() - lineheight)/2 + ascent);
			break;
			case BOTTOM:
				y0 = (float)(box.getY() + box.getHeight() - lineheight + ascent);
			break;
		}
		g2.setFont(fBis);
		g2.drawString(text,x0,y0);
	}
	
	
}
