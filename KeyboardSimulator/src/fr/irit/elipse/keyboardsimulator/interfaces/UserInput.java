package fr.irit.elipse.keyboardsimulator.interfaces;

import fr.irit.elipse.keyboardsimulator.keyboard.Keyboard;

import javax.swing.*;

public interface UserInput {
    void setWindow(JFrame window);
    void setKeyboard(Keyboard keyboard);
}
