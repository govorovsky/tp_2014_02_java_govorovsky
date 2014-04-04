package db;

import com.sun.istack.internal.NotNull;
import exceptions.AccountServiceException;
import exceptions.EmptyDataException;
import exceptions.ExceptionMessages;
import messageSystem.Address;
import messageSystem.MessageSystem;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Andrew Govorovsky on 22.02.14
 */
public class AccountServiceImpl implements AccountService {
    private Connection connection;
    private final MessageSystem messageSystem;
    private final Address address = new Address();

    public AccountServiceImpl(MessageSystem ms) {
        this.messageSystem = ms;
        messageSystem.addService(this);
        messageSystem.getAddressService().addAddress(this);
    }

    private Connection getDBConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());
            connection = DriverManager.getConnection("jdbc:mysql://" + "localhost:" + "3306/" + "gameserver?" + "user=root&" + "password=qazxsw12");
            return connection;
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void register(String username, String password) throws AccountServiceException, EmptyDataException {
        UsersDAO dao = new UsersDAO(getDBConnection());
        try {
            checkLoginPassword(username, password);
            if (dao.getUser(username) != null) throw new AccountServiceException(ExceptionMessages.USER_ALREADY_EXISTS);
            if (!dao.saveUser(new UserDataSet(username, password)))
                throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        }
    }

    @Override
    public void delete(@NotNull String username) throws AccountServiceException {
        UsersDAO dao = new UsersDAO(getDBConnection());
        try {
            if (!dao.deleteUser(username)) throw new AccountServiceException(ExceptionMessages.NO_SUCH_USER_FOUND);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        }
    }


    @Override
    public long authenticate(String username, String password) throws AccountServiceException, EmptyDataException {
        UsersDAO dao = new UsersDAO(getDBConnection());
        try {
            checkLoginPassword(username, password);
            UserDataSet user = dao.getUser(username);
            if (user == null) throw new AccountServiceException(ExceptionMessages.NO_SUCH_USER_FOUND);
            if (!user.getPassword().equals(password)) throw new AccountServiceException(ExceptionMessages.FAILED_AUTH);
            return user.getUserId();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        }
    }


    private void checkLoginPassword(String username, String pass) throws EmptyDataException {
        if (username == null || pass == null || "".equals(username) || "".equals(pass))
            throw new EmptyDataException();
    }


    @Override
    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageSystem.execForAbonent(this);
        }
    }
}
