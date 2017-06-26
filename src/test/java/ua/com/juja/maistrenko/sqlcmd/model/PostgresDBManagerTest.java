package ua.com.juja.maistrenko.sqlcmd.model;

import static ua.com.juja.maistrenko.sqlcmd.TestingCommon.*;

public class PostgresDBManagerTest extends DBManagerTest {

    @Override
    public void initConnection() {
        dbManager = new PostgresDBManager();
        connSet = new ConnectionSettings();
        //    connSet.getConfFileSettings("Postgres.ini");
        connSet.getProperties("config/postgres.properties");
        dbManager.connect(connSet);

        if (!dbManager.isConnected()) {
            String[] defParams = {"127.0.0.1", "5432", "", "postgres", "postgres"};
            connSet.setSettings(defParams);
            dbManager.connect(connSet);
        }

        if (!dbManager.isConnected()) {
            System.out.println("Connection to PostgreSQL DB unsuccessful");
        }
    }

}
