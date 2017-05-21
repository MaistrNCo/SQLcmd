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
import java.util.LinkedHashSet;
import java.util.Set;

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
        dbManager.create("test", Arrays.asList("name", "password"));
        dbManager.create("test2", Arrays.asList("name", "password"));
        dbManager.create("test3", Arrays.asList("name", "password"));
    }

    @Test
    public void testExit() {
        in.add("1");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started."+ lineBreaker +
                "Please choose type of SQL connection : "+ lineBreaker +
                "\t 1 - for PostreSQL"+ lineBreaker +
                "\t 2 - for MySQL"+ lineBreaker +
                "or q - to close program"+ lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }
@Test
    public void testQuit() {
        in.add("q");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started."+ lineBreaker +
                "Please choose type of SQL connection : "+ lineBreaker +
                "\t 1 - for PostreSQL"+ lineBreaker +
                "\t 2 - for MySQL"+ lineBreaker +
                "or q - to close program"+ lineBreaker, getData());
    }

    @Test
    public void testConnectDef() {
        in.add("1");
        in.add("connect");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started."+ lineBreaker +
                "Please choose type of SQL connection : "+ lineBreaker +
                "\t 1 - for PostreSQL"+ lineBreaker +
                "\t 2 - for MySQL"+ lineBreaker +
                "or q - to close program"+ lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testList() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("list");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started."+ lineBreaker +
                "Please choose type of SQL connection : "+ lineBreaker +
                "\t 1 - for PostreSQL"+ lineBreaker +
                "\t 2 - for MySQL"+ lineBreaker +
                "or q - to close program"+ lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "[test, test2, test3]" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testConnect() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started."+ lineBreaker +
                "Please choose type of SQL connection : "+ lineBreaker +
                "\t 1 - for PostreSQL"+ lineBreaker +
                "\t 2 - for MySQL"+ lineBreaker +
                "or q - to close program"+ lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testWrongConnect() {
        in.add("1");
        in.add("connect|" +
                connectionSettings.getServer() + ":" +
                connectionSettings.getPort() + "|" +
                connectionSettings.getDataBase() + "|" +
                "sqlcmd|unknown|xxxx");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started."+ lineBreaker +
                "Please choose type of SQL connection : "+ lineBreaker +
                "\t 1 - for PostreSQL"+ lineBreaker +
                "\t 2 - for MySQL"+ lineBreaker +
                "or q - to close program"+ lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Unsuccessful operation by reason: Connection to database sqlcmd for user unknown failed!  FATAL: password authentication failed for user \"sqlcmd\"" + lineBreaker +
                "try again please" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testNotConnected() {
        in.add("1");
        in.add("list");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started."+ lineBreaker +
                "Please choose type of SQL connection : "+ lineBreaker +
                "\t 1 - for PostreSQL"+ lineBreaker +
                "\t 2 - for MySQL"+ lineBreaker +
                "or q - to close program"+ lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "No DB connection present. Available commands is: help, exit," +
                " connect or connect with parameters" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testWrongInput() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("con");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started."+ lineBreaker +
                "Please choose type of SQL connection : "+ lineBreaker +
                "\t 1 - for PostreSQL"+ lineBreaker +
                "\t 2 - for MySQL"+ lineBreaker +
                "or q - to close program"+ lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "unknown instruction, try more" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testCreate() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("create|testtable|col1|col2|col3");
        in.add("drop|testtable");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started."+ lineBreaker +
                "Please choose type of SQL connection : "+ lineBreaker +
                "\t 1 - for PostreSQL"+ lineBreaker +
                "\t 2 - for MySQL"+ lineBreaker +
                "or q - to close program"+ lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                " created table testtable with columns [id, col1, col2, col3]" + lineBreaker +
                "Table testtable deleted from database successfully" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testCreateDrop() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("list");
        Set<String> tableList = dbManager.getTablesList();
        Set<String> tableList2 = new LinkedHashSet<>();
        tableList2.addAll(tableList)        ;
        tableList2.add("testtable");
        in.add("create|testtable|name|password|address");
        in.add("list");
        in.add("drop|testtable");
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started."+ lineBreaker +
                "Please choose type of SQL connection : "+ lineBreaker +
                "\t 1 - for PostreSQL"+ lineBreaker +
                "\t 2 - for MySQL"+ lineBreaker +
                "or q - to close program"+ lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                tableList.toString() + "" + lineBreaker +
                " created table testtable with columns [id, name, password, address]" + lineBreaker +
                tableList2.toString() + "" + lineBreaker +
                "Table testtable deleted from database successfully" + lineBreaker +
                tableList.toString() + "" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    private String getConnectionInput() {
        return "connect|" +
        connectionSettings.getServer()+":"+
        connectionSettings.getPort()+"|"+
        connectionSettings.getDataBase()+"|"+
        connectionSettings.getUsername()+"|"+
        connectionSettings.getPassword();
    }

    private String getData() {
        try {
            return new String(out.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}
