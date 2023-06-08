package fr.irit.elipse.keyboardsimulator.record;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerXML {

    public static class LoggerCreationException extends Exception {
        public LoggerCreationException(String desc) {
            super(desc);
        }
    }

    private final String path;
    private final long startTime;

    private final Document doc;
    private final Element root;
    private final Element events;

    private final Transformer transformer;

    private LoggerXML(Document doc, Transformer transformer, String path) {
        this.path = path;
        this.doc = doc;
        this.transformer = transformer;

        this.root = doc.createElement("recording");
        root.setAttribute("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        root.setAttribute("time", new SimpleDateFormat("hh:mm").format(new Date()));
        this.doc.appendChild(this.root);

        this.events = doc.createElement("events");
        this.root.appendChild(this.events);

        this.startTime = System.currentTimeMillis();
    }

    public static LoggerXML createInstance(String path) throws LoggerCreationException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            return new LoggerXML(db.newDocument(), transformer, path);
        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            throw new LoggerCreationException("Failed to create Logger");
        }
    }

    public void setKeyboardLayout(String layout) {
        Element layoutTag = doc.createElement("layout");
        layoutTag.setTextContent(layout);

        root.insertBefore(layoutTag, events);
    }

    public void log(String entryType, String value) {
        long t = System.currentTimeMillis() - startTime;

        Element event = doc.createElement("event");
        event.setAttribute("t", String.valueOf(t));
        event.setAttribute("type", entryType);
        event.setTextContent(value);

        events.appendChild(event);
    }

    public void save_to_xml() throws TransformerException {
        // for pretty print
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);

        // write to console or file
        //StreamResult console = new StreamResult(System.out);
        StreamResult file = new StreamResult(new File(path));

        // write data
        //transformer.transform(source, console);
        transformer.transform(source, file);
    }

    // Util functions
    // =========================================================================

    public static String toTimestampedPath(String path) {
        String[] parts = path.split("\\.");
        String timestamp = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());

        return parts[0] + "-" + timestamp + "." + parts[1];
    }

    // Custom functions
    // ========================================================================
    public void logEyePosition(float x, float y) {
        String text = x + ":" + y;
        log("eyePosition", text);
    }

    public void logAction(String actionType, String scope, String areaName) {
        String text = actionType + ":" + scope + ":" + areaName;
        log("action", text);
    }
}
