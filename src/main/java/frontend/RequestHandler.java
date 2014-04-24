package frontend;

import templater.PageGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew Govorovsky on 24.04.14
 * Command pattern to handle GET requests
 */

interface Page {
    void render(HttpServletRequest request, HttpServletResponse response, UserSession session, Map<String, Object> pageVariables, Map<String, UserSession> sessions) throws IOException;
}

class MainPage implements Page {
    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, UserSession session, Map<String, Object> pageVariables, Map<String, UserSession> sessions) throws IOException {
        Long userId = (session != null) ? session.getId() : null;
        if (userId != null) {
            pageVariables.put("userId", userId);
            pageVariables.put("userName", session.getName());
            RequestHandler.sendResponse(response, Templates.USER_TPL, pageVariables);
        } else {
            RequestHandler.sendResponse(response, Templates.MAIN_TPL, pageVariables);
        }
    }
}

class WaitingPage implements Page {
    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, UserSession session, Map<String, Object> pageVariables, Map<String, UserSession> sessions) throws IOException {
        String isWaiting = "false";
        if (session != null) {
            switch (session.getStatus()) {
                case WAIT_AUTH:
                case WAIT_USER_REG:
                    isWaiting = "true";
                    break;
                case AUTHORIZED:
                    response.sendRedirect(Pages.TIMER_PAGE);
                    return;
            }
        }
        pageVariables.put("waiting", isWaiting);
        RequestHandler.sendResponse(response, request.getPathInfo().equals(Pages.AUTH_PAGE) ? Templates.AUTH_TPL : Templates.REGISTER_TPL, pageVariables);
    }
}

class TimerPage implements Page {
    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, UserSession session, Map<String, Object> pageVariables, Map<String, UserSession> sessions) throws IOException {
        Long userId = (session != null) ? session.getId() : null;
        if (userId != null) {
            pageVariables.put("time", new Date().toString());
            pageVariables.put("userId", userId);
            pageVariables.put("refreshPeriod", "1000");
            RequestHandler.sendResponse(response, Templates.TIMER_TPL, pageVariables);
        } else {
            response.sendRedirect(Pages.MAIN_PAGE);
        }
    }
}

class QuitPage implements Page {
    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, UserSession session, Map<String, Object> pageVariables, Map<String, UserSession> sessions) throws IOException {
        sessions.remove(session.getSsid());
        request.getSession().invalidate();
        response.sendRedirect(Pages.MAIN_PAGE);
    }
}

public class RequestHandler {
    private Map<String, Page> pageMap = new HashMap<>();

    static void sendResponse(HttpServletResponse resp, String resultPage, Map<String, Object> variables) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(PageGenerator.getPage(resultPage, variables));
    }

    public RequestHandler() {
        Page waitingPage = new WaitingPage();
        pageMap.put(Pages.MAIN_PAGE, new MainPage());
        pageMap.put(Pages.AUTH_PAGE, waitingPage);
        pageMap.put(Pages.REG_PAGE, waitingPage);
        pageMap.put(Pages.TIMER_PAGE, new TimerPage());
        pageMap.put(Pages.QUIT_PAGE, new QuitPage());
    }

    public void handle(HttpServletRequest request, HttpServletResponse resp, UserSession session, Map<String, Object> pageVariables, Map<String, UserSession> sessions) throws IOException {
        Page page = pageMap.get(request.getPathInfo());
        if (page != null) page.render(request, resp, session, pageVariables, sessions);
        else resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
