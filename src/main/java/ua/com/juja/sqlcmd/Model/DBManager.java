package ua.com.juja.sqlcmd.Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by maistrenko on 12.03.17.
 */
public interface DBManager {

    int CONNECTION_PARAMS_NUMBER = 5;

    RowData[] selectAllFromTable(String tableName);

    int getRowCount(String tableName) throws SQLException;

    String[] getTablesList();

    String[] getColumnsNames(String tableName);

    void connect(ConnectionSettings conSettings);

    void disconnect();

    boolean isConnected();

    void clear(String tableName);

    void drop(String tableName);

    void create(String tableName, String[] columnNames);

    void delete(String tableName, String conditionName, String conditionValue);

    void insert(String tableName, RowData rd);

    void update(String tableName, String conditionName, String conditionValue, RowData newValue);

    void updatePrepared(String tableName, String conditionName, String conditionValue, RowData newValue);

    void createDB(String name);

    void dropDB(String name);

    default void connectDefault(String settingsFileName) {

        ConnectionSettings conSet = new ConnectionSettings();

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
            if (caught == CONNECTION_PARAMS_NUMBER) {
                conSet.setSettings(result);
                connect(conSet);
            } else {
                throw new IOException("Wrong parameters number");
            }

        } catch (IOException e) {
            throw new RuntimeException("Couldn't read file " + settingsFileName, e);
        }

    }


}
