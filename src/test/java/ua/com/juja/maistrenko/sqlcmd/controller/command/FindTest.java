package ua.com.juja.maistrenko.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by maistrenko on 06.04.2017.
 */
public class FindTest {

    private View view;
    private DBManager dbManager;
    private Command command;

    @Before
    public void setUp() {
        view = Mockito.mock(View.class);
        dbManager = Mockito.mock(DBManager.class);
        command = new Find(dbManager, view);
    }

    @Test
    public void testFindCanProcessTrue() {
        assertTrue(command.canProcess("find|users"));
    }

    @Test
    public void testFindCanProcessFalse() {

        assertTrue(!command.canProcess("find"));
    }


    @Test
    public void testSelectFromTable() {
        RowData user1 = new RowData(3);
        user1.addColumnValue("id", "2");
        user1.addColumnValue("name", "Jimm");
        user1.addColumnValue("password", "123");
        RowData user2 = new RowData(3);
        user2.addColumnValue("id", "3");
        user2.addColumnValue("name", "Bimm");
        user2.addColumnValue("password", "321");

        List<RowData> data = new LinkedList<>(Arrays.asList(user1, user2));

        Mockito.when(dbManager.getColumnsNames("users"))
                .thenReturn(new LinkedHashSet<String>(Arrays.asList("id", "name", "password")));  // ());
        Mockito.when(dbManager.selectAllFromTable("users")).thenReturn(data);

        //when
        command.process("find|users");
        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals("[|id\t|name\t|password\t|," +
                        " |2\t|Jimm\t|123\t|," +
                        " |3\t|Bimm\t|321\t|]",
                captor.getAllValues().toString());
    }

    @Test
    public void testSelectFromEmptyTable() {

        List <RowData> data = new LinkedList<>();

        Mockito.when(dbManager.getColumnsNames("users"))
                .thenReturn(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));
        Mockito.when(dbManager.selectAllFromTable("users")).thenReturn(data);

        //when
        command.process("find|users");
        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals("[|id\t|name\t|password\t|]",
                captor.getAllValues().toString());
    }

}
