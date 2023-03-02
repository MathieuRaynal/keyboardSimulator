package fr.irit.elipse.keyboardsimulator;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

public class GUIKeyboard extends JFrame implements KeyListener, Observer{
	private static final int DEFAULT_ACTIVATION_TIME = 1000;
	Keyboard keyboard;
	
	public GUIKeyboard(Keyboard kb){
		super("Keyboard");
		
		keyboard = kb;
		keyboard.getKeyboardLayout().addObserver(this);
		keyboard.getKeyboardLayout().addObserver(keyboard);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLayout(new BorderLayout());
		add(keyboard);
		addKeyListener(this);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		keyboard.validate();
		
	}

	@Override public void keyTyped(KeyEvent e){keyboard.validate();}
	@Override public void keyPressed(KeyEvent e) {}
	@Override public void keyReleased(KeyEvent e) {}

	@Override
	public void update(Observable o, Object arg) {
		String s = (String)arg;
//		System.out.println(s);
		if(s.startsWith("[V](K)")){
//			System.out.print(s.substring(6));
			keyboard.getKeyboardLayout().init();
			keyboard.getKeyboardLayout().activate();
			keyboard.getKeyboardLayout().validate();
		}
		keyboard.repaint();
	}
	
	public static void main(String[] args) {
		
		new GUIKeyboard(new Keyboard(DEFAULT_ACTIVATION_TIME));
	}
}
