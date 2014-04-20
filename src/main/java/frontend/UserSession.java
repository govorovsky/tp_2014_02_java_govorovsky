package frontend;

import util.StopWatch;
import util.UserState;

/**
 * Created by Andrew Govorovsky on 31.03.14
 */
public class UserSession {
    private String username;
    private Long id;
    private UserState status;
    private String ssid;
    private StopWatch stopWatch = new StopWatch();

    public UserSession(String username, String ssid) {
        this.username = username;
        this.ssid = ssid;
    }

    public UserSession(String username, String ssid, UserState status) {
        this(username, ssid);
        this.status = status;
    }

    public UserSession(String username, String ssid, UserState status, Long id) {
        this(username, ssid, status);
        this.id = id;
    }

    public void setStatus(UserState st) {
        this.status = st;
    }

    public UserState getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return username;
    }

    public String getSsid() {
        return ssid;
    }

    public void update(UserSession src) {
        this.id = src.id;
        this.ssid = src.ssid;
        this.status = src.status;
        this.username = src.username;
        this.stopWatch = src.stopWatch;
    }

    public void startWaiting() {
        stopWatch.start();
    }

    public void stopWaiting() {
        stopWatch.stop();
    }

    public boolean isWaiting() {
        return stopWatch.isRunning();
    }

    public long elapsedTime() {
        return stopWatch.getElapsedTime();
    }
}
