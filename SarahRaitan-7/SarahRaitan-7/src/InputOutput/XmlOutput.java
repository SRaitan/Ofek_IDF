package InputOutput;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
//38 693 38
public class XmlOutput implements Output {
    File xmlFile;

    public XmlOutput(String path) {
        this.xmlFile = new File(path);
        buildXML();
    }

    private Document buildXML()  {
        Document doc = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return doc;
    }

    @Override
    public void output(String s) {
        Document doc;
        Element eElement = (Element) nNode;
        Element newElement  = doc.createElement("");
        newElement.appendChild(doc.createTextNode(""));
        eElement.appendChild(newElement);
    }
}
