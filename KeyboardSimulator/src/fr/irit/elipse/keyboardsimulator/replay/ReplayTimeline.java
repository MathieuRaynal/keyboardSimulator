package fr.irit.elipse.keyboardsimulator.replay;

import fr.irit.elipse.keyboardsimulator.GUIKeyboard;
import fr.irit.elipse.keyboardsimulator.eyetracking.TobiiGUI;
import fr.irit.elipse.keyboardsimulator.interfaces.EyeTracker;
import fr.irit.elipse.keyboardsimulator.interfaces.UserInput;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class ReplayTimeline implements UserInput, EyeTracker {

    private record Event(String type, Object value) {}

    private final Document doc;
    private final TreeMap<Long, Event> events;

    private TobiiGUI tobii;
    private GUIKeyboard kb;

    public ReplayTimeline(String path) throws Exception {
        this.doc = loadDocument(path);
        this.events = new TreeMap<>();

        parseEntries();
    }

    public Document loadDocument(String path) throws IOException, ParserConfigurationException, SAXException {
        File inputFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        return doc;
    }

    private void parseEntries() {
        Map<Long, Event> unsortedEvents = new HashMap<>();

        NodeList entries = doc.getElementsByTagName("entry");
        for(int i = 0; i < entries.getLength(); i++) {
            Node node = entries.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE) continue;

            Element entry = (Element) node;
            long t = Long.parseLong(entry.getAttribute("t"));
            String type = entry.getAttribute("type");
            String value = entry.getTextContent();

            if (type.equals("eyePosition")) {
                unsortedEvents.put(t, new Event("eyePosition", parseEyePosition(value)));
            }
        }

        events.putAll(unsortedEvents);
    }

    private Point2D parseEyePosition(String value) {
        String[] values = value.split(":");
        float x = Float.parseFloat(values[0]);
        float y = Float.parseFloat(values[1]);
        return new Point2D.Float(x, y);
    }

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
        }
    }

    @Override
    public void setGUI(TobiiGUI gui) {
        this.tobii = gui;
    }

    @Override
    public void setKeyboard(GUIKeyboard kb) {
        this.kb = kb;
    }
}
