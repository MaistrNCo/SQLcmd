package ua.com.juja.maistrenko.sqlcmd.controller.command;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import static org.junit.Assert.assertTrue;

public class ExitTest {

    private final View view = Mockito.mock(View.class);
    private final DBManager dbManager = Mockito.mock(DBManager.class);

    @Test
    public void testExitCanProcessTrue() {
        Command command = new Exit(dbManager, view);
        assertTrue(command.canProcess("exit"));
    }

    @Test
    public void testExitCanProcessFalse() {
        Command command = new Exit(dbManager, view);
        assertTrue(!command.canProcess("Exit"));
    }

    @Test
    public void testExitProcess() {
        Command command = new Exit(dbManager, view);
        try {
            command.process("exit");
        } catch (NormalExitException e) {
            //
        }
        Mockito.verify(view).write("Goodbye, to see soon. ");

    }


}
