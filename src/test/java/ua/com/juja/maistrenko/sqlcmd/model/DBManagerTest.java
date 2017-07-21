package ua.com.juja.maistrenko.sqlcmd.model;

import org.junit.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static ua.com.juja.maistrenko.sqlcmd.TestingCommon.*;

public abstract class DBManagerTest {

    @After
    public void clearTestInput(){
        dbManager.clear("test");
    }

    @Test
    public void allTablesList() {
        assertEquals("[test, test2, test3]", dbManager.getTablesList().toString());
    }

    @Test
    public void select() {
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
    public void update() {
        RowData rd = new RowData();
        rd.put("name", "Jimmi");
        rd.put("password", "111111");
        rd.put("id", "48");
        dbManager.insert("test", rd);

        RowData newValue = new RowData();
        newValue.put("password", "222");

        RowData condition = new RowData();
        condition.put("id", "48");


        dbManager.update("test", condition, newValue);

        List<RowData> data = dbManager.selectAllFromTable("test");

        assertEquals(1, data.size());
        for (RowData row : data) {
            assertEquals("[id, name, password]", data.get(0).getNames().toString());
            assertEquals("[48, Jimmi, 222]", data.get(0).getValues().toString());
        }
    }

    @Test
    public void delete() {
        RowData rowData = new RowData();
        rowData.put("name", "Simone");
        rowData.put("password", "123456");
        rowData.put("id", "2");
        dbManager.insert("test2", rowData);
        rowData.put("name", "Paul");
        rowData.put("password", "56789");
        rowData.put("id", "3");
        dbManager.insert("test2", rowData);
        rowData.put("name", "Jimmi");
        rowData.put("password", "111111");
        rowData.put("id", "48");
        dbManager.insert("test2", rowData);

        List<RowData> data = dbManager.selectAllFromTable("test2");

        assertEquals(3, data.size());
        for (RowData row : data) {
            assertEquals("[id, name, password]", data.get(0).getNames().toString());
            assertEquals("[2, Simone, 123456]", data.get(0).getValues().toString());
            assertEquals("[id, name, password]", data.get(1).getNames().toString());
            assertEquals("[3, Paul, 56789]", data.get(1).getValues().toString());
            assertEquals("[id, name, password]", data.get(2).getNames().toString());
            assertEquals("[48, Jimmi, 111111]", data.get(2).getValues().toString());
        }

        RowData conditionData = new RowData();
        conditionData.put("name", "Jimmi");
        dbManager.delete("test2", conditionData);
        conditionData.put("name", "Paul");
        conditionData.put("id", "3");
        dbManager.delete("test2", conditionData);

        data = dbManager.selectAllFromTable("test2");

        assertEquals(1, data.size());
        for (RowData row : data) {
            assertEquals("[id, name, password]", data.get(0).getNames().toString());
            assertEquals("[2, Simone, 123456]", data.get(0).getValues().toString());
        }

    }

    @Test
    public void getColumnsNames() {
        assertEquals("[id, name, password]", dbManager.getColumnsNames("test").toString());
    }

    @Test
    public void clearTable() {
        dbManager.clear("test3");
        List<RowData>data = dbManager.selectAllFromTable("test3");
        assertEquals(0,data.size());
    }

   @Test
    public void createDropTable() {
        dbManager.create("test4", Arrays.asList("name", "age"));
        Assert.assertEquals("[test, test2, test3, test4]", dbManager.getTablesList().toString());
        dbManager.drop("test4");
        Assert.assertEquals("[test, test2, test3]", dbManager.getTablesList().toString());
    }

    @Test (expected = RuntimeException.class)
    public void insertAlreadyExist() {
        RowData rd = new RowData();
        rd.put("name", "Jimmi");
        rd.put("password", "111111");
        rd.put("id", "48");
        dbManager.insert("test", rd);
        dbManager.insert("test", rd);
    }
}
