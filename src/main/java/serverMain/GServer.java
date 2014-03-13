package serverMain;

import frontend.Frontend;
import org.eclipse.jetty.rewrite.handler.RedirectRegexRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

/**
 * Created by Andrew Govorovsky on 15.02.14
 */
public class GServer {
    Server server;
    private int port;

    public GServer(int port) {
        this.port = port;
    }


    public void start() throws Exception {
        server = new Server(port);
        server.setHandler(initHandlers());
        server.start();
        server.join();
    }


    private HandlerList initHandlers() {
        Servlet frontend = new Frontend();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(frontend), "/*"); /* servlet for all URL */

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("static");

        RewriteHandler rewriteHandler = new RewriteHandler();
        rewriteHandler.setRewriteRequestURI(true);
        rewriteHandler.setRewritePathInfo(true);
        rewriteHandler.setOriginalPathAttribute("requestedPath");
        RedirectRegexRule rule = new RedirectRegexRule();
        rule.setRegex("/");
        rule.setReplacement("/index");
        rewriteHandler.addRule(rule);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{rewriteHandler, resource_handler, context});
        return handlers;
    }

    public static void main(String[] args) throws Exception {
        GServer gserver = new GServer(8080);
        gserver.start();

    }

}
