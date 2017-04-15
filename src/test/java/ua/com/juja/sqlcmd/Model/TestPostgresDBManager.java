package ua.com.juja.sqlcmd.Model;

/**
 * Created by maistrenko on 07.03.17.
 */
public class TestPostgresDBManager extends TestDBManager {

    @Override
    public void setup() {
        dbManager = new PostgresDBManager();
        ConnectionSettings connSet = new ConnectionSettings();
        connSet.getConfFileSettings("Postgres.ini");
        dbManager.connect(connSet);

        if (!dbManager.isConnected()) {
            String[] defParams = {"127.0.0.1", "5432", "sqlcmd", "postgres", "postgres"};
            connSet.setSettings(defParams);
            dbManager.connect(connSet);
        }

        if (!dbManager.isConnected()) {
            System.out.println("Connection to PostgreSQL DB unsuccessful");
        }
    }
}
