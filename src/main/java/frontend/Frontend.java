package frontend;

import db.AccountService;
import exceptions.AccountServiceException;
import exceptions.EmptyDataException;
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
 * Created by Andrew Govorovsky on 15.02.14
 */
public class Frontend extends HttpServlet {
    private AtomicLong userIdGenerator = new AtomicLong(0);
    public AccountService ac;

    public Frontend() {
        ac = new AccountService();
    }

    private void sendResponse(HttpServletResponse resp, String resultPage, Map<String, Object> variables) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(PageGenerator.getPage(resultPage, variables));
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        Long userId = (Long) request.getSession().getAttribute("userId");
        switch (request.getPathInfo()) {
            case Pages.MAIN_PAGE:
                if (userId != null) {
                    pageVariables.put("userId", userId);
                    sendResponse(response, Templates.USER_TPL, pageVariables);
                } else {
                    sendResponse(response, Templates.MAIN_TPL, pageVariables);
                }
                break;

            case Pages.AUTH_PAGE:
                sendResponse(response, Templates.AUTH_TPL, pageVariables);
                break;

            case Pages.REG_PAGE:
                sendResponse(response, Templates.REGISTER_TPL, pageVariables);
                break;

            case Pages.TIMER_PAGE:
                if (userId != null) {
                    pageVariables.put("time", new Date().toString());
                    pageVariables.put("userId", userId);
                    pageVariables.put("refreshPeriod", "1000");
                    sendResponse(response, Templates.TIMER_TPL, pageVariables);
                } else {
                    response.sendRedirect(Pages.MAIN_PAGE);
                }
                break;

            case Pages.QUIT_PAGE:
                request.getSession().invalidate();
                response.sendRedirect(Pages.MAIN_PAGE);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }

    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        switch (request.getPathInfo()) {
            case Pages.AUTH_PAGE:
                doAuthenticate(request, response);
                break;
            case Pages.REG_PAGE:
                doRegister(request, response);
                break;
            default:
                response.sendRedirect(Pages.MAIN_PAGE);
                break;
        }
    }

    private void doAuthenticate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        Map<String, Object> pageVariables = new HashMap<>();

        try {
            ac.checkLoginPassword(login, password);
            ac.authenticate(login, password);
            request.getSession().setAttribute("userId", userIdGenerator.getAndIncrement());
            response.sendRedirect(Pages.TIMER_PAGE);
        } catch (EmptyDataException | AccountServiceException e) {
            pageVariables.put("errorMsg", e.getMessage());
            sendResponse(response, Templates.AUTH_TPL, pageVariables);
        }
    }

    private void doRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        Map<String, Object> pageVariables = new HashMap<>();

        try {
            ac.checkLoginPassword(login, password);
            ac.register(login, password);
            request.getSession().setAttribute("userId", userIdGenerator.getAndIncrement());
            response.sendRedirect(Pages.TIMER_PAGE);
        } catch (EmptyDataException | AccountServiceException e) {
            pageVariables.put("errorMsg", e.getMessage());
            sendResponse(response, Templates.REGISTER_TPL, pageVariables);
        }
    }
}
