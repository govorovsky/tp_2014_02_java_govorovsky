package templater;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Created by Andrew Govorovsky on 15.02.14 {18:51}.
 */
public class PageGenerator {
    private static final String HTML_DIR = "templates";
    private static final Configuration CONFIG = new Configuration();

    public static String getPage(String filename, Map<String, Object> data) {
        Writer writer = new StringWriter();
        try {
            Template template = CONFIG.getTemplate(HTML_DIR + File.separator + filename);
            template.process(data, writer);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
}
