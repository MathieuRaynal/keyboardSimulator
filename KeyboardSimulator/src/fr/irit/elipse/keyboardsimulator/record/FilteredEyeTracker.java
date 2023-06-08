package fr.irit.elipse.keyboardsimulator.record;

import fr.irit.elipse.keyboardsimulator.LoggerCSV;
import fr.irit.elipse.keyboardsimulator.gui.TobiiWidget;
import fr.irit.elipse.keyboardsimulator.interfaces.EyeTracker;
import tobii.Tobii;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FilteredEyeTracker implements EyeTracker, ActionListener {
    private float xMean, yMean;
    private final int size;
    private TobiiWidget gui;

    private final LoggerXML logger;
    private final Timer timer;

    public FilteredEyeTracker(LoggerXML logger, int bufferSize) {
        xMean = yMean = 0;
        size = bufferSize;
        this.logger = logger;

        timer = new Timer(20, this);
        timer.start();
    }

    private float approxRollingAverage (float avg, float new_sample) {
        avg -= avg / size;
        avg += new_sample / size;

        return avg;
    }

    @Override
    public void setTobiiGui(TobiiWidget gui) {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        float[] position = Tobii.gazePosition();

        xMean = approxRollingAverage(xMean, position[0]);
        yMean = approxRollingAverage(yMean, position[1]);

        logger.logEyePosition(xMean, yMean);
        if (gui != null) gui.onNewEyePosition(xMean, yMean);
    }
}
