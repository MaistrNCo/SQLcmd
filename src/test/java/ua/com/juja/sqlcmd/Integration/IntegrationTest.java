package ua.com.juja.sqlcmd.Integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.Controller.Main;
import ua.com.juja.sqlcmd.Model.ConnectionSettings;
import ua.com.juja.sqlcmd.Model.DBManager;
import ua.com.juja.sqlcmd.Model.PostgresDBManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;


/**
 * Created by maistrenko on 28.03.2017.
 */

public class IntegrationTest {

    private static ConfigurableInputStream in;
    private static ByteArrayOutputStream out;
    private DBManager dbManager;

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

        assertEquals(" Hi, program started  \n" +
                "input command please or 'help' to see commands list\n" +
                "Goodbye, to see soon. \n", getData());
    }

    @Test
    public void testConnectDef() {
        in.add("connect");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals(" Hi, program started  \n" +
                "input command please or 'help' to see commands list\n" +
                "Successful connection!!\n" +
                "input command please or 'help' to see commands list\n" +
                "Goodbye, to see soon. \n", getData());
    }

    @Test
    public void testList() {
        in.add("connect");
        in.add("list");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals(" Hi, program started  \n" +
                "input command please or 'help' to see commands list\n" +
                "Successful connection!!\n" +
                "input command please or 'help' to see commands list\n" +
                "[employee, users, test, test2]\n" +
                "input command please or 'help' to see commands list\n" +
                "Goodbye, to see soon. \n", getData());
    }

    @Test
    public void testConnect() {
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals(" Hi, program started  \n" +
                "input command please or 'help' to see commands list\n" +
                "Successful connection!!\n" +
                "input command please or 'help' to see commands list\n" +
                "Goodbye, to see soon. \n", getData());
    }

    @Test
    public void testWrongConnect() {
        in.add("connect|sqlcmd|unknown|xxxx");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals(" Hi, program started  \n" +
                "input command please or 'help' to see commands list\n" +
                "Unsuccessful operation by reason: Couldn`t connect to server 192.168.1.11:5432 to DB: sqlcmd user: unknown password: xxxx  Connection to database unknown for user xxxx failed!\n" +
                "try again please\n" +
                "input command please or 'help' to see commands list\n" +
                "Goodbye, to see soon. \n", getData());
    }

    @Test
    public void testNotConnected() {
        in.add("list");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals(" Hi, program started  \n" +
                "input command please or 'help' to see commands list\n" +
                "You can`t use this command until no DB connection present\n" +
                "input command please or 'help' to see commands list\n" +
                "Goodbye, to see soon. \n", getData());
    }

    @Test
    public void testWrongInput() {
        in.add("connect");
        in.add("con");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals(" Hi, program started  \n" +
                "input command please or 'help' to see commands list\n" +
                "Successful connection!!\n" +
                "input command please or 'help' to see commands list\n" +
                "unknown instruction, try more\n" +
                "input command please or 'help' to see commands list\n" +
                "Goodbye, to see soon. \n", getData());
    }

    @Test
    public void testCreate() {
        in.add("connect");
        //String[] tableList = dbManager.getTablesList();
        in.add("create|testtable|col1|col2|col3");
        in.add("drop|testtable");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals(" Hi, program started  \n" +
                "input command please or 'help' to see commands list\n" +
                "Successful connection!!\n" +
                "input command please or 'help' to see commands list\n" +
                " created table testtable with columns [id, col1, col2, col3]\n" +
                "input command please or 'help' to see commands list\n" +
                "Table testtable deleted from database successfully\n" +
                "input command please or 'help' to see commands list\n" +
                "Goodbye, to see soon. \n", getData());
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

        assertEquals(" Hi, program started  \n" +
                "input command please or 'help' to see commands list\n" +
                "Successful connection!!\n" +
                "input command please or 'help' to see commands list\n" +
                Arrays.toString(tableList) + "\n" +
                "input command please or 'help' to see commands list\n" +
                " created table testtable with columns [id, col1, col2, col3]\n" +
                "input command please or 'help' to see commands list\n" +
                Arrays.toString(tableList2) + "\n" +
                "input command please or 'help' to see commands list\n" +
                "Table testtable deleted from database successfully\n" +
                "input command please or 'help' to see commands list\n" +
                Arrays.toString(tableList) + "\n" +
                "input command please or 'help' to see commands list\n" +
                "Goodbye, to see soon. \n", getData());
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
