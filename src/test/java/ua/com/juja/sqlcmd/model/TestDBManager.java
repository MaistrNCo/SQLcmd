package ua.com.juja.sqlcmd.model;

import org.junit.*;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by maistrenko on 12.03.17.
 */
public abstract class TestDBManager {
    protected DBManager dbManager;
    protected ConnectionSettings connSet;

    public abstract void initConnection();

    @Before
    public void setup() {
        initConnection();
        if (dbManager.isConnected()) {
            dbManager.disconnect();
            createTestDB();
            prepareTestTables();

        } else {
            System.exit(0);
        }

    }

    private void createTestDB() {
        connSet.setDataBase("");
        dbManager.connect(connSet);
        dbManager.createDB("testdb");
        dbManager.disconnect();
        connSet.setDataBase("testdb");
        dbManager.connect(connSet);
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

    @Test
    public void testAllTablesList() {

        assertEquals("[test, test2, test3]", Arrays.toString(dbManager.getTablesList()));


    }

    private void prepareTestTables() {
        dbManager.create("test", new String[]{"name", "password"});
        dbManager.create("test2", new String[]{"name", "password"});
        dbManager.create("test3", new String[]{"name", "password"});
    }

    @Test
    public void testSelect() {
        RowData rd = new RowData(3);
        rd.addColumnValue("name", "Jimmi");
        rd.addColumnValue("password", "111111");
        rd.addColumnValue("id", "48");

        dbManager.insert("test", rd);
        RowData[] data = dbManager.selectAllFromTable("test");
        assertEquals(1, data.length);
        for (RowData row : data) {
            assertEquals("[id, name, password]", Arrays.toString(data[0].getNames()));
            assertEquals("[48, Jimmi, 111111]", Arrays.toString(data[0].getValues()));
            //System.out.println(row.toString());
        }
    }

    @Test
    public void testUpdate() {
        RowData rd = new RowData(3);
        rd.addColumnValue("name", "Jimmi");
        rd.addColumnValue("password", "111111");
        rd.addColumnValue("id", "48");
        dbManager.insert("test", rd);

        RowData newValue = new RowData(1);
        newValue.addColumnValue("password", "222");

        dbManager.update("test", "id", "48", newValue);

        RowData[] data = dbManager.selectAllFromTable("test");

        assertEquals(1, data.length);
        for (RowData row : data) {
            assertEquals("[id, name, password]", Arrays.toString(data[0].getNames()));
            assertEquals("[48, Jimmi, 222]", Arrays.toString(data[0].getValues()));
            //System.out.println(row.toString());
        }
    }


    @Test
    public void testGetColumnsNames() {
        assertEquals("[id, name, password]", Arrays.toString(dbManager.getColumnsNames("test")));
    }

    @Test
    public void testCreateTable() {
        dbManager.create("test4", new String[]{"name", "age"});
        Assert.assertEquals("[test, test2, test3, test4]", Arrays.toString(dbManager.getTablesList()));
        ;
        dbManager.drop("test4");
        Assert.assertEquals("[test, test2, test3]", Arrays.toString(dbManager.getTablesList()));
    }
}
