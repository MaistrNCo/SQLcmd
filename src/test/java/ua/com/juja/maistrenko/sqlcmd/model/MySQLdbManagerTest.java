package ua.com.juja.maistrenko.sqlcmd.model;

import static ua.com.juja.maistrenko.sqlcmd.TestingCommon.*;

public class MySQLdbManagerTest extends DBManagerTest {

    @Override
    public void initConnection() {
        dbManager = new MySQLdbManager();
        connSet = new ConnectionSettings();
        //connSet.getConfFileSettings("mySQL.ini");
        connSet.getProperties("config/mysql.properties");
        dbManager.connect(connSet);

        if (!dbManager.isConnected()) {
            String[] defParams = {"127.0.0.1", "3306", "", "root", ""};
            connSet.setSettings(defParams);
            dbManager.connect(connSet);
        }

        if (!dbManager.isConnected()) {
            System.out.println("Connection to MySQL DB unsuccessful");
        }
    }

}
