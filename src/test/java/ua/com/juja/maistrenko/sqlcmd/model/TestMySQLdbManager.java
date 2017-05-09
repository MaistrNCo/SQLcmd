package ua.com.juja.maistrenko.sqlcmd.model;

/**
 * Created by maistrenko on 07.03.17.
 */
public class TestMySQLdbManager extends TestDBManager {

    @Override
    public void initConnection() {
        dbManager = new MySQLdbManager();
        connSet = new ConnectionSettings();
        //connSet.getConfFileSettings("mySQL.ini");
        connSet.getProperties("src/main/config/mysql.properties");
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
