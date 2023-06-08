package fr.irit.elipse.keyboardsimulator.gui;

import fr.irit.elipse.keyboardsimulator.interfaces.EyeTracker;

import javax.swing.*;
import java.awt.*;

public class TobiiWidget extends JPanel {
    public static final int GAZE_SIZE = 50;

    private JFrame window;
    private int eyeX, eyeY;

    public TobiiWidget(EyeTracker tracker) {
        this.eyeX = 0;
        this.eyeY = 0;

        tracker.setTobiiGui(this);
    }

    public void setWindow(JFrame window) {
        this.window = window;
    }

    public void onNewEyePosition(float x, float y) {
        if (window == null) return;

        // Convert from values between 0 and 1 to pixels on the screen
        Dimension dims = Toolkit.getDefaultToolkit().getScreenSize();
        eyeX = (int) (x * dims.getWidth());
        eyeY = (int) (y * dims.getHeight());

        // Adjust reference point to the content pane
        Rectangle bounds = window.getBounds();
        Insets insets = window.getInsets();

        eyeX -= bounds.x + insets.left;
        eyeY -= bounds.y + insets.top;

        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponents(g);

        g.setColor(Color.BLACK);
        g.drawOval(eyeX - GAZE_SIZE/2, eyeY - GAZE_SIZE/2, GAZE_SIZE, GAZE_SIZE);
    }
}
