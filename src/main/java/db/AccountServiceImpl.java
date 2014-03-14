package db;

import com.sun.istack.internal.NotNull;
import exceptions.AccountServiceException;
import exceptions.EmptyDataException;
import exceptions.ExceptionMessages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Andrew Govorovsky on 22.02.14
 */
public class AccountServiceImpl implements AccountService {
    private AtomicLong userIdGenerator = new AtomicLong(0);
    private UsersDAO dao;

    public AccountServiceImpl() {
        dao = new UsersDAO(DatabaseConnector.getConnection());
    }

    public AccountServiceImpl(Connection conn) {
        dao = new UsersDAO(conn);
    }

    @Override
    public void register(HttpServletRequest request) throws AccountServiceException, EmptyDataException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            checkLoginPassword(username,password);
            if (dao.getUser(username) != null) throw new AccountServiceException(ExceptionMessages.USER_ALREADY_EXISTS);
            if (!dao.saveUser(new UserDataSet(username, password))) throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
            request.getSession().setAttribute("userId", userIdGenerator.getAndIncrement());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        }
    }

    @Override
    public void delete(@NotNull String username) throws AccountServiceException {
        try {
            if (!dao.deleteUser(username)) throw new AccountServiceException(ExceptionMessages.NO_SUCH_USER_FOUND);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        }
    }


    @Override
    public void authenticate(@NotNull HttpServletRequest request) throws AccountServiceException, EmptyDataException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            checkLoginPassword(username,password);
            UserDataSet user = dao.getUser(username);
            if (user == null) throw new AccountServiceException(ExceptionMessages.NO_SUCH_USER_FOUND);
            if (!user.getPassword().equals(password)) throw new AccountServiceException(ExceptionMessages.FAILED_AUTH);
            request.getSession().setAttribute("userId", userIdGenerator.getAndIncrement());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        }
    }

    @Override
    public void logout(@NotNull HttpSession session) {
        session.invalidate();
    }


    private void checkLoginPassword(String username, String pass) throws EmptyDataException {
        if (username == null || pass == null || "".equals(username) || "".equals(pass))
            throw new EmptyDataException(ExceptionMessages.EMPTY_DATA);
    }
}
