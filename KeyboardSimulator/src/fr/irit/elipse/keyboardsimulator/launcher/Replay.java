package fr.irit.elipse.keyboardsimulator.launcher;

import fr.irit.elipse.keyboardsimulator.GUIKeyboard;
import fr.irit.elipse.keyboardsimulator.keyboard.Keyboard;
import fr.irit.elipse.keyboardsimulator.replay.ReplayTimeline;

import static fr.irit.elipse.keyboardsimulator.KeyboardSimulator.DEFAULT_ACTIVATION_TIME;

public class Replay {

    public static void main(String[] args) throws Exception {
        startReplay("logs/test-kb.xml");
    }

    // =================================================================================================================

    public static void startReplay(String logPath) throws Exception {
        ReplayTimeline timeline = new ReplayTimeline(logPath);
        Keyboard kb = new Keyboard(timeline.getLayoutId());

        new GUIKeyboard(kb, timeline, timeline);
        timeline.runTimeline();
    }

}
