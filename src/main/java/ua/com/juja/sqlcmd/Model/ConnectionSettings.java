package ua.com.juja.sqlcmd.Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by maistrenko on 16.03.2017.
 */
public class ConnectionSettings {
    private String server;
    private String port;

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

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

    public void copySettings(ConnectionSettings settings) {
        this.server = settings.getServer();
        this.port = settings.getPort();
        this.dataBase = settings.getDataBase();
        this.username = settings.getUsername();
        this.password = settings.getPassword();
    }

    public String getAddress() {
        return this.server + ":" + this.port + "/" /*+ this.dataBase*/;
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


    public void getConfFileSettings(String settingsFileName) {
        String[] result = new String[5];
        ClassLoader classLoader = getClass().getClassLoader();
        int caught = 0;

        try (FileReader file = new FileReader(classLoader.getResource("config/" + settingsFileName).getFile());
             BufferedReader br = new BufferedReader(file)) {
            String curStr;
            while ((curStr = br.readLine()) != null) {
                String[] splitted = curStr.split(":");
                switch (splitted[0].trim()) {
                    case "server": {
                        result[0] = splitted[1].trim();
                        caught++;
                        break;
                    }
                    case "port": {
                        result[1] = splitted[1].trim();
                        caught++;
                        break;
                    }
                    case "base": {
                        result[2] = splitted[1].trim();
                        caught++;
                        break;
                    }
                    case "username": {
                        result[3] = splitted[1].trim();
                        caught++;
                        break;
                    }
                    case "password": {
                        result[4] = splitted[1].trim();
                        caught++;
                        break;
                    }
                }
            }
            if (caught == 5) {
                this.setSettings(result);
            } else {
                throw new IOException("Wrong parameters number");
            }

        } catch (IOException e) {
            throw new RuntimeException("Couldn't read file " + settingsFileName, e);
        }

    }


}
