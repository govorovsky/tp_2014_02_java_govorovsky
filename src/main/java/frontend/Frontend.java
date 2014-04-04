package frontend;

import db.AccountServiceImpl;
import db.AccountServiceMessages;
import exceptions.ExceptionMessages;
import messageSystem.*;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Andrew Govorovsky on 15.02.14
 */
public class Frontend extends HttpServlet implements Abonent, Runnable {
    private final Map<String, UserSession> sessions = new ConcurrentHashMap<>();
    private final MessageSystem messageSystem;
    private final Address address = new Address();

    public Frontend(MessageSystem ms) {
        this.messageSystem = ms;
        messageSystem.addService(this);
        messageSystem.getAddressService().addAddress(this);
    }

    private void sendResponse(HttpServletResponse resp, String resultPage, Map<String, Object> variables) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(PageGenerator.getPage(resultPage, variables));
    }

    private void getWaitingPage(HttpServletResponse resp, UserSession session, Map<String, Object> pageVariables, String pageTemplate) throws ServletException, IOException {
        String isWaiting = "false";
        if (session != null) {
            switch (session.getStatus()) {
                case AccountServiceMessages.WAIT_AUTH:
                case AccountServiceMessages.WAIT_USER_REG:
                    isWaiting = "true";
                    break;
                case AccountServiceMessages.AUTHORIZED:
                    resp.sendRedirect(Pages.TIMER_PAGE);
                    return;
            }
        }
        pageVariables.put("waiting", isWaiting);
        sendResponse(resp, pageTemplate, pageVariables);
    }

    private void getMainPage(HttpServletResponse response, UserSession session, Map<String, Object> pageVariables) throws ServletException, IOException {
        Long userId = (session != null) ? session.getId() : null;
        if (userId != null) {
            pageVariables.put("userId", userId);
            pageVariables.put("userName", session.getName());
            sendResponse(response, Templates.USER_TPL, pageVariables);
        } else {
            sendResponse(response, Templates.MAIN_TPL, pageVariables);
        }
    }


    private void getTimerPage(HttpServletResponse response, UserSession session, Map<String, Object> pageVariables) throws ServletException, IOException {
        Long userId = (session != null) ? session.getId() : null;
        if (userId != null) {
            pageVariables.put("time", new Date().toString());
            pageVariables.put("userId", userId);
            pageVariables.put("refreshPeriod", "1000");
            sendResponse(response, Templates.TIMER_TPL, pageVariables);
        } else {
            response.sendRedirect(Pages.MAIN_PAGE);
        }
    }

    private void checkSessionState(UserSession session) {
        if (session.getStatus().equals(AccountServiceMessages.AUTHORIZED) || session.getStatus().equals(AccountServiceMessages.USER_ADDED)) {
            session.stopWaiting();
        }
        if (session.elapsedTime() > UserSession.MAX_WAITING) {
            session.setStatus(ExceptionMessages.SQL_ERROR);
            session.stopWaiting();
        }
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        HttpSession httpSession = request.getSession();
        UserSession session = sessions.get(httpSession.getId());

        if (session != null) {
            checkSessionState(session);
            parseStatus(session.getStatus(), pageVariables);
        }

        switch (request.getPathInfo()) {
            case Pages.MAIN_PAGE:
                getMainPage(response, session, pageVariables);
                break;

            case Pages.AUTH_PAGE:
                getWaitingPage(response, session, pageVariables, Templates.AUTH_TPL);
                break;

            case Pages.REG_PAGE:
                getWaitingPage(response, session, pageVariables, Templates.REGISTER_TPL);
                break;

            case Pages.TIMER_PAGE:
                getTimerPage(response, session, pageVariables);
                break;

            case Pages.QUIT_PAGE:
                sessions.remove(httpSession.getId());
                httpSession.invalidate();
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

    private void doAuthenticate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserSession newSession = new UserSession(username, request.getSession().getId(), AccountServiceMessages.WAIT_AUTH);
        newSession.startWaiting();
        sessions.put(request.getSession().getId(), newSession);
        messageSystem.sendMessage(new MsgLogin(getAddress(), messageSystem.getAddressService().getAddress(AccountServiceImpl.class), username, password, newSession.getSsid()));
        response.sendRedirect(Pages.AUTH_PAGE);
    }

    private void doRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserSession newSession = new UserSession(username, request.getSession().getId(), AccountServiceMessages.WAIT_USER_REG);
        newSession.startWaiting();
        sessions.put(request.getSession().getId(), newSession);
        messageSystem.sendMessage(new MsgRegister(getAddress(), messageSystem.getAddressService().getAddress(AccountServiceImpl.class), username, password, newSession.getSsid()));
        response.sendRedirect(Pages.REG_PAGE);
    }

    private void parseStatus(String status, Map<String, Object> pageVariables) {
        switch (status) {
            case ExceptionMessages.EMPTY_DATA:
            case ExceptionMessages.FAILED_AUTH:
            case ExceptionMessages.NO_SUCH_USER_FOUND:
            case ExceptionMessages.SQL_ERROR:
            case ExceptionMessages.USER_ALREADY_EXISTS:
                pageVariables.put("errorMsg", status);
                break;
            case AccountServiceMessages.WAIT_AUTH:
            case AccountServiceMessages.WAIT_USER_REG:
            case AccountServiceMessages.USER_ADDED:
                pageVariables.put("infoMsg", status);
                break;
            default:
                pageVariables.put("userStatus", status);
                break;
        }
    }

    public void updateUserSession(UserSession newSession) {
        UserSession session = sessions.get(newSession.getSsid());
        if (session != null) {
            session.update(newSession);
        } else {
            sessions.put(newSession.getSsid(), newSession);
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}