package ua.com.juja.maistrenko.sqlcmd.model;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import ua.com.juja.maistrenko.sqlcmd.model.impl.PostgresDBManager;

import static ua.com.juja.maistrenko.sqlcmd.TestingCommon.*;

public class PostgresDBManagerTest extends DBManagerTest {

    @BeforeClass
    public static void initialisation() {
        Assume.assumeTrue(USE_POSTGRESQL_IN_TESTS);
        dbManager = new PostgresDBManager();
        connSet = new ConnectionSettings();
        connSet.getProperties("config/postgres.properties");
        createTestDB();
        prepareTestTables();
        dbManager.connect(connSet);
    }

    @AfterClass
    public static void dropBase() {
        Assume.assumeTrue(USE_POSTGRESQL_IN_TESTS);
        dbManager.disconnect();
        dropTestData();
    }

}
