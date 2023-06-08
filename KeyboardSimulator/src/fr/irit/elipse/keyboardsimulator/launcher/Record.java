package fr.irit.elipse.keyboardsimulator.launcher;

import fr.irit.elipse.keyboardsimulator.GUIKeyboard;
import fr.irit.elipse.keyboardsimulator.keyboard.Keyboard;
import fr.irit.elipse.keyboardsimulator.record.FilteredEyeTracker;
import fr.irit.elipse.keyboardsimulator.record.InputHandler;
import fr.irit.elipse.keyboardsimulator.record.LoggerXML;

import javax.xml.transform.TransformerException;

import static fr.irit.elipse.keyboardsimulator.GUIKeyboard.DEFAULT_ACTIVATION_TIME;

public class Record {

    public static void main(String[] args) {
        try {
            LoggerXML logger = LoggerXML.createInstance("logs/test-kb.xml");
            FilteredEyeTracker tracker = new FilteredEyeTracker(logger, 4);
            InputHandler inputHandler = new InputHandler(logger, DEFAULT_ACTIVATION_TIME);

            Keyboard kb = new Keyboard();
            logger.setKeyboardLayout(kb.getLayoutId());

            GUIKeyboard gui = new GUIKeyboard(kb, inputHandler, tracker);
            gui.onClose(() -> {
                try {
                    logger.save_to_xml();
                } catch (TransformerException e) {
                    System.out.println("Failed to write logs to file");
                }
            });
        } catch (LoggerXML.LoggerCreationException e) {
            System.out.println("Failed to create logger");
        }
    }
}
