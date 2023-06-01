package fr.irit.elipse.keyboardsimulator.gui;

import fr.irit.elipse.keyboardsimulator.gui.enums.HAlign;
import fr.irit.elipse.keyboardsimulator.gui.enums.VAlign;
import fr.irit.elipse.keyboardsimulator.keyboard.Area;
import fr.irit.elipse.keyboardsimulator.keyboard.Block;
import fr.irit.elipse.keyboardsimulator.keyboard.Key;
import fr.irit.elipse.keyboardsimulator.keyboard.Keyboard;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

public class KeyboardWidget extends JComponent {
    private final Keyboard keyboard;
    private final int keyPadLeft, keyPadRight;

    public KeyboardWidget(Keyboard kb, int keyPadLeft, int keyPadRight) {
        this.keyboard = kb;
        this.keyPadLeft = keyPadLeft;
        this.keyPadRight = keyPadRight;
        setPreferredSize(new Dimension(600, 500));
    }

    public KeyboardWidget(Keyboard kb) {
        this(kb, 1, 1);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        Block root = keyboard.getKeyboardLayout();
        renderBlock(g2, root);
    }

    private void renderBlock(Graphics2D g2, Block block) {
        if(block.getArea() != null) {
            if(block.isActive()){
                g2.setColor(Color.BLUE);
                g2.setStroke(new BasicStroke(2.0f));
            }else{
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1.0f));
            }
            g2.draw(block.getArea());
        }
        for(Area a: block.getChildren())
            if (a.getClass().equals(Block.class)) {
                renderBlock(g2, (Block) a);
            } else if (a.getClass().equals(Key.class)) {
                renderKey(g2, (Key) a);
            }
    }

    private void renderKey(Graphics2D g2, Key key) {
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        if(key.isActive()){
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2.0f));
        }else{
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1.0f));
        }
        g2.draw(key.getArea());
        if(key.getString()!=null)
            paintText(g2, f, key.getArea(), key.getString(), HAlign.CENTER, VAlign.CENTER);
    }

    private void paintText(Graphics2D g2, Font f, Rectangle2D box, String text, HAlign hAlign, VAlign vAlign){
        FontRenderContext frc = g2.getFontRenderContext();
        Rectangle2D bounds = f.getStringBounds(text,frc);
        LineMetrics metrics = f.getLineMetrics(text,frc);
        float width = (float)bounds.getWidth(); // Largeur du texte
        float lineheight = metrics.getHeight(); // Hauteur total de la ligne
        float ascent = metrics.getAscent();     // Du haut à la ligne de base

        int n=1;
        Font fBis = f;
        while(width>(box.getWidth()-keyPadLeft-keyPadRight) || lineheight>box.getHeight()){
            fBis = new Font(f.getName(),f.getStyle(),f.getSize()-n);
            bounds = fBis.getStringBounds(text,frc);
            metrics = fBis.getLineMetrics(text,frc);
            width = (float)bounds.getWidth();
            lineheight = metrics.getHeight();
            ascent = metrics.getAscent();
            n++;
        }

        float x0 = switch (hAlign) {
            case LEFT -> (float) (box.getX()) + keyPadLeft;
            case CENTER -> (float) (box.getX() + (box.getWidth() - width) / 2);
            case RIGHT -> (float) (box.getX() + box.getWidth() - width) - keyPadRight;
        };

        float y0 = switch (vAlign) {
            case TOP -> (float) (box.getY() + lineheight);
            case CENTER -> (float) (box.getY() + (box.getHeight() - lineheight) / 2 + ascent);
            case BOTTOM -> (float) (box.getY() + box.getHeight() - lineheight + ascent);
        };

        g2.setFont(fBis);
        g2.drawString(text,x0,y0);
    }
}
