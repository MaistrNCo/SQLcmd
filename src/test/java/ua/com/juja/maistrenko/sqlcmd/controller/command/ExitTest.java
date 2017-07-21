package ua.com.juja.maistrenko.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.maistrenko.sqlcmd.controller.command.impl.Exit;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ExitTest {

    private View view;
    private Command command;

    @Before
    public void init() {
        view = mock(View.class);
        DBManager dbManager = mock(DBManager.class);
        command = new Exit(dbManager, view);
    }
    @Test
    public void exitCanProcessTrue() {
        assertTrue(command.canProcess("exit"));
    }

    @Test
    public void exitCanProcessFalse() {
        assertFalse(command.canProcess("Exit"));
        assertFalse(command.canProcess("exit|we"));
    }

    @Test
    public void exitProcess() {
        try {
            command.process("exit");
        } catch (NormalExitException e) {
            //
        }
        Mockito.verify(view).write("Goodbye, to see soon. ");

    }


}
