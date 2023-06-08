package fr.irit.elipse.keyboardsimulator;

import fr.irit.elipse.keyboardsimulator.gui.KeyboardWidget;
import fr.irit.elipse.keyboardsimulator.gui.OverlayWindow;
import fr.irit.elipse.keyboardsimulator.gui.TobiiWidget;
import fr.irit.elipse.keyboardsimulator.interfaces.EyeTracker;
import fr.irit.elipse.keyboardsimulator.interfaces.UserInput;
import fr.irit.elipse.keyboardsimulator.keyboard.Keyboard;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

public class GUIKeyboard extends Thread implements Observer {
	public static final int DEFAULT_ACTIVATION_TIME = 1000;
	private final Keyboard keyboard;

	private final OverlayWindow window;
	private final KeyboardWidget kbWidget;
	private final TobiiWidget tobii;

	public interface CloseListener {
		void onClose();
	}
	
	public GUIKeyboard(Keyboard kb, UserInput userInput, EyeTracker tracker) {
		keyboard = kb;
		keyboard.getKeyboardLayout().addObserver(this);
		keyboard.getKeyboardLayout().addObserver(keyboard);

		kbWidget = new KeyboardWidget(kb);
		tobii = new TobiiWidget(tracker);

		window = new OverlayWindow("Keyboard", kbWidget, tobii);
		tobii.setWindow(window);

		userInput.setWindow(window);
		userInput.setKeyboard(keyboard);

		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		kbWidget.repaint();

		this.start();
	}

	public void onClose(CloseListener listener) {
		window.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				e.getWindow().dispose();
				listener.onClose();
			}
		});
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
		kbWidget.repaint();
	}

	public OverlayWindow getWindow() {
		return window;
	}

	public Keyboard getKeyboard() {
		return keyboard;
	}
}
