package fr.irit.elipse.keyboardsimulator.launcher;

import fr.irit.elipse.keyboardsimulator.GUIKeyboard;
import fr.irit.elipse.keyboardsimulator.keyboard.Keyboard;
import fr.irit.elipse.keyboardsimulator.replay.ReplayTimeline;

import static fr.irit.elipse.keyboardsimulator.KeyboardSimulator.DEFAULT_ACTIVATION_TIME;

public class Replay {

    public static void main(String[] args) throws Exception {
        Keyboard kb = new Keyboard(DEFAULT_ACTIVATION_TIME, null);
        ReplayTimeline timeline = new ReplayTimeline("logs/test-keyboard-202305250825.xml");

        new GUIKeyboard(kb, timeline, timeline, null);
        timeline.runTimeline();
    }
}
