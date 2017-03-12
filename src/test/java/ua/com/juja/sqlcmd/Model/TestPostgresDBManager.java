package ua.com.juja.sqlcmd.Model;

/**
 * Created by maistrenko on 07.03.17.
 */
public class TestPostgresDBManager extends TestDBManager {

    @Override
    public void setup() {
        dbManager = new PostgresDBManager();
        dbManager.connect("sqlcmd","postgres","postgres");

    }
}
