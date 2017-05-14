package ua.com.juja.maistrenko.sqlcmd.Integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.PostgresDBManager;
import ua.com.juja.maistrenko.sqlcmd.Main;
import ua.com.juja.maistrenko.sqlcmd.model.ConnectionSettings;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class IntegrationTest {

    private static ConfigurableInputStream in;
    private static ByteArrayOutputStream out;
    private DBManager dbManager;
    private ConnectionSettings connectionSettings = new ConnectionSettings();
    private String lineBreaker = System.lineSeparator();

    @Before
    public void setup() {
        dbManager = new PostgresDBManager();
        connectionSettings.getProperties("config/postgres.properties");
        createTestDB();
        prepareTestTables();
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));

    }

    @After
    public void closeConnection() {
        if (dbManager.isConnected()) {
            dbManager.drop("test");
            dbManager.drop("test2");
            dbManager.drop("test3");
            dbManager.disconnect();
//            connSet.setDataBase("");
//            dbManager.connect(connSet);
//            dbManager.dropDB("testdb");
//            dbManager.disconnect();
        }
    }

    private void createTestDB() {
        connectionSettings.setDataBase("");
        dbManager.connect(connectionSettings);
        dbManager.createDB("testdb");
        dbManager.disconnect();
        connectionSettings.setDataBase("testdb");
        dbManager.connect(connectionSettings);
    }

    private void prepareTestTables() {
        dbManager.create("test", new String[]{"name", "password"});
        dbManager.create("test2", new String[]{"name", "password"});
        dbManager.create("test3", new String[]{"name", "password"});
    }

    @Test
    public void testExit() {
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hi, program started  " + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testConnectDef() {
        in.add("connect");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hi, program started  " + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testList() {
        in.add(getConnectionInput());
        in.add("list");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hi, program started  " + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "[test, test2, test3]" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testConnect() {
        in.add(getConnectionInput());
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hi, program started  " + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testWrongConnect() {
        in.add("connect|" +
                connectionSettings.getServer() + "|" +
                connectionSettings.getPort() + "|" +
                connectionSettings.getDataBase() + "|" +
                "sqlcmd|unknown|xxxx");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hi, program started  " + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Unsuccessful operation by reason: Connection to database sqlcmd for user unknown failed!  FATAL: password authentication failed for user \"sqlcmd\"" + lineBreaker +
                "try again please" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testNotConnected() {
        in.add("list");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hi, program started  " + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "You can`t use this command until no DB connection present" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testWrongInput() {
        in.add(getConnectionInput());
        in.add("con");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hi, program started  " + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "unknown instruction, try more" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testCreate() {
        in.add(getConnectionInput());
        //String[] tableList = dbManager.getTablesList();
        in.add("create|testtable|col1|col2|col3");
        in.add("drop|testtable");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hi, program started  " + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                " created table testtable with columns [id, col1, col2, col3]" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Table testtable deleted from database successfully" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testCreateDrop() {
        in.add(getConnectionInput());
        in.add("list");
        String[] tableList = dbManager.getTablesList();
        String[] tableList2 = Arrays.copyOf(tableList, tableList.length + 1);
        tableList2[tableList2.length - 1] = "testtable";
        in.add("create|testtable|col1|col2|col3");
        in.add("list");
        in.add("drop|testtable");
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hi, program started  " + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                Arrays.toString(tableList) + "" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                " created table testtable with columns [id, col1, col2, col3]" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                Arrays.toString(tableList2) + "" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Table testtable deleted from database successfully" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                Arrays.toString(tableList) + "" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    private String getConnectionInput() {
        return "connect|" +
        connectionSettings.getServer()+"|"+
        connectionSettings.getPort()+"|"+
        connectionSettings.getDataBase()+"|"+
        connectionSettings.getUsername()+"|"+
        connectionSettings.getPassword();
    }

    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}
