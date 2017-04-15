package ua.com.juja.sqlcmd.Model;

/**
 * Created by maistrenko on 07.03.17.
 */
public class TestMySQLdbManager extends TestDBManager {

    @Override
    public void setup() {
        dbManager = new MySQLdbManager();
        ConnectionSettings connSet = new ConnectionSettings();
        connSet.getConfFileSettings("mySQL.ini");
        connSet.setDataBase("testdb");
        dbManager.connect(connSet);

        if (!dbManager.isConnected()) {
            String[] defParams = {"127.0.0.1", "3306", "sqlcmd", "root", ""};
            connSet.setSettings(defParams);
            dbManager.connect(connSet);
        }

        if (!dbManager.isConnected()) {
            System.out.println("Connection to MySQL DB unsuccessful");
        }
    }
}
