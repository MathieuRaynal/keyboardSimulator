package fr.irit.elipse.keyboardsimulator.eyetracking;

import fr.irit.elipse.keyboardsimulator.logging.LoggerXML;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class TobiiGUI extends JPanel {
    public static final int GAZE_SIZE = 50;

    private final FilteredEyeTracker tracker;
    private final LoggerXML logger;

    private int eyeX, eyeY;

    public TobiiGUI(FilteredEyeTracker tracker, LoggerXML logger) {
        this.tracker = tracker;
        this.eyeX = 0;
        this.eyeY = 0;
        this.logger = logger;
    }

    public void tick(JFrame window) {
        Point2D eyePos = tracker.getEyePosition();
        logger.logEyePosition(eyePos);

        // Convert from values between 0 and 1 to pixels on the screen
        Dimension dims = Toolkit.getDefaultToolkit().getScreenSize();
        eyeX = (int) (eyePos.getX() * dims.getWidth());
        eyeY = (int) (eyePos.getY() * dims.getHeight());

        // Adjust reference point to the content pane
        Rectangle bounds = window.getBounds();
        Insets insets = window.getInsets();

        eyeX -= bounds.x + insets.left;
        eyeY -= bounds.y + insets.top;
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponents(g);

        g.setColor(Color.BLACK);
        g.drawOval(eyeX - GAZE_SIZE/2, eyeY - GAZE_SIZE/2, GAZE_SIZE, GAZE_SIZE);
    }
}
