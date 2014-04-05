package db;

import com.sun.istack.internal.NotNull;
import frontend.UserStatus;
import messageSystem.Address;
import messageSystem.MessageSystem;
import util.Result;

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
    public Result<Boolean> register(String username, String password) {
        UsersDAO dao = new UsersDAO(getDBConnection());
        try {
            if (!checkLoginPassword(username, password)) return new Result<>(false, UserStatus.EMPTY_DATA);
            if (dao.getUser(username) != null) return new Result<>(false, UserStatus.USER_ALREADY_EXISTS);
            if (!dao.saveUser(new UserDataSet(username, password)))
                return new Result<>(false, UserStatus.SQL_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result<>(false, UserStatus.SQL_ERROR);
        }
        return new Result<>(true, UserStatus.USER_ADDED);
    }

    @Override
    public Result<Boolean> delete(@NotNull String username) {
        UsersDAO dao = new UsersDAO(getDBConnection());
        try {
            if (!dao.deleteUser(username)) return new Result<>(false, UserStatus.NO_SUCH_USER_FOUND);
            return new Result<>(true, UserStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result<>(false, UserStatus.SQL_ERROR);
        }
    }


    @Override
    public Result<Long> authenticate(String username, String password) {
        UsersDAO dao = new UsersDAO(getDBConnection());
        try {
            if (!checkLoginPassword(username, password)) return new Result<>(-1L, UserStatus.EMPTY_DATA);
            UserDataSet user = dao.getUser(username);
            if (user == null) return new Result<>(-1L, UserStatus.NO_SUCH_USER_FOUND);
            if (!user.getPassword().equals(password)) return new Result<>(-1L, UserStatus.FAILED_AUTH);
            return new Result<>(user.getUserId(), UserStatus.AUTHORIZED);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result<>(-1L, UserStatus.SQL_ERROR);
        }
    }


    private boolean checkLoginPassword(String username, String pass) {
        return !(username == null || pass == null || "".equals(username) || "".equals(pass));
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
