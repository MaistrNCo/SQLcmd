package ua.com.juja.maistrenko.sqlcmd;

import ua.com.juja.maistrenko.sqlcmd.model.ConnectionSettings;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.PostgresDBManager;

import java.util.Arrays;

public class TestingCommon {
    public static DBManager dbManager;
    public static ConnectionSettings connSet;

    public static void setConnection() {
        dbManager = new PostgresDBManager();
        connSet = new ConnectionSettings();
        connSet.getProperties("config/postgres.properties");
        dbManager.connect(connSet);
    }

    public static void createTestDB() {
        connSet.setDataBase("");
        dbManager.connect(connSet);
        dbManager.createDB("testdb");
        dbManager.disconnect();
        connSet.setDataBase("testdb");
        dbManager.connect(connSet);
    }


    public static void prepareTestTables() {
        dbManager.create("test", Arrays.asList("name", "password"));
        dbManager.create("test2", Arrays.asList("name", "password"));
        dbManager.create("test3", Arrays.asList("name", "password"));
    }

    public static void closeConnection() {
        if (dbManager.isConnected()) {
            dbManager.drop("test");
            dbManager.drop("test2");
            dbManager.drop("test3");
            dbManager.disconnect();
//            connSet.setDataBase("");
//            dbManager.connect(connSet);
//            dbManager.dropDB("testdb");
//            dbManager.disconnect();
        }
    }
}
