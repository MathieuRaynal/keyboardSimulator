package fr.irit.elipse.keyboardsimulator.input;

import fr.irit.elipse.keyboardsimulator.GUIKeyboard;
import fr.irit.elipse.keyboardsimulator.interfaces.UserInput;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener, UserInput {

    private GUIKeyboard kb;

    @Override
    public void setKeyboard(GUIKeyboard kb) {
        this.kb = kb;
        kb.getWindow().addKeyListener(this);
    }

    @Override public void keyTyped(KeyEvent e) {
        this.kb.onInput();
    }

    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
