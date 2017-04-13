package ua.com.juja.sqlcmd.Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by maistrenko on 12.03.17.
 */
public abstract  class TestDBManager {
    protected DBManager dbManager;

    @Before
    public abstract void setup();

    @After
    public void closeConnecton(){
        if (dbManager.isConnected()){
            dbManager.disconnect();
        }
    }

    @Test
    public void testAllTablesList(){
       assertEquals("[employee, users]", Arrays.toString(dbManager.getTablesList()));
    }

    @Test
    public void testSelect(){
       dbManager.clear("users");
       RowData rd = new RowData(3);
       rd.addColumnValue("name","Jimmi");
       rd.addColumnValue("password","111111");
       rd.addColumnValue("id","48");

       dbManager.insert("users",rd);
       RowData[] data = dbManager.selectAllFromTable("users");
       assertEquals(1,data.length);
       for (RowData row:data ){
           assertEquals("[id, name, password]",Arrays.toString(data[0].getNames()));
           assertEquals("[48, Jimmi, 111111]",Arrays.toString(data[0].getValues()));
           //System.out.println(row.toString());
       }
    }

    @Test
    public void testUpdate() {
        dbManager.clear("users");
        RowData rd = new RowData(3);
        rd.addColumnValue("name", "Jimmi");
        rd.addColumnValue("password", "111111");
        rd.addColumnValue("id", "48");
        dbManager.insert("users",rd);

        RowData newValue = new RowData(1);
        newValue.addColumnValue("password", "222");

        dbManager.update("users", "id", "48", newValue);

        RowData[] data = dbManager.selectAllFromTable("users");

        assertEquals(1, data.length);
        for (RowData row : data) {
            assertEquals("[id, name, password]", Arrays.toString(data[0].getNames()));
            assertEquals("[48, Jimmi, 222]", Arrays.toString(data[0].getValues()));
            //System.out.println(row.toString());
        }
    }

    @Test
    public void testUpdatePrepared() {
        dbManager.clear("users");
        RowData rd = new RowData(3);
        rd.addColumnValue("name", "Jimmi");
        rd.addColumnValue("password", "111111");
        rd.addColumnValue("id", "48");
        dbManager.insert("users",rd);

        RowData newValue = new RowData(1);
        newValue.addColumnValue("password", "222");

        dbManager.updatePrepared("users", "id", "48", newValue);

        RowData[] data = dbManager.selectAllFromTable("users");

        assertEquals(1, data.length);
        for (RowData row : data) {
            assertEquals("[id, name, password]", Arrays.toString(data[0].getNames()));
            assertEquals("[48, Jimmi, 222]", Arrays.toString(data[0].getValues()));
        }
    }

    @Test
    public void testGetColumnsNames(){
        assertEquals("[id, name, password]", Arrays.toString(dbManager.getColumnsNames("users")));
    }

    @Test
    public void testCreateDB(){

        dbManager.createDB("testDB");

        ConnectionSettings conSet = new ConnectionSettings();
//        String[] defParams = {"192.168.77.11", "5432", "testDB", "postgres", "postgres"};
//        conSet.setSettings(defParams);
//        dbManager.connect(conSet);
        dbManager.create("Test",new String[] {"name","age"});
        dbManager.getTablesList();
        dbManager.drop("Test");

        String[] defParams2 = {"192.168.77.11", "5432", "postgres", "postgres", "postgres"};
        conSet.setSettings(defParams2);
        dbManager.connect(conSet);

        dbManager.dropDB("testDB");
        dbManager.disconnect();
    }
}
