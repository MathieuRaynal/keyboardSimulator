package fr.irit.elipse.keyboardsimulator.record;

import fr.irit.elipse.keyboardsimulator.GUIKeyboard;
import fr.irit.elipse.keyboardsimulator.interfaces.UserInput;
import fr.irit.elipse.keyboardsimulator.keyboard.Keyboard;
import fr.irit.elipse.keyboardsimulator.record.LoggerXML;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

public class InputHandler implements KeyListener, UserInput, Observer, ActionListener {
    private Keyboard kb;
    private final LoggerXML logger;

    private final Timer timer;

    public InputHandler(LoggerXML logger, int activationPeriod) {
        this.logger = logger;
        this.timer = new Timer(activationPeriod, this);
    }

    @Override
    public void setWindow(JFrame window) {
        window.addKeyListener(this);
    }

    @Override
    public void setKeyboard(Keyboard keyboard) {
        this.kb = keyboard;
        this.kb.getKeyboardLayout().addObserver(this);

        this.timer.start();
    }



    // Activation
    // =========================================================================

    @Override
    public void actionPerformed(ActionEvent e) {
        kb.activate();
    }

    // User input
    // =========================================================================

    @Override public void keyTyped(KeyEvent e) {
        kb.validate();
    }

    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    // Logging
    // =========================================================================

    @Override
    public void update(Observable o, Object arg) {
        String s = (String) arg;

        String actionType = s.substring(1, 2);
        String scope = s.substring(4, 5);
        String areaName = s.substring(6);

        logger.logAction(actionType, scope, areaName);
    }
}
