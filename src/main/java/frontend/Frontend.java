package frontend;

import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by andrew on 15.02.14.
 */
public class Frontend extends HttpServlet {
    private static final Map<String, String> users;
    private AtomicLong userIdGenerator = new AtomicLong(0);

    /* hardcoded users */
    static {
        users = new HashMap<>();
        users.put("andrew", "123");
        users.put("test", "345");
    }

    private void sendOkResponse(HttpServletResponse resp, String resultPage, Map<String, Object> variables) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(PageGenerator.getPage(resultPage, variables));
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String resultPage;
        Long userId = (Long) request.getSession().getAttribute("userId");

        switch (request.getPathInfo()) {
            case "/index":
                if (userId != null) {
                    resultPage = "index.tpl";
                    pageVariables.put("userId", userId);
                    sendOkResponse(response, resultPage, pageVariables);
                } else {
                    response.sendRedirect("/auth");
                }
                break;

            case "/auth":
                resultPage = "auth.tpl";
                sendOkResponse(response, resultPage, pageVariables);
                break;

            case "/timer":
                if (userId != null) {
                    pageVariables.put("time", new Date().toString());
                    pageVariables.put("userId", userId);
                    pageVariables.put("refreshPeriod", "1000");
                    resultPage = "timer.tpl";
                    sendOkResponse(response, resultPage, pageVariables);
                } else {
                    response.sendRedirect("/auth");
                }
                break;

            case "/quit":
                request.getSession().setAttribute("userId", null);
                response.sendRedirect("/index");
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        Map<String, Object> pageVariables = new HashMap<>();
        String authPage = "/auth.tpl";

        if (users.containsKey(login)) {
            if (users.get(login).equals(password)) {
                request.getSession().setAttribute("userId", userIdGenerator.getAndIncrement());
                response.sendRedirect("/timer");
                return;
            }
        }
        pageVariables.put("errorMsg", "User or password invalid!");
        sendOkResponse(response, authPage, pageVariables);
    }
}
