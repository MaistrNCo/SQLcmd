package ua.com.juja.maistrenko.sqlcmd.model;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class ConnectionSettings {

    private static final String DEFAULT_SERVER_ADDRESS = "localhost";
    public static final String DEFAULT_POSTGRES_PORT = "5432";
    public static final String DEFAULT_MYSQL_PORT = "3306";

    private static final int INDEX_SERVER_NAME = 0;
    private static final int INDEX_SERVER_PORT = 1;
    private static final int INDEX_DATABASE_NAME = 2;
    private static final int INDEX_USER_NAME = 3;
    private static final int INDEX_USER_PASS = 4;


    private String server;
    private String port;
    private String dataBase;
    private String username;
    private String password;

    public ConnectionSettings() {
        //Default constructor
    }

    public ConnectionSettings(List<String> set, DBManager dbManager) {
        if (set.get(INDEX_SERVER_NAME).trim().length() == 0) {
            this.server = DEFAULT_SERVER_ADDRESS;
        } else {
            this.server = set.get(INDEX_SERVER_NAME);
        }

        if (set.get(INDEX_SERVER_PORT).trim().length() == 0) {
            if (dbManager instanceof MySQLdbManager) {
                this.port = DEFAULT_MYSQL_PORT;
            } else {
                this.port = DEFAULT_POSTGRES_PORT;
            }
        } else {
            this.port = set.get(INDEX_SERVER_PORT);
        }
        this.dataBase = set.get(INDEX_DATABASE_NAME);
        this.username = set.get(INDEX_USER_NAME);
        this.password = set.get(INDEX_USER_PASS);
    }

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    public void setSettings(String[] set) {
        this.server = set[0];
        this.port = set[1];
        this.dataBase = set[2];
        this.username = set[3];
        this.password = set[4];
    }

    public void setSettings(List<String> set) {
        this.server = set.get(0);
        this.port = set.get(1);
        this.dataBase = set.get(2);
        this.username = set.get(3);
        this.password = set.get(4);
    }

    public String getAddress() {
        return this.server + ":" + this.port + "/" + this.dataBase;
    }

    public String getPort() {
        return port;
    }

    public String getDataBase() {
        return dataBase;
    }

    public String getServer() {
        return server;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void getProperties(String settingsFileName) {

        FileInputStream fileInput = null;
        Properties properties = new Properties();
        File file = new File(settingsFileName);
        try {
            fileInput = new FileInputStream(settingsFileName);
            properties.load(fileInput);
            server = properties.getProperty("server.name");
            port = properties.getProperty("server.port");
            dataBase = properties.getProperty("server.dataBase");
            username = properties.getProperty("server.user.name");
            password = properties.getProperty("server.user.password");

        } catch (Exception e) {
            System.out.println("Error loading config " + file.getAbsolutePath());
            e.printStackTrace();
        } finally {
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    // do nothing;
                }
            }
        }
    }

}
