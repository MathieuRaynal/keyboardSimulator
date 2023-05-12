package fr.irit.elipse.keyboardsimulator.eyetracking;

import tobii.Tobii;
import java.awt.geom.Point2D;

public class FilteredEyeTracker {
    private float xMean, yMean;
    private final int size;

    public FilteredEyeTracker(int bufferSize) {
        xMean = yMean = 0;
        size = bufferSize;
    }

    public Point2D getEyePosition() {
        float[] position = Tobii.gazePosition();

        xMean = approxRollingAverage(xMean, position[0]);
        yMean = approxRollingAverage(yMean, position[1]);

        return new Point2D.Float(xMean, yMean);
    }

    private float approxRollingAverage (float avg, float new_sample) {
        avg -= avg / size;
        avg += new_sample / size;

        return avg;
    }
}
