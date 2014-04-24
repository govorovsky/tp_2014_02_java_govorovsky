package util.sax;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * Created by Andrew Govorovsky on 21.04.14
 */
public class XMLReaderSax {
    private static final SAXParserFactory factory = SAXParserFactory.newInstance();

    public static Object readXML(String path) {
        try {
            SAXParser saxParser = factory.newSAXParser();
            SaxHandler saxHandler = new SaxHandler();
            saxParser.parse(path, saxHandler);
            return saxHandler.getObject();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
