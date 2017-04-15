package ua.com.juja.sqlcmd.Controller.command;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.mockito.stubbing.OngoingStubbing;
import ua.com.juja.sqlcmd.Model.DBManager;
import ua.com.juja.sqlcmd.View.View;

import static org.junit.Assert.assertTrue;

/**
 * Created by maistrenko on 05.04.17.
 */
public class ClearTest {

    private View view;
    private DBManager dbManager;
    private Command command;

    @Before
    public void init() {
        view = mock(View.class);
        dbManager = mock(DBManager.class);
        command = new Clear(dbManager, view);
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
        verify(view).printOut("table users cleared successfully");

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
