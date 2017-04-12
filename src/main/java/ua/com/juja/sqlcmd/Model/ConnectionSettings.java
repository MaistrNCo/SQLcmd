package ua.com.juja.sqlcmd.Model;

/**
 * Created by maistrenko on 16.03.2017.
 */
public class ConnectionSettings {
    private String server;
    private String port;
    private String dataBase;
    private String username;
    private String password;

    public void setSettings(String[] set) {
        this.server = set[0];
        this.port = set[1];
        this.dataBase = set[2];
        this.username = set[3];
        this.password = set[4];

    }

    public String getAddress() {
        return this.server + ":" + this.port + "/" + this.dataBase;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
