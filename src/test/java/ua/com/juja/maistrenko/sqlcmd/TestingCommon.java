package ua.com.juja.maistrenko.sqlcmd;

import ua.com.juja.maistrenko.sqlcmd.model.*;

import java.util.Arrays;

public class TestingCommon {

    public final static boolean USE_MYSQL_IN_TESTS = true;
    public final static boolean USE_POSTGRESQL_IN_TESTS = true;

    public static DBManager dbManager;
    public static ConnectionSettings connSet;

    public static void setConnectionPostgres() {
        dbManager = new PostgresDBManager();
        connSet = new ConnectionSettings();
        connSet.getProperties("config/postgres.properties");
     }

    public static void setConnectionMySQL() {
        dbManager = new MySQLdbManager();
        connSet = new ConnectionSettings();
        connSet.getProperties("config/mysql.properties");
    }

    public static void createTestDB() {
        connSet.setDataBase("");
        dbManager.connect(connSet);
        dbManager.createDB("testdb");
        dbManager.disconnect();
        connSet.setDataBase("testdb");
    }


    public static void prepareTestTables() {
        dbManager.connect(connSet);
        dbManager.create("test", Arrays.asList("name", "password"));
        dbManager.create("test2", Arrays.asList("name", "password"));
        dbManager.create("test3", Arrays.asList("name", "password"));

        RowData rowData = new RowData();
        rowData.put("name", "Simone");
        rowData.put("password", "123456");
        rowData.put("id", "2");
        dbManager.insert("test3", rowData);
        rowData.put("name", "Paul");
        rowData.put("password", "56789");
        rowData.put("id", "3");
        dbManager.insert("test3", rowData);
        rowData.put("name", "Jimmi");
        rowData.put("password", "111111");
        rowData.put("id", "48");
        dbManager.insert("test3", rowData);
        dbManager.disconnect();
    }

    public static void closeConnection() {
        if (!dbManager.isConnected()) {
            dbManager.connect(connSet);
        }
        dbManager.drop("test");
        dbManager.drop("test2");
        dbManager.drop("test3");
        dbManager.disconnect();

    }

    public static void dropTestData() {
        connSet.setDataBase("");
        dbManager.connect(connSet);
        dbManager.dropDB("testdb");
        dbManager.disconnect();
    }
}
