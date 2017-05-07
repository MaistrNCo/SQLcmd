package ua.com.juja.sqlcmd.Integration;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import ua.com.juja.sqlcmd.controller.Main;
import ua.com.juja.sqlcmd.model.ConnectionSettings;
import ua.com.juja.sqlcmd.model.DBManager;
import ua.com.juja.sqlcmd.model.PostgresDBManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;



/**
 * Created by maistrenko on 28.03.2017.
 */

public class IntegrationTest {

    private static ConfigurableInputStream in;
    private static ByteArrayOutputStream out;
    private DBManager dbManager;
    private String lineBreaker = System.lineSeparator();

    @Before
    public void setup() {
        dbManager = new PostgresDBManager();


        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));

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
        in.add("connect");
        in.add("list");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hi, program started  " + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "[employee, users, test, test2]" + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void testConnect() {
        in.add("connect|sqlcmd|postgres|postgres");
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
        in.add("connect|sqlcmd|unknown|xxxx");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hi, program started  " + lineBreaker +
                "input command please or 'help' to see commands list" + lineBreaker +
                "Unsuccessful operation by reason: Couldn`t connect to server 192.168.1.11:5432 to DB: sqlcmd user: unknown password: xxxx  Connection to database unknown for user xxxx failed!" + lineBreaker +
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
        in.add("connect");
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
        in.add("connect");
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
        in.add("connect");
        in.add("list");
        ConnectionSettings connSet = new ConnectionSettings();
        connSet.getConfFileSettings("Postgres.ini");
        dbManager.connect(connSet);
        String[] tableList = dbManager.getTablesList();
        dbManager.disconnect();
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

    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}
