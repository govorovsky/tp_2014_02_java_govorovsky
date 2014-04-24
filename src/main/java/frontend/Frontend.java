package frontend;

import db.AccountServiceImpl;
import messageSystem.*;
import util.UserState;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
    private final RequestHandler requestHandler = new RequestHandler();

    public Frontend(MessageSystem ms) {
        this.messageSystem = ms;
        messageSystem.addService(this);
        messageSystem.getAddressService().addAddress(this);
    }


    private void checkSessionState(UserSession session) {
        if (session.isWaiting()) {
            if (session.getStatus().equals(UserState.AUTHORIZED) || session.getStatus().equals(UserState.USER_ADDED)) {
                session.stopWaiting();
            }
            if (session.elapsedTime() > AccountServiceImpl.MAX_WAITING) {
                session.setStatus(UserState.SQL_ERROR);
                session.stopWaiting();
            }
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

        requestHandler.handle(request, response, session, pageVariables, sessions);
    }


    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String requested = request.getPathInfo();
        if (requested.equals(Pages.AUTH_PAGE)) {
            doAuthenticate(request, response);
        } else if (requested.equals(Pages.REG_PAGE)) {
            doRegister(request, response);
        } else {
            response.sendRedirect(Pages.MAIN_PAGE);
        }
    }

    private void doAuthenticate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserSession newSession = new UserSession(username, request.getSession().getId(), UserState.WAIT_AUTH);
        newSession.startWaiting();
        sessions.put(request.getSession().getId(), newSession);
        messageSystem.sendMessage(new MsgLogin(getAddress(), messageSystem.getAddressService().getAddress(AccountServiceImpl.class), username, password, newSession.getSsid()));
        response.sendRedirect(Pages.AUTH_PAGE);
    }

    private void doRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserSession newSession = new UserSession(username, request.getSession().getId(), UserState.WAIT_USER_REG);
        newSession.startWaiting();
        sessions.put(request.getSession().getId(), newSession);
        messageSystem.sendMessage(new MsgRegister(getAddress(), messageSystem.getAddressService().getAddress(AccountServiceImpl.class), username, password, newSession.getSsid()));
        response.sendRedirect(Pages.REG_PAGE);
    }

    private void parseStatus(UserState status, Map<String, Object> pageVariables) {
        switch (status) {
            case EMPTY_DATA:
            case FAILED_AUTH:
            case NO_SUCH_USER_FOUND:
            case SQL_ERROR:
            case USER_ALREADY_EXISTS:
                pageVariables.put("errorMsg", status.getMessage());
                break;
            case WAIT_AUTH:
            case WAIT_USER_REG:
            case USER_ADDED:
                pageVariables.put("infoMsg", status.getMessage());
                break;
            default:
                pageVariables.put("userStatus", status.getMessage());
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
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}