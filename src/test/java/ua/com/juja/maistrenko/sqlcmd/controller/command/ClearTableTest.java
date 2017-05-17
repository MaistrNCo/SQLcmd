package ua.com.juja.maistrenko.sqlcmd.controller.command;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import static org.junit.Assert.assertTrue;

public class ClearTableTest {

    private View view;
    private DBManager dbManager;
    private Command command;

    @Before
    public void init() {
        view = mock(View.class);
        dbManager = mock(DBManager.class);
        command = new ClearTable(dbManager, view);
    }

    @Test
    public void testClearCanProcessTrue() {
        assertTrue(command.canProcess("clear|werwert"));
    }

    @Test
    public void testClearCanProcessFalse() {
        assertFalse(command.canProcess("clear"));
    }

    @Test
    public void testClearProcessSuccessful() {
        try {
            command.process("clear|users");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("table users cleared successfully");

    }

    @Test
    public void testClearProcessNonSuccessfulParamsLess() {

        try {
            command.process("clear");
        } catch (RuntimeException e) {
            Assert.assertEquals("Wrong number of parameters, expected minimum is : 2, actual is 1", e.getMessage());
        }


    }


}
