package ua.com.juja.maistrenko.sqlcmd.controller.command;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import ua.com.juja.maistrenko.sqlcmd.controller.command.impl.ClearTable;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import static org.junit.Assert.assertTrue;

public class ClearTableTest {

    private View view;
    private Command command;
    private DBManager dbManager;
    @Before
    public void init() {
        view = mock(View.class);
        dbManager = mock(DBManager.class);
        command = new ClearTable(dbManager, view);
    }

    @Test
    public void clearCanProcessTrue() {
        assertTrue(command.canProcess("clear|werwert"));
    }

    @Test
    public void clearCanProcessHelp() {
        try {
            command.process("clear|help");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("clear|tableName - to delete all data in table 'tableName'");
    }

    @Test
    public void clearCanProcessFalse() {
        assertFalse(command.canProcess("clear"));
    }

    @Test
    public void clearProcessSuccessful() {
        Mockito.when(dbManager.clear(isA(String.class))).thenReturn(3);
        try {
            command.process("clear|users");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("3 data rows deleted from table users");

    }

    @Test
    public void clearProcessEmptyTable() {
        Mockito.when(dbManager.clear(isA(String.class))).thenReturn(0);
        try {
            command.process("clear|users");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("Nothing deleted from table users");

    }

    @Test
    public void clearProcessNonSuccessfulParamsLess() {

        try {
            command.process("clear");
        } catch (RuntimeException e) {
            Assert.assertEquals("Wrong number of parameters, expected minimum is : 2, actual is 1", e.getMessage());
        }


    }


}
