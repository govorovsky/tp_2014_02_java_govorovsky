package resource;

/**
 * Created by Andrew Govorovsky on 23.04.14
 */
public class DBResource implements Resource {
    private String host;
    private int port;
    private String db;
    private String user;
    private String pass;
    private String driver;

    public String getHost() {
        return host;
    }

    public String getPass() {
        return pass;
    }

    public String getUser() {
        return user;
    }

    public String getDb() {
        return db;
    }

    public int getPort() {
        return port;
    }

    public String getDriver() {
        return driver;
    }

}
