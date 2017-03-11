package ua.com.juja.sqlcmd;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by maistrenko on 07.03.17.
 */
public class TestPostgresDBManager {
    private PostgresDBManager dbManager;
    @Before
    public void setup(){
        dbManager = new PostgresDBManager();
        dbManager.connect("sqlcmd","postgres","postgres");

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
       RowData[] data = dbManager.select("users");
       assertEquals(1,data.length);
       for (RowData row:data ){
           assertEquals("[id, name, password]",Arrays.toString(data[0].getNames()));
           assertEquals("[48, Jimmi, 111111]",Arrays.toString(data[0].getValues()));
           System.out.println(row.toString());
       }

    }
}
