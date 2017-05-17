package ua.com.juja.maistrenko.sqlcmd.model;

import org.junit.*;

import java.util.List;

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


    private void prepareTestTables() {
        dbManager.create("test", new String[]{"name", "password"});
        dbManager.create("test2", new String[]{"name", "password"});
        dbManager.create("test3", new String[]{"name", "password"});
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

        assertEquals("[test, test2, test3]", dbManager.getTablesList().toString());


    }

    @Test
    public void testSelect() {
        RowData rowData = new RowData();
        rowData.put("id", "48");
        rowData.put("name", "Jimmi");
        rowData.put("password", "111111");

        dbManager.insert("test", rowData);
        List<RowData> data = dbManager.selectAllFromTable("test");
        assertEquals(1, data.size());
        for (RowData row : data) {
            assertEquals("[id, name, password]", data.get(0).getNames().toString());
            assertEquals("[48, Jimmi, 111111]", data.get(0).getValues().toString());
            //System.out.println(row.toString());
        }
    }

    @Test
    public void testUpdate() {
        RowData rd = new RowData();
        rd.put("name", "Jimmi");
        rd.put("password", "111111");
        rd.put("id", "48");
        dbManager.insert("test", rd);

        RowData newValue = new RowData();
        newValue.put("password", "222");

        dbManager.update("test", "id", "48", newValue);

        List<RowData> data = dbManager.selectAllFromTable("test");

        assertEquals(1, data.size());
        for (RowData row : data) {
            assertEquals("[id, name, password]", data.get(0).getNames().toString());
            assertEquals("[48, Jimmi, 222]", data.get(0).getValues().toString());
            //System.out.println(row.toString());
        }
    }

    @Test
    public void testGetColumnsNames() {
        assertEquals("[id, name, password]", dbManager.getColumnsNames("test").toString());
    }

    @Test
    public void testCreateTable() {
        dbManager.create("test4", new String[]{"name", "age"});
        Assert.assertEquals("[test, test2, test3, test4]", dbManager.getTablesList().toString());
        ;
        dbManager.drop("test4");
        Assert.assertEquals("[test, test2, test3]", dbManager.getTablesList().toString());
    }
}
