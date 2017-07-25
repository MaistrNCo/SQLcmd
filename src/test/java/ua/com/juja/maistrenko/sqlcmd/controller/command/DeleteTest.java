package ua.com.juja.maistrenko.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.maistrenko.sqlcmd.controller.command.impl.Delete;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DeleteTest {
    private View view;
    private Command command;
    private DBManager dbManager;
    @Before
    public void init() {
        view = mock(View.class);
        dbManager = mock(DBManager.class);
        command = new Delete(dbManager, view);
    }

    @Test
    public void deleteCanProcessTrue() {
        assertTrue(command.canProcess("delete|werwert"));
    }

    @Test
    public void deleteCanProcessFalse() {
        assertFalse(command.canProcess("delete"));
    }

    @Test
    public void deleteProcessHelp() {
        try {
            command.process("delete|help");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("delete|tableName|column1|value1|column2|value2... - " +
                "to delete data in table 'tableName' where column1 = value1, column2 = value2 and so on");
    }

    @Test
    public void deleteProcessWrongParamsAmount() {
        try {
            command.process("delete|tableName");
        } catch (NormalExitException e) {
            //
        }
        verify(view).writeWrongParamsMsg("delete|tableName|column|value","delete|tableName");
    }

    @Test
    public void deleteProcessSuccessful() {
        Mockito.when(dbManager.delete(isA(String.class),isA(RowData.class))).thenReturn(5);
        try {
            command.process("delete|tableName|column1|value");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("Deleted 5 data rows from table tableName");
    }
    @Test
    public void deleteProcessEmptyTable() {
        Mockito.when(dbManager.delete(isA(String.class),isA(RowData.class))).thenReturn(0);
        try {
            command.process("delete|tableName|column1|value");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("Nothing deleted from table tableName");
    }

}
