package ua.com.juja.maistrenko.sqlcmd.model;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import ua.com.juja.maistrenko.sqlcmd.model.impl.MySQLdbManager;

import static ua.com.juja.maistrenko.sqlcmd.TestingCommon.*;

public class   MySQLdbManagerTest extends DBManagerTest {

    @BeforeClass
    public static void initialisation() {
        Assume.assumeTrue(USE_MYSQL_IN_TESTS);
        dbManager = new MySQLdbManager();
        connSet = new ConnectionSettings();
        connSet.getProperties("config/mysql.properties");
        createTestDB();
        prepareTestTables();
        dbManager.connect(connSet);
    }

    @AfterClass
    public static void dropBase() {
        Assume.assumeTrue(USE_MYSQL_IN_TESTS);
        dbManager.disconnect();
        dropTestData();
    }

}
