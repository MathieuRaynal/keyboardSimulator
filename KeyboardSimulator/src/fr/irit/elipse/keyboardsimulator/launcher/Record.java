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
        /* For the log path, you can input a definitive path or use the following function to
         * add a timestamp to a path :
         *
         * LoggerXML.toTimestampedPath("logs/test-kb.xml") -> "logs/test-kb-202307181411.xml"
         */

        startRecording(
                "logs/test-kb.xml",
                DEFAULT_ACTIVATION_TIME
        );
    }

    // =================================================================================================================

    public static void startRecording(String logPath, int activationTime) {
        try {
            LoggerXML logger = LoggerXML.createInstance(logPath);
            FilteredEyeTracker tracker = new FilteredEyeTracker(logger, 4);
            InputHandler inputHandler = new InputHandler(logger, activationTime);

            Keyboard kb = new Keyboard();
            logger.setKeyboardLayout(kb.getLayoutId());

            GUIKeyboard gui = new GUIKeyboard(kb, inputHandler, tracker);
            gui.onClose(() -> {
                try {
                    logger.save_to_xml();
                    System.out.println("[INFO] Saved logs to " + logPath);
                } catch (TransformerException e) {
                    System.out.println("[ERROR] Failed to write logs to file");
                }
            });
        } catch (LoggerXML.LoggerCreationException e) {
            System.out.println("[ERROR] Failed to create logger");
        }
    }
}
