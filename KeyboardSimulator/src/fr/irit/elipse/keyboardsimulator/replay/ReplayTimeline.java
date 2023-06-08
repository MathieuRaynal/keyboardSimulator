package fr.irit.elipse.keyboardsimulator.replay;

import fr.irit.elipse.keyboardsimulator.GUIKeyboard;
import fr.irit.elipse.keyboardsimulator.gui.TobiiWidget;
import fr.irit.elipse.keyboardsimulator.interfaces.EyeTracker;
import fr.irit.elipse.keyboardsimulator.interfaces.UserInput;
import fr.irit.elipse.keyboardsimulator.keyboard.Keyboard;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class ReplayTimeline implements UserInput, EyeTracker {

    private record Event(String type, Object value) {}
    private record ActionEvent(String actionType, String scope, String areaName) {}

    private final Document doc;
    private final TreeMap<Long, Event> events;
    private final String layoutId;

    private TobiiWidget tobii;
    private Keyboard kb;

    public ReplayTimeline(String path) throws Exception {
        this.doc = loadDocument(path);
        this.events = new TreeMap<>();

        layoutId = doc.getElementsByTagName("layout").item(0).getTextContent();

        parseEvents();
    }

    public Document loadDocument(String path) throws IOException, ParserConfigurationException, SAXException {
        File inputFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        return doc;
    }

    // Event main parsing
    // =========================================================================

    private void parseEvents() {
        Map<Long, Event> unsortedEvents = new HashMap<>();

        NodeList events = doc.getElementsByTagName("event");
        for(int i = 0; i < events.getLength(); i++) {
            Node node = events.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE) continue;

            Element event = (Element) node;
            long t = Long.parseLong(event.getAttribute("t"));
            String type = event.getAttribute("type");
            String value = event.getTextContent();

            Object obj = switch (type) {
                case "eyePosition" -> parseEyePosition(value);
                case "action"      -> parseAction(value);
                default            -> null;
            };

            if (obj != null) unsortedEvents.put(t, new Event(type, obj));
        }

        this.events.putAll(unsortedEvents);
    }

    public String getLayoutId() {
        return layoutId;
    }

    // Parsers
    // =========================================================================

    private Point2D parseEyePosition(String value) {
        String[] values = value.split(":");
        float x = Float.parseFloat(values[0]);
        float y = Float.parseFloat(values[1]);
        return new Point2D.Float(x, y);
    }

    private Object parseAction(String value) {
        String[] parts = value.split(":");
        return new ActionEvent(parts[0], parts[1], parts[2]);
    }

    // Main runner
    // =========================================================================

    public void runTimeline() throws InterruptedException {
        long lastT = 0;

        for (long t : events.keySet()) {
            Thread.sleep(t - lastT);
            lastT = t;

            System.out.println("T: " + t);
            Event event = events.get(t);

            if (event.type.equals("eyePosition")) {
                Point2D eyePos = (Point2D) event.value;
                tobii.onNewEyePosition((float) eyePos.getX(), (float) eyePos.getY());
            }

            if (event.type.equals("action")) {
                ActionEvent action = (ActionEvent) event.value;
                switch (action.actionType) {
                    case "A" -> kb.activate();
                    case "V" -> kb.validate();
                }
            }
        }
    }

    // Add widgets to be controlled
    // =========================================================================

    @Override
    public void setTobiiGui(TobiiWidget gui) {
        this.tobii = gui;
    }

    @Override
    public void setWindow(JFrame window) {}

    @Override
    public void setKeyboard(Keyboard keyboard) {
        this.kb = keyboard;
    }
}
