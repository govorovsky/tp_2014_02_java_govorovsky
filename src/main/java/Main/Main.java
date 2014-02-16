package Main;

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
 * Created by andrew on 15.02.14.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Servlet frontend = new Frontend();
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(frontend), "/*"); /* сервлет для всех URL */
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("static"); /* Dir with static files */

        RewriteHandler rewriteHandler = new RewriteHandler();
        rewriteHandler.setRewriteRequestURI(true);
        rewriteHandler.setRewritePathInfo(true);
        rewriteHandler.setOriginalPathAttribute("requestedPath");
        RedirectRegexRule rule = new RedirectRegexRule();
        rule.setRegex("/");
        rule.setReplacement("/index");
        rewriteHandler.addRule(rule);


        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{rewriteHandler, resource_handler, context}); /* The resource handler is passed the request first and looks for a matching file in the local directory to serve.
        If it does not find a file, the request passes to the default handler */
        server.setHandler(handlers);

        server.start();
        server.join();
    }

}
