package frontend;

import util.StopWatch;

/**
 * Created by Andrew Govorovsky on 31.03.14
 */
public class UserSession {
    private String username;
    private Long id;
    private String status;
    private String ssid;
    public static final long MAX_WAITING = 5000; // 5 sec waiting for DB response
    public StopWatch stopWatch = new StopWatch();

    public UserSession(String username, String ssid) {
        this.username = username;
        this.ssid = ssid;
    }

    public UserSession(String username, String ssid, String status) {
        this(username, ssid);
        this.status = status;
    }

    public UserSession(String username, String ssid, String status, Long id) {
        this(username, ssid, status);
        this.id = id;
    }

    public void setStatus(String st) {
        this.status = st;
    }

    public String getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long newId) {
        id = newId;
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
