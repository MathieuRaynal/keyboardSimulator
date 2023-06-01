package fr.irit.elipse.keyboardsimulator.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class OverlayWindow extends JFrame {
    private final JPanel container;
    private final JComponent main, overlay;

    public OverlayWindow(String name, JComponent main, JComponent overlay) {
        super(name);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.main = main;
        this.overlay = overlay;
        this.container = createOverlay();
        setContentPane(this.container);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                resizeComponents(getSize());
            }
        });
    }

    public JPanel createOverlay() {
        JPanel container = new JPanel();
        container.setLayout(null);

        container.add(main);
        container.add(overlay);

        container.setOpaque(false);
        overlay.setOpaque(false);

        container.setComponentZOrder(overlay, 0);
        container.setPreferredSize(main.getPreferredSize());

        return container;
    }

    private void resizeComponents(Dimension windowSize) {
        int w = windowSize.width, h = windowSize.height;

        container.setBounds(0, 0, w, h);
        main.setBounds(0, 0, w, h);
        overlay.setBounds(0, 0, w, h);
    }
}
