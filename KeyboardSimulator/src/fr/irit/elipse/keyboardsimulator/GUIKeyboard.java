package fr.irit.elipse.keyboardsimulator;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

public class GUIKeyboard extends JFrame implements KeyListener, Observer{
	Keyboard keyboard;
	
	public GUIKeyboard(Keyboard kb){
		super("Keyboard");
		
		keyboard = kb;
		keyboard.getKeyboardLayout().addObserver(this);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLayout(new BorderLayout());
		add(keyboard);
		addKeyListener(this);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
	}

	@Override public void keyTyped(KeyEvent e){keyboard.validate();}
	@Override public void keyPressed(KeyEvent e) {}
	@Override public void keyReleased(KeyEvent e) {}

	@Override
	public void update(Observable o, Object arg) {
		String s = (String)arg;
		if(s.startsWith("[A]")) {
//			System.out.println(s.substring(3)+" activé");
		}else{
			System.out.print(s);
			keyboard.getKeyboardLayout().init();
			keyboard.getKeyboardLayout().activate();
			keyboard.getKeyboardLayout().validate();
		}
		keyboard.repaint();
	}
	
	public static void main(String[] args) {
		new GUIKeyboard(new Keyboard());
	}
}
