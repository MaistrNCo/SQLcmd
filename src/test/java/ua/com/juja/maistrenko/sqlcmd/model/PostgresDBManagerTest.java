package ua.com.juja.maistrenko.sqlcmd.model;

import org.junit.Assume;
import org.junit.BeforeClass;

import static ua.com.juja.maistrenko.sqlcmd.TestingCommon.*;

public class PostgresDBManagerTest extends DBManagerTest {

    @BeforeClass
    public static void initialisation() {
        Assume.assumeTrue(USE_POSTGRESQL_IN_TESTS);
        dbManager = new PostgresDBManager();
        connSet = new ConnectionSettings();
        connSet.getProperties("config/postgres.properties");
        dbManager.connect(connSet);

        if (!dbManager.isConnected()) {
            System.out.println("Connection to PostgreSQL DB unsuccessful");
        }
    }

}
