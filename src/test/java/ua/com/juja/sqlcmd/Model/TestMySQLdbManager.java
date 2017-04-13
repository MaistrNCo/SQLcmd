package ua.com.juja.sqlcmd.Model;

/**
 * Created by maistrenko on 07.03.17.
 */
public class TestMySQLdbManager extends TestDBManager {

    @Override
    public void setup() {
        dbManager = new MySQLdbManager();

        dbManager.connectDefault("mySQL.ini");

        if(!dbManager.isConnected()) {
            ConnectionSettings conSet = new ConnectionSettings();
            String[] defParams = {"192.168.1.11", "3306", "sqlcmd", "root", "root"};
            conSet.setSettings(defParams);
            dbManager.connect(conSet);
        }

        if(!dbManager.isConnected()) {
            System.out.println("Connection to MySQL DB unsuccessful");
        }
    }
}
