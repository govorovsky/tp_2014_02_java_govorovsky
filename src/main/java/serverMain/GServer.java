package serverMain;

import db.AccountService;
import db.AccountServiceImpl;
import frontend.Frontend;
import messageSystem.MessageSystem;
import org.eclipse.jetty.rewrite.handler.RedirectRegexRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


/**
 * Created by Andrew Govorovsky on 15.02.14
 */
public class GServer {
    private final Server server;

    public GServer(int port) {
        server = new Server(port);
        server.setHandler(initHandlers());
    }

    public void start() throws Exception {
        server.start();
    }

    public void join() throws Exception {
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }


    private HandlerList initHandlers() {
        MessageSystem messageSystem = new MessageSystem();
        Frontend frontend = new Frontend(messageSystem);
        AccountService accountService1 = new AccountServiceImpl(messageSystem);
        AccountService accountService2 = new AccountServiceImpl(messageSystem);

        (new Thread(frontend)).start();
        (new Thread(accountService1)).start();
        (new Thread(accountService2)).start();

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
