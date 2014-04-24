package db;

import messageSystem.Address;
import messageSystem.MessageSystem;
import util.Result;
import util.UserState;

import java.sql.SQLException;

/**
 * Created by Andrew Govorovsky on 22.02.14
 */
public class AccountServiceImpl implements AccountService {
    private final UsersDAO dao;
    private final MessageSystem messageSystem;
    private final Address address = new Address();
    public static final long MAX_WAITING = 5000;

    public AccountServiceImpl(Database db, MessageSystem ms) {
        this.dao = new UsersDAO(db);
        this.messageSystem = ms;
        messageSystem.addService(this);
        messageSystem.getAddressService().addAddress(this);
    }

    @Override
    public UserState register(String username, String password) {
        try {
            if (!checkLoginPassword(username, password)) return UserState.EMPTY_DATA;
            if (dao.getUser(username) != null) return UserState.USER_ALREADY_EXISTS;
            if (!dao.saveUser(new UserDataSet(username, password)))
                return UserState.SQL_ERROR;
        } catch (SQLException e) {
            e.printStackTrace();
            return UserState.SQL_ERROR;
        }
        return UserState.USER_ADDED;
    }

    @Override
    public UserState delete(String username) {
        try {
            if (!dao.deleteUser(username)) return UserState.NO_SUCH_USER_FOUND;
            return UserState.OK;
        } catch (SQLException e) {
            e.printStackTrace();
            return UserState.SQL_ERROR;
        }
    }


    @Override
    public Result<Long> authenticate(String username, String password) {
        try {
            if (!checkLoginPassword(username, password)) return new Result<>(UserState.EMPTY_DATA);
            UserDataSet user = dao.getUser(username);
            if (user == null) return new Result<>(UserState.NO_SUCH_USER_FOUND);
            if (!user.getPassword().equals(password)) return new Result<>(UserState.FAILED_AUTH);
            return new Result<>(user.getUserId(), UserState.AUTHORIZED);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result<>(UserState.SQL_ERROR);
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
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            messageSystem.execForAbonent(this);
        }
    }
}
