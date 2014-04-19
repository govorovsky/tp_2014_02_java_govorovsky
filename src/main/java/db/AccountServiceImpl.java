package db;

import com.sun.istack.internal.NotNull;
import frontend.UserStatus;
import messageSystem.Address;
import messageSystem.MessageSystem;
import util.Result;

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
    public Result<Boolean> register(String username, String password) {
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
