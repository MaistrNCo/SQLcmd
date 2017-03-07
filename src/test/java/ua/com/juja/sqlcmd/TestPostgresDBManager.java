package ua.com.juja.sqlcmd;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by maistrenko on 07.03.17.
 */
public class TestPostgresDBManager {
    private PostgresDBManager dbManager;
    @Before
    public void setup(){
        dbManager = new PostgresDBManager();
        dbManager.connect("sqlcmd","postgres","postgres");

    }
    @Test
    public void testAllTablesList(){
       assertEquals("[employee, users]", Arrays.toString(dbManager.getTablesList()));
    }
}
