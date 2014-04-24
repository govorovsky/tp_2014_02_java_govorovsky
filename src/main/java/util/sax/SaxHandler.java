package util.sax;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import util.ReflectionHelper;

/**
 * Created by Andrew Govorovsky on 21.04.14
 */
public class SaxHandler extends DefaultHandler {
    private String element;
    private Object object;
    private static final String CLASSNAME = "class";

    @Override
    public void endElement(String uri, String localName, String qName) {
        element = null;
    }

    @Override
    public void endDocument() {
        System.out.println("Ends doc...");
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (element != null) {
            String value = new String(ch, start, length);
            ReflectionHelper.setFieldValue(object, element, value);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (!qName.equals(CLASSNAME)) {
            element = qName;
        } else {
            String className = attributes.getValue(0); /* get name of the class */
            System.out.println("Found class:" + className);
            object = ReflectionHelper.createInstance(className);
        }
    }

    @Override
    public void startDocument() {
        System.out.println("Starts doc...");
    }

    public Object getObject() {
        return object;
    }

}
