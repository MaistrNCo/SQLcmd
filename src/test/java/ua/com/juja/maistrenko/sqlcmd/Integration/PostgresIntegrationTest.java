package ua.com.juja.maistrenko.sqlcmd.Integration;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static ua.com.juja.maistrenko.sqlcmd.TestingCommon.*;

import ua.com.juja.maistrenko.sqlcmd.Main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class PostgresIntegrationTest {

    private static ConfigurableInputStream in;
    private static ByteArrayOutputStream out;
    private static final String lineBreaker = System.lineSeparator();

    @BeforeClass
    public static void setupDB() {
        Assume.assumeTrue(USE_POSTGRESQL_IN_TESTS);
        setConnectionPostgres();
        createTestDB();
        prepareTestTables();
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Before
    public void setup() {
        try {
            in.reset();
            out.reset();
        } catch (IOException e) {
            //do nothing
        }

    }

    @After
    public void disconnect() {
        dbManager.disconnect();
    }

    @AfterClass
    public static void clearDB() {
        Assume.assumeTrue(USE_POSTGRESQL_IN_TESTS);
        dropTestData();
    }

    @Test
    public void exit() {
        in.add("1");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void quit() {
        in.add("q");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker, getData());
    }
    @Test
    public void mainMenuWrongInp() {
        in.add("5");
        in.add("q");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "wrong input"+ lineBreaker, getData());
    }
    @Test
    public void mainMenuInput2() {
        in.add("2");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Goodbye, to see soon. "+ lineBreaker, getData());
    }

    @Test
    public void connectDef() {
        in.add("1");
        in.add("connect");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void list() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("list");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "[test, test2, test3]" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void findWrongTableTest() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("find|unknowntable");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "Unsuccessful operation by reason: Couldn't print table unknowntable  ERROR: relation \"unknowntable\" does not exist\n" +
                "  Position: 15" + lineBreaker +
                "try again please or use 'commandName|help' to see command description" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void findTest() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("insert|test|id|33|name|Vitja|password|prahvessor");
        in.add("find|test");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "added new row to table test  which has values: [33, Vitja, prahvessor]" + lineBreaker +
                "+----+-------+------------+" + lineBreaker +
                "| id | name  | password   | " + lineBreaker +
                "+----+-------+------------+" + lineBreaker +
                "| 33 | Vitja | prahvessor | " + lineBreaker +
                "+----+-------+------------+" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void connect() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void wrongConnect() {
        in.add("1");
        in.add("connect|" +
                connSet.getServer() + ":" +
                connSet.getPort() + "|" +
                connSet.getDataBase() + "|" +
                "unknown|xxxx");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Unsuccessful operation by reason: Connection to database testdb for user unknown failed!  FATAL: password authentication failed for user \"unknown\"" + lineBreaker +
                "try again please or use 'commandName|help' to see command description" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void notConnected() {
        in.add("1");
        in.add("list");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "No DB connection present. Available commands is: help, exit," +
                " connect or connect with parameters" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void wrongInput() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("con");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "unknown command, try again please or use 'help' to see available commands" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void create() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("create|testtable|id|col1|col2|col3");
        in.add("drop|testtable");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                " created table testtable with columns [id, col1, col2, col3]" + lineBreaker +
                "Table testtable deleted from database successfully" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void createDrop() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("list");
        in.add("create|testtable|id|name|password|address");
        in.add("list");
        in.add("drop|testtable");
        in.add("list");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "[test, test2, test3]" + lineBreaker +
                " created table testtable with columns [id, name, password, address]" + lineBreaker +
                "[test, test2, test3, testtable]" + "" + lineBreaker +
                "Table testtable deleted from database successfully" + lineBreaker +
                "[test, test2, test3]" + "" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }
    @Test
    public void wrongParamsAmount() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("delete|");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "Wrong parameters amount. Must be 'delete|tableName|column|value' " +
                "But was: 'delete|'" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }
    @Test
    public void commandHelp() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("help");
        in.add("exit");
        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "Commands full list :" + lineBreaker +
                "  help " + lineBreaker +
                "\t\t to display commands list and usage hints" + lineBreaker +
                "" + lineBreaker +
                "  exit " + lineBreaker +
                "\t\t to close current program" + lineBreaker +
                "" + lineBreaker +
                "  connect " + lineBreaker +
                "\t\t to connect to SQL server with settings saved in properties file." + lineBreaker +
                "" + lineBreaker +
                "  connect|serverName:port|dataBase|userName|password " + lineBreaker +
                "\t\t for connection to SQL server. You can omit 'port' or 'serverName:port' for default port on localhost" + lineBreaker +
                "" + lineBreaker +
                "  list " + lineBreaker +
                "\t\t to get tables list in connected database" + lineBreaker +
                "" + lineBreaker +
                "  find|tableName " + lineBreaker +
                "\t\t to show all data from table 'tableName'" + lineBreaker +
                "" + lineBreaker +
                "  clear|tableName " + lineBreaker +
                "\t\t to delete all data in table 'tableName'" + lineBreaker +
                "" + lineBreaker +
                "  drop|tableName " + lineBreaker +
                "\t\t to delete table 'tableName' with all contained data" + lineBreaker +
                "" + lineBreaker +
                "  create|tableName|column1|column2|...|columnN " + lineBreaker +
                "\t\t to create table 'tableName' with defined columns. WARNING 'column1' will be set as PRIMARY KEY, serial" + lineBreaker +
                "" + lineBreaker +
                "  insert|tableName|column1|value1|column2|value2|...|columnN|valueN " + lineBreaker +
                "\t\t to add new data row in table 'tableName'" + lineBreaker +
                "" + lineBreaker +
                "  update|tableName|conditionalColumn|conditionalValue|column1|value1|...|columnN|valueN " + lineBreaker +
                "\t\t to update data in rows of table 'tableName' selected by condition: conditionalColumn == conditionalValue" + lineBreaker +
                "" + lineBreaker +
                "  delete|tableName|column1|value1|column2|value2... " + lineBreaker +
                "\t\t to delete data in table 'tableName' where column1 = value1, column2 = value2 and so on" + lineBreaker +
                "" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void insert() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("insert|test2|id|10|name|Aria|password|Rome");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "added new row to table test2  which has values: [10, Aria, Rome]" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void insertWrongColumn() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("insert|test|id|10|name|Aria|address|Rome");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "data in table test wan`t added, check input data please" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void insertWrongParamsCount() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("insert|test|id|10|name|Aria|address");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "Wrong parameters amount. Must be 'insert|tableName|column1|value1|column2|value2|...|columnN|valueN ' But was: 'insert|test|id|10|name|Aria|address'" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void update() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("update|test3|id|3|name|Aria|password|Rome");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "data in table test3 updated" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void updatetWrongColumn() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("update|test3|id|3|name|Aria|address|Rome");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "Unsuccessful operation by reason: Couldn't update table test3  ERROR: column \"address\" of relation \"test3\" does not exist\n" +
                "  Position: 32" + lineBreaker +
                "try again please or use 'commandName|help' to see command description" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    @Test
    public void updateWrongParamsCount() {
        in.add("1");
        in.add(getConnectionInput());
        in.add("update|test|id|10|name|Aria|address");
        in.add("exit");

        Main.main(new String[0]);

        assertEquals("Hello, SQLcmd program started." + lineBreaker +
                "Please choose type of SQL connection : " + lineBreaker +
                "\t 1 - for PostgreSQL" + lineBreaker +
                "\t 2 - for MySQL" + lineBreaker +
                "or q - to close program" + lineBreaker +
                "Hi, program started !" + lineBreaker +
                "Input command please or 'help' to see commands list" + lineBreaker +
                "Successful connection!!" + lineBreaker +
                "Wrong parameters amount. Must be 'update|tableName|conditionalColumn|conditionalValue|column1|value1|...|columnN|valueN ' But was: 'update|test|id|10|name|Aria|address'" + lineBreaker +
                "Goodbye, to see soon. " + lineBreaker, getData());
    }

    private String getConnectionInput() {
        return "connect|" +
                connSet.getServer() + ":" +
                connSet.getPort() + "|" +
                connSet.getDataBase() + "|" +
                connSet.getUsername() + "|" +
                connSet.getPassword();
    }

    private String getData() {
        try {
            return new String(out.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}
