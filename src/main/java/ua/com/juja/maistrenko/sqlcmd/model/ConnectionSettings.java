package ua.com.juja.maistrenko.sqlcmd.model;

import java.io.*;
import java.util.Properties;

public class ConnectionSettings {
    private String server;
    private String port;
    private String dataBase;
    private String username;
    private String password;


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


    public void getSettingsFromFile(String settingsFileName){

        FileInputStream fileInput = null;
        Properties properties = new Properties();
        ClassLoader classLoader = getClass().getClassLoader();
     //   File file = new File(classLoader.getResource(settingsFileName).getFile());
        File file = new File(settingsFileName);
        try {
            fileInput = new FileInputStream(file);
            properties.load(fileInput);
            server   =  properties.getProperty("server.name");
            port     =  properties.getProperty("server.port");
            dataBase =  properties.getProperty("server.dataBase");
            username =  properties.getProperty("server.user.name");
            password =  properties.getProperty("server.user.password");

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
