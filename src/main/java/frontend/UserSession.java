package frontend;

import util.StopWatch;

/**
 * Created by Andrew Govorovsky on 31.03.14
 */
public class UserSession {
    private String username;
    private Long id;
    private UserStatus status;
    private String ssid;
    public StopWatch stopWatch = new StopWatch();

    public static final long MAX_WAITING = 5000; // 5 sec waiting for DB response

    public UserSession(String username, String ssid) {
        this.username = username;
        this.ssid = ssid;
    }

    public UserSession(String username, String ssid, UserStatus status) {
        this(username, ssid);
        this.status = status;
    }

    public UserSession(String username, String ssid, UserStatus status, Long id) {
        this(username, ssid, status);
        this.id = id;
    }

    public void setStatus(UserStatus st) {
        this.status = st;
    }

    public UserStatus getStatus() {
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

    public long elapsedTime() {
        return stopWatch.getElapsedTime();
    }
}
