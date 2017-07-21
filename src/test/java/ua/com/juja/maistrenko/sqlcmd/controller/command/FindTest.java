package ua.com.juja.maistrenko.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ua.com.juja.maistrenko.sqlcmd.controller.command.impl.Find;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

public class FindTest {

    private View view;
    private DBManager dbManager;
    private Command command;

    @Captor
    private ArgumentCaptor<List<RowData>> listCaptor;

    @Before
    public void setUp() {
        view = Mockito.mock(View.class);
        dbManager = Mockito.mock(DBManager.class);
        command = new Find(dbManager, view);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findCanProcessTrue() {
        assertTrue(command.canProcess("find|users"));
    }

    @Test
    public void findCanProcessFalse() {

        assertFalse(command.canProcess("find"));
        assertFalse(command.canProcess("fin"));
    }

    @Test
    public void findProcessHelp() {
        try {
            command.process("find|help");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("find|tableName - to show all data from table 'tableName'");
    }

    @Test
    public void findFromTable() {
        //given
        RowData user1 = new RowData();
        user1.put("id", "2");
        user1.put("name", "Jimm");
        user1.put("password", "123");
        RowData user2 = new RowData();
        user2.put("id", "3");
        user2.put("name", "Bimm");
        user2.put("password", "321");
        RowData user3 = new RowData();
        user3.put("id", "5");
        user3.put("name", "John");
        user3.put("password", "468987");

        List<RowData> data = new LinkedList<>(Arrays.asList(user1, user2, user3));

        Mockito.when(dbManager.getColumnsNames("users"))
                .thenReturn(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));  // ());
        Mockito.when(dbManager.selectAllFromTable("users")).thenReturn(data);

        //when
        command.process("find|users");

        //then

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        verify(view, atLeastOnce()).printTable(listCaptor.capture(), captor.capture());

        assertEquals("[id, name, password]", captor.getAllValues().toString());
        assertEquals("[[2, Jimm, 123], [3, Bimm, 321], [5, John, 468987]]",
                listCaptor.getAllValues().get(0).toString());
    }

    @Test
    public void findFromEmptyTable() {

        List<RowData> data = new LinkedList<>();


        Mockito.when(dbManager.getColumnsNames("users"))
                .thenReturn(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));
        Mockito.when(dbManager.selectAllFromTable("users")).thenReturn(data);

        //when
        command.process("find|users");
        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).printTable(listCaptor.capture(), captor.capture());

        assertEquals("[id, name, password]", captor.getAllValues().toString());
        assertEquals("[]", listCaptor.getAllValues().get(0).toString());
    }

    @Test
    public void findProcessWrongParamsAmount() {
        try {
            command.process("find|tableName|sdfg");
        } catch (NormalExitException e) {
            //
        }
        verify(view).writeWrongParamsMsg("find|tableName","find|tableName|sdfg");
    }

}
