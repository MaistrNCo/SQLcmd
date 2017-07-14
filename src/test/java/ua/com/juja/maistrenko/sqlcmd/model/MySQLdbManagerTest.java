package ua.com.juja.maistrenko.sqlcmd.model;

import org.junit.Assume;
import org.junit.BeforeClass;

import static ua.com.juja.maistrenko.sqlcmd.TestingCommon.*;

public class   MySQLdbManagerTest extends DBManagerTest {

    @BeforeClass
    public static void initialisation() {
        Assume.assumeTrue(USE_MYSQL_IN_TESTS);
        dbManager = new MySQLdbManager();
        connSet = new ConnectionSettings();
        connSet.getProperties("config/mysql.properties");
        dbManager.connect(connSet);

        if (!dbManager.isConnected()) {
            System.out.println("Connection to MySQL DB unsuccessful");
        }
    }

}
