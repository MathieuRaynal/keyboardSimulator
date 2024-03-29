package fr.irit.elipse.keyboardsimulator.launcher;

import fr.irit.elipse.keyboardsimulator.GUIKeyboard;
import fr.irit.elipse.keyboardsimulator.Keyboard;
import fr.irit.elipse.keyboardsimulator.eyetracking.FilteredEyeTracker;
import fr.irit.elipse.keyboardsimulator.input.InputHandler;
import fr.irit.elipse.keyboardsimulator.logging.LoggerXML;

import static fr.irit.elipse.keyboardsimulator.KeyboardSimulator.DEFAULT_ACTIVATION_TIME;

public class Recording {

    public static void main(String[] args) {
        try {
            Keyboard kb = new Keyboard(DEFAULT_ACTIVATION_TIME);
            LoggerXML logger = LoggerXML.createInstance(LoggerXML.toTimestampedPath("logs/test-keyboard.xml"));

            FilteredEyeTracker tracker = new FilteredEyeTracker(8);
            InputHandler inputHandler = new InputHandler();

            new GUIKeyboard(kb, inputHandler, tracker, logger);
        } catch (LoggerXML.LoggerCreationException e) {
            System.out.println("Failed to create logger");
        }
    }
}
