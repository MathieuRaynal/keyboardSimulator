package fr.irit.elipse.keyboardsimulator;

import fr.irit.elipse.keyboardsimulator.eyetracking.FilteredEyeTracker;
import fr.irit.elipse.keyboardsimulator.eyetracking.TobiiGUI;
import fr.irit.elipse.keyboardsimulator.input.InputHandler;
import fr.irit.elipse.keyboardsimulator.interfaces.EyeTracker;
import fr.irit.elipse.keyboardsimulator.interfaces.UserInput;
import fr.irit.elipse.keyboardsimulator.logging.LoggerCSV;
import fr.irit.elipse.keyboardsimulator.logging.LoggerXML;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.xml.transform.TransformerException;

public class GUIKeyboard extends Thread implements Observer {
	private static final int DEFAULT_ACTIVATION_TIME = 1000;
	private final Keyboard keyboard;
	private final TobiiGUI tobii;
	private final OverlayWindow window;
	private final LoggerXML logger;
	
	public GUIKeyboard(Keyboard kb, UserInput userInput, EyeTracker tracker, LoggerXML log) {
		logger = log;

		keyboard = kb;
		keyboard.getKeyboardLayout().addObserver(this);
		keyboard.getKeyboardLayout().addObserver(keyboard);

		tobii = new TobiiGUI(tracker, logger);

		window = new OverlayWindow("Keyboard", kb, tobii);
		tobii.setWindow(window);

		userInput.setKeyboard(this);

		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		keyboard.validate();
		tobii.validate();

		window.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				e.getWindow().dispose();
				System.out.println("Closed");
				try {
					logger.save_to_xml();
				} catch (TransformerException ignored) {
					System.out.println("Failed to write logs to file");
				}
			}
		});

		this.start();
	}

	public void onInput() {
		keyboard.validate();
		tobii.validate();
	}

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

	public OverlayWindow getWindow() {
		return window;
	}
}
